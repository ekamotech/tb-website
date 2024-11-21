package com.example.website.controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventsController {
    
    
    
    @GetMapping("/events")
    public String index(Principal principal, Model model) throws IOException {
        
        return "events/index";
    }
    
    

}
