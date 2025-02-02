package com.example.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.example.website.filter.FormAuthenticationProvider;
import com.example.website.repository.UserRepository;
import com.example.website.security.CustomAuthenticationSuccessHandler;

/**
 * アプリケーションのセキュリティ設定クラス。
 * 各種セキュリティ設定を行います。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UserRepository repository;
    @Autowired
    private FormAuthenticationProvider authenticationProvider;
    
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    
    /**
     * セキュリティフィルターチェーンの設定を行います。
     *
     * @param http HttpSecurityオブジェクト
     * @param introspector HandlerMappingIntrospectorオブジェクト
     * @return SecurityFilterChainオブジェクト
     * @throws Exception 設定中に例外が発生した場合
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
            throws Exception {
        MvcRequestMatcher h2RequestMatcher = new MvcRequestMatcher(introspector, "/**");
        h2RequestMatcher.setServletPath("/h2-console");

        RequestMatcher publicMatchers = new OrRequestMatcher(
                new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/events"),
                new AntPathRequestMatcher("/events/{eventId:[0-9]+}"),
                new AntPathRequestMatcher("/favicon.ico"),
                new AntPathRequestMatcher("/error"),
                new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/login"),
                new AntPathRequestMatcher("/users/new"),
                new AntPathRequestMatcher("/user/**"),
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/fonts/**"),
                new AntPathRequestMatcher("/images/**"),
                new AntPathRequestMatcher("/js/**"),
                new AntPathRequestMatcher("/push7-worker.js"),
                new AntPathRequestMatcher("/manifest.json"));

        // @formatter:off
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(publicMatchers).permitAll() // publicMatchersに含まれるURLパターンへのアクセスを許可
                .requestMatchers("/events/**").authenticated() // それ以外の "/events/**" は認証する
                .requestMatchers("/admin/**").hasRole("ADMIN") // "/admin/**" は管理者権限を要求
                .anyRequest().authenticated()) // その他のリクエストは認証を要求
                .formLogin(login -> login
                        .loginProcessingUrl("/login") // 指定したURLがリクエストされるとログイン認証を行う
                        .loginPage("/login") // ログインURLの指定
                     // .defaultSuccessUrl("/events") // ログイン成功時の遷移先
                        .successHandler(customAuthenticationSuccessHandler) // ログイン成功時にカスタム認証成功ハンドラを使用
                        .failureUrl("/login-failure") // ログイン失敗時の遷移先
                        .permitAll()) // 未ログインでもアクセス可能
                .logout(logout -> logout
                        .logoutSuccessUrl("/logout-complete") // ログアウト成功時の遷移先
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(h2RequestMatcher))
                .headers(headers -> headers.frameOptions(
                        frame -> frame.sameOrigin()))
                .cors(cors -> cors.disable());
        // @formatter:on

        return http.build();
    }
    
    /**
     * カスタム認証プロバイダーを返します。
     *
     * @return FormAuthenticationProviderオブジェクト
     */
    public FormAuthenticationProvider userDetailsService() {
        return this.authenticationProvider;
    }
    
    /**
     * 認証マネージャーを構築します。
     *
     * @param http HttpSecurityオブジェクト
     * @return AuthenticationManagerオブジェクト
     * @throws Exception 設定中に例外が発生した場合
     */
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }
    
    /**
     * パスワードエンコーダーのBeanを定義します。
     *
     * @return PasswordEncoderオブジェクト
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
