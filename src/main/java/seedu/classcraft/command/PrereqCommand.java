package seedu.classcraft.command;

import com.fasterxml.jackson.databind.JsonNode;
import seedu.classcraft.nusmodsfetcher.NUSmodsFetcher;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

public class PrereqCommand extends Command {

    private String moduleCode;

    public PrereqCommand(String moduleCode) {
        super();
        this.moduleCode = moduleCode;
    }

    @Override
    public void executeCommand(StudyPlan studyPlan, Ui ui) {
        try {
            // Fetch module information from API
            String moduleTitle = NUSmodsFetcher.getModuleTitle(moduleCode);
            JsonNode rootJson = NUSmodsFetcher.fetchModuleJson(moduleCode);
            JsonNode prereqTree = rootJson.get("prereqTree");

            // Delegate display to UI
            ui.displayPrerequisites(moduleCode, moduleTitle, prereqTree);

        } catch (Exception e) {
            // Delegate error display to UI
            ui.displayPrereqError(moduleCode);
        }
    }
}
