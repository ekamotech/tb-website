package com.example.website.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.website.entity.UserInf;
import com.example.website.form.EventForm;
import com.example.website.service.FavoriteService;

/**
 * お気に入りに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class FavoritesController {
    
    @Autowired
    private MessageSource messageSource;
    
    private final FavoriteService favoriteService;
    
    public FavoritesController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }
    
    /**
     * 認証されたユーザーのお気に入りイベント一覧を表示します。
     *
     * @param userInf 認証されたユーザー情報
     * @param model モデルオブジェクト
     * @return お気に入り一覧ページのテンプレート名
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping("/favorites")
    public String index(@AuthenticationPrincipal UserInf userInf, Model model) throws IOException {
        
        List<EventForm> list = favoriteService.index(userInf.getUserId());
        model.addAttribute("list", list);
        
        return "favorites/index";
    }
    
    /**
     * イベントをお気に入りに登録します。
     *
     * @param userInf 認証されたユーザー情報
     * @param eventId イベントのID
     * @param redirAttrs リダイレクト属性
     * @param request HTTPリクエストオブジェクト
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     * @throws IOException 入出力例外が発生した場合
     */
    @PostMapping("/favorite")
    public String createFavorite(@AuthenticationPrincipal UserInf userInf, @RequestParam("event_id") long eventId, RedirectAttributes redirAttrs, HttpServletRequest request, Locale locale) throws IOException {
        
        favoriteService.createFavorite(userInf.getUserId(), eventId);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("favorites.flash.favoriteRegistrationComplete", new String[] {}, "お気に入りに登録しました。", locale));
        
        String referer = request.getHeader("Referer");
        
        return "redirect:" + referer;
    }
    
    /**
     * イベントのお気に入りを解除します。
     *
     * @param userInf 認証されたユーザー情報
     * @param eventId イベントのID
     * @param redirAttrs リダイレクト属性
     * @param request HTTPリクエストオブジェクト
     * @param locale ロケール情報
     * @return リダイレクト先のURL
     * @throws IOException 入出力例外が発生した場合
     */
    @DeleteMapping("/favorite")
    public String destroyFavorite(@AuthenticationPrincipal UserInf userInf, @RequestParam("event_id") long eventId, RedirectAttributes redirAttrs, HttpServletRequest request, Locale locale) throws IOException {
        
        favoriteService.destroyFavorite(userInf.getUserId(), eventId);
        
        redirAttrs.addFlashAttribute("hasMessage", true);
        redirAttrs.addFlashAttribute("class", "alert-info");
        redirAttrs.addFlashAttribute("message", messageSource.getMessage("favorites.flash.favoriteCancell", new String[] {}, "お気に入りを解除しました。", locale));
        
        String referer = request.getHeader("Referer");
        
        return "redirect:" + referer;
    }

}
