package org.example.cdweb_be.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberConstraint, String> {

    private static final String PHONE_REGEX_1 = "^0\\d{9}$"; // Ví dụ regex cho số điện thoại
    private static final String PHONE_REGEX_2 = "^\\+\\d{2} \\d{9}$"; // Ví dụ regex cho số điện thoại

    @Override
    public void initialize(PhoneNumberConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return true; // Không kiểm tra nếu là null (có thể dùng @NotNull để kiểm tra)
        }
        return Pattern.matches(PHONE_REGEX_1, phoneNumber) || Pattern.matches(PHONE_REGEX_2, phoneNumber);
    }
}