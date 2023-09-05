package com.example.film.customValidator;

import com.example.film.validateInterface.ValidBoundaryDates;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BoundaryDateValidator implements ConstraintValidator<ValidBoundaryDates, LocalDate> {
    private LocalDate startDate;
    private LocalDate endDate;

    @Override
    public void initialize(ValidBoundaryDates constraintAnnotation) {
        startDate = LocalDate.parse(constraintAnnotation.startDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        endDate = LocalDate.parse(constraintAnnotation.endDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Skip validation if the date is null
        }
        // Implement your custom logic to check if the date is within the specified range
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
