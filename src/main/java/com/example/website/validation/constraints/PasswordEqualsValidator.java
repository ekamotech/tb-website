package com.example.website.validation.constraints;

import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * パスワードフィールドと確認用パスワードフィールドが一致するかを検証するバリデータークラス。
 */
public class PasswordEqualsValidator implements ConstraintValidator<PasswordEquals, Object> {

    private String field1;
    private String field2;
    private String message;

    /**
     * アノテーションの初期化メソッド。
     *
     * @param annotation PasswordEquals アノテーション
     */
    @Override
    public void initialize(PasswordEquals annotation) {
        field1 = "password";
        field2 = "passwordConfirmation";
        message = annotation.message();
    }

    /**
     * フィールドの値が一致するかを検証します。
     *
     * @param value 検証対象のオブジェクト
     * @param context コンテキスト
     * @return フィールドの値が一致する場合は true、それ以外の場合は false
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        String field1Value = (String) beanWrapper.getPropertyValue(field1);
        String field2Value = (String) beanWrapper.getPropertyValue(field2);
        if ((field1Value.isEmpty() || field2Value.isEmpty()) || Objects.equals(field1Value, field2Value)) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(field2).addConstraintViolation();
            return false;
        }
    }

    
}
