package seedu.classcraft;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import seedu.classcraft.storage.Storage;
import seedu.classcraft.studyplan.StudyPlan;

class StorageTest {

    static final String TEST_FILE_PATH = "./test/test-studyplan.txt";
    Storage storage;

    @BeforeEach
    void setup() {
        storage = new Storage(TEST_FILE_PATH);
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    @Test
    void testCreateFile_createsFileWithSemesters() throws IOException {
        Path path = Paths.get(TEST_FILE_PATH);
        assertTrue(Files.exists(path));

        var lines = Files.readAllLines(path);
        assertEquals(8, lines.size());
        for (int i = 1; i <= 8; i++) {
            assertEquals(i + " -", lines.get(i - 1));
        }
    }

    @Test
    void testAppendToFile_appendsModulesCorrectly() throws IOException {
        storage.appendToFile("CS1010", 1);
        storage.appendToFile("MA1521", 1);

        var lines = Files.readAllLines(Paths.get(TEST_FILE_PATH));
        assertTrue(lines.get(0).contains("CS1010"));
        assertTrue(lines.get(0).contains("MA1521"));
    }

    @Test
    void testDeleteModule_removesModuleCorrectly() throws IOException {
        storage.appendToFile("CS1010", 1);
        storage.deleteModule("CS1010", 1);

        var lines = Files.readAllLines(Paths.get(TEST_FILE_PATH));
        assertFalse(lines.get(0).contains("CS1010"));
    }

    @Test
    void testRestoreData_addsModulesCorrectly() {
        storage.appendToFile("CS1010", 1);
        storage.appendToFile("MA1521", 2);
        StudyPlan restoredPlan = storage.restoreData(storage);

        assertTrue(restoredPlan.getStudyPlan().get(0).stream().anyMatch(m -> m.getModCode().equals("CS1010")));
        assertTrue(restoredPlan.getStudyPlan().get(1).stream().anyMatch(m -> m.getModCode().equals("MA1521")));
    }

}
