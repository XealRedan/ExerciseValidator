package com.exercisevalidator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class used to validate an exercise
 */
public class ExerciseValidator {

    private File workingDirectory;
    private List<File> files = new ArrayList<>();

    public void validate() throws IOException, InterruptedException {
        if(this.files == null)
            throw new IllegalArgumentException("No input files");

        if(this.workingDirectory == null)
            throw new IllegalArgumentException("Working directory is not defined");

        // If there is already a compiled executable, it is deleted
        final File oldMain = new File(this.workingDirectory.getAbsolutePath() + "/main");
        if(oldMain.exists())
            oldMain.delete();

        // Compile files
        String compilationCommandLine = "gcc -o main ";
        for(File file : this.files) {
            compilationCommandLine += file.getAbsolutePath() + " ";
        }
        compilationCommandLine = compilationCommandLine.trim();

        final ProcessBuilder compilationProcessBuilder = new ProcessBuilder(compilationCommandLine);
        compilationProcessBuilder.directory(this.workingDirectory);
        compilationProcessBuilder.redirectErrorStream(true);

        final Process compilationProcess = compilationProcessBuilder.start();

        final Scanner s = new Scanner(compilationProcess.getInputStream());
        final StringBuilder text = new StringBuilder();
        while (s.hasNextLine()) {
            text.append(s.nextLine());
            text.append("\n");
        }
        s.close();

        final int result = compilationProcess.waitFor();

        // Run them in a sandbox
        final ProcessBuilder executionProcessBuilder = new ProcessBuilder("systrace -a main");
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
