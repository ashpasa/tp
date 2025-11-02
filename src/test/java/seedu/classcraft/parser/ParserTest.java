package seedu.classcraft.parser;

import org.junit.jupiter.api.Test;
import seedu.classcraft.command.AddCommand;
import seedu.classcraft.command.CalcCreditsCommand;
import seedu.classcraft.command.DeleteCommand;
import seedu.classcraft.command.ExitCommand;
import seedu.classcraft.command.HelpCommand;
import seedu.classcraft.command.InvalidCommand;
import seedu.classcraft.command.PrereqCommand;
import seedu.classcraft.command.SpecCommand;
import seedu.classcraft.command.ViewCurrentPlanCommand;
import seedu.classcraft.command.ViewGradReqCommand;
import seedu.classcraft.command.ViewSamplePlanCommand;
import seedu.classcraft.exceptions.EmptyInstruction;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ParserTest {

    private Parser parser;
    private String userInput = "null";


    @Test
    void parseInstructions_validInput() {
        Parser parser;

        parser = new Parser("help");
        assertEquals("help", parser.getCommandType());
        assertNull(parser.getUserInstructions(), "userInstructions should be null for input 'help'.");

        parser = new Parser("exit");
        assertEquals("exit", parser.getCommandType());
        assertNull(parser.getUserInstructions(), "userInstructions should be null for input 'exit'.");

        parser = new Parser("delete CS2113");
        assertEquals("delete", parser.getCommandType());
        assertEquals("CS2113", parser.getUserInstructions());

        parser = new Parser("unknown arg");
        assertEquals("invalid", parser.getCommandType());

        parser = new Parser("");
        assertEquals("invalid", parser.getCommandType(), "The commandType should be 'invalid' for empty input.");
    }


    @Test
    void parseView_validInstructions() throws EmptyInstruction {
        parser = new Parser("view plan");
        assertEquals("plan", parser.parseView());

        parser = new Parser("view grad");
        assertEquals("grad", parser.parseView());

        parser = new Parser("view sample");
        assertEquals("sample", parser.parseView());

        parser = new Parser("view ");
        assertThrows(EmptyInstruction.class, () -> parser.parseView());

    }

    @Test
    void parseDelete_validModuleCode() throws EmptyInstruction {
        userInput = "delete CG2111A extra arguments";
        parser = new Parser(userInput);
        assertEquals("CG2111A", parser.parseDelete());

        parser = new Parser("delete ");
        assertThrows(EmptyInstruction.class, () -> parser.parseDelete(),
                "An EmptyInstruction exception " +
                        "should be thrown for null input.");

    }

    @Test
    void parseAdd_validInput() throws EmptyInstruction {
        userInput = "add n/CG2111A s/2";
        parser = new Parser(userInput);
        String[] result = parser.parseAdd();
        assertEquals(2, result.length);
        assertEquals("CG2111A", result[0]);
        assertEquals("2", result[1]);

        userInput = "n/CG2111A";
        parser = new Parser(userInput);
        assertThrows(EmptyInstruction.class, () -> parser.parseAdd(),
                "An EmptyInstruction exception " +
                        "should be thrown for null input.");

        userInput = "s/2";
        parser = new Parser(userInput);
        assertThrows(EmptyInstruction.class, () -> parser.parseAdd(),
                "An EmptyInstruction exception " +
                        "should be thrown for null input.");

        userInput = "";
        parser = new Parser(userInput);
        assertThrows(EmptyInstruction.class, () -> parser.parseAdd(),
                "An EmptyInstruction exception " +
                        "should be thrown for null input.");

    }

    @Test
    void parseInput_variousCommands() {
        Parser parser = new Parser("help");
        assertInstanceOf(HelpCommand.class, parser.parseInput());

        parser = new Parser("add n/CS1010 s/1");
        assertInstanceOf(AddCommand.class, parser.parseInput());

        parser = new Parser("delete CS2113");
        assertInstanceOf(DeleteCommand.class, parser.parseInput());

        parser = new Parser("mc 2");
        assertInstanceOf(CalcCreditsCommand.class, parser.parseInput());

        parser = new Parser("view plan");
        assertInstanceOf(ViewCurrentPlanCommand.class, parser.parseInput());

        parser = new Parser("view grad");
        assertInstanceOf(ViewGradReqCommand.class, parser.parseInput());

        parser = new Parser("view sample");
        assertInstanceOf(ViewSamplePlanCommand.class, parser.parseInput());

        parser = new Parser("view unknown");
        assertInstanceOf(InvalidCommand.class, parser.parseInput());

        parser = new Parser("spec ae");
        assertInstanceOf(SpecCommand.class, parser.parseInput());

        parser = new Parser("exit");
        assertInstanceOf(ExitCommand.class, parser.parseInput());

        parser = new Parser("prereq CS2103T");
        assertInstanceOf(PrereqCommand.class, parser.parseInput());

        parser = new Parser("unknowncmd");
        assertInstanceOf(InvalidCommand.class, parser.parseInput());

        parser = new Parser("");
        assertInstanceOf(InvalidCommand.class, parser.parseInput());
    }

    @Test
    void testParseSpec() throws EmptyInstruction {
        Parser parser = new Parser("spec ae");
        assertEquals("ae", parser.parseSpec());

        parser = new Parser("spec 4.0");
        assertEquals("4.0", parser.parseSpec());

        parser = new Parser("spec iot");
        assertEquals("iot", parser.parseSpec());

        parser = new Parser("spec robotics");
        assertEquals("robotics", parser.parseSpec());

        parser = new Parser("spec st");
        assertEquals("st", parser.parseSpec());

        assertThrows(EmptyInstruction.class, () -> {
            Parser p = new Parser("spec unknown");
            p.parseSpec();
        });

        assertThrows(EmptyInstruction.class, () -> {
            Parser p = new Parser("spec");
            p.parseSpec();
        });
    }

    @Test
    void testParsePrereq() throws EmptyInstruction {
        Parser parser = new Parser("prereq CS2103T extra");
        assertEquals("CS2103T", parser.parsePrereq());

        parser = new Parser("prereq CS2103");
        assertEquals("CS2103", parser.parsePrereq());

        assertThrows(EmptyInstruction.class, () -> {
            Parser p = new Parser("prereq invalid_code");
            p.parsePrereq();
        });

        assertThrows(EmptyInstruction.class, () -> {
            Parser p = new Parser("prereq");
            p.parsePrereq();
        });

    }


}
