package seedu.duke.StudyPlan;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StudyPlanTest {
    @Test
    public void StudyPlanTest_with_four_mods() throws Exception {
        StudyPlan studyPlan = new StudyPlan(8);
        studyPlan.addModule("EE2026",1);
        studyPlan.addModule("CG2271",2);
        studyPlan.addModule("CG2028",2);
        studyPlan.addModule("CS2040C",3);

        ArrayList<ArrayList<String>> studyPlanList = studyPlan.getStudyPlan();

        assertNotNull(studyPlanList, "StudyPlan list should not be null");

        assertEquals(8, studyPlanList.size(), "StudyPlan list should have 8 sems");
        assertEquals(2, studyPlanList.get(1).size(), "StudyPlan sem 2 should have 2 mods");

        assertEquals("EE2026", studyPlanList.get(0).get(0), "The module EE2026 should be in sem 1");
        assertEquals("CG2271", studyPlanList.get(1).get(0), "The module CG2271 should be in sem 1");
        assertEquals("CG2028", studyPlanList.get(1).get(1), "The module CG2028 should be in sem 1");
        assertEquals("CS2040C", studyPlanList.get(2).get(0), "The module CS2040C should be in sem 1");
    }


}
