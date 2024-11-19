package com.example.website.controller;

import java.io.IOException;
import java.security.Principal;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.website.entity.Group;
import com.example.website.entity.UserInf;
import com.example.website.form.GroupForm;
import com.example.website.repository.GroupRepository;

@Controller
public class GroupsController {
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GroupRepository repository;
    
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
        
        Group entity = new Group();
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        
        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        entity.setCreatedBy(user.getUserId());
        repository.saveAndFlush(entity);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "グループ作成に成功しました。");
        
        return "redirect:/mypage";
        
    }
    

}
