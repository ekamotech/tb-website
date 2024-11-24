package com.example.website.validation.constraints;

import java.time.LocalTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.example.website.form.EventForm;

public class ValidTimeRangeValidator implements ConstraintValidator<ValidTimeRange, EventForm> {
    
    private String startTime;
    private String endTime;
    private String message;
    
    @Override
    public void initialize(ValidTimeRange annotation) {
        startTime = "startTime";
        endTime = "endTime";
        message = annotation.message();
    }
    
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

