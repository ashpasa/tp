package seedu.classcraft.studyplan;

import seedu.classcraft.exceptions.StudyPlanException;
import seedu.classcraft.storage.Storage;

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
    ArrayList<ArrayList<Module>> studyPlan = new ArrayList<>();
    HashMap<String, Integer> modules; // stores moduleCode: semester
    private ModuleHandler moduleHandler;

    public StudyPlan(int totalSemesters) {
        for (int i = 0; i < totalSemesters; i++) {
            ArrayList<Module> innerList = new ArrayList<>();
            studyPlan.add(innerList);
        }
        this.modules = new HashMap<>();
        this.moduleHandler = new ModuleHandler();
    }

    public void addModule(Module module, int semester) throws IllegalArgumentException {
        if (semester < 1 || semester > studyPlan.size()) {
            throw new IllegalArgumentException("Semester " + semester + " is invalid.");
        }

        studyPlan.get(semester - 1).add(module);
        modules.put(module.getModCode(), semester);

        module.setSemesterTaught(semester);

        assert studyPlan.get(semester - 1).contains(module) :
                "Module should be in the study plan list after adding.";
        assert modules.containsKey(module.getModCode()) :
                "Module code should be in the modules map after adding.";
    }

    public void addModule(String moduleCode, int semester, Storage storage, boolean isRestored) throws Exception {
        // Use ModuleHandler to fetch data and create the Module object
        int previousSemester = modules.get(moduleCode);
        boolean isModAddedPrev = modules.containsKey(moduleCode);
      
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
            storage.appendToFile(moduleCode,semester);
        }

        LOGGER.info("Added " + moduleCode + " to semester " + semester);
        // Removed old System.out.println that used fetcher.getModulePrerequisites(moduleCode)
    }


    public void removeModule(String moduleString, Storage storage) {
        try {
            if (!modules.containsKey(moduleString)) {
                LOGGER.warning("Module " + moduleString + " does not exist");
                throw new StudyPlanException("Module " + moduleString + " does not exist");
            }

            Integer sem = modules.get(moduleString);
            for (int i = 0; i < studyPlan.get(sem - 1).size(); i++) {
                if (Objects.equals(studyPlan.get(sem - 1).get(i).getModCode(), moduleString)) {
                    studyPlan.get(sem - 1).remove(i);
                }
            }

            modules.remove(moduleString);
            storage.deleteModule(moduleString, sem);

            LOGGER.info("Removed " + moduleString);
        } catch (StudyPlanException e) {
            LOGGER.log(Level.SEVERE, "Error occurred when removing " + moduleString, e);
        }
    }

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

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating sample study plan module: " + e.getMessage(), e);
            // This usually means NUSModsFetcher failed to retrieve data.
        }

        return samplePlan;
    }

    public static StudyPlan getSampleStudyPlan() {
        return createSampleStudyPlan();
    }

    // @@author ashpasa
    /**
     * Calculates the total credits for a specific semester or for the entire study plan.
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
            semesterCredits += module.getModCreds();
        }
        return semesterCredits;
    }

    /**
     * @return Total credits for the entire study plan
     */
    private int calculateTotalCredits() {
        int totalCredits = 0;
        for (int i = 0; i < studyPlan.size(); i++) {
            int semCreds = calculateSemCredits(i);
            totalCredits += semCreds;
        }
        return totalCredits;
    }
    // @@author
}
