package org.ironhack.project.eventmanagement.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ironhack.project.eventmanagement.auth.request.ResetPasswordRequest;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, ResetPasswordRequest> {

    @Override
    public boolean isValid(ResetPasswordRequest request, ConstraintValidatorContext context) {
        if (request.getNewPassword() == null || request.getConfirmPassword() == null) {
            return true;
        }
        boolean match = request.getNewPassword().equals(request.getConfirmPassword());
        if (!match) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return match;
    }
}