package seedu.classcraft.nusmodsfetcher;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NUSmodsFetcherTest {

    @Test
    public void getModuleTitle_validModuleCode_returnsCorrectTitle() throws Exception {
        String moduleCode = "CS2040C";
        String expectedTitle = "Data Structures and Algorithms";
        String actualTitle = NUSmodsFetcher.getModuleTitle(moduleCode);
        assertEquals(expectedTitle, actualTitle, "Retrieved module title should match actual");
    }

    @Test
    public void getModuleTitle_inValidModuleCode_throwsException() {
        String moduleCode = "INVALIDCODE";
        assertThrows(Exception.class, () -> NUSmodsFetcher.getModuleTitle(moduleCode));
    }

    @Test
    public void extractField_missingField_returnsEmptyString() throws Exception {
        String moduleCode = "CS1010";
        assertEquals("", NUSmodsFetcher.getModulePrerequisites(moduleCode),
            "Missing prerequisite field should return empty string");
    }

    @Test
    public void getModuleCredits_validModuleCode_returnsCorrectCredits() throws Exception {
        String moduleCode = "CS1231";
        int expectedCredits = 4;
        int actualCredits = NUSmodsFetcher.getModuleCredits(moduleCode);
        assertEquals(expectedCredits, actualCredits, "Retrieved module credits should match actual");
    }

    @Test
    public void getDepartment_validModuleCode_returnsCorrectDepartment() throws Exception {
        String moduleCode = "MA1508E";
        String expectedDepartment = "Mathematics";
        String actualDepartment = NUSmodsFetcher.getDepartment(moduleCode);
        assertEquals(expectedDepartment, actualDepartment, "Retrieved department should match actual");
    }

    @Test
    public void getFaculty_validModuleCode_returnsCorrectFaculty() throws Exception {
        String moduleCode = "EG1311";
        String expectedFaculty = "College of Design and Engineering";
        String actualFaculty = NUSmodsFetcher.getFaculty(moduleCode);
        assertEquals(expectedFaculty, actualFaculty, "Retrieved faculty should match actual");
    }

    @Test
    public void getModuleDescription_validModuleCode_returnsCorrectDescription() throws Exception {
        String moduleCode = "CS1010";
        String expectedDescriptionStart = "This course introduces the fundamental concepts of problem solving";
        String actualDescription = NUSmodsFetcher.getModuleDescription(moduleCode);
        assertEquals(true, actualDescription.startsWith(expectedDescriptionStart),
            "Retrieved module description should start with expected text");
    }

    @Test
    public void getModulePrerequisites_validModuleCode_returnsCorrectPrerequisites() throws Exception {
        String moduleCode = "CS2040C";
        String expectedPrerequisites = "If undertaking an Undergraduate DegreeTHEN"
            + "( must have completed 1 of CS1010/CS1010A/CS1010E/CS1010J/CS1010S/CS1010X/CS1101S/UTC2851"
            + " at a grade of at least D)";
        String actualPrerequisites = NUSmodsFetcher.getModulePrerequisites(moduleCode);
        assertEquals(expectedPrerequisites, actualPrerequisites,
            "Retrieved module prerequisites should match actual");
    }

    @Test
    public void getModulePrerequisites_noPrerequisites_returnsEmptyString() throws Exception {
        String moduleCode = "GEA1000";
        String expectedPrerequisites = "";
        String actualPrerequisites = NUSmodsFetcher.getModulePrerequisites(moduleCode);
        assertEquals(expectedPrerequisites, actualPrerequisites,
            "Modules with no prerequisites should return empty string");
    }

    @Test
    public void getSemesterOffered_validModuleCode_returnsCorrectSemesters() throws Exception {
        String moduleCode = "CG2111A";
        int expectedSemesters = 2;
        int actualSemesters = NUSmodsFetcher.getSemesterOffered(moduleCode);
        assertEquals(expectedSemesters, actualSemesters,
            "Retrieved semesters taught should match actual");
    }
}
