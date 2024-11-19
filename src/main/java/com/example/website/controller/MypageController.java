package com.example.website.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.website.entity.UserInf;

@Controller
public class MypageController {
    
    @GetMapping("/mypage")
    public String index(Principal principal, Model model) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf user = (UserInf) authentication.getPrincipal();
        
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("name", user.getName());

        return "mypage/index";
    }

}
