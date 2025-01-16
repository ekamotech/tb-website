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

/**
 * グループに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class GroupsController {
    
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MessageSource messageSource;
    
    /**
     * 指定されたユーザーが管理者であるグループ一覧を取得します。
     *
     * @param principal 認証されたユーザー情報
     * @param model モデルオブジェクト
     * @return グループ一覧ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/groups")
    public String index(Principal principal, Model model) throws IOException {

        List<GroupForm> list = groupService.getGroupsForAdmin(principal);
        model.addAttribute("list", list);

        return "groups/index";
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
     * @param principal 認証されたユーザー情報
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
    
    /**
     * グループの詳細ページを表示します。
     *
     * @param id グループのID
     * @param model モデルオブジェクト
     * @return グループ詳細ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/groups/{id}")
    public String detail(@PathVariable Long id, Model model) throws IOException {
        Group entity = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        GroupForm form = groupService.getGroup(null, entity);
        model.addAttribute("form", form);
        return "groups/detail";
    }
    
    /**
     * グループ編集フォームを表示します。
     *
     * @param id グループのID
     * @param model モデルオブジェクト
     * @return グループ編集フォームのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/groups/{id}/edit")
    public String editGroup(@PathVariable Long id, Model model) throws IOException {
        Group entity = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        GroupForm form = groupService.getGroup(null, entity);
        model.addAttribute("form", form);
        return "groups/edit";
    }
    
    /**
     * グループを更新します。
     *
     * @param principal 認証されたユーザー情報
     * @param form グループフォームオブジェクト
     * @param result バリデーション結果
     * @param model モデルオブジェクト
     * @param redirAttrs リダイレクト属性
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     * @throws IOException 入出力例外が発生した場合
     */
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
