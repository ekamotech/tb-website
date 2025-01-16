package com.example.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ページに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class PagesController {

    /**
     * ホームページを表示します。
     *
     * @param model モデルオブジェクト
     * @return ホームページのテンプレート名
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("isStartPage", true);
        return "pages/index";
    }
}
