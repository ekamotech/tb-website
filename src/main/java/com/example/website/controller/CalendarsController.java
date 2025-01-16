package com.example.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * カレンダーに関連するリクエストを処理するコントローラークラス。
 */
@Controller
public class CalendarsController {
    
    /**
     * カレンダーのページを表示します。
     *
     * @param model モデルオブジェクト
     * @return テンプレート名
     */
    @GetMapping("/calendars")
    public String index(Model model) {
        return "calendars/index";
    }

}
