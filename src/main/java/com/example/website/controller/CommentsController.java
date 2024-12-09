package com.example.website.controller;

import java.io.IOException;

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

@Controller
public class CommentsController {
    
    private final CommentService commentService;
    
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    @GetMapping("/events/{eventId}/comments/new")
    public String newComment(@AuthenticationPrincipal UserInf userInf, @PathVariable("eventId") long eventId, Model model) throws IOException {
        CommentForm form = commentService.createCommentForm(userInf.getUserId(), eventId);
        model.addAttribute("form", form);
        
        return "comments/new";
    }
    
    @PostMapping("/events/{eventId}/comment")
    public String createComment(@AuthenticationPrincipal UserInf userInf, @PathVariable("eventId") long eventId, @Validated @ModelAttribute("form") CommentForm form,
            BindingResult result, Model model, RedirectAttributes redirAttrs) throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "コメント投稿に失敗しました。");
            return "comments/new";
        }
        
        commentService.createComment(userInf.getUserId(), eventId, form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "コメント投稿に成功しました。");

        return "redirect:/events/" + eventId;
        
    }
}
