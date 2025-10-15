package seedu.classcraft;
/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.parser.Parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull; */

class ParserTest {
    /*        
    private Parser parser;
    private String userInput = "null";

    @BeforeEach
    void setUp() {
        parser = new Parser( userInput);
    }

    //Test for valid parsing of user input string into commandType and userInstructions
    @Test
    void parseInstructions_validInput() {
        parser.userInputString = "help";
        parser.parseInstructions();
        assertEquals("help", parser.commandType);
        assertNull(parser.userInstructions,
                "userInstructions should be " +
                        "null for input 'help'.");

        parser.userInputString = "exit";
        parser.parseInstructions();
        assertEquals("exit", parser.commandType);
        assertNull(parser.userInstructions,
                "userInstructions should be " +
                        "null for input 'exit'.");

        parser.userInputString = "delete CS2113";
        parser.parseInstructions();
        assertEquals("delete", parser.commandType);
        assertEquals("CS2113", parser.userInstructions);

        parser.userInputString = "unknown arg";
        parser.parseInstructions();
        assertEquals("invalid", parser.commandType);

        parser.userInputString = "delete ";
        parser.parseInstructions();
        assertEquals("invalid", parser.commandType,
                "The commandType " +
                        "should be 'invalid' for input 'delete' without a corresponding module code.");

        parser.userInputString = "";
        parser.parseInstructions();
        assertEquals("invalid", parser.commandType,
                "The commandType " +
                        "should be 'invalid' for empty input.");
    }

    //Test for valid parsing of user instructions for view command
    @Test
    void parseView_validInstructions() {
        parser.userInstructions = "plan";
        assertEquals("plan", parser.parseView());

        parser.userInstructions = "grad";
        assertEquals("grad", parser.parseView());

        parser.userInstructions = "sample extra arguments";
        assertEquals("sample", parser.parseView());

        parser.userInstructions = "invalidCommand";
        assertEquals("", parser.parseView(),
                "The view command should " +
                "return an empty string for invalid input.");
    }

    //Test for valid parsing of user instructions for delete command
    @Test
    void parseDelete_validModuleCode() {
        parser.userInstructions = "CG2111A extra arguments";
        assertEquals("CG2111A", parser.parseDelete());

        parser.userInstructions = null;
        assertEquals("", parser.parseDelete(),
                "The delete command should " +
                "return an empty string for null input.");

        parser.userInstructions = "";
        assertEquals("", parser.parseDelete(),
                "The delete command " +
                "should return an empty string for empty input.");
    }


    //Test for valid parsing of user instructions for add command
    @Test
    void parseAdd_validInput() {
        parser.userInstructions = "n/CG2111A s/2";
        String[] result = parser.parseAdd();
        assertEquals(2, result.length);
        assertEquals("CG2111A", result[0]);
        assertEquals("2", result[1]);

        parser.userInstructions = "n/CG2111A";
        assertEquals(0, parser.parseAdd().length,
                "The result array " +
                "should have length 0 for missing semester.");

        parser.userInstructions = "s/2";
        assertEquals(0, parser.parseAdd().length,
                "The result array " +
                "should have length 0 for missing module code.");

        parser.userInstructions = null;
        assertEquals(0, parser.parseAdd().length,
                "The result array " +
                "should have length 0 for null input.");

    }
    */
}
