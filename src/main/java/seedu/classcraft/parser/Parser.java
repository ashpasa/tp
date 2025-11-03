package seedu.classcraft.parser;

import seedu.classcraft.command.AddCommand;
import seedu.classcraft.command.AddExemptedCommand;
import seedu.classcraft.command.CalcCreditsCommand;
import seedu.classcraft.command.CheckCommand;
import seedu.classcraft.command.Command;
import seedu.classcraft.command.CommandList;
import seedu.classcraft.command.DeleteCommand;
import seedu.classcraft.command.ExitCommand;
import seedu.classcraft.command.HelpCommand;
import seedu.classcraft.command.InvalidCommand;
import seedu.classcraft.command.PrereqCommand;
import seedu.classcraft.command.SetCurrentSemesterCommand;
import seedu.classcraft.command.SpecCommand;
import seedu.classcraft.command.ViewCurrentPlanCommand;
import seedu.classcraft.command.ViewGradReqCommand;
import seedu.classcraft.command.ViewProgressCommand;
import seedu.classcraft.command.ViewSamplePlanCommand;
import seedu.classcraft.exceptions.EmptyInstruction;
import seedu.classcraft.studyplan.ModuleStatus;
import seedu.classcraft.ui.Ui;

import java.util.Map;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parser class to parse user input into commands.
 * A parser object is instantiated with the user input string,
 * and it extracts the command type and instructions.
 */
public class Parser {
    private static Logger logger = Logger.getLogger(Parser.class.getName());

    Map<String, Integer> commandArgLimits = Map.ofEntries(

            Map.entry("help", 1),
            Map.entry("exit", 1),
            Map.entry("progress", 1),
            Map.entry("check", 1),
            Map.entry("view", 2),
            Map.entry("plan", 2),
            Map.entry("add", 3),
            Map.entry("delete", 2),
            Map.entry("mc", 2),
            Map.entry("spec", 2),
            Map.entry("prereq", 2),
            Map.entry("add-exempted", 2),
            Map.entry("set-current", 2)
    );


    private String commandType;
    private String userInputString;
    private String userInstructions;
    private String[] argumentList;
    private Ui ui = new Ui();

    /**
     * Constructor for Parser class.
     * Initializes the parser with the user input string and
     * calls parseInstructions to extract command type and instructions.
     *
     * @param userInput The full user input string.
     */
    public Parser(String userInput) {
        setLoggerLevel();
        assert userInput != null : "User input must not be null";
        this.userInputString = userInput.trim();
        logger.log(Level.INFO, "Received user input: " + userInputString);
        parseInstructions();
    }

    public String getUserInstructions() {
        return userInstructions;
    }


    public String getCommandType() {
        return commandType;
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
        logger.log(Level.INFO, "Parsing input, detected commandType: " + commandType);
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
                // semester - 1 is used for 0-based indexing in CalcCreditsCommand
                return new CalcCreditsCommand(semester - 1);

            // @@author lingru (Start of changes for new commands)
            /*
             * e.g. : add-exempted CS1231
             */
            case "add-exempted":
                return parseAddWithStatus(ModuleStatus.EXEMPTED, "add-exempted");

            case "progress":
                return new ViewProgressCommand();
            // @@author lingru (End of changes)

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
            case "prereq":
                String prereqModuleCode = parsePrereq();
                return new PrereqCommand(prereqModuleCode);
            case "set-current":
                String semesterToSet = parseCurrentSem();
                return new SetCurrentSemesterCommand(semesterToSet);
            case "check":
                return new CheckCommand();
            default:
                return new InvalidCommand();
            }
        } catch (EmptyInstruction e) {
            logger.log(Level.SEVERE, "Error parsing input into command: " + e.getMessage());
        }
        return new InvalidCommand();
    }

    /**
     * Parses the user input string to extract command type and instructions.
     * Splits the input string into command and instructions based on the first space,
     * handles single and dual instruction cases using helper methods.
     * Catches EmptyInstruction exceptions and sets commandType to "invalid"
     * if any required instructions/its components are missing.
     */
    private void parseInstructions() {
        String normalizedInput = userInputString.trim().replaceAll("\\s+", " ");
        this.argumentList = normalizedInput.split("\\s+");
        String[] instructions = normalizedInput.split(" ", 2);
        assert instructions.length > 0 : "Instructions must have at least one element";


        instructions[0] = instructions[0].toLowerCase();
        if (!isCommandFound(instructions)) {
            ui.showMessage("OOPS!!! I'm sorry, but I don't know what that means :-(\n" +
                    "Please type 'help' to see the list of available commands.");
            this.commandType = "invalid";
            return;
        }

        try {
            if (instructions.length == 1) {
                if (!(instructions[0].equals("help") || instructions[0].equals("exit")
                        || instructions[0].equals("check")
                        || instructions[0].equals("balance")
                        || instructions[0].equals("progress"))) {
                    throw new IllegalArgumentException("OOPS!!! The description of a " +
                            instructions[0] + " cannot be empty.");
                }
                this.commandType = instructions[0];
                handleSingleInstruction(instructions);
            } else if (instructions.length == 2) {
                handleDualInstruction(instructions);
            }

            try {
                int maxArgs = commandArgLimits.get(commandType);

                if (argumentList.length > maxArgs) {
                    throw new EmptyInstruction(commandType);

                }
            } catch (EmptyInstruction e) {
                ui.showMessage("OOPS!!! Too many arguments provided for command '" + commandType + "'.");
                this.commandType = "invalid";
            } catch (NullPointerException e) {
                // This is expected if commandType is "invalid" or not in the map, do nothing.
            }
        } catch (EmptyInstruction | IllegalArgumentException e) {
            ui.showMessage(e.getMessage());
            this.commandType = "invalid";
        }
    }


    /**
     * Checks if the command is found in the CommandList enum.
     *
     * @param instructions Array of strings containing command and instructions,
     * @return boolean indicating if the command is found.
     */
    private boolean isCommandFound(String[] instructions) {
        return CommandList.isCommandFound(instructions[0]);
    }

    /**
     * Handles single instruction commands.
     * Validates that the command is one of the allowed single instruction commands
     * (help, exit, confirm, progress) and sets the commandType accordingly.
     * Throws EmptyInstruction if the command is not valid.
     */
    private void handleSingleInstruction(String[] instructions) throws EmptyInstruction {
        if (!(instructions[0].equals("help") || instructions[0].equals("exit")
                || instructions[0].equals("check") // from master
                || instructions[0].equals("balance") // from bug-fixing
                || instructions[0].equals("confirm") || instructions[0].equals("progress"))) {
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
            ui.showMessage("OOPS!!! The description of a " +
                    instructions[0] + " cannot be empty.");
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
        String moduleCode;
        String semester;

        try {
            String normalized = userInstructions.trim().replaceAll("\\s+", " ");
            int nIndex = normalized.indexOf("n/");
            int sIndex = normalized.indexOf("s/");

            if (nIndex == -1 || sIndex == -1) {
                ui.showMessage("Both module code 'n/' and semester 's/' must be present in your input.");
                throw new EmptyInstruction("add");
            }

            if (nIndex < sIndex) {
                // n/ appears first
                String codePart = normalized.substring(nIndex + 2, sIndex).trim();
                String semesterPart = normalized.substring(sIndex + 2).trim();
                moduleCode = codePart.toUpperCase();
                semester = semesterPart;
            } else {
                // s/ appears first
                String semesterPart = normalized.substring(sIndex + 2, nIndex).trim();
                String codePart = normalized.substring(nIndex + 2).trim();
                moduleCode = codePart.toUpperCase();
                semester = semesterPart;
            }

            if (moduleCode.isEmpty() || semester.isEmpty()) {
                ui.showMessage("Missing module code or semester information.\n" +
                        "Please make sure your input contains both module code and semester information.");
                logger.log(Level.WARNING, "Missing module code or semester for add command.");
                throw new EmptyInstruction("add");
            }

            if (!(moduleCode.matches("^[A-Z]{2,3}\\d{4}[A-Z]?$") && semester.matches("^[1-8]$"))) {
                ui.showMessage("Invalid module code or semester format.\n" +
                        "Please ensure module code is valid (e.g., CS1010) " +
                        "and semester is a number between 1 and 8.");
                logger.log(Level.WARNING, "Invalid format for add command, may contain non-alphanumeric characters.");
                throw new EmptyInstruction("add");
            }

            addModuleInformation[0] = moduleCode;
            addModuleInformation[1] = semester;

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error parsing add command - Incorrect format " + e.getMessage());
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
            String moduleCode = userInstructions.split(" ", 2)[0].trim().toUpperCase();
            if (moduleCode.isEmpty()) {
                ui.showMessage("Module code is missing.\n" +
                        "Please make sure your input contains module code information.");
                throw new EmptyInstruction("delete");
            }

            if (!moduleCode.matches("^[A-Z]{2,3}\\d{4}[A-Z]?$")) {
                ui.showMessage("Invalid module code format.\n" +
                        "Please ensure module code is valid (e.g., CS1010).");
                logger.log(Level.WARNING, "Invalid format for delete command, " +
                        "may contain non-alphanumeric characters.");
                throw new EmptyInstruction("add");
            }
            return moduleCode;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            ui.showMessage("Error: Invalid input format. Please enter input in the correct format " +
                    "(e.g., delete CS1010).");
            logger.log(Level.WARNING, "Error parsing delete command - Incorrect format " + e.getMessage());
            throw new EmptyInstruction("delete");
        }
    }

    /**
     * Parses the user input for view command.
     * Extracts the item to view from the userInstructions.
     * Validates that the view item is one of the allowed options (plan, grad, sample).
     * Throws EmptyInstruction if view item is missing, empty, or invalid.
     *
     * @return String containing the item to view.
     * @throws EmptyInstruction if parsing fails or item is invalid.
     */
    public String parseView() throws EmptyInstruction {
        try {
            String viewInstructions = userInstructions.split(" ", 2)[0].trim().toLowerCase();
            switch (viewInstructions) {
            case "plan":
            case "grad":
            case "sample":
                logger.log(Level.INFO, "Parsed view command successfully, item to view: " + viewInstructions);
                return viewInstructions;
            default:
                ui.showMessage("You can only view 'plan', 'grad' or 'sample'!" +
                        "Please enter a valid view option.");
                throw new EmptyInstruction("view");
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            logger.log(Level.WARNING, "Error parsing view command - Incorrect format " + e.getMessage());
            throw new EmptyInstruction("view");
        }
    }

    // @@author ashpasa

    /**
     * Parses the user input for mc command.
     * Expects either a semester number or "total".
     *
     * @return int representing the semester (0 for "total", -1 for error).
     */
    private int parseMC() {
        int semester = -1;
        try {
            String trimmedInstructions = userInstructions.trim().toLowerCase();
            if ("total".equals(trimmedInstructions)) { // Fixed bug: use .equals() for strings
                semester = 0;
            } else {
                semester = Integer.parseInt(trimmedInstructions);
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
            ui.showMessage("Error: Invalid input format. Please enter input in the correct format "
                    + "(e.g., mc 1 or mc total).");
            return -1;
        }
        return semester;
    }
    // @@author

    /**
     * Parses the user input for spec command.
     * Expects one of the valid specialisation codes.
     *
     * @return String containing the specialisation, or empty string on error.
     */
    public String parseSpec() throws EmptyInstruction {
        String specItemsInformation;
        try {
            String specInstructions = userInstructions.split(" ", 2)[0].trim().toLowerCase();
            switch (specInstructions) {
            case "ae":
            case "4.0":
            case "iot":
            case "robotics":
            case "st":
                specItemsInformation = specInstructions;
                break;
            default:
                throw new IllegalArgumentException("OOPS!!! The specialisation must be either "
                        + "ae, 4.0, iot, robotics or st.");
            }
        } catch (IllegalArgumentException e) {
            ui.showMessage("The specialisation must be either " +
                    "ae, 4.0, iot, robotics or st. Please enter a valid specialisation.");
            throw new EmptyInstruction("spec");
        } catch (ArrayIndexOutOfBoundsException e) {
            ui.showMessage("Error: Missing specialisation for 'spec' command.");
            throw new EmptyInstruction("spec");

        } catch (NullPointerException e) {
            ui.showMessage("Error: Invalid input format. Please enter input in the correct format. ");
            throw new EmptyInstruction("spec");
        }
        return specItemsInformation;
    }

    /**
     * Parses the user input for prereq command.
     * Expects a valid module code.
     *
     * @return String containing the module code, or empty string on error.
     */
    public String parsePrereq() throws EmptyInstruction {
        String moduleCode;
        try {
            moduleCode = userInstructions.split(" ", 2)[0].trim().toUpperCase();
            if (!moduleCode.matches("^[A-Z]{2,3}\\d{4}[A-Z]?$")) {
                throw new IllegalArgumentException("Invalid module code format.");
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | IllegalArgumentException e) {
            ui.showMessage("Error: Invalid input format. Please enter input in "
                    + "the correct format (e.g., prereq CS2103T).");
            throw new EmptyInstruction("prereq");
        }
        return moduleCode;
    }

    /**
     * Helper method to parse commands for adding modules with a specific status
     * (e.g., add-completed, add-exempted).
     *
     * @param status      The ModuleStatus to be assigned.
     * @param commandName The name of the command (for error messages).
     * @return An AddCompletedCommand or InvalidCommand if parsing fails.
     */
    private Command parseAddWithStatus(ModuleStatus status, String commandName) {
        try {
            String moduleCode = userInstructions.split(" ", 2)[0].trim().toUpperCase();
            if (moduleCode.isEmpty()) {
                ui.showMessage("Error: Missing module code for '" + commandName + "'.");
                return new InvalidCommand();
            }

            if (!moduleCode.matches("^[a-zA-Z0-9]+$")) {
                logger.log(Level.WARNING, "Invalid format for addWithStatus command, " +
                        "may contain non-alphanumeric characters.");
                throw new EmptyInstruction("add");
            }

            return new AddExemptedCommand(moduleCode, status);
        } catch (Exception e) {
            ui.showMessage("Error: Invalid input format for '" + commandName
                    + "' (e.g. " + commandName + " CS1010).");
            return new InvalidCommand();
        }
    }

    /**
     * Parses the user input for the set-current command.
     * Validates the inputs before setting the currentSemester attribute in StudyPlan.
     *
     * @return returns a String containing the current semester
     *                  Catches EmptyInstruction exceptions and sets commandType to "invalid"
     *                  if any required instructions/its components are missing.
     */
    public String parseCurrentSem() throws EmptyInstruction {
        String currentSem;
        try {

            String semester = userInstructions.trim();

            if (semester.isEmpty()) {
                ui.showMessage("Semester is missing.\n" +
                        "Please make sure your input contains semester information.");
                logger.log(Level.WARNING, "Missing semester for set-current command.");
                throw new EmptyInstruction("set-current");
            }

            if (!(semester.matches("^[1-8]$"))) {
                ui.showMessage("Invalid semester format.\n" +
                        "Please ensure semester is a number between 1 and 8.");
                logger.log(Level.WARNING, "Invalid format for set-current command, " +
                        "may contain non-alphanumeric characters.");
                throw new EmptyInstruction("set-current");
            }

            currentSem = semester;

        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            ui.showMessage("Error: Invalid input format. Please enter input in the correct format "
                    + "(e.g., set-current 3).");
            logger.log(Level.WARNING, "Error parsing set-current command - Incorrect format " + e.getMessage());
            throw new EmptyInstruction("set-current");
        }
        return currentSem;
    }

    /**
     * Sets logger level depending on how the program is run.
     * When running from a jar file, it disables logging.
     * Otherwise, when running from an IDE, it displays all logging messages.
     */
    public void setLoggerLevel() {
        String className = "/" + this.getClass().getName().replace('.', '/') + ".class";
        URL resource = this.getClass().getResource(className);

        if (resource == null) {
            return;
        }

        String protocol = resource.getProtocol();
        if (Objects.equals(protocol, "jar")) {
            logger.setLevel(Level.OFF);
        } else if (Objects.equals(protocol, "file")) {
            logger.setLevel(Level.ALL);
        }
    }
}

