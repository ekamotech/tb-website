package com.example.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 管理者用のコントローラークラス。
 */
@Controller
public class AdminController {
    
    /**
     * 管理者用ページを表示します。
     *
     * @return 管理者用ページのテンプレート名
     */
    @GetMapping("/admin")
    public String adminHome() {
        return "admin/index";
    }
    
}
