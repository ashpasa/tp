package seedu.classcraft.studyplan;

import com.fasterxml.jackson.databind.JsonNode;
import seedu.classcraft.exceptions.StudyPlanException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates module prerequisites by evaluating the prereqTree structure
 * Handles complex AND/OR logic properly
 */
public class PrerequisiteChecker {

    /**
     * Validates if prerequisites are satisfied before adding module
     */
    public static void validatePrerequisites(Module module, int targetSemester,
                                             StudyPlan studyPlan) throws StudyPlanException {
        JsonNode prereqTree = module.getPrereqTree();

        // No prerequisites means module can be taken anytime
        if (prereqTree == null || prereqTree.isNull() || prereqTree.isMissingNode()) {
            return;
        }

        // Get completed modules from previous semesters
        Set<String> completedModules = getCompletedModules(targetSemester, studyPlan);

        // Evaluate the prerequisite tree
        boolean satisfied = evaluatePrereqTree(prereqTree, completedModules);

        if (!satisfied) {
            String errorMessage = buildErrorMessage(module.getModCode(),
                    targetSemester,
                    prereqTree,
                    completedModules);
            throw new StudyPlanException(errorMessage);
        }
    }

    /**
     * Recursively evaluates the prerequisite tree
     * Returns true if prerequisites are satisfied
     */
    private static boolean evaluatePrereqTree(JsonNode node, Set<String> completedModules) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return true;
        }

        if (node.isObject() && node.has("or")) {
            return evaluateOrNode(node.get("or"), completedModules);
        }

        if (node.isObject() && node.has("and")) {
            return evaluateAndNode(node.get("and"), completedModules);
        }
        
        if (node.isTextual()) {
            return isModuleCompleted(node.asText(), completedModules);
        }

        if (node.isObject() && node.has("moduleCode")) {
            return isModuleCompleted(node.get("moduleCode").asText(), completedModules);
        }

        return true;
    }

    private static boolean evaluateOrNode(JsonNode orNode, Set<String> completedModules) {
        if (!orNode.isArray()) {
            return false;
        }

        for (JsonNode child : orNode) {
            if (evaluatePrereqTree(child, completedModules)) {
                return true;
            }
        }
        return false;
    }

    private static boolean evaluateAndNode(JsonNode andNode, Set<String> completedModules) {
        if (!andNode.isArray()) {
            return true;
        }

        for (JsonNode child : andNode) {
            if (!evaluatePrereqTree(child, completedModules)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isModuleCompleted(String moduleText, Set<String> completedModules) {
        String moduleCode = stripGradeRequirement(moduleText);

        // Guard clause: Ignore bridging modules
        if (isBridgingModule(moduleCode)) {
            return true;
        }

        // Guard clause: Ignore invalid module codes
        if (!isValidModuleCode(moduleCode)) {
            return true;
        }

        return completedModules.contains(moduleCode);
    }

    private static boolean isValidModuleCode(String moduleCode) {
        return moduleCode.matches("^[A-Z]{2,3}\\d{4}[A-Z]?$");
    }

    /**
     * Gets all completed modules from previous semesters
     */
    private static Set<String> getCompletedModules(int targetSemester, StudyPlan studyPlan) {
        Set<String> completedModules = new HashSet<>();
        ArrayList<ArrayList<Module>> plan = studyPlan.getStudyPlan();

        for (int sem = 0; sem < targetSemester - 1 && sem < plan.size(); sem++) {
            for (Module mod : plan.get(sem)) {
                completedModules.add(mod.getModCode());
            }
        }

        return completedModules;
    }

    /**
     * Strips grade requirements from module codes (e.g., "CS1010:B" -> "CS1010")
     */
    private static String stripGradeRequirement(String moduleCode) {
        int colonIndex = moduleCode.indexOf(':');
        if (colonIndex != -1) {
            return moduleCode.substring(0, colonIndex);
        }
        return moduleCode;
    }

    /**
     * Checks if module is a bridging course that should be ignored
     */
    private static boolean isBridgingModule(String moduleCode) {
        return moduleCode.equals("MA1301")
                || moduleCode.equals("MA1301X")
                || moduleCode.equals("MA1301FC")
                || moduleCode.equals("PC1201");
    }

    /**
     * Builds user-friendly error message
     */
    private static String buildErrorMessage(String moduleCode, int targetSemester,
                                            JsonNode prereqTree,
                                            Set<String> completedModules) {
        StringBuilder message = new StringBuilder();
        message.append("Cannot add ").append(moduleCode)
                .append(" to semester ").append(targetSemester)
                .append(".\n\n");

        message.append("Required prerequisites: ")
                .append(prettifyPrereqTree(prereqTree))
                .append("\n\n");

        if (completedModules.isEmpty()) {
            message.append("You have not completed any prerequisite modules in earlier semesters.\n");
        } else {
            message.append("Completed modules: ")
                    .append(String.join(", ", completedModules))
                    .append("\n");
        }

        message.append("\nPlease add the required prerequisite modules to an earlier semester first.");

        return message.toString();
    }

    /**
     * Converts prereqTree to human-readable format
     */
    private static String prettifyPrereqTree(JsonNode node) {
        if (node == null || node.isNull()) {
            return "";
        }

        if (node.has("or")) {
            return prettifyLogicNode(node.get("or"), " OR ");
        }

        if (node.has("and")) {
            return prettifyLogicNode(node.get("and"), " AND ");
        }

        if (node.isTextual()) {
            return prettifyModuleCode(node.asText());
        }

        if (node.has("moduleCode")) {
            return prettifyModuleCode(node.get("moduleCode").asText());
        }

        return "";
    }

    private static String prettifyLogicNode(JsonNode logicNode, String separator) {
        if (!logicNode.isArray()) {
            return "";
        }

        List<String> parts = new ArrayList<>();
        for (JsonNode child : logicNode) {
            String part = prettifyPrereqTree(child);
            if (!part.isEmpty()) {
                parts.add(part);
            }
        }

        return parts.isEmpty() ? "" : "(" + String.join(separator, parts) + ")";
    }

    private static String prettifyModuleCode(String moduleText) {
        String code = stripGradeRequirement(moduleText);

        if (!isBridgingModule(code) && isValidModuleCode(code)) {
            return code;
        }
        return "";
    }
}
