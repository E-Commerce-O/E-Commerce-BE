package org.example.cdweb_be.validator;

import java.util.regex.Pattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailValidator implements ConstraintValidator<CustomEmail, String> {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(\\.[a-zA-Z]{2,})*\\.[a-zA-Z]{2,}$";;

    @Override
    public void initialize(CustomEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        log.info("CustomEmail: "+email);
        log.info("CustomEmail: "+Pattern.matches(EMAIL_REGEX, email));
        return Pattern.matches(EMAIL_REGEX, email);
    }
}