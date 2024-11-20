package com.example.website.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
import com.example.website.entity.GroupMember;
import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.GroupForm;
import com.example.website.form.UserForm;
import com.example.website.repository.GroupMemberRepository;
import com.example.website.repository.GroupRepository;
import com.example.website.repository.UserRepository;

@Controller
public class GroupsController {
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserRepository userRepository;

    
    @GetMapping("/groups")
    public String index(Principal principal, Model model) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        
        List<Group> groups = groupRepository.findByCreatedByOrderByUpdatedAtDesc(user.getUserId());
        List<GroupForm> list = new ArrayList<>();
        for (Group entity : groups) {
            GroupForm form = getGroup(user, entity);
            list.add(form);
        }
        model.addAttribute("list", list);
        
//        model.addAttribute("userId", user.getUserId());
//        model.addAttribute("username", user.getUsername());
//        model.addAttribute("name", user.getName());

        return "groups/index";
    }
    
    public GroupForm getGroup(UserInf user, Group entity) throws IOException {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Group.class, GroupForm.class).addMappings(mapper -> mapper.skip(GroupForm::setUser));
        
        GroupForm form = modelMapper.map(entity, GroupForm.class);
        
        UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
        form.setUser(userForm);
        
        return form;
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
        
        Group entity = new Group();
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();
        
        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        entity.setCreatedBy(userInf.getUserId());
        groupRepository.saveAndFlush(entity);

        // UserInf から User オブジェクトを取得
        User user = userRepository.findById(userInf.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(entity.getId());
        groupMember.setUserId(user.getUserId());
        groupMember.setAuthority(GroupMember.Authority.ROLE_ADMIN);
        groupMember.setGroup(entity);
        groupMember.setUser(user);
        groupMemberRepository.saveAndFlush(groupMember);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "グループ作成に成功しました。");
        
        return "redirect:/groups";
        
    }
    

}
