package seedu.duke.StudyPlan;

import seedu.duke.exceptions.StudyPlanException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates a study plan based on modules added by user
 */
public class StudyPlan {
    ArrayList<ArrayList<String>> studyPlan = new ArrayList<>();
    HashMap<String, Integer> modules; // stores moduleCode: semester

    public StudyPlan(int totalSemesters) {
        try {
            if (!((totalSemesters > 0) && (totalSemesters <= 8))) {
                throw new StudyPlanException("An invalid number of semesters was input when creating StudyPlan");
            }
            for (int i = 0; i < totalSemesters; i++) {
                ArrayList<String> innerList = new ArrayList<>();
                studyPlan.add(innerList);
            }
            modules = new HashMap<>();
        } catch (StudyPlanException e) {
            System.out.println("Please input a valid number of semesters, from 1 to 8");
        }
    }

    // queries for module info, prints prereqs and adds to study plan based on semester
    public void addModule(String moduleString, int semester) throws Exception {
        try {
            if (!((semester > 0) && (semester <= studyPlan.size()))) {
                System.out.println("An invalid semester was input when creating StudyPlan");
                throw new StudyPlanException("An invalid semester was input when creating StudyPlan");
            }
            if (modules.containsKey(moduleString)) {
                System.out.println("Module " + moduleString + " already exists");
                throw new StudyPlanException("Module " + moduleString + " already exists");
            }

            ModuleHandler fetcher = new ModuleHandler();
            String prereqs;
            try {
                prereqs = fetcher.getModulePrerequisites(moduleString);
            } catch (Exception e) {
                throw new StudyPlanException(moduleString + " does not contain any prerequisites");
            }
            System.out.println("Prerequisites for " + moduleString + ": " + prereqs);

            studyPlan.get(semester - 1).add(moduleString);
            modules.put(moduleString, semester);

            System.out.println("Added " + moduleString + " to semester " + prereqs);
        } catch (StudyPlanException e) {
            System.out.println("Error occurred while fetching prerequisites for " + moduleString);
        }
    }

    public void removeModule(String moduleString) {
        Integer sem = modules.get(moduleString);
        studyPlan.get(sem - 1).remove(moduleString);
        modules.remove(moduleString);
        System.out.println("Removed " + moduleString);
    }

    public ArrayList<ArrayList<String>> getStudyPlan() {
        return studyPlan;
    }
}
