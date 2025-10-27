package seedu.classcraft.studyplan;

import org.junit.jupiter.api.Test;
import seedu.classcraft.storage.Storage;
// import seedu.duke.studyplan.Module;
// import seedu.duke.studyplan.StudyPlan;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudyPlanTest {
    @Test
    public void studyPlanTestWithFourMods() throws Exception {
        StudyPlan studyPlan = new StudyPlan(8);
        Storage storage = new Storage("./ClassCraftData/studyPlan.txt");
        studyPlan.addModule("EE2026", 1, storage, true);
        studyPlan.addModule("CG2271", 2, storage, true );
        studyPlan.addModule("CG2028", 2, storage, true);
        studyPlan.addModule("CS2040C", 3, storage, true);

        ArrayList<ArrayList<Module>> studyPlanList = studyPlan.getStudyPlan();

        assertNotNull(studyPlanList, "StudyPlan list should not be null");

        assertEquals(8, studyPlanList.size(), "StudyPlan list should have 8 sems");
        assertEquals(2, studyPlanList.get(1).size(), "StudyPlan sem 2 should have 2 mods");

        assertEquals("EE2026", studyPlanList.get(0).get(0).getModCode(), "The module EE2026 should be in sem 1");
        assertEquals("CG2271", studyPlanList.get(1).get(0).getModCode(), "The module CG2271 should be in sem 2");
        assertEquals("CG2028", studyPlanList.get(1).get(1).getModCode(), "The module CG2028 should be in sem 2");
        assertEquals("CS2040C", studyPlanList.get(2).get(0).getModCode(), "The module CS2040C should be in sem 3");
    }
}

