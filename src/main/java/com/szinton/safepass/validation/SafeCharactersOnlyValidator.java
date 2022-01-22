package com.szinton.safepass.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SafeCharactersOnlyValidator implements ConstraintValidator<SafeCharactersOnly, String> {

    private static final String allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.-";

    @Override
    public void initialize(SafeCharactersOnly str) { }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        return StringUtils.containsOnly(str, allowedCharacters);
    }
}
