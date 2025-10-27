package seedu.classcraft.parser;

import seedu.classcraft.command.Command;
import seedu.classcraft.command.AddCommand;
import seedu.classcraft.command.CalcCreditsCommand;
import seedu.classcraft.command.DeleteCommand;
import seedu.classcraft.command.CommandList;
import seedu.classcraft.command.ExitCommand;
import seedu.classcraft.command.HelpCommand;
import seedu.classcraft.command.InvalidCommand;
import seedu.classcraft.command.SpecCommand;
import seedu.classcraft.command.ViewSamplePlanCommand;
import seedu.classcraft.command.ViewGradReqCommand;
import seedu.classcraft.command.ViewCurrentPlanCommand;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.exceptions.EmptyInstruction;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parser class to parse user input into commands.
 * A parser object is instantiated with the user input string,
 * and it extracts the command type and instructions.
 */
public class Parser {
    private static Logger logger = Logger.getLogger(Parser.class.getName());
    private String commandType;
    private String userInputString;
    private String userInstructions;
    public StudyPlan studyPlan;

    /**
     * Constructor for Parser class.
     * Initializes the parser with the user input string and
     * calls parseInstructions to extract command type and instructions.
     *
     * @return Command object corresponding to the user input.
     */


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



    /**
     * Parses the user input into a command object.
     * Using the commandType and userInstructions parsed earlier,
     * it creates the corresponding Command object based on the command type.
     * Catches EmptyInstruction exceptions and returns an InvalidCommand
     * if any required instructions/its components are missing.
     *
     * @return Command object corresponding to the user input.
     */
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
            case "mc":
                int semester = parseMC();
                return new CalcCreditsCommand(semester - 1);
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
            case "spec":
                String specItems = parseSpec();
                return new SpecCommand(specItems);
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

    /**
     * Parses the user input string to extract command type and instructions.
     * Splits the input string into command and instructions based on the first space,
     * handles single and dual instruction cases using helper methods.
     * Catches EmptyInstruction exceptions and sets commandType to "invalid"
     * if any required instructions/its components are missing.
     */
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

    /**
     * Checks if the command is found in the CommandList enum.
     *
     * @param instructions Array of strings containing command and instructions,
     *                     which checks the first element of the array.
     *
     *  @return boolean indicating if the command is found.
     */
    private boolean isCommandFound(String[] instructions) {
        return CommandList.isCommandFound(instructions[0]);
    }

    /**
     * Handles single instruction commands.
     * Validates that the command is one of the allowed single instruction commands
     * (help, exit, confirm) and sets the commandType accordingly.
     * Throws EmptyInstruction if the command is not valid.
     */
    private void handleSingleInstruction(String[] instructions) throws  EmptyInstruction{
        if (!(instructions[0].equals("help") || instructions[0].equals("exit") || instructions[0].equals("confirm"))) {
            logger.log(Level.WARNING, "Detected empty description for command: " + instructions[0]);
            throw new EmptyInstruction(instructions[0]);
        }
        this.commandType = instructions[0];

    }

    /**
     * Handles dual instruction commands.
     * Validates that the second part of the array is not empty,
     * sets the commandType and userInstructions accordingly.
     * Throws EmptyInstruction if the second part of array is empty.
     *
     * @param instructions Array of strings containing command and instructions.
     */
    private void handleDualInstruction(String[] instructions) throws EmptyInstruction {
        if (instructions[1].isEmpty()) {
            logger.log(Level.WARNING, "Detected empty description for command: " + instructions[0]);
            throw new EmptyInstruction(instructions[0]);
        }
        this.commandType = instructions[0];
        this.userInstructions = instructions[1];

    }

    /**
     * Parses the user input for add command.
     * Extracts module code and semester information from the userInstructions,
     * and splits them based on the expected format to obtain module code and semester.
     * Throws EmptyInstruction if any required components are missing or empty.
     * Catches ArrayIndexOutOfBoundsException and NullPointerException.
     *
     * @return String array containing module code and semester information.
     */
    public String[] parseAdd() throws EmptyInstruction {
        String[] addModuleInformation = new String[2];
        try {

            String[] addInstructions = userInstructions.split("s/", 2);
            if (addInstructions.length < 2) {
                throw new EmptyInstruction("add");
            }
            String[] moduleSplit = addInstructions[0].split("n/", 2);
            if (moduleSplit.length < 2) {
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

    /**
     * Parses the user input for delete command.
     * Extracts module code from the userInstructions and splits it based
     * on the expected format to get module code.
     * Throws EmptyInstruction if module code is missing or empty.
     * Catches ArrayIndexOutOfBoundsException and NullPointerException.
     *
     * @return String containing module code to be deleted.
     */
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

    /**
     * Parses the user input for view command.
     * Extracts the item to view from the userInstructions and splits it
     * based on the expected format to get the view item.
     * Validates that the view item is one of the allowed options (plan, grad, sample).
     * Throws EmptyInstruction if view item is missing, empty, or invalid.
     * Catches ArrayIndexOutOfBoundsException, NullPointerException, and IllegalArgumentException
     *
     * @return String containing the item to view.
     */
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


    private int parseMC() {
        int semester = -1;
        try {
            if (userInstructions == "total") {
                semester = 0;
            } else {
                semester = Integer.parseInt(userInstructions.trim());
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
            System.out.println("Error: Invalid input format. Please enter input in the correct format. ");
            return -1;
        }
        return semester;
    }

    public String parseSpec() {
        String specItemsInformation;
        try {
            String specInstructions = userInstructions.split(" ", 2)[0].trim().toLowerCase();

            if (specInstructions.equals("ae")) {
                specItemsInformation = "ae";
            } else if (specInstructions.equals("4.0")) {
                specItemsInformation = "4.0";
            } else if (specInstructions.equals("iot")) {
                specItemsInformation = "iot";
            } else if (specInstructions.equals("robotics")) {
                specItemsInformation = "robotics";
            } else if (specInstructions.equals("st")) {
                specItemsInformation = "st";
            } else {
                throw new IllegalArgumentException("OOPS!!! The specialisation must be either " +
                        "ae, 4.0, iot, robotics or st.");
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | IllegalArgumentException e) {
            System.out.println("Error: Invalid input format. Please enter input in the correct format. ");
            return "";
        }
        return specItemsInformation;
    }
}
