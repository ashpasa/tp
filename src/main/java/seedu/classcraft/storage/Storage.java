package seedu.classcraft.storage;

import seedu.classcraft.studyplan.StudyPlan;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Storage class responsible for handling file operations related to
 * storing and retrieving study plan data.
 * A storage object is initialized with a file path where the data is stored.
 */
public class Storage {

    private static Logger logger = Logger.getLogger(Storage.class.getName());
    private String dataFile;

    /**
     * Constructor for Storage class.
     * Initializes the storage with the specified data file path.
     * Calls createFile to ensure the file and directory exists.
     *
     * @param dataFile The path to the data file for storing study plan information.
     */
    public Storage(String dataFile) {
        setLoggerLevel();
        assert dataFile != null : "Data file path cannot be null.";
        this.dataFile = dataFile;
        createFile();
    }

    /**
     * Appends a module code to the specified semester in the data file.
     * Reads all lines from the file, updates the line for the given semester,
     * and writes the updated lines back to the file.
     *
     * @param moduleCode The module code to append.
     * @param semester   The semester number (1-8) to which the module code should be appended.
     */
    public void appendToFile(String moduleCode, int semester) {
        Path filePath = Paths.get(dataFile);
        try {
            List<String> lines = Files.readAllLines(filePath);
            String line = lines.get(semester - 1);
            String updatedLine = line.concat(" " + moduleCode + ",");
            lines.set(semester - 1, updatedLine);
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
                logger.log(Level.WARNING, "Failed to create parent directory.");
                System.out.println("Failed to create parent directory.");
                return;
            }
        }

        try {
            if (f.createNewFile()) {
                System.out.println("Yay! A file created successfully.");
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                    logger.log(Level.INFO, "Initializing new data file with semester headers.");
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
     * @param semester       The semester number (1-8) from which the module code should be deleted.
     */
    public void deleteModule(String moduleToDelete, int semester) {
        try {
            Path filePath = Paths.get(dataFile);
            List<String> lines = Files.readAllLines(filePath);
            assert semester >= 1 && semester <= lines.size() : "Semester number out of bounds.";
            String line = lines.get(semester - 1);
            String updatedLine = line.replace(" " + moduleToDelete + ",", "");
            logger.log(Level.INFO, " Updated line after deletion." + updatedLine);
            lines.set(semester - 1, updatedLine);
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
    public StudyPlan restoreData(Storage storage) {
        int totalSemesters = 8;
        StudyPlan studyPlan = new StudyPlan(totalSemesters);
        try {
            Path filePath = Paths.get(dataFile);
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] restorationParts = line.split("-");
                int semester = Integer.parseInt(restorationParts[0].trim());
                if (restorationParts.length == 1) {
                    continue;
                }
                assert restorationParts.length == 2 : "Each line should contain a semester and module codes.";
                String modulesPart = restorationParts[1].trim();

                String[] modules = modulesPart.split(",");
                for (String module : modules) {
                    String moduleCode = module.trim();
                    if (!moduleCode.isEmpty()) {
                        studyPlan.addModule(moduleCode, semester, storage, true);
                    }
                }

            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to read the data file: " + e.getMessage());
            System.out.println("Oh no! I was not able to read the file: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while restoring data: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return studyPlan;
    }

    /**
     * Sets logger level depending on how the program is run.
     * When running from a jar file, it disables logging.
     * Otherwise, when running from an IDE, it displays all logging messages.
     */
    public void setLoggerLevel() {
        String className = "/" + this.getClass().getName().replace('.', '/') + ".class";
        URL resource = this.getClass().getResource(className);

        if (resource == null) {
            return;
        }

        String protocol = resource.getProtocol();
        if (Objects.equals(protocol, "jar")) {
            logger.setLevel(Level.OFF);
        } else if (Objects.equals(protocol, "file")) {
            logger.setLevel(Level.ALL);
        }
    }
}

