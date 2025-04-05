package org.example.cdweb_be.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })//phạm vi áp dụng
@Target({FIELD})
@Retention(RUNTIME) // annotation sẽ dc xử lý lúc nào
@Constraint(validatedBy = {PhoneNumberValidator.class})
public @interface PhoneNumberConstraint {
    String message() default "Invalid ...";


    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

