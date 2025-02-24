package com.business.i4_be.global.validation;

import com.business.i4_be.global.annotation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
  private Class<? extends Enum<?>> enumClass;
  private StringBuilder sb;

  @Override
  public void initialize(ValidEnum annotation) {
    this.enumClass = annotation.enumClass();
    this.sb = new StringBuilder();
    for(Enum<?> e : enumClass.getEnumConstants()) {
      if(sb.length() > 0) {
        sb.append(", ");
      }
      sb.append(e.name());
    }
  }

  @Override
  public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
    boolean isValid = Arrays.stream(enumClass.getEnumConstants())
        .anyMatch(e -> e == value);

    if(!isValid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
          "유효하지 않은 값 입니다: 사용 가능한 값: [" + sb.toString() + "]")
          .addConstraintViolation();
    }

    return isValid;
  }
}
