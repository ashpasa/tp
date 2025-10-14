package seedu.duke.data;

import java.util.Collections;
import java.util.Map;

/**
 * Stores the default graduation requirements for the CEG programme.
 */
public class GraduationRequirements {

    private final int totalRequiredMCs;                      // Minimum total MCs required for graduation (e.g., 160)

    // Map storing required MCs per category (e.g., {"Core": 80, "UEM": 20, ...})
    private final Map<String, Integer> categoryMCs;

    /**
     * Constructor for GraduationRequirements.
     *
     * @param totalRequiredMCs The overall minimum MCs.
     * @param categoryMCs      A map detailing minimum MCs for each requirement category.
     */
    public GraduationRequirements(int totalRequiredMCs, Map<String, Integer> categoryMCs) {
        this.totalRequiredMCs = totalRequiredMCs;
        // Store an unmodifiable copy to ensure the requirements remain static and safe
        this.categoryMCs = Collections.unmodifiableMap(categoryMCs);
    }

    public int getTotalRequiredMCs() {
        return totalRequiredMCs;
    }

    /**
     * Returns an unmodifiable map of category MC requirements.
     *
     * @return A map where keys are requirement types and values are minimum MCs.
     */
    public Map<String, Integer> getCategoryMCs() {
        return categoryMCs;
    }

    // for easy debugging and display
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CEG Graduation Requirements:\n");
        sb.append("  Total Required MCs: ").append(totalRequiredMCs).append("\n");
        sb.append("  Category Breakdown:\n");
        categoryMCs.forEach((category, mcs) ->
                sb.append("    - ").append(category).append(": ").append(mcs).append(" MCs\n")
        );
        return sb.toString();
    }
}
