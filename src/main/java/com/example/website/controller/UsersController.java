package com.example.website.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.website.entity.User;
import com.example.website.entity.User.Authority;
import com.example.website.form.UserForm;
import com.example.website.repository.UserRepository;

/**
 * ユーザーに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class UsersController {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;
    @Autowired
    private MessageSource messageSource;
    
    /**
     * 新規ユーザー登録フォームを表示します。
     *
     * @param model モデルオブジェクト
     * @return 新規ユーザー登録フォームのテンプレート名
     */
    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("form", new UserForm());
        return "users/new";
    }
    
    /**
     * 新しいユーザーを作成します。
     *
     * @param form ユーザー登録フォームオブジェクト
     * @param result バリデーション結果
     * @param model モデルオブジェクト
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return ユーザー登録完了ページのテンプレート名
     */
    @PostMapping("/user")
    public String create(@Validated @ModelAttribute("form") UserForm form, BindingResult result, Model model,
            RedirectAttributes redirAttrs, Locale locale) {
        String name = form.getName();
        String email = form.getEmail();
        String password = form.getPassword();
        
        if (repository.findByUsername(email) != null) {
            FieldError fieldError = new FieldError(result.getObjectName(), "email", messageSource.getMessage("users.create.error.duplicate", new String[] {}, "その E メールはすでに使用されています。", locale));
            result.addError(fieldError);
        }
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("users.flash.userRegistrationFailure", new String[] {}, "ユーザー登録に失敗しました。", locale));
            return "users/new";
        }
        
        User entity = new User(email, name, passwordEncoder.encode(password), Authority.ROLE_USER);
        repository.saveAndFlush(entity);
        
        model.addAttribute("hasMessage", true);
        model.addAttribute("class", "alert-info");
        model.addAttribute("message", messageSource.getMessage("users.flash.userRegistrationComplete", new String[] {}, "ユーザー登録が完了しました。", locale));
        
        return "layouts/complete";
        
    }

}
