package com.example.website.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.website.entity.User;
import com.example.website.repository.UserRepository;

/**
 * ユーザーの詳細情報を提供するサービスクラス。
 * Spring Security の認証のためにユーザー情報をロードします。
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    protected static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
    /**
     * ユーザー名でユーザー情報をロードします。
     *
     * @param username ユーザー名
     * @return ユーザーの詳細情報
     * @throws UsernameNotFoundException ユーザー名が見つからない場合にスローされます
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("username={}", username);
        
        if (username == null || "".equals(username)) {
            throw new UsernameNotFoundException("Username is empty");
        }
        User entity = repository.findByUsername(username);
        
        return entity;
    }
    
    
}
