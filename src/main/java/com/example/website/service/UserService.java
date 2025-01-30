package com.example.website.service;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.website.entity.UserInf;

/**
 * ユーザー情報に関連するサービスクラス。
 */
@Service
public class UserService {
    
    /**
     * ログインユーザーの ID を取得します。
     * 
     * @return ログインユーザーの ID (未ログインの場合は Optional.empty())
     */
    public Optional<Long> getUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof UserInf) {
            return Optional.of(((UserInf) principal).getUserId());
        }
        
        return Optional.empty();
    }

}
