package seedu.classcraft.ui;

import com.fasterxml.jackson.databind.JsonNode;
import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.studyplan.Module;
import java.util.ArrayList;

public class Ui {
    private String line = "_____________________________________________________" + System.lineSeparator();

    public void printMessage(String message) {
        System.out.print(line);
        System.out.println(message);
        System.out.print(line);
    }

    /**
     * Prints the contents of a study plan, showing modules by semester.
     * @param plan The study plan data (either current or sample).
     * @param title The title to display (e.g., "CEG Sample Study Plan").
     */
    private void displayStudyPlan(StudyPlan plan, String title) {
        System.out.print(line);
        System.out.println(title);
        System.out.print(line);

        ArrayList<ArrayList<Module>> planData = plan.getStudyPlan();

        for (int i = 0; i < planData.size(); i++) {
            System.out.println("Semester " + (i + 1) + ":");
            ArrayList<Module> semesterMods = planData.get(i);

            if (semesterMods.isEmpty()) {
                System.out.println("  (Empty)");
                continue;
            }

            for (Module mod : semesterMods) {
                String prereqsInfo = mod.getPrerequisitesDisplay();

                System.out.println("  - " + mod.getModCode() + " (" + mod.getModName() + ")" + prereqsInfo);
            }
        }
        System.out.print(line);
    }

    public void displaySamplePlan(StudyPlan samplePlan) {
        displayStudyPlan(samplePlan, "CEG Sample Study Plan");
    }

    public void displayCurrentPlan(StudyPlan currentPlan) {
        displayStudyPlan(currentPlan, "Current Study Plan");
    }

    /**
     * @param semesterIndex The index of the semester in the ArrayList, with -1 representing overall total
     * @param totalCredits The number of module credits for the corresponding semester, or overall
     */
    public void displayTotalCredits(int semesterIndex, int totalCredits) {
        String semesterString = "";
        try {
            if (semesterIndex == -1) {
                semesterString = "Total";
            } else {
                semesterString = "Semester " + Integer.toString(semesterIndex + 1);
            }
        } catch (Exception e) {
            semesterString = "Invalid Semester";
        }

        System.out.print(line);
        System.out.println(semesterString + " Module Credits: " + Integer.toString(totalCredits));
        System.out.print(line);
    }

    public void showError(String errorMessage) {
        System.out.println("============================================================");
        System.out.println("ERROR: " + errorMessage);
        System.out.println("============================================================");
    }

    public void showMessage(String message) {
        System.out.println("============================================================");
        System.out.println(message);
        System.out.println("============================================================");
    }

    public void displayPrerequisites(String moduleCode, String moduleTitle, JsonNode prereqTree) {
        System.out.print(line);
        System.out.println("Module: " + moduleCode + " - " + moduleTitle);
        System.out.print(line);

        if (prereqTree == null || prereqTree.isNull() || prereqTree.isMissingNode()) {
            System.out.println("Prerequisites: None");
            System.out.println();
            System.out.println("This module has no prerequisites. You can take it in any semester!");
        } else {
            String prereqString = prettifyPrereqTree(prereqTree);
            System.out.println("Prerequisites: " + prereqString);
            System.out.println();
            System.out.println("Note: You need to satisfy these prerequisites before taking this module.");
        }

        System.out.print(line);
    }

    /**
     * Displays an error message for prerequisite lookup
     */
    public void displayPrereqError(String moduleCode) {
        System.out.print(line);
        System.out.println("Error: Could not fetch prerequisites for " + moduleCode);
        System.out.println("Please check that the module code is valid.");
        System.out.print(line);
    }

    /**
     * Converts prereqTree JSON to human-readable format
     */
    private String prettifyPrereqTree(JsonNode node) {
        if (node == null || node.isNull()) {
            return "None";
        }

        // Handle OR nodes
        if (node.has("or")) {
            StringBuilder result = new StringBuilder();
            JsonNode orNode = node.get("or");
            if (orNode.isArray()) {
                result.append("(");
                boolean first = true;
                for (JsonNode child : orNode) {
                    String childStr = prettifyPrereqTree(child);
                    if (!childStr.isEmpty() && !isBridgingModule(childStr)) {
                        if (!first) {
                            result.append(" OR ");
                        }
                        result.append(childStr);
                        first = false;
                    }
                }
                result.append(")");
                return result.toString();
            }
        }

        // Handle AND nodes
        if (node.has("and")) {
            StringBuilder result = new StringBuilder();
            JsonNode andNode = node.get("and");
            if (andNode.isArray()) {
                result.append("(");
                boolean first = true;
                for (JsonNode child : andNode) {
                    String childStr = prettifyPrereqTree(child);
                    if (!childStr.isEmpty() && !isBridgingModule(childStr)) {
                        if (!first) {
                            result.append(" AND ");
                        }
                        result.append(childStr);
                        first = false;
                    }
                }
                result.append(")");
                return result.toString();
            }
        }

        // Handle text nodes (module codes with grades)
        if (node.isTextual()) {
            String code = stripGradeRequirement(node.asText());
            if (isValidModuleCode(code) && !isBridgingModule(code)) {
                return code;
            }
            return "";
        }

        // Handle objects with moduleCode field
        if (node.has("moduleCode")) {
            String code = stripGradeRequirement(node.get("moduleCode").asText());
            if (isValidModuleCode(code) && !isBridgingModule(code)) {
                return code;
            }
            return "";
        }

        return "";
    }

    private String stripGradeRequirement(String moduleCode) {
        int colonIndex = moduleCode.indexOf(':');
        if (colonIndex != -1) {
            return moduleCode.substring(0, colonIndex);
        }
        return moduleCode;
    }

    private boolean isValidModuleCode(String code) {
        return code != null && code.matches("^[A-Z]{2,3}\\d{4}[A-Z]?$");
    }

    private boolean isBridgingModule(String code) {
        return code.equals("MA1301") || code.equals("MA1301X")
                || code.equals("MA1301FC") || code.equals("PC1201");
    }
}
