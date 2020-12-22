package br.com.grillo.util.validator;

import javax.validation.ConstraintValidatorContext;

public interface ValidModel {

    default boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
