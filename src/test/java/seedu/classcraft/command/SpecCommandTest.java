package seedu.classcraft.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Specialisations Command Tests")
class SpecCommandTest {
    public static String studyPlanFile = "./ClassCraftData/studyPlan.txt";
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() throws IOException {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should print specialisation modules and unlocks correctly")
    void testSpecCommandExecution() throws IOException {
        SpecCommand command = new SpecCommand("ae");
        StudyPlan studyPlan = new StudyPlan(8);
        Ui ui = new Ui();
        Storage storage = new Storage(studyPlanFile);

        command.executeCommand(studyPlan, ui, storage);

        String output = outputStream.toString();

        assertTrue(output.contains("EE3408C, EE3431C, EE4218, EE4407, EE4415, EE5507, CG3207, EE4409, EE4435, " +
                "EE4436, EE4437, EE4438"));
        assertTrue(output.contains("EE2026 unlocks EE4415"));
        assertTrue(output.contains("CG2027 unlocks EE3408C, EE4407, EE4409, EE4435, EE4436, EE4437, EE4438"));
        assertTrue(output.contains("CG2028 unlocks EE4218, CG3207"));
        assertTrue(output.contains("EE3408C unlocks EE5507"));
    }
}
