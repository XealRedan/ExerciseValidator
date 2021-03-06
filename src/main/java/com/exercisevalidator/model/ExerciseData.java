package com.exercisevalidator.model;

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


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/**
 * Contains data about an exercise
 */
@JsonIgnoreProperties({"inputFiles", "outputFiles", "valid"})
public class ExerciseData {
    /** The identifier of the exercise */
    private int id;
    /** The meta data of the exercise */
    private ExerciseMetaData metaData;

    /** Set to true if the exercise is valid (files not found for instance) */
    private boolean valid = false;

    private List<File> inputFiles = new ArrayList<>();
    private List<File> outputFiles = new ArrayList<>();

    public ExerciseData() {
        //
    }

    /**
     * Parse a directory to extract data like input files, output files, etc.
     * Id is in the name of the directory (it is the last number, after _)
     * Input files must have "IN_X_" at the beginning with X a unique number.
     * Output files must have "OUT_X_" at the beginning with X a unique number related to an input file.
     * Title, description and other meta data are not mandatory and can be found as strings in a json file with tags
     * "title", "description" and so on.
     * @param directory the directory to parse
     */
    public ExerciseData(File directory) {
        try {
            // Parse the exercise ID
            int underscoreIndex = directory.getName().lastIndexOf('_');

            if(underscoreIndex == -1 || underscoreIndex + 1 >= directory.getName().length()) {
                this.valid = false;
                return;
            }

            final String exerciseIdStr =
                    directory.getName().substring(underscoreIndex + 1, directory.getName().length());

            this.id = Integer.parseInt(exerciseIdStr);

            // Get the exercise meta data
            final File metaDataFile = new File(directory.getAbsolutePath() + File.separator + "meta.json");

            if(metaDataFile.exists()) {
                try {
                    this.metaData = new ObjectMapper().readValue(metaDataFile, ExerciseMetaData.class);
                } catch (IOException e) {
                    // Unable to read meta data file, they are not mandatory but log a warning
                }
            }

            // Get the input/output files in an ordered manner
            final SortedMap<Integer, File> inputFilesMap = new TreeMap<>();
            final SortedMap<Integer, File> outputFilesMap = new TreeMap<>();
            for(File file : directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith("in_") || name.startsWith("out_");
                }
            })) {
                final String fileName = FilenameUtils.removeExtension(file.getName());

                final String[] splittedInputFileName = fileName.split("_");
                if(splittedInputFileName.length < 2) {
                    this.valid = false;
                    return;
                }

                if(splittedInputFileName[0].equals("in")) {
                    inputFilesMap.put(Integer.parseInt(splittedInputFileName[1]), file);
                } else if(splittedInputFileName[0].equals("out")) {
                    outputFilesMap.put(Integer.parseInt(splittedInputFileName[1]), file);
                } else {
                    this.valid = false;
                    return;
                }
            }

            for(Map.Entry<Integer, File> inputFile : inputFilesMap.entrySet()) {
                final File outputFile = outputFilesMap.get(inputFile.getKey());

                if(outputFile == null) {
                    this.valid = false;
                    return;
                }

                this.inputFiles.add(inputFile.getValue());
                this.outputFiles.add(outputFile);
            }

            // TODO Parse the eventual meta data file
        } catch (NumberFormatException e) {
            this.valid = false;
            return;
        }

        this.valid = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ExerciseMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(ExerciseMetaData metaData) {
        this.metaData = metaData;
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public List<File> getOutputFiles() {
        return outputFiles;
    }

    public boolean isValid() {
        return valid;
    }

    public static class ExerciseMetaData {
        private String title;
        private String goal;
        private String rules;
        private String example;
        private String input;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGoal() {
            return goal;
        }

        public void setGoal(String goal) {
            this.goal = goal;
        }

        public String getRules() {
            return rules;
        }

        public void setRules(String rules) {
            this.rules = rules;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }
    }
}
