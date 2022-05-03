package com.cp.projects.messagingsystem.authserverapp.validation;

import com.cp.projects.messagingsystem.authserverapp.config.props.AuthServerAppProperties;
import com.cp.projects.messagingsystem.authserverapp.validation.annotation.ValidRegisterRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.RegisterRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class RegisterRequestValidator implements ConstraintValidator<ValidRegisterRequest, RegisterRequest> {
    private final int MIN_USERNAME_LEN;
    private final int MIN_PASSWORD_LEN;

    public RegisterRequestValidator(AuthServerAppProperties props) {
        MIN_USERNAME_LEN = props.getMinimumUsernameLength();
        MIN_PASSWORD_LEN = props.getMinimumPasswordLength();
    }

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext ctx) {
        ctx.disableDefaultConstraintViolation();
        int errors = 0;

        if (request.getUsername().length() < MIN_USERNAME_LEN) {
            ctx.buildConstraintViolationWithTemplate("Username must be at least " + MIN_USERNAME_LEN + " characters long")
                .addConstraintViolation();
            errors++;
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            ctx.buildConstraintViolationWithTemplate("Passwords didn't match")
                .addConstraintViolation();
            errors++;
        }

        if (request.getPassword().length() < MIN_PASSWORD_LEN || request.getConfirmPassword().length() < MIN_PASSWORD_LEN) {
            ctx.buildConstraintViolationWithTemplate("Password must be at least " + MIN_PASSWORD_LEN + " characters long")
                .addConstraintViolation();
            errors++;
        }

        return errors == 0;
    }

}
