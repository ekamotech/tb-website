package com.example.website.controller;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.website.entity.UserInf;
import com.example.website.form.CommentForm;
import com.example.website.service.CommentService;

/**
 * コメントに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class CommentsController {
    
    @Autowired
    private MessageSource messageSource;
    
    private final CommentService commentService;
    
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    /**
     * 新しいコメント投稿フォームを表示します。
     *
     * @param userInf 認証されたユーザー情報
     * @param eventId イベントのID
     * @param model モデルオブジェクト
     * @return コメント投稿フォームのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/events/{eventId}/comments/new")
    public String newComment(@AuthenticationPrincipal UserInf userInf, @PathVariable("eventId") long eventId, Model model) throws IOException {
        CommentForm form = commentService.createCommentForm(userInf.getUserId(), eventId);
        model.addAttribute("form", form);
        
        return "comments/new";
    }
    
    /**
     * 新しいコメントを作成します。
     *
     * @param userInf 認証されたユーザー情報
     * @param eventId イベントのID
     * @param form コメントフォームオブジェクト
     * @param result バリデーション結果
     * @param model モデルオブジェクト
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     * @throws IOException 入出力例外が発生した場合
     */
    @PostMapping("/events/{eventId}/comment")
    public String createComment(@AuthenticationPrincipal UserInf userInf, @PathVariable("eventId") long eventId, @Validated @ModelAttribute("form") CommentForm form,
            BindingResult result, Model model, RedirectAttributes redirAttrs, Locale locale) throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("comments.flash.commentCreatingFailure", new String[] {}, "コメント投稿に失敗しました。", locale));
            return "comments/new";
        }
        
        commentService.createComment(userInf.getUserId(), eventId, form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("comments.flash.commentCreatingComplete", new String[] {}, "コメント投稿に成功しました。", locale));

        return "redirect:/events/" + eventId;
        
    }
}
