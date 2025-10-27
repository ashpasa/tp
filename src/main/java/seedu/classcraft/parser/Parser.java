package seedu.classcraft.parser;

import seedu.classcraft.command.Command;
import seedu.classcraft.command.AddCommand;
import seedu.classcraft.command.DeleteCommand;
import seedu.classcraft.command.CommandList;
import seedu.classcraft.command.ExitCommand;
import seedu.classcraft.command.HelpCommand;
import seedu.classcraft.command.InvalidCommand;
import seedu.classcraft.command.ViewSamplePlanCommand;
import seedu.classcraft.command.ViewGradReqCommand;
import seedu.classcraft.command.ViewCurrentPlanCommand;
import seedu.classcraft.exceptions.EmptyInstruction;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {
    private static Logger logger = Logger.getLogger(Parser.class.getName());
    private String commandType;
    private String userInputString;
    private String userInstructions;

    public Parser(String userInput) {
        assert userInput != null : "User input must not be null";
        this.userInputString = userInput;
        logger.log(Level.INFO,"Received user input: " + userInputString);
        parseInstructions();
    }

    public String getUserInstructions() {
        return userInstructions;
    }

    public String getUserInputString() {
        return userInputString;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public void setUserInputString(String userInputString) {
        this.userInputString = userInputString;
    }

    public void setUserInstructions(String userInstructions) {
        this.userInstructions = userInstructions;
    }



    public Command parseInput() {
        logger.log(Level.INFO,"Parsing input, detected commandType: " + commandType);
        try {
            switch (commandType) {
            case "help":
                return new HelpCommand();
            case "add":
                String[] addModuleInfo = parseAdd();
                return new AddCommand(addModuleInfo);
            case "delete":
                String deleteModuleCode = parseDelete();
                return new DeleteCommand(deleteModuleCode);
            case "view":
                String viewItems = parseView();
                switch (viewItems) {
                case "sample":
                    return new ViewSamplePlanCommand();
                case "grad":
                    return new ViewGradReqCommand();
                case "plan":
                    return new ViewCurrentPlanCommand();
                default:
                    return new InvalidCommand();
                }
            case "exit":
                return new ExitCommand();
            default:
                return new InvalidCommand();
            }
        } catch (EmptyInstruction e) {
            logger.log(Level.SEVERE,"Error parsing input into command: " + e.getMessage());
            return new InvalidCommand();
        }

    }

    //parses the user input into command type and instructions
    //returns invalid command if command type is not found
    public void parseInstructions() {
        String[] instructions = userInputString.split(" ", 2);
        assert instructions.length > 0 : "Instructions must have at least one element";

        if (!isCommandFound(instructions)) {
            this.commandType = "invalid";
            return;
        }

        try {
            if (instructions.length == 1) {
                handleSingleInstruction(instructions);
            } else if (instructions.length == 2) {
                handleDualInstruction(instructions);
            }
        } catch (EmptyInstruction e) {
            System.out.println(e.getMessage());
            this.commandType = "invalid";
        }
    }

    private boolean isCommandFound(String[] instructions) {
        return CommandList.isFound(instructions[0]);
    }

    private void handleSingleInstruction(String[] instructions) throws  EmptyInstruction{
        if (!(instructions[0].equals("help") || instructions[0].equals("exit") || instructions[0].equals("confirm"))) {
            logger.log(Level.WARNING, "Detected empty description for command: " + instructions[0]);
            throw new EmptyInstruction(instructions[0]);
        }
        this.commandType = instructions[0];

    }

    private void handleDualInstruction(String[] instructions) throws EmptyInstruction {
        if (instructions[1].isEmpty()) {
            logger.log(Level.WARNING, "Detected empty description for command: " + instructions[0]);
            throw new EmptyInstruction(instructions[0]);
        }
        this.commandType = instructions[0];
        this.userInstructions = instructions[1];

    }

    public String[] parseAdd() throws EmptyInstruction {
        String[] addModuleInformation = new String[2];
        try {

            String[] addInstructions = userInstructions.split("s/", 2);
            if (addInstructions.length < 2) {
                System.out.println("Error: Invalid input format. Please enter input in " +
                    "the correct format (e.g.add n/CG2111A s/2). ");
                throw new EmptyInstruction("add");
            }
            String[] moduleSplit = addInstructions[0].split("n/", 2);
            if (moduleSplit.length < 2) {
                System.out.println("Error: Invalid input format. Please enter input in " +
                    "the correct format (e.g.add n/CG2111A s/2). ");
                throw new EmptyInstruction("add");
            }
            String moduleCode = moduleSplit[1].trim();
            String semester = addInstructions[1].trim();

            if (moduleCode.isEmpty() || semester.isEmpty()) {
                throw new EmptyInstruction("add");
            }

            addModuleInformation[0] = moduleCode;
            addModuleInformation[1] = semester;

        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            logger.log(Level.WARNING,"Error parsing add command - Incorrect format " + e.getMessage());
            throw new EmptyInstruction("add");
        }
        return addModuleInformation;
    }


    //parses the user input for delete command
    //returns the module code to be deleted
    public String parseDelete() throws EmptyInstruction {
        try {
            String moduleCode = userInstructions.split(" ", 2)[0].trim();
            if (moduleCode.isEmpty()) {
                throw new EmptyInstruction("delete");
            }
            return moduleCode;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            logger.log(Level.WARNING, "Error parsing delete command - Incorrect format " + e.getMessage());
            throw new EmptyInstruction("delete");
        }
    }

    //parses the user input for view command
    //returns the information to be viewed (plan, grad or sample)
    public String parseView() throws EmptyInstruction {
        try {

            String viewInstructions = userInstructions.split(" ", 2)[0].trim().toLowerCase();

            if (viewInstructions.equals("plan") || viewInstructions.equals("grad")
                    || viewInstructions.equals("sample")) {
                logger.log(Level.INFO, "Parsed view command successfully, item to view: " + viewInstructions);
                return viewInstructions;
            } else {
                throw new EmptyInstruction("view");
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | IllegalArgumentException e) {
            logger.log(Level.WARNING, "Error parsing view command - Incorrect format " + e.getMessage());
            throw new EmptyInstruction("view");
        }
    }

}
