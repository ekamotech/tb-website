package com.example.website.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.website.entity.Group;
import com.example.website.form.GroupForm;
import com.example.website.repository.GroupRepository;
import com.example.website.service.GroupService;

@Controller
public class GroupsController {
    
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MessageSource messageSource;
    
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
            Model model, RedirectAttributes redirAttrs, Locale locale)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("groups.flash.groupCreatingFailure", new String[] {}, "グループ作成に失敗しました。", locale));
            return "groups/new";
        }
        
        groupService.createGroup(principal, form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("groups.flash.groupCreatingComplete", new String[] {}, "グループ作成に成功しました。", locale));
        
        return "redirect:/groups";
        
    }
    
    @GetMapping("/groups/{id}")
    public String detail(@PathVariable Long id, Model model) throws IOException {
        Group entity = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        GroupForm form = groupService.getGroup(null, entity);
        model.addAttribute("form", form);
        return "groups/detail";
    }
    
    @GetMapping("/groups/{id}/edit")
    public String editGroup(@PathVariable Long id, Model model) throws IOException {
        Group entity = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        GroupForm form = groupService.getGroup(null, entity);
        model.addAttribute("form", form);
        return "groups/edit";
    }
    
    @PostMapping("/groups/update")
    public String update(Principal principal, @Validated @ModelAttribute("form") GroupForm form, BindingResult result,
            Model model, RedirectAttributes redirAttrs, Locale locale)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("groups.flash.groupUpdateFailure", new String[] {}, "グループ更新に失敗しました。", locale));
            return "groups/edit";
        }
        
        groupService.updateGroup(principal, form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("groups.flash.groupUpdateComplete", new String[] {}, "グループ更新に成功しました。", locale));

        return "redirect:/groups";
        
    }
    
    

}
