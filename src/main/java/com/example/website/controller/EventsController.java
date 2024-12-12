package com.example.website.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.website.entity.Event;
import com.example.website.entity.UserInf;
import com.example.website.form.EventForm;
import com.example.website.service.EventAttendeeService;
import com.example.website.service.EventService;
import com.example.website.service.GroupMemberService;

@Controller
public class EventsController {
    
    private final EventService eventService;
    private final GroupMemberService groupMemberService;
    private final EventAttendeeService eventAttendeeService;
    
    public EventsController(EventService eventService, GroupMemberService groupMemberService, EventAttendeeService eventAttendeeService) {
        this.eventService = eventService;
        this.groupMemberService = groupMemberService;
        this.eventAttendeeService = eventAttendeeService;
    }
    
    @GetMapping("/events")
    public String index(Principal principal, Model model) throws IOException {
        
        List<EventForm> list = eventService.index(principal);
        model.addAttribute("list", list);
        
        return "events/index";
    }
    
    @GetMapping("/groups/{groupId}/events/new")
    public String newEvent(@AuthenticationPrincipal UserInf userInf, @PathVariable Long groupId, Model model) throws IOException {
        
        EventForm form = eventService.createEventForm(userInf.getUserId(), groupId);

        model.addAttribute("form", form);
        model.addAttribute("groupId", groupId);
        return "events/new";
        
    }
    
    @PostMapping("/groups/{groupId}/events")
    public String createEvent(@AuthenticationPrincipal UserInf userInf, @PathVariable Long groupId, @Validated @ModelAttribute("form") EventForm form, BindingResult result,
            Model model, @RequestParam MultipartFile image, RedirectAttributes redirAttrs)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "イベント作成に失敗しました。");
            return "events/new";
        }
        
        eventService.createEvent(userInf.getUserId(), groupId, form, image);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "イベント作成に成功しました。");
        
        return "redirect:/groups";
        
    }
    
    @GetMapping("/events/{id}")
    public String detail(@AuthenticationPrincipal UserInf userInf, @PathVariable Long id, Model model) throws IOException {
        
        Event entity = eventService.findById(id);
        EventForm event = eventService.getEvent(null, entity);
        
        // イベントグループの管理者かを判定
        boolean isAdmin = groupMemberService.isUserGroupAdmin(userInf.getUserId(), entity.getGroup().getId());
        
        // イベントに参加済みかを判定
        boolean isParticipating = eventAttendeeService.isUserParticipating(userInf.getUserId(), entity.getId());

        model.addAttribute("event", event);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isParticipating", isParticipating);
        
        return "events/detail";
    }


}
