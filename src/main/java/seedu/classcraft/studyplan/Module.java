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

    /**
     * @@author lingru
     * The status of the module (e.g., PLANNED, COMPLETED, EXEMPTED).
     */
    private ModuleStatus status;

    /**
     * @@author lingru
     * The number of Module Credits (MCs) this module is worth.
     */
    private int moduleCredit;

    public Module(String modName, String modCode, String modDescription, List<String> prerequisites,
                  int semesterTaught, int defaultSemester, int moduleCredit
    ) {
        this.modName = modName;
        this.modCode = modCode;
        this.modDescription = modDescription;
        this.prerequisites = prerequisites;
        this.semesterTaught = semesterTaught;
        this.defaultSemester = defaultSemester;
        this.prerequisitesCount = 0;

        // @@author lingru
        // By default, a newly created module is considered 'PLANNED'
        this.status = ModuleStatus.PLANNED;
        this.moduleCredit = moduleCredit;
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

    public int getModuleCredit() {
        return moduleCredit;
    }

    public void setModuleCredit(int moduleCredit) {
        this.moduleCredit = moduleCredit;
    }
    // @@author

    public String getPrerequisitesDisplay() {
        List<String> prereqs = getPrerequisites();

        if (prereqs == null || prereqs.isEmpty()) {
            return "(No Prerequisites)";
        }

        String prereqsString = String.join(", ", prereqs);
        return " (Prereqs: " + prereqsString + ")";
    }
}