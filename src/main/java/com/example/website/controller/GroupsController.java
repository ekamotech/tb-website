package com.example.website.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.EventForm;
import com.example.website.form.GroupForm;
import com.example.website.service.GroupMemberService;
import com.example.website.service.GroupService;

/**
 * グループに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class GroupsController {
    
    @Autowired
    private MessageSource messageSource;
    
    private final GroupService groupService;
    private final GroupMemberService groupMemberService;
    
    public GroupsController(GroupService groupService, GroupMemberService groupMemberService) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
    }
    
    /**
     * 指定されたユーザーが管理者であるグループ一覧を取得します。
     *
     * @param model モデルオブジェクト
     * @return グループ一覧ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/groups")
    public String index(Model model) throws IOException {

        List<GroupForm> list = groupService.getGroupsForAdmin();
        model.addAttribute("list", list);

        return "groups/index";
    }
    
    /**
     * 指定されたユーザーが一般ユーザーであるグループ一覧を取得します。
     *
     * @param model モデルオブジェクト
     * @return グループ一覧ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/groups/member")
    public String showJoinedGroups(Model model) throws IOException {
        
        List<GroupForm> list = groupService.getGroupsForUser();
        model.addAttribute("list", list);
        
        return "groups/member";
    }
    
    /**
     * 新規グループ作成フォームを表示します。
     *
     * @param model モデルオブジェクト
     * @return グループ作成フォームのテンプレート名
     */
    @GetMapping("/groups/new")
    public String newGroup(Model model) {
        model.addAttribute("form", new GroupForm());
        return "groups/new";
    }
    
    /**
     * 新規グループを作成します。
     *
     * @param userInf 認証されたユーザー情報
     * @param form グループフォームオブジェクト
     * @param result バリデーション結果
     * @param model モデルオブジェクト
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     * @throws IOException 入出力例外が発生した場合
     */
    @PostMapping("/group")
    @Transactional
    public String create(@AuthenticationPrincipal UserInf userInf, @Validated @ModelAttribute("form") GroupForm form, BindingResult result,
            Model model, RedirectAttributes redirAttrs, Locale locale)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("groups.flash.groupCreatingFailure", new String[] {}, "グループ作成に失敗しました。", locale));
            return "groups/new";
        }
        
        groupService.createGroup(userInf.getUserId(), form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("groups.flash.groupCreatingComplete", new String[] {}, "グループ作成に成功しました。", locale));
        
        return "redirect:/groups";
        
    }
    
    /**
     * グループの詳細ページを表示します。
     *
     * @param groupId グループのID
     * @param model モデルオブジェクト
     * @return グループ詳細ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/groups/{groupId}")
    public String detail(@AuthenticationPrincipal UserInf userInf, @PathVariable Long groupId, Model model) throws IOException {
        
        GroupForm form = groupService.getGroup(groupId);
        
        // グループの管理者かを判定
        boolean isAdmin = groupMemberService.isUserGroupAdmin(userInf.getUserId(), form.getId());
        
        // グループに参加済みかを判定
        boolean isMember = groupMemberService.isUserGroupMember(userInf.getUserId(), form.getId());
        
        // グループのイベントを取得
        List<EventForm> events = form.getEvents();

        // グループの参加者を取得
        List<User> members = groupMemberService.getMembersByGroup(groupId);
        
        model.addAttribute("form", form);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isMember", isMember);
        model.addAttribute("events", events);
        model.addAttribute("members", members);
        
        return "groups/detail";
    }
    
    /**
     * グループ編集フォームを表示します。
     *
     * @param groupId グループのID
     * @param model モデルオブジェクト
     * @return グループ編集フォームのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/groups/{groupId}/edit")
    public String editGroup(@PathVariable Long groupId, Model model) throws IOException {
        GroupForm form = groupService.getGroup(groupId);
        model.addAttribute("form", form);
        return "groups/edit";
    }
    
    /**
     * グループを更新します。
     *
     * @param userInf 認証されたユーザー情報
     * @param form グループフォームオブジェクト
     * @param result バリデーション結果
     * @param model モデルオブジェクト
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     * @throws IOException 入出力例外が発生した場合
     */
    @PostMapping("/groups/update")
    public String update(@AuthenticationPrincipal UserInf userInf, @Validated @ModelAttribute("form") GroupForm form, BindingResult result,
            Model model, RedirectAttributes redirAttrs, Locale locale)
            throws IOException {
        
        if (result.hasErrors()) {
            model.addAttribute("hasMessage", true);
            model.addAttribute("class", "alert-danger");
            model.addAttribute("message", messageSource.getMessage("groups.flash.groupUpdateFailure", new String[] {}, "グループ更新に失敗しました。", locale));
            return "groups/edit";
        }
        
        groupService.updateGroup(userInf.getUserId(), form);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("groups.flash.groupUpdateComplete", new String[] {}, "グループ更新に成功しました。", locale));

        return "redirect:/groups";
        
    }
    
    /**
     * グループに参加者を登録します。
     *
     * @param userInf 認証されたユーザー情報
     * @param groupId グループID
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     */
    @PostMapping("/groups/{groupId}/join")
    public String joinGroup(@AuthenticationPrincipal UserInf userInf, @PathVariable Long groupId, RedirectAttributes redirAttrs, Locale locale) {
        try {
            groupMemberService.joinGroup(userInf.getUserId(), groupId);
            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "グループ参加に成功しました。");
        } catch (IllegalStateException e) {
            redirAttrs.addAttribute("hasMessage", true);
            redirAttrs.addAttribute("class", "alert-danger");
            redirAttrs.addAttribute("message", "グループ参加に失敗しました。" + e.getMessage());
        } catch (IllegalArgumentException e) {
            redirAttrs.addAttribute("hasMessage", true);
            redirAttrs.addAttribute("class", "alert-danger");
            redirAttrs.addAttribute("message", "グループ参加に失敗しました。" + e.getMessage());
        }
        return "redirect:/groups/" + groupId;
    }
    
    /**
     * グループからユーザーを脱退させます。
     *
     * @param userInf 認証されたユーザー情報
     * @param groupId グループID
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     */
    @DeleteMapping("/groups/{groupId}/leave")
    public String leaveGroup(@AuthenticationPrincipal UserInf userInf, @PathVariable Long groupId, RedirectAttributes redirAttrs, Locale locale) {
        try {
            groupMemberService.leaveGroup(userInf.getUserId(), groupId);
            redirAttrs.addFlashAttribute("hasMessage", true);
            redirAttrs.addFlashAttribute("class", "alert-info");
            redirAttrs.addFlashAttribute("message", "グループ脱退に成功しました。");
        } catch (IllegalArgumentException e) {
            redirAttrs.addAttribute("hasMessage", true);
            redirAttrs.addAttribute("class", "alert-danger");
            redirAttrs.addAttribute("message", "グループ脱退に失敗しました。" + e.getMessage());
        }
        return "redirect:/groups/" + groupId;
    }
    
    
    

}
