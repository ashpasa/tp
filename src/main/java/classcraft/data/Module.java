package classcraft.data;

import java.util.Objects;

/**
 * Represents a single academic module (e.g., CS1010, MA1511).
 * This is the fundamental data unit shared across all DM components.
 * Using a record for a simple, immutable data carrier class
 */
public record Module(
        String moduleCode,    // Unique code for the module (e.g., "CS1010")
        String moduleTitle,   // Full title of the module (e.g., "Programming Methodology")
        int credits,          // Modular Credits (MCs) assigned to the module (e.g., 4)
        String requirementType// Category of requirement (e.g., "Core", "CEG-Elective", "UEM")
) {
    // Records automatically provide a constructor, getters, equals(), hashCode(), and toString().
}