package seedu.classcraft.studyplan;

import seedu.classcraft.exceptions.StudyPlanException;
import seedu.classcraft.storage.Storage;

import java.net.URL;
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
    private static int totalSemesters = 8;
    private static int currentSemester = 1;

    /**
     * @@author lingru
     * Define the total MCs required for graduation.
     * (e.g., CEG/CS is 160 MCs). Adjust this value as needed.
     */
    private static final int TOTAL_MCS_FOR_GRADUATION = 160;

    // --- Fields for PLANNED modules ---
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
        setLoggerLevel();
        StudyPlan.totalSemesters = totalSemesters;
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

    public static int getCurrentSemester() {
        return currentSemester;
    }

    public static void setCurrentSemester(int currentSemester) {
        StudyPlan.currentSemester = currentSemester;
    }

    public static int getTotalSemesters() {
        return totalSemesters;
    }

    public static void setTotalSemesters(int totalSemesters) {
        StudyPlan.totalSemesters = totalSemesters;
    }

    /**
     * Adds a module to a specific semester in the study plan.
     *
     * @param module   The module to be added.
     * @param semester The semester number (1-based index).
     * @throws IllegalArgumentException
     */
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
     * Method to add a module to the study plan with prerequisite validation.
     *
     * @param moduleCode The module code to be added.
     * @param semester   The semester number (1-based index).
     * @param storage    Storage object for persistence.
     * @param isRestored Indicates if the module is being restored from storage.
     * @throws Exception
     */
    public void addModule(String moduleCode, int semester, Storage storage, boolean isRestored) throws Exception {
        // Use ModuleHandler to fetch data and create the Module object
        boolean isModAddedPrev = modules.containsKey(moduleCode);
        int previousSemester = 0;
        if (isModAddedPrev) {
            previousSemester = modules.get(moduleCode);
        }

        Module newModule = moduleHandler.createModule(moduleCode);

        try {
            PrerequisiteChecker.validatePrerequisites(newModule, semester, this);
        } catch (StudyPlanException e) {
            LOGGER.info("Prerequisite validation failed for " + moduleCode
                    + " in semester " + semester + ": " + e.getMessage());
            throw e;
        }

        if (isModAddedPrev) {
            storage.deleteModule(moduleCode, previousSemester);
        }

        addModule(newModule, semester);

        if (!isRestored) {
            storage.appendToFile(moduleCode, semester);
        }

        LOGGER.info("Added " + moduleCode + " to semester " + semester);
    }

    /**
     * Removes a module from the study plan.
     *
     * @param moduleString The module code to be removed.
     * @param storage      Storage object for persistence.
     */
    public void removeModule(String moduleString, Storage storage) {
        try {
            if (!modules.containsKey(moduleString) && !completedModulesMap.containsKey(moduleString)) {
                LOGGER.warning("Module " + moduleString + " does not exist in study plan.");
                throw new StudyPlanException("Module " + moduleString + " does not exist");
            }

            if (modules.containsKey(moduleString)) {
                Integer sem = modules.get(moduleString);
                for (int i = 0; i < studyPlan.get(sem - 1).size(); i++) {
                    if (Objects.equals(studyPlan.get(sem - 1).get(i).getModCode(), moduleString)) {
                        studyPlan.get(sem - 1).remove(i);
                        break;
                    }
                }
                modules.remove(moduleString);
                storage.deleteModule(moduleString, sem);
                LOGGER.info("Removed " + moduleString + " from semester " + sem);

            } else if (completedModulesMap.containsKey(moduleString)) {
                Module modToRemove = completedModulesMap.get(moduleString);
                completedModulesList.remove(modToRemove);
                completedModulesMap.remove(moduleString);
                LOGGER.info("Removed " + moduleString + " from completed modules list.");
            }

        } catch (StudyPlanException e) {
            LOGGER.log(Level.SEVERE, "Error occurred when removing " + moduleString, e);
        }
    }

    /**
     * @author lingru
     * @param moduleCode The code of the module to add.
     * @param status     The status (COMPLETED or EXEMPTED).
     * @throws Exception If module fetching fails or module is already in the plan.
     *                   Adds a module that is already completed or exempted to the study plan.
     */
    public void addCompletedModule(String moduleCode, ModuleStatus status) throws Exception {
        if (status == ModuleStatus.PLANNED) {
            throw new IllegalArgumentException("Use addModule() for planned modules.");
        }

        // Check if already exists in PLANNED modules
        if (modules.containsKey(moduleCode)) {
            throw new StudyPlanException("Module " + moduleCode
                    + " is already PLANNED in Semester " + modules.get(moduleCode));
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
     * @author lingru
     * @return The progress percentage, rounded to two decimal places.
     *                   calculates the student's degree progress percentage.
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
     * @author lingru
     * @return Total secured MCs.
     * Gets the total number of secured MCs (from completed/exempted modules).
     */
    public int getTotalSecuredMCs() {
        int totalSecuredMCs = 0;
        for (Module mod : completedModulesList) {
            totalSecuredMCs += mod.getModCreds();
        }
        return totalSecuredMCs;
    }

    /**
     * @return Total required MCs.
     * Gets the total MCs required for graduation.
     * @author lingru
     */
    public int getTotalMcsForGraduation() {
        return TOTAL_MCS_FOR_GRADUATION;
    }

    /**
     * @author lingru
     * @param moduleCode The module code to check.
     * @return true if the module exists, false otherwise.
     *                   Helper method to check if a module exists anywhere in the plan (planned or completed).
     */
    public boolean hasModule(String moduleCode) {
        return modules.containsKey(moduleCode) || completedModulesMap.containsKey(moduleCode);
    }
    // @@author


    public ArrayList<ArrayList<Module>> getStudyPlan() {
        return studyPlan;
    }

    /**
     * Creates a sample study plan for demonstration purposes.
     *
     * @return A StudyPlan object populated with sample modules.
     */
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

    /**
     * Returns a sample study plan for demonstration purposes.
     *
     * @return A StudyPlan object populated with sample modules.
     */
    public static StudyPlan getSampleStudyPlan() {
        return createSampleStudyPlan();
    }

    // @@author ashpasa

    /**
     * Calculates the total credits for a specific semester or for the entire study plan.
     *
     * @param semesterIndex Index of the semester (0-based), with -1 returning total credits for the entire study plan
     * @return Total credits for the specified semester or entire study plan
     */
    public int calculateSemCredits(int semesterIndex) {
        if (semesterIndex == -1) {
            return calculateTotalCredits();
        }

        if (semesterIndex < 0 || semesterIndex >= studyPlan.size()) {
            LOGGER.warning("Semester " + (semesterIndex + 1) + " is invalid.");
            throw new IllegalArgumentException("Semester " + (semesterIndex + 1) + " is invalid.");
        }

        int semesterCredits = 0;
        for (Module module : studyPlan.get(semesterIndex)) {
            assert module.getModCreds() >= 0 : "Module credits should be non-negative.";
            semesterCredits += module.getModCreds();
        }
        return semesterCredits;
    }

    /**
     * Calculates the total module credits in the entire study plan.
     *
     * @return Total credits for the entire study plan
     */
    private int calculateTotalCredits() {
        int totalCredits = 0;
        for (int i = 0; i < studyPlan.size(); i++) {
            int semCreds = calculateSemCredits(i);
            assert semCreds >= 0 : "Semester credits should be non-negative.";
            totalCredits += semCreds;
        }
        return totalCredits;
    }
    // @@author

    /**
     * Indicates to the user which semesters have a high workload (2 or more modules than their average workload)
     */
    public void balanceStudyPlan() {
        int totalCredits = calculateTotalCredits();
        int numberOfSems = studyPlan.size();
        int numberOfHighWorkloadSemesters = 0;
        for (int i = 0; i < numberOfSems; i++) {
            if (calculateSemCredits(i) > (totalCredits / numberOfSems) + 5) {
                System.out.println("Semester " + (i + 1) + " has a high workload. Please consider moving some modules" +
                        " to other semesters instead");
                numberOfHighWorkloadSemesters++;
            }
        }
        if (numberOfHighWorkloadSemesters > 0) {
            System.out.println("YAY! Your study plan is well balanced!");
        }
    }

    /**
     * Sets logger level depending on how the program is run.
     * When running from a jar file, it disables logging.
     * Otherwise, when running from an IDE, it displays all logging messages.
     */
    public void setLoggerLevel() {
        String className = "/" + this.getClass().getName().replace('.', '/') + ".class";
        URL resource = this.getClass().getResource(className);

        if (resource == null) {
            System.out.println("Unable to determine runtime environment.");
            return;
        }

        String protocol = resource.getProtocol();
        System.out.println("Protocol: " + protocol);
        if (Objects.equals(protocol, "jar")) {
            LOGGER.setLevel(Level.OFF);
        } else if (Objects.equals(protocol, "file")) {
            LOGGER.setLevel(Level.ALL);
        }
    }
}
