package com.example.website.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.EventForm;
import com.example.website.form.EventUpdateForm;
import com.example.website.service.EventAttendeeService;
import com.example.website.service.EventService;
import com.example.website.service.GroupMemberService;
import com.example.website.service.UserService;

/**
 * イベントに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class EventsController {
    
    @Autowired
    private MessageSource messageSource;
    
    private final UserService userService;
    private final EventService eventService;
    private final GroupMemberService groupMemberService;
    private final EventAttendeeService eventAttendeeService;
    
    public EventsController(UserService userService, EventService eventService, GroupMemberService groupMemberService, EventAttendeeService eventAttendeeService) {
        this.userService = userService;
        this.eventService = eventService;
        this.groupMemberService = groupMemberService;
        this.eventAttendeeService = eventAttendeeService;
    }
    
    /**
     * イベントの一覧ページを表示します。
     *
     * @param model モデルオブジェクト
     * @return イベント一覧ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/events")
    public String index(Model model) throws IOException {

        // ログイン状態を取得
        Optional<Long> optionalUserId = userService.getUserId();
        boolean isAuthenticated = (optionalUserId.isPresent()) ? true : false;
        
        List<EventForm> list = eventService.index();
        
        model.addAttribute("list", list);
        model.addAttribute("isAuthenticated", isAuthenticated);
        
        return "events/index";
    }
    
    /**
     * 認証されたユーザーが参加中のイベントの一覧ページを表示します。
     *
     * @param model モデルオブジェクト
     * @return イベント一覧ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/events/attendee")
    public String showJoinedEvents(Model model) throws IOException {
        
        List<EventForm> list = eventService.getEventsForUser();
        model.addAttribute("list", list);
        
        return "events/attendee";
    }
    
    /**
     * 新規イベント作成フォームを表示します。
     *
     * @param userInf 認証されたユーザー情報
     * @param groupId グループのID
     * @param model モデルオブジェクト
     * @return イベント作成フォームのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/groups/{groupId}/events/new")
    public String newEvent(@AuthenticationPrincipal UserInf userInf, @PathVariable Long groupId, Model model) throws IOException {
        
        EventForm form = eventService.createEventForm(userInf.getUserId(), groupId);

        model.addAttribute("form", form);
        model.addAttribute("groupId", groupId);
        return "events/new";
        
    }
    
    /**
     * 新規イベントを作成します。
     *
     * @param userInf 認証されたユーザー情報
     * @param groupId グループのID
     * @param form イベントフォームオブジェクト
     * @param result バリデーション結果
     * @param model モデルオブジェクト
     * @param image イベント画像
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     * @throws IOException 入出力例外が発生した場合
     */
    @PostMapping("/groups/{groupId}/events")
    public String createEvent(@AuthenticationPrincipal UserInf userInf, @PathVariable Long groupId, @Validated @ModelAttribute("form") EventForm form, BindingResult result,
            Model model, @RequestParam MultipartFile image, RedirectAttributes redirAttrs, Locale locale)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("events.flash.eventCreatingFailure", new String[] {}, "イベント作成に失敗しました。", locale));
            return "events/new";
        }
        
        eventService.createEvent(userInf.getUserId(), groupId, form, image);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("events.flash.eventCreatingComplete", new String[] {}, "イベント作成に成功しました。", locale));
        
        return "redirect:/groups";
        
    }
    
    /**
     * イベントの詳細ページを表示します。
     *
     * @param eventId イベントのID
     * @param model モデルオブジェクト
     * @return イベント詳細ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/events/{eventId}")
    public String detail(@PathVariable Long eventId, Model model) throws IOException {
        
        // ログイン状態を取得
        Optional<Long> optionalUserId = userService.getUserId();
        boolean isAuthenticated = (optionalUserId.isPresent()) ? true : false;
        
        // イベントを取得
        EventForm event = eventService.getEvent(eventId);

        // イベントの参加者を取得
        List<User> attendees = eventAttendeeService.getAttendeesByEvent(eventId);

        boolean isAdmin = false;
        boolean isParticipating = false;
        
        // ログイン済みだったら実行
        if (isAuthenticated) {
            Long userId = optionalUserId.get();
            
            // イベントグループの管理者かを判定
            isAdmin = groupMemberService.isUserGroupAdmin(userId, event.getGroup().getId());
            
            // イベントに参加済みかを判定
            isParticipating = eventAttendeeService.isUserParticipating(userId, eventId);
        }

        model.addAttribute("event", event);
        model.addAttribute("attendees", attendees);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isParticipating", isParticipating);
        
        return "events/detail";
    }
    
    /**
     * イベント編集フォームを表示します。
     *
     * @param userInf 認証されたユーザー情報
     * @param eventId イベントのID
     * @param model モデルオブジェクト
     * @return イベント編集フォームのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/events/{eventId}/edit")
    public String editEvent(@AuthenticationPrincipal UserInf userInf, @PathVariable Long eventId, Model model) throws IOException {
        EventUpdateForm form = eventService.editEvent(userInf.getUserId(), eventId);
        model.addAttribute("form", form);
        return "events/edit";
    }
    
    /**
     * イベントを更新します。
     *
     * @param userInf 認証されたユーザー情報
     * @param form イベント更新フォームオブジェクト
     * @param result バリデーション結果
     * @param model モデルオブジェクト
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     * @throws IOException 入出力例外が発生した場合
     */
    @PostMapping("/events/update")
    public String updateEvent(@AuthenticationPrincipal UserInf userInf, @Validated @ModelAttribute("form") EventUpdateForm form, BindingResult result,
            Model model, RedirectAttributes redirAttrs, Locale locale)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("events.flash.eventUpdateFailure", new String[] {}, "イベント更新に失敗しました。", locale));
            return "events/edit";
        }
        
        eventService.updateEvent(userInf.getUserId(), form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("events.flash.eventUpdateComplete", new String[] {}, "イベント更新に成功しました。", locale));
        
        return "redirect:/groups";
    }
    
    /**
     * イベントに参加します。
     *
     * @param userInf 認証されたユーザー情報
     * @param eventId イベントのID
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     */
    @PostMapping("/events/{eventId}/join")
    public String joinEvent(@AuthenticationPrincipal UserInf userInf, @PathVariable Long eventId, RedirectAttributes redirAttrs, Locale locale) {
        try {
            eventService.joinEvent(userInf.getUserId(), eventId);
            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", messageSource.getMessage("events.flash.eventEntryComplete", new String[] {}, "イベント参加に成功しました。", locale));
        } catch (IllegalArgumentException e) {
            redirAttrs.addAttribute("hasMessage", true);
            redirAttrs.addAttribute("class", "alert-danger");
            redirAttrs.addAttribute("message", messageSource.getMessage("events.flash.eventEntryFailure", new String[] {}, "イベント参加に失敗しました。", locale));
        }
        return "redirect:/events/" + eventId;
    }


}
