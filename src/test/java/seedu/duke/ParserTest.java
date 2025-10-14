package seedu.duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private Parser parser;
    private String userInput = "null";

    @BeforeEach
    void setUp() {
        parser = new Parser(userInput);
    }

    @Test
    void parseView_validInstructions() {
        parser.userInstructions = "plan";
        assertEquals("plan", parser.parseView(), "The view command should " +
                "return 'plan' for input 'plan'.");

        parser.userInstructions = "grad";
        assertEquals("grad", parser.parseView(), "The view command should " +
                "return 'grad' for input 'grad'.");

        parser.userInstructions = "sample extra arguments";
        assertEquals("sample", parser.parseView(), "The view command should " +
                "return 'sample' for input 'sample extra arguments'.");

        parser.userInstructions = "invalidCommand";
        assertEquals("", parser.parseView(), "The view command should " +
                "return an empty string for invalid input.");
    }

    @Test
    void parseDelete_validModuleCode() {
        parser.userInstructions = "CG2111A extra arguments";
        assertEquals("CG2111A", parser.parseDelete(), "The delete command should " +
                "return 'CG2111A' for input 'CG2111A extra arguments'.");

        parser.userInstructions = null;
        assertEquals("", parser.parseDelete(), "The delete command should " +
                "return an empty string for null input.");

        parser.userInstructions = "";
        assertEquals("", parser.parseDelete(), "The delete command " +
                "should return an empty string for empty input.");
    }

    @Test
    void parseInstructions_validInput() {
        parser.userInputString = "help";
        parser.parseInstructions();
        assertEquals("help", parser.commandType, "The commandType should" +
                " be 'help' for input 'help'.");
        assertNull(parser.userInstructions, "userInstructions should be " +
                "null for input 'help'.");

        parser.userInputString = "exit";
        parser.parseInstructions();
        assertEquals("exit", parser.commandType, "The commandType " +
                "should be 'exit' for input 'exit'.");
        assertNull(parser.userInstructions, "userInstructions should be " +
                "null for input 'exit'.");

        parser.userInputString = "delete CS2113";
        parser.parseInstructions();
        assertEquals("delete", parser.commandType, "The commandType should" +
                " be 'delete' for input 'delete CS2113'.");
        assertEquals("CS2113", parser.userInstructions, "userInstructions " +
                "should be 'CS2113' for input 'delete CS2113'.");

        parser.userInputString = "unknowncmd arg";
        parser.parseInstructions();
        assertEquals("invalid", parser.commandType, "The commandType " +
                "should be 'invalid' for unknown command.");

        parser.userInputString = "delete ";
        parser.parseInstructions();
        assertEquals("invalid", parser.commandType, "The commandType " +
                "should be 'invalid' for input 'delete '.");

        parser.userInputString = "";
        parser.parseInstructions();
        assertEquals("invalid", parser.commandType, "The commandType " +
                "should be 'invalid' for empty input.");
    }

    @Test
    void parseAdd_validInput() {
        parser.userInstructions = "n/CG2111A s/2";
        String[] result = parser.parseAdd();
        assertEquals(2, result.length);
        assertEquals("CG2111A", result[0], "The module code should " +
                "be  e.g.'CG2111A' for input 'n/CG2111A s/2'.");
        assertEquals("2", result[1], "The semester should " +
                "be '2' for input 'n/CG2111A s/2'.");

        parser.userInstructions = "n/CG2111A";
        assertEquals(0, parser.parseAdd().length, "The result array " +
                "should have length 0 for missing semester.");

        parser.userInstructions = "s/2";
        assertEquals(0, parser.parseAdd().length, "The result array " +
                "should have length 0 for missing module code.");

        parser.userInstructions = null;
        assertEquals(0, parser.parseAdd().length, "The result array " +
                "should have length 0 for null input.");

    }

}
