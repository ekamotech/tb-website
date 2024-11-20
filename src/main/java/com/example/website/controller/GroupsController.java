package com.example.website.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.website.form.GroupForm;
import com.example.website.service.GroupService;

@Controller
public class GroupsController {
    
    @Autowired
    private GroupService groupService;

    
    @GetMapping("/groups")
    public String index(Principal principal, Model model) throws IOException {

        List<GroupForm> list = groupService.getGroupsForAdmin(principal);
        model.addAttribute("list", list);
        
//        model.addAttribute("userId", user.getUserId());
//        model.addAttribute("username", user.getUsername());
//        model.addAttribute("name", user.getName());

        return "groups/index";
    }
    
    @GetMapping("/groups/new")
    public String newGroup(Model model) {
        model.addAttribute("form", new GroupForm());
        return "groups/new";
    }
    
    @PostMapping("/group")
    @Transactional
    public String create(Principal principal, @Validated @ModelAttribute("form") GroupForm form, BindingResult result,
            Model model, RedirectAttributes redirAttrs)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "グループ作成に失敗しました。");
            return "groups/new";
        }
        
        groupService.createGroup(principal, form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "グループ作成に成功しました。");
        
        return "redirect:/groups";
        
    }
    

}
