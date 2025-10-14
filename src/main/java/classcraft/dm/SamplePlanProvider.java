package seedu.duke.dm;

import java.util.HashMap;
import java.util.Map;

import seedu.duke.data.GraduationRequirements;
import seedu.duke.data.Module;
import seedu.duke.data.StudyPlan;
import seedu.duke.data.StudyPlanEntry;

/**
 * SamplePlanProvider: Responsible for creating and storing static data models,
 * including the pre-made sample CEG study plan and default graduation requirements.
 * This acts as the Data Model 3 (DM3) component.
 */
public class SamplePlanProvider {

    // 1. Static fields to hold the pre-made data
    private static final StudyPlan SAMPLE_STUDY_PLAN;  // this is immutable (?)
    private static final GraduationRequirements DEFAULT_REQUIREMENTS;

    // 2. Static Initialization Block: Data is created here when the class is loaded.
    static {
        // --- 2.1 Initialize Modules (A few examples) ---
        Module cs1010 = new Module("CS1010", "Programming Methodology", 4, "Core");
        Module ma1511 = new Module("MA1511", "Engineering Calculus", 2, "Core");
        Module cg1111 = new Module("CG1111A", "Engineering Principles and Practice I", 4, "Core");
        Module dtc = new Module("DTK1234", "Design Thinking", 4, "General-Education");
        Module uem = new Module("UEM1001", "Unrestricted Elective", 4, "UEM");

        // --- 2.2 Create Sample Study Plan ---
        SAMPLE_STUDY_PLAN = createSampleStudyPlan(cs1010, ma1511, cg1111, dtc, uem);

        // --- 2.3 Create Default Graduation Requirements ---
        DEFAULT_REQUIREMENTS = createDefaultCEGRequirements();
    }

    private SamplePlanProvider() {
    }

    // --- Private Helper Methods for Initialization ---

    /**
     * Helper to create the actual sample plan structure.
     */
    private static StudyPlan createSampleStudyPlan(Module... modules) {
        StudyPlan plan = new StudyPlan("Sample CEG 4-Year Plan");

        // Example schedule: Year 1 Semester 1
        plan.addEntry(new StudyPlanEntry(modules[0], 1, 1)); // CS1010
        plan.addEntry(new StudyPlanEntry(modules[1], 1, 1)); // MA1511
        plan.addEntry(new StudyPlanEntry(modules[2], 1, 1)); // CG1111A
        plan.addEntry(new StudyPlanEntry(modules[3], 1, 2)); // DTK1234 (Year 1 Sem 2)
        plan.addEntry(new StudyPlanEntry(modules[4], 2, 1)); // UEM1001 (Year 2 Sem 1)

        // In the final version, ensure all necessary modules are added here.
        return plan;
    }

    /**
     * Helper to define CEG default requirements.
     */
    private static GraduationRequirements createDefaultCEGRequirements() {
        // NOTE: Verify these numbers with actual NUS CEG requirements.
        int totalMCs = 160;

        Map<String, Integer> categoryMap = new HashMap<>();
        categoryMap.put("Core", 80);
        categoryMap.put("CEG-Elective", 28);
        categoryMap.put("General-Education", 20);
        categoryMap.put("UEM", 32);

        return new GraduationRequirements(totalMCs, categoryMap);
    }

    // --- Public Static Accessors (API for DM2 and Display) ---

    /**
     * Returns the pre-made sample study plan for viewing.
     *
     * @return The static sample StudyPlan instance.
     */
    public static StudyPlan getSampleStudyPlan() {
        return SAMPLE_STUDY_PLAN;
    }

    /**
     * Returns the default CEG graduation requirements.
     *
     * @return The static GraduationRequirements instance.
     */
    public static GraduationRequirements getDefaultCEGRequirements() {
        return DEFAULT_REQUIREMENTS;
    }
}
