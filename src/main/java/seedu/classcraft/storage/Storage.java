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

public class Storage {

    private String dataFile;

    public Storage(String dataFile) {
        this.dataFile = dataFile;
        createFile(dataFile);
    }

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



    public void createFile(String filePath) {
        File f = new File(filePath);
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

    public StudyPlan restoreData (Storage storage) {
        StudyPlan studyPlan = new StudyPlan(8);
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

