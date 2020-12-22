package br.com.grillo.util.validator;

import br.com.grillo.service.PartnerService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueCNPJValidator implements ConstraintValidator<ValidCNPJ, String> {

    @Autowired
    private PartnerService partnerService;


    @Override
    public void initialize(ValidCNPJ constraintAnnotation) {
        constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !partnerService.findByDocument(value).isPresent();
    }
}
