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
        assertEquals("", NUSmodsFetcher.getModulePrerequisites(moduleCode), "Missing prerequisite field should return empty string");
    }

    @Test
    public void getModuleCredits_validModuleCode_returnsCorrectCredits() throws Exception {
        String moduleCode = "CS1231";
        String expectedCredits = "4";
        String actualCredits = NUSmodsFetcher.getModuleCredits(moduleCode);
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
        String expectedFaculty = "Engineering";
        String actualFaculty = NUSmodsFetcher.getFaculty(moduleCode);
        assertEquals(expectedFaculty, actualFaculty, "Retrieved faculty should match actual");
    }

    @Test
    public void getModuleDescription_validModuleCode_returnsCorrectDescription() throws Exception {
        String moduleCode = "CS1010";
        String expectedDescriptionStart = "This module introduces the fundamental concepts of programming";
        String actualDescription = NUSmodsFetcher.getModuleDescription(moduleCode);
        assertEquals(true, actualDescription.startsWith(expectedDescriptionStart), "Retrieved module description should start with expected text");
    }

    @Test
    public void getModulePrerequisites_validModuleCode_returnsCorrectPrerequisites() throws Exception {
        String moduleCode = "CS2040C";
        String expectedPrerequisites = "CS1010 or CS1010E or CS1101S";
        String actualPrerequisites = NUSmodsFetcher.getModulePrerequisites(moduleCode);
        assertEquals(expectedPrerequisites, actualPrerequisites, "Retrieved module prerequisites should match actual");
    }
}
