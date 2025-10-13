package classcraft.dm;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import classcraft.data.Module;
import classcraft.data.StudyPlan;
import classcraft.data.StudyPlanEntry;
import classcraft.data.GraduationRequirements;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SamplePlanProvider (DM3) to ensure static data is correctly initialized
 * and immutable as expected.
 */
class SamplePlanProviderTest {

    private static StudyPlan samplePlan;
    private static GraduationRequirements requirements;

    /**
     * Set up method: Executes once before all tests.
     * Loads the static data provided by DM3.
     */
    @BeforeAll
    static void setUp() {
        // Access the static data via the provider methods
        samplePlan = SamplePlanProvider.getSampleStudyPlan();
        requirements = SamplePlanProvider.getDefaultCEGRequirements();
    }

    // --- Tests for SamplePlanProvider initialization ---

    @Test
    void testSamplePlanInitialization() {
        // 1. Check if the plan was created
        assertNotNull(samplePlan, "SamplePlanProvider should successfully initialize the sample study plan.");

        // 2. Check if the plan has the expected name
        assertEquals("Sample CEG 4-Year Plan", samplePlan.getPlanName(),
                "The plan name should match the initialization value.");

        // 3. Check if the plan has modules (based on the current SamplePlanProvider data)
        assertFalse(samplePlan.getEntries().isEmpty(), "Sample plan should not be empty.");
        assertEquals(5, samplePlan.getEntries().size(),
                "Sample plan should contain 5 initial entries (based on current implementation).");
    }

    @Test
    void testRequirementsInitialization() {
        // 1. Check if requirements were created
        assertNotNull(requirements, "SamplePlanProvider should successfully initialize the graduation requirements.");

        // 2. Check a few key values (based on the current example data: 160)
        assertEquals(160, requirements.getTotalRequiredMCs(),
                "Total required MCs should be 160 (based on example data).");

        // 3. Check if all categories are present (4 categories in example)
        Map<String, Integer> categoryMap = requirements.getCategoryMCs();
        assertEquals(4, categoryMap.size(), "Graduation requirements should have 4 categories defined.");
        assertTrue(categoryMap.containsKey("Core"), "Requirements must contain the 'Core' category.");
        assertEquals(32, categoryMap.get("UEM"), "UEM required MCs should be 32 (based on example data).");
    }

    // --- Tests for Immutability ---

    @Test
    void testSamplePlanImmutability() {
        // Try to modify the list returned by the getter
        List<StudyPlanEntry> entriesView = samplePlan.getEntries();

        // Create a dummy module to attempt adding
        Module dummyMod = new Module("DUMMY999", "Dummy Module", 4, "UEM");
        StudyPlanEntry dummyEntry = new StudyPlanEntry(dummyMod, 1, 1);

        // Attempt 1: Try adding to the list returned by getEntries()
        assertThrows(UnsupportedOperationException.class, () -> {
            entriesView.add(dummyEntry);
        }, "Adding to the list returned by getEntries() should fail.");

        // Verify the original size is unchanged
        assertEquals(5, samplePlan.getEntries().size(),
                "Original plan size must remain unchanged after failed modification attempt.");
    }

    @Test
    void testRequirementsMapImmutability() {
        // Try to modify the map returned by the getter
        Map<String, Integer> categoriesView = requirements.getCategoryMCs();

        // Attempt 1: Try putting a new entry into the map
        assertThrows(UnsupportedOperationException.class, () -> {
            categoriesView.put("NewCategory", 10);
        }, "Modifying the map returned by getCategoryMCs() should fail.");

        // Verify the original size is unchanged
        assertEquals(4, categoriesView.size(),
                "Original map size must remain unchanged after failed modification attempt.");
    }

    // --- Tests for specific data integrity ---

    @Test
    void testSpecificModuleDataIntegrity() {
        // Check if one of the sample modules has the correct properties
        StudyPlanEntry cs1010Entry = samplePlan.getEntries().stream()
                .filter(e -> e.module().moduleCode().equals("CS1010"))
                .findFirst()
                .orElse(null);

        assertNotNull(cs1010Entry, "CS1010 should exist in the sample plan.");

        Module cs1010 = cs1010Entry.module();
        assertEquals("Core", cs1010.requirementType(), "CS1010 requirement type must be 'Core'.");
        assertEquals(4, cs1010.credits(), "CS1010 credits must be 4.");
        assertEquals(1, cs1010Entry.year(), "CS1010 should be planned in Year 1.");
    }
}
