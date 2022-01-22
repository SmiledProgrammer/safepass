package com.szinton.safepass.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
    private static final String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String digits = "0123456789";
    private static final String specialCharacters = "@+/'!:.-_*";
    private static final int minLength = 12;
    private static final int maxLength = 128;

    @Override
    public void initialize(StrongPassword strongPassword) { }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<String> errorMessages = new ArrayList<>();
        if (!StringUtils.containsAny(password, lowercaseLetters))
            errorMessages.add("Password must contain at least one lowercase letter.");
        if (!StringUtils.containsAny(password, uppercaseLetters))
            errorMessages.add("Password must contain at least one uppercase letter.");
        if (!StringUtils.containsAny(password, digits))
            errorMessages.add("Password must contain at least one digit.");
        if (!StringUtils.containsAny(password, specialCharacters))
            errorMessages.add("Password must contain at least one special character (one of: " + specialCharacters + ").");
        String allowedCharacters = lowercaseLetters + uppercaseLetters + digits + specialCharacters;
        if (!StringUtils.containsOnly(password, allowedCharacters))
            errorMessages.add("Password can only letters, digits and special characters (any of: " + specialCharacters + ").");
        if (password.length() < minLength) {
            errorMessages.add("Password must contain at least " + minLength + " characters.");
        } else if (password.length() > maxLength) {
            errorMessages.add("Password must contain at most " + maxLength + " characters.");
        }

        if (!errorMessages.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            StringUtils.join(errorMessages, ";"))
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
