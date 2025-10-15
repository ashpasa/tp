package seedu.duke.studyplan;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import seedu.duke.nusmodsfetcher.NUSmodsFetcher;

import java.util.Iterator;
import java.util.Map;

/**
 * Handles the storage and creation of Module classes
 * Stores a HashMap with modCode: Module as key value pair
 */
public class ModuleHandler {
    private HashMap<String, Module> modules;


    public ModuleHandler() {
        this.modules = new HashMap<>();
    }

    // Fetches module data from NUSMods and create a module object
    public Module createModule(String moduleCode) {
        String modName = moduleCode;
        String modDescription = "N/A (Details not fetched)";
        List<String> prerequisites = new ArrayList<>();

        try {
            JsonNode rootJson = NUSmodsFetcher.fetchModuleJson(moduleCode);

            modName = rootJson.get("title").asText();
            modDescription = rootJson.get("description").asText();

            JsonNode prerequisiteNode = rootJson.get("prereqTree");
            if (prerequisiteNode != null && !prerequisiteNode.isNull()) {
                extractModuleCodes(prerequisiteNode, prerequisites);
            }

        } catch (Exception e) {
            System.err.println("Warning: Could not fetch details for " + moduleCode + ". Using default values.");
        }
        Module newModule = new Module(modName, moduleCode, modDescription, prerequisites, -1, -1);

        addModule(newModule);
        return newModule;
    }

    public HashMap<String, Module> getModules() {
        return modules;
    }

    public void addModule(Module module) {
        modules.putIfAbsent(module.getModCode(), module);
    }

    private void extractModuleCodes(JsonNode node, List<String> result) {
        if (node == null || node.isNull()) {
            return;
        }

        if (node.isTextual()) {
            String text = node.asText().trim();
            if (text.contains(":") && !text.split(":")[0].trim().isEmpty()) {
                String moduleCode = text.split(":")[0].trim();
                result.add(moduleCode);
            } else if (!text.isEmpty()) {
                result.add(text);
            }
            return;
        }

        if (node.isObject() && node.has("moduleCode")) {
            String modCode = node.get("moduleCode").asText();
            if (!modCode.trim().isEmpty()) {
                result.add(modCode);
            }
            return;
        }

        if (node.isArray()) {
            for (JsonNode subNode : node) {
                extractModuleCodes(subNode, result);
            }
        } else if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode value = field.getValue();

                extractModuleCodes(value, result);
            }
        }
    }
}

