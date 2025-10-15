package seedu.classcraft.studyplan;

import java.util.List;

/**
 * Contains all the relevant info about a module
 */
public class Module {
    private String modName;
    private String modCode;
    private String modDescription;
    private List<String> prerequisites;
    private int semesterTaught;
    private int defaultSemester;
    private int prerequisitesCount; // default 0, updates when added to ModuleHandler

    public Module(String modName, String modCode, String modDescription, List<String> prerequisites,
                   int semesterTaught, int defaultSemester) {
        this.modName = modName;
        this.modCode = modCode;
        this.modDescription = modDescription;
        this.prerequisites = prerequisites;
        this.semesterTaught = semesterTaught;
        this.defaultSemester = defaultSemester;
        this.prerequisitesCount = 0;
    }

    public int getPrerequisitesCount() {
        return prerequisitesCount;
    }

    public void setPrerequisitesCount(int prerequisitesCount) {
        this.prerequisitesCount = prerequisitesCount;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getModCode() {
        return modCode;
    }

    public void setModCode(String modCode) {
        this.modCode = modCode;
    }

    public String getModDescription() {
        return modDescription;
    }

    public void setModDescription(String modDescription) {
        this.modDescription = modDescription;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public int getSemesterTaught() {
        return semesterTaught;
    }

    public void setSemesterTaught(int semesterTaught) {
        this.semesterTaught = semesterTaught;
    }

    public int getDefaultSemester() {
        return defaultSemester;
    }

    public void setDefaultSemester(int defaultSemester) {
        this.defaultSemester = defaultSemester;
    }

    public String getPrerequisitesDisplay() {
        List<String> prereqs = getPrerequisites();

        if (prereqs == null || prereqs.isEmpty()) {
            return "(No Prerequisites)";
        }

        String prereqsString = String.join(", ", prereqs);
        return " (Prereqs: " + prereqsString + ")";
    }
}

