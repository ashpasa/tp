package seedu.duke.nusmodsfetcher;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

// Fetches module information from NUSMods API, returns data as strings
public abstract class NUSmodsFetcher {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    // Fetch JSON as a tree of JsonNodes
    private static JsonNode fetchModuleJson(String moduleCode) throws Exception {
        String url = "https://api.nusmods.com/v2/2024-2025/modules/" + moduleCode + ".json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readTree(response.body());
    }

    // Get module title
    public static String getModuleTitle(String moduleCode) throws Exception {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.get("title").asText();
    }

    // Get module credits
    public static String getModuleCredits(String moduleCode) throws Exception {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.get("moduleCredit").asText();
    }

    // Get department
    public static String getDepartment(String moduleCode) throws Exception {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.get("department").asText();
    }

    // Get faculty
    public static String getFaculty(String moduleCode) throws Exception {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.get("faculty").asText();
    }

    // Get module description
    public static String getModuleDescription(String moduleCode) throws Exception {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.get("description").asText();
    }

    // Get module prerequisites
    public static String getModulePrerequisites(String moduleCode) throws Exception {
        JsonNode root = fetchModuleJson(moduleCode);
        return root.get("prerequisite").asText();
    }
}
