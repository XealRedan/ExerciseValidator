package com.exercisevalidator.validators;

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

import com.exercisevalidator.ExerciseMap;
import com.exercisevalidator.model.ValidationData;
import com.exercisevalidator.model.ValidationDataList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Class used to validate an exercise
 */
public class ExerciseValidatorC implements IExerciseValidator {

    private static String COMPARE_PATH = "";
    private static String COMPARE_COMMAND_LINE = "";

    static {
        final Properties properties = new Properties();
        try (final InputStream in = ExerciseValidatorC.class.getResourceAsStream("ExerciseValidator.properties")) {
            properties.load(in);
            COMPARE_PATH = properties.getProperty("ComparePath");
            COMPARE_COMMAND_LINE = properties.getProperty("CompareCommandLine");
        } catch (IOException e) {
            //
        }
    }

    private File workingDirectory;

    private int exerciseId;

    private List<File> sourceFiles = new ArrayList<>();

    /** The list of validation data produced by the validate() function */
    private ValidationDataList validationDataList = new ValidationDataList();

    public ExerciseValidatorC(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public void validate() throws IOException {
        if(this.sourceFiles == null)
            throw new IllegalArgumentException("No input files");

        if(this.workingDirectory == null)
            throw new IllegalArgumentException("Working directory is not defined");

        if(ExerciseMap.getInstance().getExerciseData(this.exerciseId) == null)
            throw new IllegalArgumentException("No exercise found with id: " + this.exerciseId);

        // If there is already a compiled executable, it is deleted
        final File oldMain = new File(this.workingDirectory.getAbsolutePath() + "/main");
        if(oldMain.exists())
            oldMain.delete();

        // Compile files
        final List<String> compilationCommandLine = new ArrayList<>();
        compilationCommandLine.add("gcc");
        compilationCommandLine.add("-o main");
        for(File file : this.sourceFiles) {
            compilationCommandLine.add(file.getAbsolutePath());
        }

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

        int result = -1;
        try {
            result = compilationProcess.waitFor();
        } catch (InterruptedException e) {
            //
        }

        final List<File> inputFiles = ExerciseMap.getInstance().getExerciseData(this.exerciseId).getInputFiles();
        final List<File> outputFiles = ExerciseMap.getInstance().getExerciseData(this.exerciseId).getOutputFiles();

        // If the code does not compile, all validation results will have a success rate of 0
        if(result != 0) {
            for(int idx = 0; idx < inputFiles.size(); idx++) {
                final ValidationData validationData = new ValidationData();
                validationData.setExerciseId(this.exerciseId);
                validationData.setInputFile(inputFiles.get(idx).getName());
                validationData.setOutputFile(outputFiles.get(idx).getName());
                validationData.setSuccessRate(0);
                validationData.setError(text.toString());

                this.validationDataList.getValidationDataList().add(validationData);
            }

            return;
        }

        // Run the tests in a sandbox with each input file and compare it with output files
        for(int idx = 0; idx < inputFiles.size(); idx++) {
            final File inputFile = inputFiles.get(idx);
            final File outputFile = outputFiles.get(idx);

            final Object[] args = new Object[] {inputFile.getAbsolutePath(), outputFile.getAbsolutePath()};
            final MessageFormat commandLineFormat = new MessageFormat(COMPARE_COMMAND_LINE);

            // Compare return a 0 value if it matches
            final Process compareProcess = Runtime.getRuntime().exec(commandLineFormat.format(args));

            int compareReturnVal = -1;
            try {
                compareReturnVal = compareProcess.waitFor();
            } catch (InterruptedException e) {
                //
            }

            final ValidationData validationData = new ValidationData();
            validationData.setExerciseId(this.exerciseId);
            validationData.setInputFile(inputFile.getName());
            validationData.setOutputFile(outputFile.getName());
            validationData.setSuccessRate(compareReturnVal == 0 ? 1 : 0);
            // TODO Grab a more explicit error message from the standard output of compare
            validationData.setError(compareReturnVal == 0 ? "SUCCESS" : "FAILURE");

            this.validationDataList.getValidationDataList().add(validationData);
        }
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

    public ValidationDataList getValidationDataList() {
        return validationDataList;
    }
}
