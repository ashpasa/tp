package seedu.classcraft.storage;

import seedu.classcraft.studyplan.StudyPlan;
import seedu.classcraft.ui.Ui;

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
    private Ui ui = new Ui();

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
            ui.showMessage("Oh no! I was not able to update the file with the module " + e.getMessage());
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
                System.out.println("Yay! A file has been created successfully.");
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                    logger.log(Level.INFO, "Initializing new data file with semester headers.");
                    for (int i = 1; i <= 8; i++) {
                        bw.write(i + " -");
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            ui.showMessage(" :{ an error occurred while creating the file: " + e.getMessage());
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
            ui.showMessage("Oh no! I was not able to update the file: " + e.getMessage());
        }

    }


    /**
     * Restores study plan data from the data file.
     * Creates a StudyPlan object and populates it with data read from the file.
     * If the file format is invalid, it recreates the file and returns an empty StudyPlan.
     *
     * @param storage The storage handler to read/write data.
     * @return A StudyPlan object populated with the restored data.
     */
    public StudyPlan restoreData(Storage storage) {
        int totalSemesters = 8;
        StudyPlan studyPlan = new StudyPlan(totalSemesters);
        Path filePath = Paths.get(dataFile);

        try {
            if (isFileFormatInvalid(filePath)) {
                recreateFile(filePath);
                return new StudyPlan(totalSemesters);
            }
            populateStudyPlan(filePath, studyPlan, storage);
            System.out.println("Data restored successfully from " + dataFile);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to read the data file: " + e.getMessage());
            ui.showMessage("Oh no! I was not able to read the file: " + e.getMessage());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while restoring data: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return studyPlan;
    }


    private boolean isFileFormatInvalid(Path filePath) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            if (lines.size() != 8) {
                ui.showMessage("File has incorrect number of lines.\n" +
                        "File format is invalid. Recreating a new file.");
                return true;
            }

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] restorationParts;
                int countDash = line.length() - line.replace("-", "").length();
                if (countDash > 1) {
                    ui.showMessage("Line " + (i + 1) + " has multiple dashes.\n" +
                            "File format is invalid. Recreating a new file.");
                    return true;
                }
                if (countDash == 0) {
                    ui.showMessage("Line " + (i + 1) + " is missing a dash.\n" +
                            "File format is invalid. Recreating a new file.");
                    return true;
                }
                restorationParts = line.split("-", 2);
                if (restorationParts.length != 2) {
                    ui.showMessage("Line " + (i + 1) + " is not properly formatted.\n +" +
                            "File format is invalid. Recreating a new file.");
                    return true;
                }

                String semesterStr = restorationParts[0].trim();
                int expectedSemester = i + 1;
                int actualSemester;
                try {
                    actualSemester = Integer.parseInt(semesterStr);
                } catch (NumberFormatException e) {
                    ui.showMessage("Semester number in line " + (i + 1) + " is not a valid integer.\n" +
                            "File format is invalid. Recreating a new file.");
                    return true;
                }
                if (actualSemester != expectedSemester) {
                    ui.showMessage("Semester number in line " + (i + 1) + " is incorrect.\n" +
                            "File format is invalid. Recreating a new file.");
                    return true;
                }

                String[] modules = restorationParts[1].split(",");
                for (String module : modules) {
                    module = module.trim();
                    if (module.split(" ").length > 1) {
                        ui.showMessage("Module code '" + module + "' in line " + (i + 1) +
                                " contains invalid spaces.\n" +
                                "File format is invalid. Recreating a new file.");
                        return true;
                    }
                }

                if (!restorationParts[1].matches("^[a-zA-Z0-9,\\s]*$")) {
                    ui.showMessage("Line " + (i + 1) + " contains invalid characters.\n" +
                            "File format is invalid. Recreating a new file.");
                    return true;
                }

            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "File format is incorrect " + e.getMessage());
            ui.showMessage("Oh no! I was not able to read the file: " + e.getMessage() +
                    "\nFile format is invalid. Recreating a new file.");
            return true;
        }
        return false;
    }


    private void recreateFile(Path filePath) throws IOException {
        Files.deleteIfExists(filePath);
        createFile();
    }


    private void populateStudyPlan(Path filePath, StudyPlan studyPlan, Storage storage) {

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] restorationParts = line.trim().split("-");
                int semester = Integer.parseInt(restorationParts[0].trim());
                if (restorationParts.length == 1) {
                    continue;
                }
                String modulesPart = restorationParts[1].trim();

                String[] modules = modulesPart.split(",");
                for (String module : modules) {
                    if (module.isEmpty() || module.equals(" ")) {
                        continue;
                    }
                    String moduleCode = module.trim();
                    if (!(moduleCode.isEmpty() && moduleCode.matches("^[a-zA-Z0-9]+$"))) {
                        studyPlan.addModule(moduleCode, semester, storage, true);
                    }
                }

            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to populate study plan: " + e.getMessage());
            ui.showMessage("Oh no! I was not able to read the file: " + e.getMessage());
        }

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

