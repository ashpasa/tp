package seedu.classcraft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.classcraft.exceptions.EmptyInstruction;
import seedu.classcraft.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ParserTest {

    private Parser parser;
    private String userInput = "null";

    @BeforeEach
    void setUp() {
        parser = new Parser( userInput);
    }

    //Test for valid parsing of user input string into commandType and userInstructions
    @Test
    void parseInstructions_validInput() {
        parser.setUserInputString("help");
        parser.parseInstructions();
        assertEquals("help", parser.getCommandType());
        assertNull(parser.getUserInstructions(),
                "userInstructions should be " +
                        "null for input 'help'.");

        parser.setUserInputString("exit");
        parser.parseInstructions();
        assertEquals("exit", parser.getCommandType());
        assertNull(parser.getUserInstructions(),
                "userInstructions should be " +
                        "null for input 'exit'.");

        parser.setUserInputString("delete CS2113");
        parser.parseInstructions();
        assertEquals("delete", parser.getCommandType());
        assertEquals("CS2113", parser.getUserInstructions());

        parser.setUserInputString("unknown arg");
        parser.parseInstructions();
        assertEquals("invalid", parser.getCommandType());

        parser.setUserInputString("");
        parser.parseInstructions();
        assertEquals("invalid", parser.getCommandType(),
                "The commandType " +
                        "should be 'invalid' for empty input.");

    }

    //Test for valid parsing of user instructions for view command
    @Test
    void parseView_validInstructions() throws EmptyInstruction {
        parser.setUserInstructions("plan");
        assertEquals("plan", parser.parseView());

        parser.setUserInstructions("grad");
        assertEquals("grad", parser.parseView());

        parser.setUserInstructions("sample extra arguments");
        assertEquals("sample", parser.parseView());

        parser.setUserInstructions("invalidCommand");
        assertThrows(EmptyInstruction.class, () -> parser.parseView());

    }

    //Test for valid parsing of user instructions for delete command
    @Test
    void parseDelete_validModuleCode() throws EmptyInstruction {
        parser.setUserInstructions("CG2111A extra arguments");
        assertEquals("CG2111A", parser.parseDelete());

        parser.setUserInstructions(null);
        assertThrows(EmptyInstruction.class, () -> parser.parseDelete(),
                "An EmptyInstruction exception " +
                "should be thrown for null input.");

        parser.setUserInstructions("");
        assertThrows(EmptyInstruction.class, () -> parser.parseDelete(),
                "An EmptyInstruction exception " +
                "should be thrown for empty input.");

    }


    //Test for valid parsing of user instructions for add command
    @Test
    void parseAdd_validInput() throws EmptyInstruction {
        parser.setUserInstructions("n/CG2111A s/2");
        String[] result = parser.parseAdd();
        assertEquals(2, result.length);
        assertEquals("CG2111A", result[0]);
        assertEquals("2", result[1]);

        parser.setUserInstructions("n/CG2111A");
        assertThrows(EmptyInstruction.class, () -> parser.parseAdd(),
                "An EmptyInstruction exception " +
                        "should be thrown for null input.");

        parser.setUserInstructions("s/2");
        assertThrows(EmptyInstruction.class, () -> parser.parseAdd(),
                "An EmptyInstruction exception " +
                        "should be thrown for null input.");

        parser.setUserInstructions(null);
        assertThrows(EmptyInstruction.class, () -> parser.parseAdd(),
                "An EmptyInstruction exception " +
                "should be thrown for null input.");

    }

}
