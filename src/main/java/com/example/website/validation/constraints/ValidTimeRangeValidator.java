package com.example.website.validation.constraints;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.example.website.form.EventForm;

/**
 * イベントの開始時間と終了時間の範囲が有効であることを検証するバリデータークラス。
 */
public class ValidTimeRangeValidator implements ConstraintValidator<ValidTimeRange, EventForm> {
    
    private String startTime;
    private String endTime;
    private String message;
    
    /**
     * アノテーションの初期化メソッド。
     *
     * @param annotation ValidTimeRange アノテーション
     */
    @Override
    public void initialize(ValidTimeRange annotation) {
        startTime = "startTime";
        endTime = "endTime";
        message = annotation.message();
    }
    
    /**
     * イベントの開始時間と終了時間の範囲が有効であるかを検証します。
     *
     * @param eventForm 検証対象のイベントフォームオブジェクト
     * @param context コンテキスト
     * @return 開始時間が終了時間より前である場合は true、それ以外の場合は false
     */
    @Override
    public boolean isValid(EventForm eventForm, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(eventForm);
        LocalTime startTimeValue = (LocalTime) beanWrapper.getPropertyValue(startTime);
        LocalTime endTimeValue = (LocalTime) beanWrapper.getPropertyValue(endTime);
        if(startTimeValue == null || endTimeValue == null) {
            return true;
        }
        if(startTimeValue.isBefore(endTimeValue)) {
            return true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(endTime).addConstraintViolation();
            return false;
        }
    }
    

}

