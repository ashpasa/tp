package seedu.classcraft.studyplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;

public class StudyPlanTest {
    
    @Test
    public void calculateSemCredits_singleSemester_returnsSumOfCredits() {
        StudyPlan plan = new StudyPlan(3); // 3 semesters
        Module m1 = new Module("Mod A", "A1010", 4, "desc", Collections.emptyList(), 0, 1);
        Module m2 = new Module("Mod B", "B2020", 3, "desc", Collections.emptyList(), 0, 1);

        plan.addModule(m1, 1);
        plan.addModule(m2, 1);

        assertEquals(7, plan.calculateSemCredits(0)); // semesterIndex 0 == semester 1
    }

    @Test
    public void calculateSemCredits_totalCredits_returnsSumAcrossAllSemesters() {
        StudyPlan plan = new StudyPlan(2);
        Module m1 = new Module("Mod A", "A1010", 4, "desc", Collections.emptyList(), 0, 1);
        Module m2 = new Module("Mod B", "B2020", 3, "desc", Collections.emptyList(), 0, 1);
        Module m3 = new Module("Mod C", "C3030", 5, "desc", Collections.emptyList(), 0, 2);

        plan.addModule(m1, 1);
        plan.addModule(m2, 1);
        plan.addModule(m3, 2);

        assertEquals(12, plan.calculateSemCredits(-1));
    }

    @Test
    public void calculateSemCredits_emptySemester_returnsZero() {
        StudyPlan plan = new StudyPlan(2);
        assertEquals(0, plan.calculateSemCredits(1));
    }

    @Test
    public void calculateSemCredits_invalidSemester_throwsIllegalArgumentException() {
        StudyPlan plan = new StudyPlan(2);
        assertThrows(IllegalArgumentException.class, () -> plan.calculateSemCredits(5));
    }
}
