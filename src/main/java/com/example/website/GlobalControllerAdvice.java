package com.example.website;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class GlobalControllerAdvice {
    
    protected static Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);
    
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {
        
        log.error(e.getMessage(), e);
        
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-danger");
        model.addAttribute("message", "ただいま利用できません。");

        return "layouts/complete";
    }

}
