package com.riley.combinedpe.emc;

import com.riley.combinedpe.CombinedPE;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates detailed reports of EMC value assignments
 *
 * Report includes:
 * - Summary statistics
 * - All discovered EMC values with sources
 * - Config overrides applied
 * - Blacklisted items
 * - Scan performance metrics
 */
public class EMCReportGenerator {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Report data container
     */
    public static class ReportData {
        public int totalItems;
        public int itemsWithExistingEMC;
        public int blacklistedItems;
        public int newEMCAssignments;
        public int overriddenEMC;
        public int recipeBasedEMC;
        public int tagBasedEMC;
        public long scanDurationMs;

        public Map<Item, Double> discoveredEMC;
        public Map<String, Long> configOverrides;
        public List<String> blacklistedItemIds;

        // Track source of each EMC value
        public Map<Item, String> emcSources = new HashMap<>();
    }

    /**
     * Generate and save EMC report to file
     * @param data Report data from scanning
     * @return Path to generated report file
     */
    public static Path generateReport(ReportData data) {
        try {
            // Create reports directory if it doesn't exist
            Path reportsDir = Paths.get("config", "combinedpe", "reports");
            Files.createDirectories(reportsDir);

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path reportFile = reportsDir.resolve("emc_report_" + timestamp + ".txt");

            // Write report
            try (BufferedWriter writer = Files.newBufferedWriter(reportFile)) {
                writeReport(writer, data);
            }

            CombinedPE.LOGGER.info("EMC report generated: {}", reportFile.toAbsolutePath());
            return reportFile;

        } catch (IOException e) {
            CombinedPE.LOGGER.error("Failed to generate EMC report", e);
            return null;
        }
    }

    /**
     * Write report content to writer
     */
    private static void writeReport(BufferedWriter writer, ReportData data) throws IOException {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);

        // Header
        writer.write("=".repeat(70));
        writer.newLine();
        writer.write("CombinedPE Dynamic EMC Report");
        writer.newLine();
        writer.write("=".repeat(70));
        writer.newLine();
        writer.write("Generated: " + timestamp);
        writer.newLine();
        writer.write("Scan Duration: " + data.scanDurationMs + "ms");
        writer.newLine();
        writer.newLine();

        // Summary Section
        writer.write("SUMMARY");
        writer.newLine();
        writer.write("-".repeat(70));
        writer.newLine();
        writer.write(String.format("Total items scanned:        %d", data.totalItems));
        writer.newLine();
        writer.write(String.format("Items with existing EMC:    %d", data.itemsWithExistingEMC));
        writer.newLine();
        writer.write(String.format("Blacklisted items:          %d", data.blacklistedItems));
        writer.newLine();
        writer.write(String.format("New EMC values discovered:  %d", data.newEMCAssignments));
        writer.newLine();
        writer.write(String.format("  - From config overrides:  %d", data.overriddenEMC));
        writer.newLine();
        writer.write(String.format("  - From recipes:           %d", data.recipeBasedEMC));
        writer.newLine();
        writer.write(String.format("  - From tags:              %d", data.tagBasedEMC));
        writer.newLine();
        writer.newLine();

        // Config Overrides Section
        if (!data.configOverrides.isEmpty()) {
            writer.write("CONFIG OVERRIDES APPLIED");
            writer.newLine();
            writer.write("-".repeat(70));
            writer.newLine();

            // Sort by item ID for readability
            List<Map.Entry<String, Long>> sortedOverrides = new ArrayList<>(data.configOverrides.entrySet());
            sortedOverrides.sort(Map.Entry.comparingByKey());

            for (Map.Entry<String, Long> entry : sortedOverrides) {
                writer.write(String.format("%-50s = %d", entry.getKey(), entry.getValue()));
                writer.newLine();
            }
            writer.newLine();
        }

        // Blacklisted Items Section
        if (!data.blacklistedItemIds.isEmpty()) {
            writer.write("BLACKLISTED ITEMS");
            writer.newLine();
            writer.write("-".repeat(70));
            writer.newLine();

            List<String> sortedBlacklist = new ArrayList<>(data.blacklistedItemIds);
            Collections.sort(sortedBlacklist);

            for (String itemId : sortedBlacklist) {
                writer.write(itemId);
                writer.newLine();
            }
            writer.newLine();
        }

        // Discovered EMC Values Section
        writer.write("DISCOVERED EMC VALUES");
        writer.newLine();
        writer.write("-".repeat(70));
        writer.newLine();
        writer.write(String.format("%-50s %10s  %s", "Item", "EMC", "Source"));
        writer.newLine();
        writer.write("-".repeat(70));
        writer.newLine();

        // Sort by item ID
        List<Map.Entry<Item, Double>> sortedEMC = data.discoveredEMC.entrySet().stream()
            .sorted((a, b) -> {
                String idA = BuiltInRegistries.ITEM.getKey(a.getKey()).toString();
                String idB = BuiltInRegistries.ITEM.getKey(b.getKey()).toString();
                return idA.compareTo(idB);
            })
            .collect(Collectors.toList());

        for (Map.Entry<Item, Double> entry : sortedEMC) {
            Item item = entry.getKey();
            double emcValue = entry.getValue();
            long roundedEMC = Math.round(emcValue);
            String itemId = BuiltInRegistries.ITEM.getKey(item).toString();
            String source = data.emcSources.getOrDefault(item, "unknown");

            writer.write(String.format("%-50s %10d  %s", itemId, roundedEMC, source));
            writer.newLine();
        }

        writer.newLine();
        writer.write("=".repeat(70));
        writer.newLine();
        writer.write("End of Report");
        writer.newLine();
    }
}
