package seedu.classcraft.studyplan;

import seedu.classcraft.exceptions.StudyPlanException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Creates a study plan based on modules added by user
 */
public class StudyPlan {
    private static final Logger LOGGER = Logger.getLogger(StudyPlan.class.getName());

    /**
     * @@author lingru
     * Define the total MCs required for graduation.
     * (e.g., CEG/CS is 160 MCs). Adjust this value as needed.
     */
    private static final int TOTAL_MCS_FOR_GRADUATION = 160;

    // --- Fields for PLANNED modules ---
    // @@author
    /**
     * Stores modules that are PLANNED for future semesters.
     */
    ArrayList<ArrayList<Module>> studyPlan = new ArrayList<>();
    HashMap<String, Integer> modules; // stores moduleCode: semester

    // @@author lingru
    // --- Fields for COMPLETED/EXEMPTED modules---
    /**
     * @@author lingru
     * Stores all modules that are marked as COMPLETED or EXEMPTED.
     * These modules count towards degree progress but aren't in a specific semester.
     */
    private ArrayList<Module> completedModulesList;
    private HashMap<String, Module> completedModulesMap;
    // @@author

    private ModuleHandler moduleHandler;


    public StudyPlan(int totalSemesters) {
        for (int i = 0; i < totalSemesters; i++) {
            ArrayList<Module> innerList = new ArrayList<>();
            studyPlan.add(innerList);
        }
        this.modules = new HashMap<>();
        this.moduleHandler = new ModuleHandler();

        // @@author lingru
        this.completedModulesList = new ArrayList<>();
        this.completedModulesMap = new HashMap<>();
        // @@author
    }

    public void addModule(Module module, int semester) throws IllegalArgumentException {
        if (semester < 1 || semester > studyPlan.size()) {
            throw new IllegalArgumentException("Semester " + semester + " is invalid.");
        }

        // @@author lingru
        // Check if module is already marked as completed/exempted
        if (completedModulesMap.containsKey(module.getModCode())) {
            throw new IllegalArgumentException("Module " + module.getModCode()
                    + " is already marked as COMPLETED/EXEMPTED.");
        }
        // @@author

        studyPlan.get(semester - 1).add(module);
        modules.put(module.getModCode(), semester);

        module.setSemesterTaught(semester);

        // @@author lingru
        // Set status just in case (though it should be PLANNED by default)
        module.setStatus(ModuleStatus.PLANNED);
        // @@author

        assert studyPlan.get(semester - 1).contains(module) :
                "Module should be in the study plan list after adding.";
        assert modules.containsKey(module.getModCode()) :
                "Module code should be in the modules map after adding.";
    }

    /**
     * Fetches and adds a PLANNED module to a specific semester.
     */
    public void addModule(String moduleCode, int semester) throws Exception {
        // Use ModuleHandler to fetch data and create the Module object
        Module newModule = moduleHandler.createModule(moduleCode);
        addModule(newModule, semester);

        LOGGER.info("Added " + moduleCode + " to semester " + semester);
    }


    public void removeModule(String moduleString) {
        try {
            if (!modules.containsKey(moduleString) && !completedModulesMap.containsKey(moduleString)) {
                LOGGER.warning("Module " + moduleString + " does not exist in study plan.");
                throw new StudyPlanException("Module " + moduleString + " does not exist");
            }

            // If it's a PLANNED module
            if (modules.containsKey(moduleString)) {
                Integer sem = modules.get(moduleString);
                for (int i = 0; i < studyPlan.get(sem - 1).size(); i++) {
                    if (Objects.equals(studyPlan.get(sem - 1).get(i).getModCode(), moduleString)) {
                        studyPlan.get(sem - 1).remove(i);
                        break;
                    }
                }
                modules.remove(moduleString);
                LOGGER.info("Removed " + moduleString + " from semester " + sem);

                // If it's a COMPLETED/EXEMPTED module
            } else if (completedModulesMap.containsKey(moduleString)) {
                Module modToRemove = completedModulesMap.get(moduleString);
                completedModulesList.remove(modToRemove);
                completedModulesMap.remove(moduleString);
                LOGGER.info("Removed " + moduleString + " from completed modules list.");
            }
            // @@author

        } catch (StudyPlanException e) {
            LOGGER.log(Level.SEVERE, "Error occurred when removing " + moduleString, e);
        }
    }

    /**
     * @@author lingru
     * Adds a module that is already completed or exempted to the study plan.
     *
     * @param moduleCode The code of the module to add.
     * @param status The status (COMPLETED or EXEMPTED).
     * @throws Exception If module fetching fails or module is already in the plan.
     */
    public void addCompletedModule(String moduleCode, ModuleStatus status) throws Exception {
        if (status == ModuleStatus.PLANNED) {
            throw new IllegalArgumentException("Use addModule() for planned modules.");
        }

        // Check if already exists in PLANNED modules
        if (modules.containsKey(moduleCode)) {
            throw new StudyPlanException("Module " + moduleCode + " is already PLANNED in Semester " + modules.get(moduleCode));
        }

        // Check if already exists in COMPLETED/EXEMPTED modules
        if (completedModulesMap.containsKey(moduleCode)) {
            throw new StudyPlanException("Module " + moduleCode + " is already marked as "
                    + completedModulesMap.get(moduleCode).getStatus());
        }

        // Fetch module data
        Module newModule = moduleHandler.createModule(moduleCode);

        // Set the correct status
        newModule.setStatus(status);

        // Add to the completed lists
        completedModulesList.add(newModule);
        completedModulesMap.put(moduleCode, newModule);

        LOGGER.info("Added " + moduleCode + " as " + status.toString());
    }

    /**
     * @@author lingru
     * Calculates the student's degree progress percentage.
     *
     * @return The progress percentage, rounded to two decimal places.
     */
    public double getDegreeProgressPercentage() {
        if (TOTAL_MCS_FOR_GRADUATION <= 0) {
            return 0.0;
        }

        int totalSecuredMCs = getTotalSecuredMCs();

        // Calculate percentage
        double percentage = ((double) totalSecuredMCs / TOTAL_MCS_FOR_GRADUATION) * 100.0;

        // Format to 2 decimal places
        return Math.round(percentage * 100.0) / 100.0;
    }

    /**
     * @@author lingru
     * Gets the total number of secured MCs (from completed/exempted modules).
     * @return Total secured MCs.
     */
    public int getTotalSecuredMCs() {
        int totalSecuredMCs = 0;
        for (Module mod : completedModulesList) {
            totalSecuredMCs += mod.getModuleCredit();
        }
        return totalSecuredMCs;
    }

    /**
     * @@author lingru
     * Gets the total MCs required for graduation.
     * @return Total required MCs.
     */
    public int getTotalMcsForGraduation() {
        return TOTAL_MCS_FOR_GRADUATION;
    }

    /**
     * @@author lingru
     * Helper method to check if a module exists anywhere in the plan (planned or completed).
     * Useful for prerequisite checking (Sean's task).
     *
     * @param moduleCode The module code to check.
     * @return true if the module exists, false otherwise.
     */
    public boolean hasModule(String moduleCode) {
        return modules.containsKey(moduleCode) || completedModulesMap.containsKey(moduleCode);
    }
    // @@author


    public ArrayList<ArrayList<Module>> getStudyPlan() {
        return studyPlan;
    }

    public static StudyPlan createSampleStudyPlan() {
        // Assuming CEG is an 8-semester course
        StudyPlan samplePlan = new StudyPlan(8);
        ModuleHandler handler = samplePlan.moduleHandler; // Use StudyPlan's internal handler

        try {

            // Semester 1
            Module cs1010 = handler.createModule("CS1010");
            samplePlan.addModule(cs1010, 1);

            Module ma1511 = handler.createModule("MA1511");
            samplePlan.addModule(ma1511, 1);

            // Semester 2
            Module cs2030s = handler.createModule("CS2030S");
            samplePlan.addModule(cs2030s, 2);

            Module ee2026 = handler.createModule("EE2026");
            samplePlan.addModule(ee2026, 2);

            // Semester 3
            Module cs2040s = handler.createModule("CS2040S");
            samplePlan.addModule(cs2040s, 3);

            // Add other core/sample modules for Semesters 3 to 8 (to be added)

            // @@author lingru
            // Example of adding a completed module (e.g., from poly exemption)
            // samplePlan.addCompletedModule("CS1010", ModuleStatus.EXEMPTED);
            // Note: This line is commented out, but shows how *could* add to the sample.
            // If add this, make sure to remove the "addModule(cs1010, 1)" above.
            // @@author

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating sample study plan module: " + e.getMessage(), e);
            // This usually means NUSModsFetcher failed to retrieve data.
        }

        return samplePlan;
    }

    public static StudyPlan getSampleStudyPlan() {
        return createSampleStudyPlan();
    }
}

