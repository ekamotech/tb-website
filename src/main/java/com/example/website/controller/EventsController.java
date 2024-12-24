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
import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.EventForm;
import com.example.website.form.EventUpdateForm;
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
        
        // イベントの参加者を取得
        List<User> attendees = eventAttendeeService.getAttendeesByEvent(entity);

        model.addAttribute("event", event);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isParticipating", isParticipating);
        model.addAttribute("attendees", attendees);
        
        return "events/detail";
    }
    
    @GetMapping("/events/{eventId}/edit")
    public String editEvent(@AuthenticationPrincipal UserInf userInf, @PathVariable Long eventId, Model model) throws IOException {
        EventUpdateForm form = eventService.editEvent(userInf.getUserId(), eventId);
        model.addAttribute("form", form);
        return "events/edit";
    }
    
    @PostMapping("/events/update")
    public String updateEvent(@AuthenticationPrincipal UserInf userInf, @Validated @ModelAttribute("form") EventUpdateForm form, BindingResult result,
            Model model, RedirectAttributes redirAttrs)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", "イベント更新に失敗しました。");
            return "events/edit";
        }
        
        eventService.updateEvent(userInf.getUserId(), form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", "イベント更新に成功しました。");
        
        return "redirect:/groups";
    }
    
    @PostMapping("/events/{eventId}/join")
    public String joinEvent(@AuthenticationPrincipal UserInf userInf, @PathVariable Long eventId, RedirectAttributes redirAttrs) {
        try {
            eventService.joinEvent(userInf.getUserId(), eventId);
            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "イベントに参加しました！");
        } catch (IllegalArgumentException e) {
            redirAttrs.addAttribute("hasMessage", true);
            redirAttrs.addAttribute("class", "alert-danger");
            redirAttrs.addAttribute("message", "イベント参加に失敗しました。");
        }
        return "redirect:/events/" + eventId;
    }


}
