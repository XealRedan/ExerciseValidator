package com.exercisevalidator.aspect.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import java.io.File;
import java.util.logging.Logger;

/**
 * Logger aspect for the ExerciseData class (and more specifically its file parser)
 */
@Aspect
public class ExerciseDataLogger {
    @AfterThrowing("execution(* *.new(java.io.File)) && args(file)")
    public void logExerciseDataException(JoinPoint joinPoint, File file) {
        Logger.getLogger("ExerciseValidator").warning(
                "Exception thrown when parsing exercise folder " + file.getAbsolutePath());
    }
}
