package seedu.classcraft.studyplan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seedu.classcraft.exceptions.StudyPlanException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Prerequisite Checker Tests")
class PrerequisiteCheckerTest {

    private StudyPlan studyPlan;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studyPlan = new StudyPlan(8);
        objectMapper = new ObjectMapper();
    }

    /**
     * Helper method to create a Module with your constructor signature
     */
    private Module createModule(String modCode, String modName, int credits) {
        return new Module(modName, modCode, credits, "", new ArrayList<>(), 0, 1);
    }

    /**
     * Helper method to create a Module and set its prereqTree
     */
    private Module createModuleWithPrereqTree(String modCode, String modName,
                                              int credits, JsonNode prereqTree) {
        Module module = createModule(modCode, modName, credits);
        module.setPrereqTree(prereqTree);
        return module;
    }

    //No Prerequisites Tests
    @Test
    @DisplayName("Should allow module with null prerequisite tree")
    void testNullPrerequisiteTree() {
        Module module = createModule("CS1010", "Programming Methodology", 4);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 1, studyPlan,false)
        );
    }

    @Test
    @DisplayName("Should allow module with missing prerequisite tree")
    void testMissingPrerequisiteTree() throws Exception {
        JsonNode emptyNode = objectMapper.readTree("{}").get("nonexistent");
        Module module = createModuleWithPrereqTree("CS1010", "Programming Methodology", 4,
                emptyNode);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 1, studyPlan,false)
        );
    }

    //Normal Prerequisites Tests
    @Test
    @DisplayName("Should throw exception when simple prerequisite not met")
    void testSimplePrerequisiteNotMet() throws Exception {
        String prereqJson = "{\"moduleCode\": \"CS1010\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2040", "Data Structures and Algorithms",
                4, prereqTree);

        StudyPlanException exception = assertThrows(StudyPlanException.class, () ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );

        assertTrue(exception.getMessage().contains("Cannot add CS2040"));
        assertTrue(exception.getMessage().contains("semester 2"));
    }

    @Test
    @DisplayName("Should accept module when simple prerequisite is satisfied")
    void testSimplePrerequisiteMet() throws Exception {
        Module prereqModule = createModule("CS1010", "Programming Methodology", 4);
        studyPlan.addModule(prereqModule, 1);

        String prereqJson = "{\"moduleCode\": \"CS1010\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2040", "Data Structures and Algorithms",
                4, prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should accept prerequisite from multiple semesters earlier")
    void testPrerequisiteFromEarlierSemester() throws Exception {
        Module prereqModule = createModule("CS1010", "Programming Methodology", 4);
        studyPlan.addModule(prereqModule, 1);

        String prereqJson = "{\"moduleCode\": \"CS1010\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2040", "Data Structures and Algorithms",
                4, prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 4, studyPlan, false)
        );
    }

    //OR Prerequisites Tests
    @Test
    @DisplayName("Should accept module when first OR prerequisite is met")
    void testOrPrerequisiteFirstOptionMet() throws Exception {
        // Add first option
        Module prereq1 = createModule("CS1010", "Programming Methodology", 4);
        studyPlan.addModule(prereq1, 1);

        String prereqJson = "{\"or\": [{\"moduleCode\": \"CS1010\"}, {\"moduleCode\": \"CS1101S\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2030", "Programming Methodology II", 4,
                prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should accept module when second OR prerequisite is met")
    void testOrPrerequisiteSecondOptionMet() throws Exception {
        Module prereq2 = createModule("CS1101S", "Programming Methodology", 6);
        studyPlan.addModule(prereq2, 1);

        String prereqJson = "{\"or\": [{\"moduleCode\": \"CS1010\"}, {\"moduleCode\": \"CS1101S\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2030", "Programming Methodology II", 4,
                prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should throw exception when no OR prerequisite is met")
    void testOrPrerequisiteNoneMetThrowsException() throws Exception {
        String prereqJson = "{\"or\": [{\"moduleCode\": \"CS1010\"}, {\"moduleCode\": \"CS1101S\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2030", "Programming Methodology II", 4,
                prereqTree);

        assertThrows(StudyPlanException.class, () ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should accept when both OR prerequisites are met")
    void testOrPrerequisiteBothMet() throws Exception {
        Module prereq1 = createModule("CS1010", "Programming Methodology", 4);
        Module prereq2 = createModule("CS1101S", "Programming Methodology", 6);
        studyPlan.addModule(prereq1, 1);
        studyPlan.addModule(prereq2, 1);

        String prereqJson = "{\"or\": [{\"moduleCode\": \"CS1010\"}, {\"moduleCode\": \"CS1101S\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2030", "Programming Methodology II", 4,
                prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    //AND Prerequisites Tests

    @Test
    @DisplayName("Should accept module when all AND prerequisites are met")
    void testAndPrerequisitesAllMet() throws Exception {
        Module prereq1 = createModule("CS2040", "Data Structures and Algorithms", 4);
        Module prereq2 = createModule("MA1521", "Calculus for Computing", 4);
        studyPlan.addModule(prereq1, 1);
        studyPlan.addModule(prereq2, 1);

        String prereqJson = "{\"and\": [{\"moduleCode\": \"CS2040\"}, {\"moduleCode\": \"MA1521\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS3230", "Design and Analysis of Algorithms",
                4, prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should throw exception when only some AND prerequisites are met")
    void testAndPrerequisitesPartiallyMet() throws Exception {
        Module prereq1 = createModule("CS2040", "Data Structures and Algorithms", 4);
        studyPlan.addModule(prereq1, 1);

        String prereqJson = "{\"and\": [{\"moduleCode\": \"CS2040\"}, {\"moduleCode\": \"MA1521\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS3230", "Design and Analysis of Algorithms",
                4, prereqTree);

        assertThrows(StudyPlanException.class, () ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should throw exception when no AND prerequisites are met")
    void testAndPrerequisitesNoneMet() throws Exception {
        String prereqJson = "{\"and\": [{\"moduleCode\": \"CS2040\"}, {\"moduleCode\": \"MA1521\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS3230", "Design and Analysis of Algorithms",
                4, prereqTree);

        assertThrows(StudyPlanException.class, () ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    //Nested Prerequisites Tests
    @Test
    @DisplayName("Should handle nested OR within AND prerequisites")
    void testNestedOrWithinAnd() throws Exception {
        Module prereq1 = createModule("CS2040", "Data Structures and Algorithms", 4);
        Module prereq2 = createModule("MA1521", "Calculus for Computing", 4);
        studyPlan.addModule(prereq1, 1);
        studyPlan.addModule(prereq2, 1);

        String prereqJson = "{\"and\": [{\"or\": [{\"moduleCode\": \"CS2040\"}, " +
                "{\"moduleCode\": \"CS2040C\"}]}, {\"moduleCode\": \"MA1521\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS4234", "Optimisation Algorithms", 4,
                prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should handle nested AND within OR prerequisites")
    void testNestedAndWithinOr() throws Exception {
        Module prereq1 = createModule("CS2040", "Data Structures and Algorithms", 4);
        Module prereq2 = createModule("MA1521", "Calculus for Computing", 4);
        studyPlan.addModule(prereq1, 1);
        studyPlan.addModule(prereq2, 1);

        String prereqJson = "{\"or\": [{\"and\": [{\"moduleCode\": \"CS2040\"}, " +
                "{\"moduleCode\": \"MA1521\"}]}, {\"moduleCode\": \"CS3230\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS4231", "Parallel and Distributed Algorithms",
                4, prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should throw exception for complex nested prerequisites not met")
    void testComplexNestedPrerequisitesNotMet() throws Exception {
        Module prereq1 = createModule("CS2040", "Data Structures and Algorithms", 4);
        studyPlan.addModule(prereq1, 1);

        String prereqJson = "{\"or\": [{\"and\": [{\"moduleCode\": \"CS2040\"}, " +
                "{\"moduleCode\": \"MA1521\"}]}, {\"moduleCode\": \"CS3230\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS4231", "Parallel and Distributed Algorithms",
                4, prereqTree);

        assertThrows(StudyPlanException.class, () ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan,false)
        );
    }

    //Grade Requirements Tests
    @Test
    @DisplayName("Should strip grade requirements and validate correctly")
    void testGradeRequirementStripping() throws Exception {
        Module prereqModule = createModule("CS2040", "Data Structures and Algorithms", 4);
        studyPlan.addModule(prereqModule, 1);

        String prereqJson = "{\"moduleCode\": \"CS2040:B\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS3230", "Design and Analysis of Algorithms",
                4, prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    //Bridging Modules Tests
    @Test
    @DisplayName("Should ignore bridging module MA1301 in prerequisites")
    void testBridgingModuleMA1301Ignored() throws Exception {
        String bridgingModule = "MA1301";
    }

    @Test
    @DisplayName("Should ignore bridging module MA1301X in prerequisites")
    void testBridgingModuleMA1301XIgnored() throws Exception {
        String bridgingModule = "MA1301X";
    }

    @Test
    @DisplayName("Should ignore bridging modules in OR prerequisites")
    void testBridgingModulesInOr() throws Exception {
        String prereqJson = "{\"or\": [{\"moduleCode\": \"MA1301\"}, {\"moduleCode\": \"A-Level Math\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("MA1521", "Calculus for Computing",
                4, prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 1, studyPlan, false)
        );
    }

    //Edge Cases Tests
    @Test
    @DisplayName("Should handle textual prerequisite node")
    void testTextualPrerequisite() throws Exception {
        Module prereqModule = createModule("CS1010", "Programming Methodology", 4);
        studyPlan.addModule(prereqModule, 1);

        String prereqJson = "\"CS1010\"";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2040", "Data Structures and Algorithms",
                4, prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should handle empty study plan")
    void testEmptyStudyPlan() throws Exception {
        String prereqJson = "{\"moduleCode\": \"CS1010\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2040", "Data Structures and Algorithms",
                4, prereqTree);

        StudyPlanException exception = assertThrows(StudyPlanException.class, () ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );

        assertTrue(exception.getMessage().contains("Cannot add CS2040"));
    }

    @Test
    @DisplayName("Should validate for first semester correctly")
    void testFirstSemesterValidation() throws Exception {
        String prereqJson = "{\"moduleCode\": \"CS1010\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS2040", "Data Structures and Algorithms",
                4, prereqTree);

        assertThrows(StudyPlanException.class, () ->
                PrerequisiteChecker.validatePrerequisites(module, 1, studyPlan, false)
        );
    }

    @Test
    @DisplayName("Should handle multiple modules in different semesters")
    void testMultipleSemesters() throws Exception {
        Module mod1 = createModule("CS1010", "Programming Methodology", 4);
        studyPlan.addModule(mod1, 1);

        String prereq1Json = "{\"moduleCode\": \"CS1010\"}";
        JsonNode prereq1 = objectMapper.readTree(prereq1Json);
        Module mod2 = createModuleWithPrereqTree("CS2040", "Data Structures and Algorithms", 4,
                prereq1);
        studyPlan.addModule(mod2, 2);

        String prereqJson = "{\"and\": [{\"moduleCode\": \"CS1010\"}, {\"moduleCode\": \"CS2040\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module mod3 = createModuleWithPrereqTree("CS3230", "Design and Analysis of Algorithms", 4,
                prereqTree);

        assertDoesNotThrow(() ->
                PrerequisiteChecker.validatePrerequisites(mod3, 3, studyPlan,false)
        );
    }

    @Test
    @DisplayName("Should provide informative error message")
    void testErrorMessageContent() throws Exception {
        String prereqJson = "{\"and\": [{\"moduleCode\": \"CS2040\"}, {\"moduleCode\": \"MA1521\"}]}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);
        Module module = createModuleWithPrereqTree("CS3230", "Design and Analysis of Algorithms",
                4, prereqTree);

        StudyPlanException exception = assertThrows(StudyPlanException.class, () ->
                PrerequisiteChecker.validatePrerequisites(module, 2, studyPlan, false)
        );

        String message = exception.getMessage();
        assertTrue(message.contains("CS3230"));
        assertTrue(message.contains("semester 2"));
    }

    //Module Constructor Tests
    @Test
    @DisplayName("Should correctly create module with all constructor parameters")
    void testModuleCreationWithAllParameters() {
        List<String> prereqs = new ArrayList<>();
        prereqs.add("CS1010");
        prereqs.add("MA1521");

        Module module = new Module("Data Structures and Algorithms", "CS2040", 4,
                "Introduction to data structures", prereqs, 1, 2);

        assertEquals("CS2040", module.getModCode());
        assertEquals("Data Structures and Algorithms", module.getModName());
        assertEquals(4, module.getModCreds());
        assertEquals("Introduction to data structures", module.getModDescription());
        assertEquals(2, module.getPrerequisites().size());
        assertEquals(1, module.getSemesterTaught());
        assertEquals(2, module.getDefaultSemester());
        assertNull(module.getPrereqTree());
        assertEquals(0, module.getPrerequisitesCount());
    }

    @Test
    @DisplayName("Should handle module with empty prerequisites list")
    void testModuleWithEmptyPrerequisites() {
        Module module = createModule("CS1010", "Programming Methodology", 4);

        assertNotNull(module.getPrerequisites());
        assertTrue(module.getPrerequisites().isEmpty());
        assertEquals("(No Prerequisites)", module.getPrerequisitesDisplay());
    }

    @Test
    @DisplayName("Should display prerequisites correctly")
    void testPrerequisitesDisplay() {
        List<String> prereqs = new ArrayList<>();
        prereqs.add("CS1010");
        prereqs.add("MA1521");

        Module module = new Module("Data Structures", "CS2040", 4, "",
                prereqs, 0, 2);

        String display = module.getPrerequisitesDisplay();
        assertTrue(display.contains("Prerequisites:"));
        assertTrue(display.contains("CS1010"));
        assertTrue(display.contains("MA1521"));
    }

    @Test
    @DisplayName("Should correctly set and get prereqTree")
    void testSetAndGetPrereqTree() throws Exception {
        Module module = createModule("CS2040", "Data Structures", 4);

        String prereqJson = "{\"moduleCode\": \"CS1010\"}";
        JsonNode prereqTree = objectMapper.readTree(prereqJson);

        module.setPrereqTree(prereqTree);

        assertNotNull(module.getPrereqTree());
        assertEquals("CS1010", module.getPrereqTree().get("moduleCode").asText());
    }
}
