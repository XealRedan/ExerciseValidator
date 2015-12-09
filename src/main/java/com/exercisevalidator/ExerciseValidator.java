package com.exercisevalidator;

/*
 * #%L
 * Exercise validator
 * %%
 * Copyright (C) 2015 Alexandre Lombard
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.exercisevalidator.model.ValidationData;
import com.exercisevalidator.model.ValidationDataList;

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

    private int exerciseId;

    private List<File> sourceFiles = new ArrayList<>();

    /** The list of input files for validation */
    private List<File> inputFiles = new ArrayList<>();

    /** The list of expected output files */
    private List<File> outputFiles = new ArrayList<>();

    /** The list of validation data produced by the validate() function */
    private ValidationDataList validationDataList = new ValidationDataList();

    public ExerciseValidator(int exerciseId) {
        this.exerciseId = exerciseId;

        // TODO Find the input/output files according to the exercise ID
    }

    public void validate() throws IOException, InterruptedException {
        if(this.sourceFiles == null)
            throw new IllegalArgumentException("No input files");

        if(this.workingDirectory == null)
            throw new IllegalArgumentException("Working directory is not defined");

        // If there is already a compiled executable, it is deleted
        final File oldMain = new File(this.workingDirectory.getAbsolutePath() + "/main");
        if(oldMain.exists())
            oldMain.delete();

        // Compile files
        String compilationCommandLine = "gcc -o main ";
        for(File file : this.sourceFiles) {
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

        // If the code does not compile, all validation results will have a success rate of 0
        if(result != 0) {
            for(int idx = 0; idx < this.inputFiles.size(); idx++) {
                final ValidationData validationData = new ValidationData();
                validationData.setExerciseId(this.exerciseId);
                validationData.setInputFile(this.inputFiles.get(idx).getName());
                validationData.setOutputFile(this.outputFiles.get(idx).getName());
                validationData.setSuccessRate(0);
                validationData.setError(text.toString());

                this.validationDataList.getValidationDataList().add(validationData);
            }

            return;
        }

        // Run the tests in a sandbox with each input file and compare it with output files
        final ProcessBuilder executionProcessBuilder = new ProcessBuilder("systrace -a main");
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public List<File> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(List<File> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public List<File> getOutputFiles() {
        return outputFiles;
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public ValidationDataList getValidationDataList() {
        return validationDataList;
    }
}
