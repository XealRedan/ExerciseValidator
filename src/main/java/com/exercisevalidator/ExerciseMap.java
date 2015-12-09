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


import com.exercisevalidator.model.ExerciseData;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton with a mapping between an exercise ID and the associated exercise data
 */
public final class ExerciseMap {

    private static final String EXERCISES_DIRECTORY = "/Exercises/";

    private static ExerciseMap instance;

    public static ExerciseMap getInstance() {
        if(instance == null)
            instance = new ExerciseMap();

        return instance;
    }

    private Map<Integer, ExerciseData> exercises = new HashMap<>();

    private ExerciseMap() {
        // Each directory in the exercises directory is an exercise
        final File exercisesDirectory = new File(EXERCISES_DIRECTORY);

        for(final File file : exercisesDirectory.listFiles()) {
            final ExerciseData exerciseData = new ExerciseData(file);

            if(exerciseData.isValid()) {
                this.exercises.put(exerciseData.getId(), exerciseData);
            }
        }
    }

    /**
     * Gets the exercise data for the given exercise
     * @param exerciseId the exercise ID
     * @return the exercise data
     */
    public ExerciseData getExerciseData(int exerciseId) {
        return this.exercises.get(exerciseId);
    }

    /**
     * Gets the collection of exercises
     * @return the collection of all exercises
     */
    public Collection<ExerciseData> getExercises() {
        return this.exercises.values();
    }
}
