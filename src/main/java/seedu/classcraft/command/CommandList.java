package seedu.classcraft.command;

public enum CommandList {
    help, add, delete, view, mc, spec, exit, confirm, prereq;

    public static boolean isFound(String test) {
        for (CommandList c : CommandList.values()) {
            if (c.name().equalsIgnoreCase(test)) {
                return true;
            }
        }
        return false;
    }

}
