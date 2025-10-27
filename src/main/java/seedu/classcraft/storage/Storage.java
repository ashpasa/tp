package seedu.classcraft.storage;

import seedu.classcraft.studyplan.StudyPlan;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Storage class responsible for handling file operations related to
 * storing and retrieving study plan data.
 * A storage object is initialized with a file path where the data is stored.
 */
public class Storage {

    private String dataFile;

    /**
     * Constructor for Storage class.
     * Initializes the storage with the specified data file path.
     * Calls createFile to ensure the file and directory exists.
     *
     * @param dataFile The path to the data file for storing study plan information.
     */
    public Storage(String dataFile) {
        this.dataFile = dataFile;
        createFile();
    }

    /**
     * Appends a module code to the specified semester in the data file.
     * Reads all lines from the file, updates the line for the given semester,
     * and writes the updated lines back to the file.
     * @param moduleCode The module code to append.
     * @param semester The semester number (1-8) to which the module code should be appended.
     */
    public void appendToFile( String moduleCode, int semester) {
        Path filePath = Paths.get(dataFile);
        try {
            List<String> lines = Files.readAllLines(filePath);
            String line = lines.get(semester -1);
            String updatedLine = line.concat(" " + moduleCode + ",");
            lines.set(semester -1, updatedLine);
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Oh no! I was not able to update the file: " + e.getMessage());
        }

    }



    /**
     * Method to create a file at the specified file path.
     * If the parent directory does not exist, it creates the necessary directories.
     * If the file is created successfully, it initializes the file with
     * semester headers from 1 to 8.
     */
    public void createFile() {
        File f = new File(dataFile);
        File parentDir = f.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                System.out.println("Parent directory created successfully.");
            } else {
                System.out.println("Failed to create parent directory.");
                return;
            }
        }

        try {
            if (f.createNewFile()) {
                System.out.println("Yay! A file created successfully.");
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                    for (int i = 1; i <= 8; i++) {
                        bw.write(i + " -");
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(" :{ an error occurred while creating the file: " + e.getMessage());
        }

    }


    /**
     * Deletes a module code from the specified semester in the data file.
     * Reads all lines from the file, updates the line for the given semester, by
     * removing the specified module code, and writes the updated lines back to the file.
     *
     * @param moduleToDelete The module code to delete.
     */
    public void deleteModule(String moduleToDelete, int semester) {
        try {
            Path filePath = Paths.get(dataFile);
            List<String> lines = Files.readAllLines(filePath);
            String line = lines.get(semester -1);
            String updatedLine = line.replace(" " + moduleToDelete + ",", "");
            lines.set(semester -1, updatedLine);
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Oh no! I was not able to update the file: " + e.getMessage());
        }

    }

    /**
     * Restores study plan data from the data file.
     * Reads each line from the file, parses the semester and module codes,
     * and adds the modules to a StudyPlan object using the addModule method.
     *
     * @param storage The storage handler to read/write data.
     * @return A StudyPlan object populated with the restored data.
     */
    public StudyPlan restoreData (Storage storage) {
        int totalSemesters = 8;
        StudyPlan studyPlan = new StudyPlan(totalSemesters);
        try {
            Path filePath = Paths.get(dataFile);
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] parts = line.split("-");
                int semester = Integer.parseInt(parts[0].trim());

                if (parts.length == 1) {// No modules for this semester
                    continue;
                }
                String modulesPart = parts[1].trim();

                String[] modules = modulesPart.split(",");
                for (String module : modules) {
                    String moduleCode = module.trim();
                    if (!moduleCode.isEmpty()) {
                        studyPlan.addModule(moduleCode, semester,storage, true);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Oh no! I was not able to read the file: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return studyPlan;
    }

}

