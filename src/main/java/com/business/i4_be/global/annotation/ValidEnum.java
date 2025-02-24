package com.business.i4_be.global.annotation;

import com.business.i4_be.global.validation.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {

  Class<? extends Enum<?>> enumClass();

  String message() default "유효하지 않은 값입니다:";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
