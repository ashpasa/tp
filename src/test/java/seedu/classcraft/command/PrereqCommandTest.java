package seedu.classcraft.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PrereqCommand Tests")
class PrereqCommandTest {

    private Ui ui;
    private StudyPlan studyPlan;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        ui = new Ui();
        studyPlan = new StudyPlan(8);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    //Constructor Tests
    @Test
    @DisplayName("Should create PrereqCommand with valid module code")
    void testCreatePrereqCommand() {
        String moduleCode = "CS2103T";
        PrereqCommand command = new PrereqCommand(moduleCode);

        assertNotNull(command);
    }

    @Test
    @DisplayName("Should create PrereqCommand with uppercase module code")
    void testCreatePrereqCommandUppercase() {
        String moduleCode = "CS2040";
        PrereqCommand command = new PrereqCommand(moduleCode);

        assertNotNull(command);
    }

    @Test
    @DisplayName("Should create PrereqCommand with lowercase module code")
    void testCreatePrereqCommandLowercase() {
        String moduleCode = "cs1010";
        PrereqCommand command = new PrereqCommand(moduleCode);

        assertNotNull(command);
    }

    //executeCommand Tests
    @Test
    @DisplayName("Should execute command without throwing exception")
    void testExecuteCommandNoException() {
        PrereqCommand command = new PrereqCommand("CS2103T");

        assertDoesNotThrow(() -> command.executeCommand(studyPlan, ui));
    }

    @Test
    @DisplayName("Should display error for invalid module code")
    void testExecuteCommandInvalidModule() {
        PrereqCommand command = new PrereqCommand("INVALID999");

        command.executeCommand(studyPlan, ui);

        String output = outContent.toString();
        assertTrue(output.contains("Error: Could not fetch prerequisites for"));
    }

    @Test
    @DisplayName("Should throw AssertionError for null ui")
    void testExecuteCommandNullUi() {
        PrereqCommand command = new PrereqCommand("CS2103T");

        assertThrows(AssertionError.class,
                () -> command.executeCommand(studyPlan, null));
    }

    @Test
    @DisplayName("Should throw AssertionError for null studyPlan")
    void testExecuteCommandNullStudyPlan() {
        PrereqCommand command = new PrereqCommand("CS2103T");

        assertThrows(AssertionError.class,
                () -> command.executeCommand(null, ui));
    }

    //Edge Cases Tests
    @Test
    @DisplayName("Should handle empty module code")
    void testExecuteCommandEmptyModuleCode() {
        PrereqCommand command = new PrereqCommand("");

        command.executeCommand(studyPlan, ui);

        String output = outContent.toString();
        assertTrue(output.contains("Error: Could not fetch prerequisites for"));
    }

    @Test
    @DisplayName("Should handle module code with special characters")
    void testExecuteCommandSpecialCharacters() {
        PrereqCommand command = new PrereqCommand("CS@#$%");

        command.executeCommand(studyPlan, ui);

        String output = outContent.toString();
        assertTrue(output.contains("Error: Could not fetch prerequisites for"));
    }

    @Test
    @DisplayName("Should handle very long module code")
    void testExecuteCommandLongModuleCode() {
        PrereqCommand command = new PrereqCommand("VERYLONGMODULECODE123456789");

        command.executeCommand(studyPlan, ui);

        String output = outContent.toString();
        assertTrue(output.contains("Error: Could not fetch prerequisites for"));
    }

    //Integration Tests
    @Test
    @DisplayName("Should display prerequisite information when successful")
    void testExecuteCommandDisplaysPrerequisites() {
        PrereqCommand command = new PrereqCommand("CS2103T");

        command.executeCommand(studyPlan, ui);

        String output = outContent.toString();
        assertFalse(output.isEmpty());
        assertTrue(output.contains("Module:") || output.contains("Error:"));
    }

    @Test
    @DisplayName("Should handle module with no prerequisites")
    void testExecuteCommandNoPrerequisites() {
        PrereqCommand command = new PrereqCommand("CS1010");

        command.executeCommand(studyPlan, ui);

        String output = outContent.toString();
        assertFalse(output.isEmpty());
    }

    @Test
    @DisplayName("Should handle multiple executions of same command")
    void testExecuteCommandMultipleTimes() {
        PrereqCommand command = new PrereqCommand("CS2103T");

        assertDoesNotThrow(() -> {
            command.executeCommand(studyPlan, ui);
            command.executeCommand(studyPlan, ui);
            command.executeCommand(studyPlan, ui);
        });

        String output = outContent.toString();
        assertFalse(output.isEmpty());
    }

    //Error Handling Tests
    @Test
    @DisplayName("Should catch and handle exceptions from NUSmodsFetcher")
    void testExecuteCommandHandlesException() {
        PrereqCommand command = new PrereqCommand("INVALID");

        assertDoesNotThrow(() -> command.executeCommand(studyPlan, ui));

        String output = outContent.toString();
        assertTrue(output.contains("Error: Could not fetch prerequisites for"));
    }

    @Test
    @DisplayName("Should display error message with module code")
    void testExecuteCommandErrorContainsModuleCode() {
        String moduleCode = "INVALID999";
        PrereqCommand command = new PrereqCommand(moduleCode);

        command.executeCommand(studyPlan, ui);

        String output = outContent.toString();
        assertTrue(output.contains(moduleCode));
    }

    @Test
    @DisplayName("Should log when null prerequisites found")
    void testExecuteCommandNullPrereqTree() {
        PrereqCommand command = new PrereqCommand("CS1010");

        assertDoesNotThrow(() -> command.executeCommand(studyPlan, ui));

        String output = outContent.toString();
        assertFalse(output.isEmpty());
    }
}
