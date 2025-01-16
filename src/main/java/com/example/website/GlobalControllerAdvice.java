package com.example.website;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * グローバルな例外処理を提供するクラス。
 * 全てのコントローラーで発生する例外を処理します。
 */
@ControllerAdvice
@Component
public class GlobalControllerAdvice {
    
    @Autowired
    private MessageSource messageSource;
    
    protected static Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);
    
    /**
     * 全ての例外を処理し、エラーメッセージをモデルに追加します。
     *
     * @param e 発生した例外
     * @param model モデルオブジェクト
     * @param locale ロケール情報
     * @return エラーページのテンプレート名
     */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model, Locale locale) {
        
        log.error(e.getMessage(), e);
        
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-danger");
        model.addAttribute("message", messageSource.getMessage("common.error", new String[] {}, "ただいま利用できません。", locale));

        return "layouts/complete";
    }

}
