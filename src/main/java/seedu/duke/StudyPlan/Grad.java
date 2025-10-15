package seedu.duke.StudyPlan;

import java.util.List;
import java.util.Arrays;

/**
 * Stores and provides access to the CEG Default Graduation Requirements
 */
public class Grad {

    private static final List<String> CEG_CORE_MODULES = Arrays.asList(
            "CS1010", "CS2030S", "CS2040S", "CS2100", "CS2101", "CS2103T",
            "CS3230", "CS3244", "EE2026", "EE2027" // samples
    );

    private static final List<String> GENERAL_EDUCATION_MODULES = Arrays.asList(
            "THE1001", "THE1002" // samples
    );

    /**
     * Returns the list of all core modules required for CEG graduation.
     * @return A list of CEG core module codes.
     */
    public static List<String> getCoreRequirements() {
        return CEG_CORE_MODULES;
    }

    /**
     * Returns a formatted string displaying all graduation requirements,
     * including module names fetched via ModuleHandler.
     */
    public static String getAllRequirementsDisplay() {
        StringBuilder sb = new StringBuilder();
        ModuleHandler tempHandler = new ModuleHandler();

        sb.append("===== CEG Default Graduation Requirements (Minimum) =====\n");

        sb.append("--- Core Modules (Required: ").append(CEG_CORE_MODULES.size()).append(" mods) ---\n");
        for (String modCode : CEG_CORE_MODULES) {
            try {
                Module module = tempHandler.createModule(modCode);
                String prereqsInfo = module.getPrerequisitesDisplay();

                // format：- CS1010 (Programming Methodology) (Prereqs: ...)
                sb.append("- ").append(module.getModCode())
                        .append(" (").append(module.getModName()).append(")")
                        .append(prereqsInfo).append("\n");

            } catch (Exception e) {
                sb.append("- ").append(modCode).append(" (Error fetching details)\n");
            }
        }

        sb.append("\n--- General Education Modules ---\n");
        for (String mod : GENERAL_EDUCATION_MODULES) {
            sb.append("- ").append(mod).append("\n");
        }
        return sb.toString();
    }
}
