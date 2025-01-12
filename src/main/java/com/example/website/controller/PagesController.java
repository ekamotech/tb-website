package com.example.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("isStartPage", true);
        return "pages/index";
    }
}
