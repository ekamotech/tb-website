package com.example.website.security;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * カスタム認証成功ハンドラー。
 * 認証成功後のリダイレクト先をユーザーのロールに基づいて決定します。
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    /**
     * 認証成功時の処理を行います。
     * ユーザーのロールに応じてリダイレクト先を決定します。
     * 
     * @param request HTTP リクエスト
     * @param response HTTP レスポンス
     * @param authentication 認証情報
     * @throws IOException 入出力例外が発生した場合
     * @throws ServletException サーブレット例外が発生した場合
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/events");
        }
    }

}
