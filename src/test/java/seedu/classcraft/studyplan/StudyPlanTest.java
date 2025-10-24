package seedu.classcraft.studyplan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;

/**
 * Tests for the StudyPlan class, focusing on the creation of the sample study plan.
 * Note: Since createSampleStudyPlan involves network calls (NUSModsFetcher),
 * these tests might fail if the network is down or the NUSMods API changes.
 */
public class StudyPlanTest {

    /**
     * Tests the createSampleStudyPlan method for correct structure and content.
     */
    @Test
    void createSampleStudyPlan_validCreation_correctStructureAndContent() {
        StudyPlan samplePlan = StudyPlan.createSampleStudyPlan();

        assertNotNull(samplePlan, "Sample study plan should not be null.");

        ArrayList<ArrayList<Module>> plan = samplePlan.getStudyPlan();
        assertEquals(8, plan.size(), "Sample study plan should have 8 semesters.");

        ArrayList<Module> sem1Modules = plan.get(0);
        assertEquals(2, sem1Modules.size(), "Semester 1 should have 2 modules.");

        // Find CS1010 using Java assert for the required check
        boolean cs1010Found = false;
        for (Module module : sem1Modules) {
            if (module.getModCode().equals("CS1010")) {
                assert module.getSemesterTaught() == 1 : "CS1010 semesterTaught not 1";
                cs1010Found = true;
            }
        }

        assertTrue(cs1010Found, "CS1010 should be in Semester 1.");

        ArrayList<Module> sem2Modules = plan.get(1);
        assertEquals(2, sem2Modules.size(), "Semester 2 should have 2 modules.");

        ArrayList<Module> sem3Modules = plan.get(2);
        assertEquals(1, sem3Modules.size(), "Semester 3 should have 1 module.");
        assertEquals("CS2040S", sem3Modules.get(0).getModCode(), "First module in Semester 3 should be CS2040S.");

        for (int i = 3; i < 8; i++) {
            assertTrue(plan.get(i).isEmpty(), "Semester " + (i + 1) + " should be empty in the current sample plan.");
        }
    }
}

