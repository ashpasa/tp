package seedu.classcraft.studyplan;

import java.util.List;
import java.util.Arrays;

/**
 * Stores and provides access to the CEG Default Graduation Requirements
 */
public class Grad {

    private static final List<String> CEG_CORE_MODULES = Arrays.asList(
            "CG1111A", "CS1231", "CS2040C",
            "EE2026", "MA1511", "MA1512", "MA1508E", "EG2401A",
            "CG2111A", "CG2023", "CG2027", "CG2028", "CG2271", "CG3201",
            "CG3207", "CS2107", "CS2113", "EE4204", "EG3611A"
    );

    private static final List<String> GENERAL_EDUCATION_MODULES = Arrays.asList(
            "GEA1000", "CDE2501", "PF1101A", "ES2631", "DTK1234", "EG1311",
            "CS1010", "EE2211", "CG4001"
    );

    /**
     * Returns the list of all core modules required for CEG graduation.
     *
     * @return A list of CEG core module codes.
     */
    public static List<String> getCoreRequirements() {
        return CEG_CORE_MODULES;
    }

    /**
     * Produces a formatted string displaying all graduation requirements,
     * including module names fetched via ModuleHandler.
     * 
     * @return A string representation of all graduation requirements.
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
