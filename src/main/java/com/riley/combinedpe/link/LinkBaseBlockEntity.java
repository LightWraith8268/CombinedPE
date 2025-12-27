package com.riley.combinedpe.link;

import com.riley.combinedpe.compat.ProjectECompat;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.PECapabilities;
import moze_intel.projecte.api.capabilities.block_entity.IEmcStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Base block entity for all EMC Link types.
 *
 * Provides:
 * - Owner tracking (UUID and name)
 * - Item-specific filtering (one item type per link)
 * - EMC buffering and sync to player
 * - Tier-based throughput limiting
 */
public abstract class LinkBaseBlockEntity extends BlockEntity implements IEmcStorage {

    // Owner tracking
    protected UUID owner = null;
    protected String ownerName = "";

    // Item filter (what item this link handles)
    protected ItemStack selectedItem = ItemStack.EMPTY;

    // Tier information
    protected LinkTier tier;

    // EMC buffering (accumulated before syncing to player)
    protected BigInteger storedEMC = BigInteger.ZERO;

    // Tick counter for throughput limiting
    protected int tickCounter = 0;

    public LinkBaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, LinkTier tier) {
        super(type, pos, state);
        this.tier = tier;
    }

    /**
     * Set the owner of this link (called when placed)
     */
    public void setOwner(UUID uuid, String name) {
        this.owner = uuid;
        this.ownerName = name;
        setChanged();
    }

    /**
     * Set the item filter for this link
     */
    public void setSelectedItem(ItemStack stack) {
        if (stack.isEmpty()) {
            this.selectedItem = ItemStack.EMPTY;
        } else {
            this.selectedItem = stack.copyWithCount(1); // Store only 1 for comparison
        }
        setChanged();
    }

    /**
     * Get the current item filter
     */
    public ItemStack getSelectedItem() {
        return selectedItem.copy();
    }

    /**
     * Check if this link has an owner
     */
    public boolean hasOwner() {
        return owner != null;
    }

    /**
     * Get the owner's name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Get the owner's UUID
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * Get this link's tier
     */
    public LinkTier getTier() {
        return tier;
    }

    /**
     * Get the owner player (if online)
     */
    @Nullable
    protected ServerPlayer getOwnerPlayer() {
        if (!hasOwner() || !(level instanceof ServerLevel serverLevel)) {
            return null;
        }
        return serverLevel.getServer().getPlayerList().getPlayer(owner);
    }

    /**
     * Get the owner's knowledge provider
     */
    @Nullable
    protected IKnowledgeProvider getKnowledge() {
        ServerPlayer player = getOwnerPlayer();
        if (player == null) {
            return null;
        }
        return player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY);
    }

    /**
     * Check if the owner knows this item
     */
    protected boolean ownerKnowsItem(ItemStack stack) {
        IKnowledgeProvider knowledge = getKnowledge();
        if (knowledge == null) {
            return false;
        }
        ItemInfo itemInfo = ItemInfo.fromStack(stack);
        return knowledge.hasKnowledge(itemInfo);
    }

    /**
     * Learn an item for the owner
     */
    protected void learnItem(ItemStack stack) {
        ServerPlayer player = getOwnerPlayer();
        IKnowledgeProvider knowledge = getKnowledge();
        if (player == null || knowledge == null) {
            return;
        }

        ItemInfo itemInfo = ItemInfo.fromStack(stack);
        if (!knowledge.hasKnowledge(itemInfo)) {
            knowledge.addKnowledge(itemInfo);
            knowledge.syncKnowledgeChange(player, itemInfo, true);
        }
    }

    /**
     * Get the owner's current EMC balance
     */
    protected BigInteger getOwnerEMC() {
        IKnowledgeProvider knowledge = getKnowledge();
        if (knowledge == null) {
            return BigInteger.ZERO;
        }
        return knowledge.getEmc();
    }

    /**
     * Add EMC to the owner's balance
     */
    protected void addEMCToOwner(BigInteger amount) {
        ServerPlayer player = getOwnerPlayer();
        IKnowledgeProvider knowledge = getKnowledge();
        if (player == null || knowledge == null) {
            return;
        }

        BigInteger newEMC = knowledge.getEmc().add(amount);
        knowledge.setEmc(newEMC);
        knowledge.syncEmc(player);
    }

    /**
     * Deduct EMC from the owner's balance
     */
    protected boolean deductEMCFromOwner(BigInteger amount) {
        ServerPlayer player = getOwnerPlayer();
        IKnowledgeProvider knowledge = getKnowledge();
        if (player == null || knowledge == null) {
            return false;
        }

        BigInteger current = knowledge.getEmc();
        if (current.compareTo(amount) < 0) {
            return false; // Not enough EMC
        }

        BigInteger newEMC = current.subtract(amount);
        knowledge.setEmc(newEMC);
        knowledge.syncEmc(player);
        return true;
    }

    /**
     * Check if an item stack matches the selected item filter
     */
    protected boolean matchesFilter(ItemStack stack) {
        if (selectedItem.isEmpty()) {
            return false; // No filter set
        }
        return ItemStack.isSameItemSameComponents(stack, selectedItem);
    }

    /**
     * Tick handler for subclasses
     */
    public void tick() {
        if (level == null || level.isClientSide()) {
            return;
        }

        tickCounter++;

        // For Ultimate tier (infinite throughput), process operations every tick
        // For other tiers, respect the tick delay
        if (tier.getTicksPerOperation() == 0 || tickCounter >= tier.getTicksPerOperation()) {
            tickCounter = 0;
            // Subclasses can override to process buffered operations here
        }

        // Sync buffered EMC to player every second
        if (tickCounter % 20 == 0 && !storedEMC.equals(BigInteger.ZERO)) {
            addEMCToOwner(storedEMC);
            storedEMC = BigInteger.ZERO;
            setChanged();
        }
    }

    // ========== NBT Serialization ==========

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (owner != null) {
            tag.putUUID("Owner", owner);
        }
        tag.putString("OwnerName", ownerName);
        tag.putString("Tier", tier.getName());
        tag.putString("StoredEMC", storedEMC.toString());
        tag.putInt("TickCounter", tickCounter);

        if (!selectedItem.isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            selectedItem.save(registries, itemTag);
            tag.put("SelectedItem", itemTag);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.hasUUID("Owner")) {
            this.owner = tag.getUUID("Owner");
        }
        this.ownerName = tag.getString("OwnerName");
        this.tier = LinkTier.fromName(tag.getString("Tier"));

        String emcStr = tag.getString("StoredEMC");
        this.storedEMC = emcStr.isEmpty() ? BigInteger.ZERO : new BigInteger(emcStr);

        this.tickCounter = tag.getInt("TickCounter");

        if (tag.contains("SelectedItem")) {
            this.selectedItem = ItemStack.parseOptional(registries, tag.getCompound("SelectedItem"));
        } else {
            this.selectedItem = ItemStack.EMPTY;
        }
    }

    // ========== IEmcStorage Implementation ==========
    // This allows other mods to insert EMC into links

    @Override
    public long getStoredEmc() {
        // Return 0 - we don't store EMC, it goes directly to player
        return 0L;
    }

    @Override
    public long getMaximumEmc() {
        return Long.MAX_VALUE;
    }

    @Override
    public long extractEmc(long emc, EmcAction action) {
        // Links don't support EMC extraction directly
        // (use item extraction instead)
        return 0L;
    }

    @Override
    public long insertEmc(long emc, EmcAction action) {
        if (emc <= 0L) {
            return 0L;
        }

        if (action.execute()) {
            storedEMC = storedEMC.add(BigInteger.valueOf(emc));
            setChanged();
        }

        return emc;
    }
}
