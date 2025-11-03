package seedu.classcraft.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Comparator;

import seedu.classcraft.studyplan.StudyPlan;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StorageTest {

    static final String TEST_FILE_PATH = "./test/test-studyplan.txt";
    Storage storage;

    @AfterEach
    void cleanup() throws IOException {
        Path filePath = Paths.get(TEST_FILE_PATH);
        Path parentDir = filePath.getParent();

        Files.deleteIfExists(filePath);

        if (parentDir != null && Files.exists(parentDir)) {
            Files.walk(parentDir)
                    .sorted(Comparator.reverseOrder()) // Delete children before parent
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }


    @Test
    void testCreateFile_createsFileWithSemesters() throws IOException {
        File f = new File(TEST_FILE_PATH);
        File parentDir = f.getParentFile();
        assertFalse(parentDir.exists(), "Parent directory should initially not exist.");
        storage = new Storage(TEST_FILE_PATH);
        assertTrue(parentDir.exists() && parentDir.isDirectory(), "Parent directory should be created.");
        assertTrue(new File(TEST_FILE_PATH).exists(), "Data file should be created inside the parent directory.");
    }

    @Test
    void testCreateFile_initializesWithEightSemesters() throws IOException {
        storage = new Storage(TEST_FILE_PATH);
        Path path = Paths.get(TEST_FILE_PATH);
        assertTrue(Files.exists(path));
        var lines = Files.readAllLines(path);
        assertEquals(9, lines.size());

        assertEquals("EXEMPTED -", lines.get(8));
    }

    @Test
    void testAppendToFile_appendsModulesCorrectly() throws IOException {
        storage = new Storage(TEST_FILE_PATH);

        storage.appendToFile("CS1010", 1);
        storage.appendToFile("MA1521", 1);

        var lines = Files.readAllLines(Paths.get(TEST_FILE_PATH));
        assertTrue(lines.get(0).contains("CS1010"));
        assertTrue(lines.get(0).contains("MA1521"));
    }

    @Test
    void testDeleteModule_removesModuleCorrectly() throws IOException {
        storage = new Storage(TEST_FILE_PATH);

        storage.appendToFile("CS1010", 1);
        storage.deleteModule("CS1010", 1);

        var lines = Files.readAllLines(Paths.get(TEST_FILE_PATH));
        assertFalse(lines.get(0).contains("CS1010"));
    }

    @Test
    void testRestoreData_addsModulesCorrectly() {
        storage = new Storage(TEST_FILE_PATH);

        storage.appendToFile("CS1010", 1);
        storage.appendToFile("MA1521", 2);
        StudyPlan restoredPlan = storage.restoreData(storage);

        assertTrue(restoredPlan.getStudyPlan().get(0).stream().anyMatch(m -> m.getModCode().equals("CS1010")));
        assertTrue(restoredPlan.getStudyPlan().get(1).stream().anyMatch(m -> m.getModCode().equals("MA1521")));


    }

}
