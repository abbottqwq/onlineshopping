package com.edu.neu.project.validation;

import com.edu.neu.project.validationannotation.NoXSSString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NoXSSStringValidator implements ConstraintValidator<NoXSSString, String> {
    private final String PATTERN = "[\\<]\\w+[\\>]";

    @Override
    public void initialize(NoXSSString constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !Pattern.compile(PATTERN).matcher(value).find();
    }
}
