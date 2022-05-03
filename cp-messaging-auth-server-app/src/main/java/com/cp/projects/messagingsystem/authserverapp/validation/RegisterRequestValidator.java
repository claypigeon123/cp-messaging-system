package com.cp.projects.messagingsystem.authserverapp.validation;

import com.cp.projects.messagingsystem.authserverapp.config.props.AuthServerAppProperties;
import com.cp.projects.messagingsystem.authserverapp.validation.annotation.ValidRegisterRequest;
import com.cp.projects.messagingsystem.cpmessagingdomain.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class RegisterRequestValidator implements ConstraintValidator<ValidRegisterRequest, RegisterRequest> {

    private final AuthServerAppProperties props;

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext ctx) {
        ctx.disableDefaultConstraintViolation();
        int errors = 0;

        if (request.getUsername().length() < props.getMinimumUsernameLength()) {
            ctx.buildConstraintViolationWithTemplate(props.getMessageUsernameTooShort())
                .addConstraintViolation();
            errors++;
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            ctx.buildConstraintViolationWithTemplate(props.getMessagePasswordsDontMatch())
                .addConstraintViolation();
            errors++;
        }

        if (request.getPassword().length() < props.getMinimumPasswordLength() || request.getConfirmPassword().length() < props.getMinimumPasswordLength()) {
            ctx.buildConstraintViolationWithTemplate(props.getMessagePasswordTooShort())
                .addConstraintViolation();
            errors++;
        }

        return errors == 0;
    }

}
