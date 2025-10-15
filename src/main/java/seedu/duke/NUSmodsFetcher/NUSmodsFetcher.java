package seedu.duke.NUSmodsFetcher;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import seedu.duke.exceptions.NUSmodsFetcherException;
import com.fasterxml.jackson.databind.JsonNode;

// Fetches module information from NUSMods API, returns data as strings
public abstract class NUSmodsFetcher {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    // Fetch JSON as a tree of JsonNodes
    private static JsonNode fetchModuleJson(String moduleCode) throws NUSmodsFetcherException {
        try {
            String url = "https://api.nusmods.com/v2/2024-2025/modules/" + moduleCode + ".json";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new Exception("Failed to fetch module data: HTTP " + response.statusCode());
            }
            return mapper.readTree(response.body());
        } catch (Exception e) {
            throw new NUSmodsFetcherException("Error fetching module data for " + moduleCode, e);
        }
    }

    // Get module title
    public static String getModuleTitle(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.path("title").asText("Unknown Title");
    }

    // Get module credits
    public static String getModuleCredits(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.path("moduleCredit").asText("N/A");
    }

    // Get department
    public static String getDepartment(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.path("department").asText("Unknown Department");
    }

    // Get faculty
    public static String getFaculty(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.path("faculty").asText("Unknown Faculty");
    }

    // Get module description
    public static String getModuleDescription(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.path("description").asText("Description unavailable");
    }

    // Get module prerequisites
    public static String getModulePrerequisites(String moduleCode) throws NUSmodsFetcherException {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.get("prerequisite").asText("None");
    }
}
