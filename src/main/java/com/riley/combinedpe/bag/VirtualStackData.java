package com.riley.combinedpe.bag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;

/**
 * Data component for storing virtual stack counts
 * Allows items to exceed normal stack limits (e.g., 256, 1024, etc.)
 */
public record VirtualStackData(Map<Integer, Integer> virtualCounts) {

    /**
     * Codec for saving/loading to disk
     */
    public static final Codec<VirtualStackData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(Codec.INT, Codec.INT)
                .fieldOf("virtual_counts")
                .forGetter(VirtualStackData::virtualCounts)
        ).apply(instance, VirtualStackData::new)
    );

    /**
     * Stream codec for network synchronization
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, VirtualStackData> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.map(HashMap::new, ByteBufCodecs.VAR_INT, ByteBufCodecs.VAR_INT),
            VirtualStackData::virtualCounts,
            VirtualStackData::new
        );

    /**
     * Create empty virtual stack data
     */
    public static VirtualStackData empty() {
        return new VirtualStackData(new HashMap<>());
    }

    /**
     * Get the virtual count for a specific slot
     * @param slot Slot index
     * @return Virtual count (0 if not set)
     */
    public int getVirtualCount(int slot) {
        return virtualCounts.getOrDefault(slot, 0);
    }

    /**
     * Set the virtual count for a specific slot
     * Returns a new VirtualStackData instance (immutable pattern)
     */
    public VirtualStackData setVirtualCount(int slot, int count) {
        Map<Integer, Integer> newCounts = new HashMap<>(virtualCounts);
        if (count <= 0) {
            newCounts.remove(slot);
        } else {
            newCounts.put(slot, count);
        }
        return new VirtualStackData(newCounts);
    }

    /**
     * Get the total virtual count across all slots
     */
    public int getTotalVirtualCount() {
        return virtualCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Check if this slot has any virtual stacks
     */
    public boolean hasVirtualStack(int slot) {
        return virtualCounts.containsKey(slot) && virtualCounts.get(slot) > 0;
    }

    /**
     * Clear virtual count for a specific slot
     */
    public VirtualStackData clearVirtualCount(int slot) {
        return setVirtualCount(slot, 0);
    }

    /**
     * Create a copy of this data
     */
    public VirtualStackData copy() {
        return new VirtualStackData(new HashMap<>(virtualCounts));
    }
}
