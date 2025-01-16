package com.example.website.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * イベントの開始時間と終了時間の範囲が有効であることを検証するためのアノテーション。
 */
@Documented
@Constraint(validatedBy = ValidTimeRangeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTimeRange {
    
    String message() default "終了時間は開始時間より後である必要があります";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};

}

