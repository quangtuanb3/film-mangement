package com.example.film.validateInterface;

import com.example.film.customValidator.BoundaryDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BoundaryDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBoundaryDates {
    String message() default "Date is not within the specified range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String startDate(); // Specify the start date for validation
    String endDate();   // Specify the end date for validation
}
