package seedu.duke.parser;

import seedu.duke.command.Command;
import seedu.duke.command.CommandList;
import seedu.duke.command.ExitCommand;
import seedu.duke.command.HelpCommand;
import seedu.duke.exceptions.InvalidCommand;
import seedu.duke.command.ViewSamplePlanCommand;
import seedu.duke.command.ViewGradReqCommand;


public class Parser {
    private String commandType;
    private String userInputString;
    private String userInstructions;

    public Parser(String userInput) {
        this.userInputString = userInput;
        parseInstructions();
    }

    public Command parseInput() {
        switch (commandType) {
        case "help":
            return new HelpCommand();
        case "add":
            String[] addModule = parseAdd();
            return new InvalidCommand();
        //return new AddCommand(addModule);
        case "delete":
            String deleteModule = parseDelete();
            return new InvalidCommand();
        //return new DeleteModule(deleteModule);
        case "confirm":
            return new InvalidCommand();
        //return new ConfirmCommand();
        case "view":
            String viewItems = parseView();
            switch (viewItems) {
            case "sample":
                return new ViewSamplePlanCommand();
            case "grad":
                return new ViewGradReqCommand();
            case "plan":
                // return new ViewCurrentPlanCommand();
                return new InvalidCommand();
            default:
                return new InvalidCommand();
            }
        case "exit":
            return new ExitCommand();
        default:
            return new InvalidCommand();
        }
    }


    public void parseInstructions() {
        String[] instructions = userInputString.split(" ", 2);

        if (!CommandList.isFound(instructions[0])) {
            System.out.println("Command not found");
            this.commandType = "invalid";
            return;
        }

        try {
            if (instructions.length == 1) {
                if (!(instructions[0].equals("help") || instructions[0].equals("exit")
                        || instructions[0].equals("confirm"))) {
                    throw new IllegalArgumentException("OOPS!!! The description of a " +
                            instructions[0] + " cannot be empty.");
                }
                this.commandType = instructions[0];
            } else if (instructions.length == 2) {
                if (instructions[1].isEmpty()) {
                    throw new IllegalArgumentException("OOPS!!! The description of a " +
                            instructions[0] + " cannot be empty.");
                }
                this.commandType = instructions[0];
                this.userInstructions = instructions[1];
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            this.commandType = "invalid";
        }
    }


    public String[] parseAdd() {
        String[] addModuleInformation = new String[2];
        try {
            String[] addInstructions = userInstructions.split("s/", 2);
            String moduleCode = addInstructions[0].split("n/", 2)[1].trim();
            String semester = addInstructions[1].trim();
            addModuleInformation[0] = moduleCode;
            addModuleInformation[1] = semester;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Error: Invalid input format. Please enter input in " +
                    "the correct format (e.g.add n/CG2111A s/2). ");
            return new String[0];
        }
        return addModuleInformation;

    }

    public String parseDelete() {
        String deleteModuleInformation;
        try {
            String moduleCode = userInstructions.split(" ", 2)[0].trim();
            deleteModuleInformation = moduleCode;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("Error: Invalid input format. Please enter " +
                    "input in the correct format (e.g.delete CG2111A).");
            return "";
        }
        return deleteModuleInformation;
    }


    public String parseView() {
        String viewItemsInformation;
        try {
            String viewInstructions = userInstructions.split(" ", 2)[0].trim().toLowerCase();

            if (viewInstructions.equals("plan")) {
                viewItemsInformation = "plan";
            } else if (viewInstructions.equals("grad")) {
                viewItemsInformation = "grad";
            } else if (viewInstructions.equals("sample")) {
                viewItemsInformation = "sample";
            } else {
                throw new IllegalArgumentException("OOPS!!! The description of a view " +
                        "command must be either plan, grad or sample.");
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | IllegalArgumentException e) {
            System.out.println("Error: Invalid input format. Please enter input in the correct format. ");
            return "";
        }
        return viewItemsInformation;
    }


}
