package seedu.classcraft.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.studyplan.Module;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UI Class Tests")
class UiTest {

    private Ui ui;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ObjectMapper objectMapper;
    private String line = "=============================================================" +
            "====================================================================" + System.lineSeparator();

    @BeforeEach
    void setUp() {
        ui = new Ui();
        objectMapper = new ObjectMapper();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Helper method to create a Module
     */
    private Module createModule(String modCode, String modName, int credits) {
        return new Module(modName, modCode, credits, "", new ArrayList<>(), 0, 1);
    }

    /**
     * Helper method to create a Module with prerequisites list
     */
    private Module createModuleWithPrereqs(String modCode, String modName, int credits, List<String> prereqs) {
        return new Module(modName, modCode, credits, "", prereqs, 0, 1);
    }

    //printMessage Tests
    @Test
    @DisplayName("Should print message with separator lines")
    void testPrintMessage() {
        String testMessage = "Hello World";
        ui.printMessage(testMessage);

        String output = outContent.toString();
        assertTrue(output.contains(testMessage));
        assertTrue(output.contains(line));
    }

    @Test
    @DisplayName("Should handle empty message")
    void testPrintEmptyMessage() {
        ui.printMessage("");
        String output = outContent.toString();
        assertTrue(output.contains(line));
    }

    @Test
    @DisplayName("Should handle multiline message")
    void testPrintMultilineMessage() {
        String multilineMessage = "Line 1\nLine 2\nLine 3";
        ui.printMessage(multilineMessage);

        String output = outContent.toString();
        assertTrue(output.contains("Line 1"));
        assertTrue(output.contains("Line 2"));
        assertTrue(output.contains("Line 3"));
    }

    //showError Tests
    @Test
    @DisplayName("Should display error message with ERROR prefix")
    void testShowError() {
        String errorMessage = "Test error occurred";
        ui.showError(errorMessage);

        String output = outContent.toString();
        assertTrue(output.contains("ERROR: " + errorMessage));
        assertTrue(output.contains(line));
    }

    @Test
    @DisplayName("Should handle empty error message")
    void testShowEmptyError() {
        ui.showError("");
        String output = outContent.toString();
        assertTrue(output.contains("ERROR:"));
    }

    @Test
    @DisplayName("Should display error with special characters")
    void testShowErrorWithSpecialCharacters() {
        String errorMessage = "Invalid input: @#$%";
        ui.showError(errorMessage);
        String output = outContent.toString();
        assertTrue(output.contains(errorMessage));
    }

    //showMessage Tests
    @Test
    @DisplayName("Should display message with separators")
    void testShowMessage() {
        String message = "Operation successful";
        ui.showMessage(message);

        String output = outContent.toString();
        assertTrue(output.contains(message));
        assertTrue(output.contains(line));
    }

    @Test
    @DisplayName("Should display long message correctly")
    void testShowLongMessage() {
        String longMessage = "This is a very long message that contains multiple words";
        ui.showMessage(longMessage);

        String output = outContent.toString();
        assertTrue(output.contains(longMessage));
    }

    //displayTotalCredits Tests
    @Test
    @DisplayName("Should display total credits when semester is -1")
    void testDisplayOverallTotalCredits() {
        ui.displayTotalCredits(-1, 160);
        String output = outContent.toString();
        assertTrue(output.contains("Total Module Credits: 160"));
    }

    @Test
    @DisplayName("Should display credits for semester 1")
    void testDisplaySemester1Credits() {
        ui.displayTotalCredits(0, 20);
        String output = outContent.toString();
        assertTrue(output.contains("Semester 1 Module Credits: 20"));
    }

    @Test
    @DisplayName("Should display credits for semester 2")
    void testDisplaySemester2Credits() {
        ui.displayTotalCredits(1, 18);
        String output = outContent.toString();
        assertTrue(output.contains("Semester 2 Module Credits: 18"));
    }

    @Test
    @DisplayName("Should display zero credits correctly")
    void testDisplayZeroCredits() {
        ui.displayTotalCredits(0, 0);
        String output = outContent.toString();
        assertTrue(output.contains("Semester 1 Module Credits: 0"));
    }

    //displayPrereqError Tests
    @Test
    @DisplayName("Should display prerequisite error message")
    void testDisplayPrereqError() {
        String moduleCode = "CS2103T";
        ui.displayPrereqError(moduleCode);

        String output = outContent.toString();
        assertTrue(output.contains("Error: Could not fetch prerequisites for " + moduleCode));
        assertTrue(output.contains("Please check that the module code is valid"));
    }

    @Test
    @DisplayName("Should handle invalid module code in prereq error")
    void testDisplayPrereqErrorWithInvalidCode() {
        String invalidCode = "INVALID123";
        ui.displayPrereqError(invalidCode);

        String output = outContent.toString();
        assertTrue(output.contains(invalidCode));
    }

    @Test
    @DisplayName("Should handle empty module code in prereq error")
    void testDisplayPrereqErrorWithEmptyCode() {
        ui.displayPrereqError("");
        String output = outContent.toString();
        assertTrue(output.contains("Error: Could not fetch prerequisites for"));
    }

    //displayPrerequisites Tests
    @Test
    @DisplayName("Should display module with no prerequisites")
    void testDisplayNoPrerequisites() {
        String moduleCode = "CS1010";
        String moduleTitle = "Programming Methodology";

        ui.displayPrerequisites(moduleCode, moduleTitle, null);

        String output = outContent.toString();
        assertTrue(output.contains("Module: " + moduleCode + " - " + moduleTitle));
        assertTrue(output.contains("Prerequisites: None"));
        assertTrue(output.contains("This module has no prerequisites. You can take it in any semester!"));
    }

    @Test
    @DisplayName("Should display module with simple prerequisite")
    void testDisplaySimplePrerequisite() throws Exception {
        String moduleCode = "CS2040";
        String moduleTitle = "Data Structures and Algorithms";
        String prereqJson = "{\"moduleCode\": \"CS1010\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);

        ui.displayPrerequisites(moduleCode, moduleTitle, prereqTree);

        String output = outContent.toString();
        assertTrue(output.contains("Module: " + moduleCode + " - " + moduleTitle));
        assertTrue(output.contains("CS1010"));
        assertTrue(output.contains("Prerequisites:"));
        assertTrue(output.contains("Note: You need to satisfy these prerequisites before taking this module."));
    }

    @Test
    @DisplayName("Should display module with OR prerequisites")
    void testDisplayOrPrerequisites() throws Exception {
        String moduleCode = "CS2030";
        String moduleTitle = "Programming Methodology II";
        String prereqJson = "{\"or\": [{\"moduleCode\": \"CS1010\"}, {\"moduleCode\": \"CS1101S\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);

        ui.displayPrerequisites(moduleCode, moduleTitle, prereqTree);

        String output = outContent.toString();
        assertTrue(output.contains("CS1010"));
        assertTrue(output.contains("CS1101S"));
        assertTrue(output.contains("OR"));
    }

    @Test
    @DisplayName("Should display module with AND prerequisites")
    void testDisplayAndPrerequisites() throws Exception {
        String moduleCode = "CS3230";
        String moduleTitle = "Design and Analysis of Algorithms";
        String prereqJson = "{\"and\": [{\"moduleCode\": \"CS2040\"}, {\"moduleCode\": \"MA1521\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);

        ui.displayPrerequisites(moduleCode, moduleTitle, prereqTree);

        String output = outContent.toString();
        assertTrue(output.contains("CS2040"));
        assertTrue(output.contains("MA1521"));
        assertTrue(output.contains("AND"));
    }

    @Test
    @DisplayName("Should display module with nested prerequisites")
    void testDisplayNestedPrerequisites() throws Exception {
        String moduleCode = "CS4234";
        String moduleTitle = "Optimisation Algorithms";
        String prereqJson = "{\"and\": [{\"or\": [{\"moduleCode\": \"CS2040\"}, " +
                "{\"moduleCode\": \"CS2040C\"}]}, {\"moduleCode\": \"MA1521\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);

        ui.displayPrerequisites(moduleCode, moduleTitle, prereqTree);

        String output = outContent.toString();
        assertTrue(output.contains("CS2040"));
        assertTrue(output.contains("MA1521"));
    }

    @Test
    @DisplayName("Should strip grade requirements from prerequisites")
    void testDisplayPrerequisitesWithGrades() throws Exception {
        String moduleCode = "CS3230";
        String moduleTitle = "Design and Analysis of Algorithms";
        String prereqJson = "{\"moduleCode\": \"CS2040:B\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);

        ui.displayPrerequisites(moduleCode, moduleTitle, prereqTree);

        String output = outContent.toString();
        assertTrue(output.contains("CS2040"));
        assertFalse(output.contains(":B"));
    }

    @Test
    @DisplayName("Should ignore bridging modules in display")
    void testDisplayPrerequisitesIgnoresBridgingModules() throws Exception {
        String moduleCode = "MA1521";
        String moduleTitle = "Calculus for Computing";
        String prereqJson = "{\"or\": [{\"moduleCode\": \"MA1301\"}, {\"moduleCode\": \"CS1010\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);

        ui.displayPrerequisites(moduleCode, moduleTitle, prereqTree);

        String output = outContent.toString();
        assertFalse(output.contains("MA1301"));
        assertTrue(output.contains("CS1010"));
    }

    @Test
    @DisplayName("Should handle null prereqTree node")
    void testDisplayPrerequisitesNullNode() throws Exception {
        String moduleCode = "CS1010";
        String moduleTitle = "Programming Methodology";
        JsonNode nullNode = objectMapper.readTree("{}").get("nonexistent");

        ui.displayPrerequisites(moduleCode, moduleTitle, nullNode);

        String output = outContent.toString();
        assertTrue(output.contains("Prerequisites: None"));
    }

    @Test
    @DisplayName("Should handle textual prereqTree nodes")
    void testDisplayTextualPrerequisites() throws Exception {
        String moduleCode = "CS2040";
        String moduleTitle = "Data Structures";
        String prereqJson = "\"CS1010\"";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);

        ui.displayPrerequisites(moduleCode, moduleTitle, prereqTree);

        String output = outContent.toString();
        assertTrue(output.contains("CS1010"));
    }

    //displayCurrentPlan Tests
    @Test
    @DisplayName("Should display empty study plan")
    void testDisplayEmptyPlan() {
        StudyPlan emptyPlan = new StudyPlan(4);
        ui.displayCurrentPlan(emptyPlan);

        String output = outContent.toString();
        assertTrue(output.contains("Current Study Plan"));
        assertTrue(output.contains("Semester 1:"));
        assertTrue(output.contains("(Empty)"));
    }

    @Test
    @DisplayName("Should display study plan with modules")
    void testDisplayPlanWithModules() {
        StudyPlan plan = new StudyPlan(2);

        Module mod1 = createModule("CS1010", "Programming Methodology", 4);
        Module mod2 = createModule("MA1521", "Calculus for Computing", 4);
        plan.addModule(mod1, 1);
        plan.addModule(mod2, 1);

        ui.displayCurrentPlan(plan);

        String output = outContent.toString();
        assertTrue(output.contains("Current Study Plan"));
        assertTrue(output.contains("CS1010"));
        assertTrue(output.contains("Programming Methodology"));
        assertTrue(output.contains("MA1521"));
        assertTrue(output.contains("Calculus for Computing"));
    }

    @Test
    @DisplayName("Should display modules with prerequisites info")
    void testDisplayPlanWithModulePrereqs() {
        StudyPlan plan = new StudyPlan(2);

        List<String> prereqs = new ArrayList<>();
        prereqs.add("CS1010");
        Module mod = createModuleWithPrereqs("CS2040", "Data Structures", 4, prereqs);
        plan.addModule(mod, 1);

        ui.displayCurrentPlan(plan);

        String output = outContent.toString();
        assertTrue(output.contains("CS2040"));
        assertTrue(output.contains("Prerequisites: CS1010"));
    }

    @Test
    @DisplayName("Should display sample plan with correct title")
    void testDisplaySamplePlan() {
        StudyPlan samplePlan = new StudyPlan(2);
        Module mod = createModule("CS1010", "Programming Methodology", 4);
        samplePlan.addModule(mod, 1);

        ui.displaySamplePlan(samplePlan);

        String output = outContent.toString();
        assertTrue(output.contains("CEG Sample Study Plan"));
        assertTrue(output.contains("CS1010"));
    }

    @Test
    @DisplayName("Should display plan with multiple semesters")
    void testDisplayPlanMultipleSemesters() {
        StudyPlan plan = new StudyPlan(3);

        Module mod1 = createModule("CS1010", "Programming Methodology", 4);
        Module mod2 = createModule("CS2040", "Data Structures", 4);
        Module mod3 = createModule("CS3230", "Algorithms", 4);

        plan.addModule(mod1, 1);
        plan.addModule(mod2, 2);
        plan.addModule(mod3, 3);

        ui.displayCurrentPlan(plan);

        String output = outContent.toString();
        assertTrue(output.contains("Semester 1:"));
        assertTrue(output.contains("Semester 2:"));
        assertTrue(output.contains("Semester 3:"));
        assertTrue(output.contains("CS1010"));
        assertTrue(output.contains("CS2040"));
        assertTrue(output.contains("CS3230"));
    }

    @Test
    @DisplayName("Should display mixed empty and non-empty semesters")
    void testDisplayPlanMixedSemesters() {
        StudyPlan plan = new StudyPlan(3);

        Module mod1 = createModule("CS1010", "Programming Methodology", 4);
        plan.addModule(mod1, 1);
        Module mod3 = createModule("CS2040", "Data Structures", 4);
        plan.addModule(mod3, 3);

        ui.displayCurrentPlan(plan);

        String output = outContent.toString();
        assertTrue(output.contains("Semester 1:"));
        assertTrue(output.contains("Semester 2:"));
        assertTrue(output.contains("(Empty)"));
        assertTrue(output.contains("Semester 3:"));
    }

    //Edge Cases Tests
    @Test
    @DisplayName("Should handle module with no prerequisites display correctly")
    void testModuleWithNoPrerequisitesDisplay() {
        Module mod = createModule("CS1010", "Programming", 4);
        StudyPlan plan = new StudyPlan(1);
        plan.addModule(mod, 1);

        ui.displayCurrentPlan(plan);

        String output = outContent.toString();
        assertTrue(output.contains("(No Prerequisites)"));
    }

    @Test
    @DisplayName("Should handle module with multiple prerequisites display")
    void testModuleWithMultiplePrerequisitesDisplay() {
        List<String> prereqs = new ArrayList<>();
        prereqs.add("CS1010");
        prereqs.add("MA1521");
        prereqs.add("CS1231");

        Module mod = createModuleWithPrereqs("CS3230", "Algorithms", 4, prereqs);
        StudyPlan plan = new StudyPlan(1);
        plan.addModule(mod, 1);

        ui.displayCurrentPlan(plan);

        String output = outContent.toString();
        assertTrue(output.contains("CS1010"));
        assertTrue(output.contains("MA1521"));
        assertTrue(output.contains("CS1231"));
    }
}
