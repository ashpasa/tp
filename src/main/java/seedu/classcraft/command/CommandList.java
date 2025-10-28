package seedu.classcraft.command;

public enum CommandList {
    help, add, delete, view, exit, confirm,
    progress, add_completed, add_exempted;


    /**
     * Checks if a given string matches any of the enum command names.
     * This method is modified to support user input with hyphens
     * (e.g., "add-completed") by mapping them to enum names
     * with underscores (e.g., "add_completed").
     *
     * @param test The command word from user input.
     * @return true if a matching command is found, false otherwise.
     */
    public static boolean isFound(String test) {
        String testWithUnderscore = test.replace('-', '_');

        for (CommandList c : CommandList.values()) {
            if (c.name().equalsIgnoreCase(testWithUnderscore)) {
                return true;
            }
        }
        return false;
    }

}

