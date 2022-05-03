package com.cp.projects.messagingsystem.authserverapp.validation.annotation;

import com.cp.projects.messagingsystem.authserverapp.validation.RegisterRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegisterRequestValidator.class)
@Documented
public @interface ValidRegisterRequest {
    String message() default "Invalid registration request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
