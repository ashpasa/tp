package seedu.classcraft.studyplan;

import com.fasterxml.jackson.databind.JsonNode;
import seedu.classcraft.exceptions.StudyPlanException;
import seedu.classcraft.storage.Storage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static int numModules = 0;

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
    private ArrayList<Module> exemptedModulesList;
    private HashMap<String, Module> exemptedModulesMap;
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
        this.exemptedModulesList = new ArrayList<>();
        this.exemptedModulesMap = new HashMap<>();
        this.currentSemester = 1;
        // @@author
    }

    public static int getCurrentSemester() {
        return currentSemester;
    }


    /**
     * Sets the current semester.
     * Moves all modules *before* this semester to COMPLETED status
     * and updates storage accordingly.
     *
     * @param newCurrentSemester The semester to set as current (1-based).
     * @param storage            Storage handler for persistence.
     * @return The number of modules moved to COMPLETED.
     * @throws StudyPlanException If semester is invalid.
     */
    public int setCurrentSemester(int newCurrentSemester, Storage storage, boolean isRestore) throws
            StudyPlanException {
        if (newCurrentSemester < 1 || newCurrentSemester > studyPlan.size()) {
            throw new StudyPlanException("Semester " + newCurrentSemester + " is invalid. " +
                    "Must be between 1 and " + studyPlan.size());
        }

        int modulesCompletedCount = 0;
        int prevSemester = this.currentSemester;
        this.currentSemester = newCurrentSemester;

        if (!isRestore) {
            storage.addCompletionStatus(newCurrentSemester);
        }

        if (newCurrentSemester < prevSemester) {
            for (int i = newCurrentSemester - 1; i < prevSemester - 1; i++) {
                int semCreds = calculateSemCredits(i);
                assert semCreds >= 0 : "Semester credits should be non-negative.";
                modulesCompletedCount += numModules;
            }
            return modulesCompletedCount;
        }


        for (int i = prevSemester - 1; i < newCurrentSemester - 1; i++) {
            ArrayList<Module> semesterModules = studyPlan.get(i);
            int currentSemesterNumber = i + 1;

            for (int j = semesterModules.size() - 1; j >= 0; j--) {
                Module mod = semesterModules.get(j);

                mod.setStatus(ModuleStatus.COMPLETED);

                modulesCompletedCount++;
            }
        }

        return modulesCompletedCount;
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

        if (modules.containsKey(module.getModCode())) {
            throw new IllegalArgumentException("Module " + module.getModCode()
                    + " is already PLANNED in Semester " + modules.get(module.getModCode()));
        }

        // @@author lingru
        // Check if module is already marked as completed/exempted
        if (exemptedModulesMap.containsKey(module.getModCode())) {
            throw new IllegalArgumentException("Module " + module.getModCode()
                    + " is already marked as EXEMPTED.");
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

        PrerequisiteChecker.validatePrerequisites(newModule, semester, this);

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
    public void removeModule(String moduleString, Storage storage) throws StudyPlanException {
        if (!modules.containsKey(moduleString) && !exemptedModulesMap.containsKey(moduleString)) {
            LOGGER.warning("Module " + moduleString + " does not exist in study plan.");
            throw new StudyPlanException("Module " + moduleString + " does not exist");
        }

        List<String> dependentModules = checkForDependentModules(moduleString);
        if (!dependentModules.isEmpty()) {
            throw new StudyPlanException("Cannot delete " + moduleString + " because it is a prerequisite for: " +
                    String.join(", ", dependentModules) + "\n\n" +
                    "Please delete those modules first before deleting " + moduleString + ".");
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

        } else if (exemptedModulesMap.containsKey(moduleString)) {
            Module modToRemove = exemptedModulesMap.get(moduleString);
            exemptedModulesList.remove(modToRemove);
            exemptedModulesMap.remove(moduleString);
            storage.deleteSecuredModule(moduleString);
            LOGGER.info("Removed " + moduleString + " from completed modules list.");
        }

    }

    /**
     * Checks if the given module is a prerequisite for any other module in the study plan.
     * Returns a list of modules that depend on the given module.
     *
     * @param moduleCode The module code to check
     * @return List of dependent modules that have this module as a prerequisite
     */
    private List<String> checkForDependentModules(String moduleCode) {
        List<String> dependentModules = new ArrayList<>();

        for (Module module : getAllModules()) {
            if (module.getModCode().equals(moduleCode)) {
                continue;
            }

            if (isModuleDependentOn(module, moduleCode)) {
                dependentModules.add(module.getModCode());
            }
        }
        return dependentModules;
    }

    /**
     * Checks if a module is dependent on another module as a prerequisite
     */
    private boolean isModuleDependentOn(Module module, String moduleCode) {
        if (hasPrerequisiteInList(module.getPrerequisites(), moduleCode)) {
            return true;
        }

        return hasPrerequisiteInTree(module.getPrereqTree(), moduleCode);
    }

    /**
     * Checks if a prerequisite list contains the given module code
     */
    private boolean hasPrerequisiteInList(List<String> prerequisites, String moduleCode) {
        if (prerequisites == null) {
            return false;
        }

        return prerequisites.stream()
                .anyMatch(prereq -> isPrerequisiteMatch(moduleCode, prereq));
    }

    /**
     * Checks if a prereqTree (JsonNode) contains the given module code
     */
    private boolean hasPrerequisiteInTree(JsonNode prereqTree, String moduleCode) {
        if (prereqTree == null || prereqTree.isNull() || !prereqTree.isTextual()) {
            return false;
        }

        String prereqText = prereqTree.asText();
        return isPrerequisiteMatch(moduleCode, prereqText);
    }

    /**
     * Checks if moduleCode satisfies the prerequisite requirement.
     * Handles both exact matches and wildcard patterns like "CS1010%"
     *
     * @param moduleCode The module code to check (e.g., "CS1010")
     * @param prereq     The prerequisite requirement (e.g., "CS1010" or "CS1010%")
     * @return true if moduleCode matches the prerequisite
     */
    private boolean isPrerequisiteMatch(String moduleCode, String prereq) {
        String cleanPrereq = prereq.replaceAll(":%[A-Z]", "").replaceAll(":[A-Z]", "");

        if (moduleCode.equals(cleanPrereq)) {
            return true;
        }

        if (cleanPrereq.contains("%")) {
            String baseCode = cleanPrereq.replace("%", "");
            return moduleCode.startsWith(baseCode);
        }

        return false;
    }

    /**
     * Gets all modules in the study plan (both planned and completed)
     *
     * @return List of all modules
     */
    private List<Module> getAllModules() {
        List<Module> allModules = new ArrayList<>();

        for (ArrayList<Module> semester : studyPlan) {
            allModules.addAll(semester);
        }

        allModules.addAll(exemptedModulesList);

        return allModules;
    }

    /**
     * @author lingru
     * @param moduleCode The code of the module to add.
     * @param status     The status (COMPLETED or EXEMPTED).
     * @param storage    Storage handler added.
     * @param isRestored Flag to prevent re-saving on load.
     * @throws Exception If module fetching fails or module is already in the plan.
     */

    public void addExemptedModule(String moduleCode, ModuleStatus status,
                                  Storage storage, boolean isRestored) throws Exception {
        if (status == ModuleStatus.PLANNED) {
            throw new IllegalArgumentException("Use addModule() for planned modules.");
        }

        if (exemptedModulesMap.containsKey(moduleCode)) {
            if (isRestored) {
                return;
            }
            throw new StudyPlanException("Module " + moduleCode + " is already marked as "
                    + exemptedModulesMap.get(moduleCode).getStatus());
        }

        Module moduleToMove = null;
        boolean wasMoved = false;
        Integer sem = 0;

        if (modules.containsKey(moduleCode)) {
            LOGGER.info("Module " + moduleCode + " is PLANNED. Moving it to " + status.toString());
            sem = modules.get(moduleCode);
            ArrayList<Module> semesterList = studyPlan.get(sem - 1);

            for (int i = 0; i < semesterList.size(); i++) {
                if (Objects.equals(semesterList.get(i).getModCode(), moduleCode)) {
                    moduleToMove = semesterList.remove(i);
                    wasMoved = true;
                    break;
                }
            }

            modules.remove(moduleCode);
        }


        if (moduleToMove == null) {
            moduleToMove = moduleHandler.createModule(moduleCode);
        }

        moduleToMove.setStatus(status);
        exemptedModulesList.add(moduleToMove);
        exemptedModulesMap.put(moduleCode, moduleToMove);

        if (!isRestored) {
            storage.saveSecuredModule(moduleToMove);
            if (wasMoved) {
                storage.deleteModule(moduleCode, sem);
            }
        }

        LOGGER.info("Added " + moduleCode + " as " + status.toString());
    }

    /**
     * @author lingru
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
     * @return The HashMap of completed/exempted modules.
     */
    public HashMap<String, Module> getExemptedModulesMap() {
        return exemptedModulesMap;
    }

    /**
     * @return The ArrayList of completed/exempted modules.
     */
    public ArrayList<Module> getExemptedModulesList() {
        return exemptedModulesList;
    }

    /**
     * @author lingru
     * @return Total secured MCs.
     */
    public int getTotalSecuredMCs() {
        int totalSecuredMCs = 0;
        for (Module mod : exemptedModulesList) {
            totalSecuredMCs += mod.getModCreds();
        }
        for (int i = 0; i < currentSemester - 1; i++) {
            int semCreds = calculateSemCredits(i);
            assert semCreds >= 0 : "Semester credits should be non-negative.";
            totalSecuredMCs += semCreds;
        }
        return totalSecuredMCs;
    }

    /**
     * @author lingru
     * @return Total required MCs.
     */
    public int getTotalMcsForGraduation() {
        return TOTAL_MCS_FOR_GRADUATION;
    }

    /**
     * @author lingru
     * @param moduleCode The module code to check.
     * @return true if the module exists, false otherwise.
     */
    public boolean hasModule(String moduleCode) {
        return modules.containsKey(moduleCode) || exemptedModulesMap.containsKey(moduleCode);
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
        numModules = 0;
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
            numModules++;
        }
        return semesterCredits;
    }

    /**
     * Calculates the total module credits in the entire study plan.
     * This now includes BOTH planned and secured (completed/exempted) modules.
     *
     * @return Total credits for the entire study plan
     */
    private int calculateTotalCredits() {
        int totalPlannedCredits = 0;
        for (int i = 0; i < studyPlan.size(); i++) {
            int semCreds = calculateSemCredits(i);
            assert semCreds >= 0 : "Semester credits should be non-negative.";
            totalPlannedCredits += semCreds;
        }

        int totalSecuredCredits = getTotalSecuredMCs();

        return totalPlannedCredits + totalSecuredCredits;
    }
    // @@author

    /**
     * Indicates to the user which semesters have a high workload (2 or more modules than their average workload)
     */
    public void checkStudyPlan() {
        int totalUncompletedCredits = 0;
        for (int i = currentSemester - 1; i < studyPlan.size(); i++) {
            int semCreds = calculateSemCredits(i);
            assert semCreds >= 0 : "Semester credits should be non-negative.";
            totalUncompletedCredits += semCreds;
        }

        int numberOfSems = studyPlan.size() - currentSemester + 1;
        int numberOfHighWorkloadSemesters = 0;
        for (int i = currentSemester - 1; i < numberOfSems; i++) {
            if (calculateSemCredits(i) > (totalUncompletedCredits / numberOfSems) + 5) {
                System.out.println("Semester " + (i + 1) + " has a high workload. Please consider moving some modules" +
                        " to other semesters instead");
                numberOfHighWorkloadSemesters++;
            }
        }
        if (numberOfHighWorkloadSemesters == 0) {
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
            return;
        }

        String protocol = resource.getProtocol();

        if (Objects.equals(protocol, "jar")) {
            LOGGER.setLevel(Level.OFF);
        } else if (Objects.equals(protocol, "file")) {
            LOGGER.setLevel(Level.ALL);
        }
    }
}

