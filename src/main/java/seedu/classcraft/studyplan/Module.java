package seedu.classcraft.studyplan;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Contains all the relevant info about a module
 */
public class Module {
    private String modName;
    private String modCode;
    private int modCreds;
    private String modDescription;
    private List<String> prerequisites;
    private JsonNode prereqTree;
    private int semesterTaught;
    private int defaultSemester;
    private int prerequisitesCount; // default 0, updates when added to ModuleHandler

    /**
     * @@author lingru
     * The status of the module (e.g., PLANNED, COMPLETED, EXEMPTED).
     */
    private ModuleStatus status;

    public Module(String modName, String modCode, int modCreds, String modDescription, List<String> prerequisites,
                  int semesterTaught, int defaultSemester) {

        this.modName = modName;
        this.modCode = modCode;
        this.modCreds = modCreds;
        this.modDescription = modDescription;
        this.prerequisites = prerequisites;
        this.prereqTree = null;
        this.semesterTaught = semesterTaught;
        this.defaultSemester = defaultSemester;
        this.prerequisitesCount = 0;

        // @@author lingru
        // By default, a newly created module is considered 'PLANNED'
        this.status = ModuleStatus.PLANNED;

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

    public void setModCreds(int modCreds) {
        this.modCreds = modCreds;
    }

    public int getModCreds() {
        return modCreds;
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

    // @@author lingru
    /**
     * @@author lingru
     * Gets the current status of the module.
     * @return The ModuleStatus (PLANNED, COMPLETED, or EXEMPTED).
     */
    public ModuleStatus getStatus() {
        return status;
    }

    /**
     * @@author lingru
     * Sets the status of the module.
     * @param status The new status to set.
     */
    public void setStatus(ModuleStatus status) {
        this.status = status;
    }
    // @@author

    public JsonNode getPrereqTree() {
        return prereqTree;
    }

    public void setPrereqTree(JsonNode prereqTree) {
        this.prereqTree = prereqTree;
    }

    public String getPrerequisitesDisplay() {
        List<String> prereqs = getPrerequisites();

        if (prereqs == null || prereqs.isEmpty()) {
            return "(No Prerequisites)";
        }

        String prereqsString = String.join(", ", prereqs);
        return " (Prerequisites: " + prereqsString + ")";
    }
}

