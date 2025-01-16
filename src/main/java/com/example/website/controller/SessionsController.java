package com.example.website.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * セッションに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class SessionsController {
    
    @Autowired
    private MessageSource messageSource;

    /**
     * ログインページを表示します。
     *
     * @return ログインページのテンプレート名
     */
    @GetMapping("/login")
    public String index() {
        return "sessions/new";
    }

    /**
     * ログイン失敗時の処理を行います。
     *
     * @param model モデルオブジェクト
     * @param locale ロケール情報
     * @return ログインページのテンプレート名
     */
    @GetMapping("/login-failure")
    public String loginFailure(Model model, Locale locale) {
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-danger");
        model.addAttribute("message", messageSource.getMessage("sessions.flash.loginFailure", new String[] {}, "Emailまたはパスワードに誤りがあります。", locale));

        return "sessions/new";
    }

    /**
     * ログアウト完了時の処理を行います。
     *
     * @param model モデルオブジェクト
     * @param locale ロケール情報
     * @return 完了ページのテンプレート名
     */
    @GetMapping("/logout-complete")
    public String logoutComplete(Model model, Locale locale) {
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-info");
        model.addAttribute("message", messageSource.getMessage("sessions.flash.logoutComplete", new String[] {}, "ログアウトしました。", locale));

        return "layouts/complete";
    }
}
