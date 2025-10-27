package seedu.classcraft.command;

/**
 * CommandList enum contains all valid commands for the ClassCraft application.
 */
public enum CommandList {
    help, add, delete, view, exit;

    /**
     * Checks if the user-inputted command exists in the CommandList enum.
     * Returns true if it exists, false otherwise.
     *
     * @param command The user-inputted command as a String.
     * @return boolean indicating if the command exists in CommandList.
     */
    public static boolean isCommandFound(String command) {
        for (CommandList c : CommandList.values()) {
            if (c.name().equalsIgnoreCase(command)) {
                return true;
            }
        }
        return false;
    }

}
