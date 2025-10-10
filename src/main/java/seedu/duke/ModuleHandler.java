package seedu.duke;

import java.util.HashMap;

/**
 * Handles the storage and creation of Module classes
 * Stores a HashMap with modCode: Module as key value pair
 */
public class ModuleHandler {
    private HashMap<String, Module> modules;


    public ModuleHandler() {
    }

    public HashMap<String, Module> getModules() {
        return modules;
    }

    public void addModule(Module module) {
        modules.putIfAbsent(module.getModCode(), module);
    }
}
