package seedu.duke.data;


/**
 * Represents a module placed within a specific academic semester.
 * This links a Module to a Semester for scheduling purposes.
 */
public record StudyPlanEntry(
        Module module,        // The module being planned
        int year,             // The academic year (e.g., 1, 2, 3, 4)
        int semester          // The semester number (e.g., 1 for Sem 1, 2 for Sem 2)
) {
    /**
     * Helper method to generate a human-readable display string for the semester.
     *
     * @return A string like "Year 1 Sem 1".
     */
    public String getSemesterDisplay() {
        return "Year " + year + " Sem " + semester;
    }
}

