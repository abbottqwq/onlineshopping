package com.edu.neu.project.validation;

import com.edu.neu.project.model.UserAccountInfo;
import com.edu.neu.project.validationannotation.MatchPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<MatchPassword, Object> {

    @Override
    public void initialize(MatchPassword constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        UserAccountInfo userAccountInfo = (UserAccountInfo) value;
        return userAccountInfo.getPassword().equals(userAccountInfo.getRetypePassword());
    }
}
