package seedu.classcraft.studyplan;

import seedu.classcraft.exceptions.StudyPlanException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import java.util.Iterator;
import java.util.Map;

/**
 * Creates a study plan based on modules added by user
 */
public class StudyPlan {
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

    public void addModule(String moduleCode, int semester) throws Exception {
        // Use ModuleHandler to fetch data and create the Module object
        Module newModule = moduleHandler.createModule(moduleCode);
        addModule(newModule, semester);

        System.out.println("Added " + moduleCode + " to semester " + semester);
        // Removed old System.out.println that used fetcher.getModulePrerequisites(moduleCode)
    }


    public void removeModule(String moduleString) {
        try {
            if (!modules.containsKey(moduleString)) {
                System.out.println("Module " + moduleString + " does not exist");
                throw new StudyPlanException("Module " + moduleString + " does not exist");
            }

            Integer sem = modules.get(moduleString);
            for (int i = 0; i < studyPlan.get(sem - 1).size(); i++) {
                if (Objects.equals(studyPlan.get(sem - 1).get(i).getModCode(), moduleString)) {
                    studyPlan.get(sem - 1).remove(i);
                }
            }

            modules.remove(moduleString);

            System.out.println("Removed " + moduleString);
        } catch (StudyPlanException e) {
            System.out.println("Error occurred when removing " + moduleString);
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
            System.err.println("Error creating sample study plan module: " + e.getMessage());
            // This usually means NUSModsFetcher failed to retrieve data.
        }

        return samplePlan;
    }

    public static StudyPlan getSampleStudyPlan() {
        return createSampleStudyPlan();
    }
}
