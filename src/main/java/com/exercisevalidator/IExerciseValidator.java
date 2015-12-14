package com.exercisevalidator;

import com.exercisevalidator.model.ValidationDataList;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Generic interface for all exercises validator
 */
public interface IExerciseValidator {
    /**
     * Sets the working directory
     * @param file the working directory
     */
    void setWorkingDirectory(File file);

    /**
     * Sets the source files
     * @param sourceFiles the source files
     */
    void setSourceFiles(List<File> sourceFiles);

    /**
     * Runs the validation process
     * @throws IOException thrown if a source file cannot be read, or the executable cannot be written
     */
    void validate() throws IOException;

    /**
     * Gets the validation results under the form of a ValidationDataList
     * @return the validation results
     */
    ValidationDataList getValidationDataList();
}
