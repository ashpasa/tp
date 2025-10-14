package seedu.duke.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of modules planned across various semesters.
 */
public class StudyPlan {

    private final String planName;                      // Name of the plan (e.g., "Sample CEG Plan", "My Current Plan")
    private final List<StudyPlanEntry> entries;         // List of modules scheduled with year/semester info

    /**
     * Constructor for creating a new StudyPlan.
     *
     * @param planName The name of the study plan.
     */
    public StudyPlan(String planName) {
        this.planName = planName;
        this.entries = new ArrayList<>();
    }

    // --- Core Operations (Used by DM2 to modify the plan) ---

    /**
     * Adds a scheduled module entry to the study plan.
     *
     * @param entry The StudyPlanEntry to add.
     */
    public void addEntry(StudyPlanEntry entry) {
        this.entries.add(entry);
    }

    /**
     * Removes a specific module entry from the study plan based on the Module code.
     * Note: A more robust implementation might require year/semester for uniqueness.
     *
     * @param moduleCode The module code to remove (e.g., "CS1010").
     * @return true if an entry was removed, false otherwise.
     */
    public boolean removeEntryByModuleCode(String moduleCode) {
        return entries.removeIf(entry -> entry.module().moduleCode().equalsIgnoreCase(moduleCode));
    }

    // --- Getters (Used by Display and DM2) ---

    public String getPlanName() {
        return planName;
    }

    /**
     * Returns an unmodifiable list of all scheduled module entries.
     *
     * @return A list of StudyPlanEntry objects.
     */
    public List<StudyPlanEntry> getEntries() {
        // Return an unmodifiable view to protect the internal state
        return List.copyOf(entries);
    }

    // Add a toString() for easy debugging
    @Override
    public String toString() {
        return "StudyPlan [planName=" + planName + ", entriesCount=" + entries.size() + "]";
    }
}
