package seedu.classcraft.nusmodsfetcher;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import seedu.classcraft.exceptions.NUSmodsFetcherException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

// @@author ashpasa
// Fetches module information from NUSMods API, returns data as strings
public abstract class NUSmodsFetcher {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Fetches the module JSON data from NUSMods API for the given module code
     * @param moduleCode The module code to fetch data for, as displayed on NUSMods
     * @return JsonNode representing the module data
     */
    public static JsonNode fetchModuleJson(String moduleCode) throws NUSmodsFetcherException {
        String url = "https://api.nusmods.com/v2/2025-2026/modules/" + moduleCode + ".json";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readTree(response.body());
        } catch (Exception e) {
            throw new NUSmodsFetcherException("Failed to fetch module data for " + moduleCode + ": " + e.getMessage());
        }
    }

    /**
     * Helper method to extract a specific field from the JSON root
     * @param root The root JsonNode, obtained from fetchModuleJson
     * @param fieldName The field name to extract
     * @return The text stored in the .json file under fieldName, as a string
     */
    private static String extractField(JsonNode root, String fieldName) {
        return root.path(fieldName).asText("");
    }

    /**
     * @param moduleCode The module code to fetch data for, as displayed on NUSMods
     * @return String representing the module title
     * @throws Exception
     */
    public static String getModuleTitle(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return extractField(root, "title");
    }

    /**
     * @param moduleCode The module code to fetch data for, as displayed on NUSMods
     * @return String representing the module credits that the module counts for
     * @throws Exception
     */
    public static int getModuleCredits(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        String moduleCreditsAsString = extractField(root, "moduleCredit");
        int moduleCredits = Integer.parseInt(moduleCreditsAsString);
        return moduleCredits;
    }

    /**
     * @param moduleCode The module code to fetch data for, as displayed on NUSMods
     * @return String representing the department offering the module
     * @throws Exception
     */
    public static String getDepartment(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return extractField(root, "department");
    }

    /**
     * @param moduleCode The module code to fetch data for, as displayed on NUSMods
     * @return String representing the faculty offering the module
     * @throws Exception
     */
    public static String getFaculty(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return extractField(root, "faculty");
    }

    /**
     * @param moduleCode The module code to fetch data for, as displayed on NUSMods
     * @return The description of the module on NUSMods, as a string
     * @throws Exception
     */
    public static String getModuleDescription(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return extractField(root, "description");
    }

    /**
     * @param moduleCode The module code to fetch data for, as displayed on NUSMods
     * @return The direct prerequisites of the module on NUSMods, as a string
     * @throws Exception
     */
    public static String getModulePrerequisites(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return extractField(root, "prerequisite");
    }
}
// @@author
