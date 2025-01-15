package com.example.website.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import com.example.website.validation.constraints.PasswordEquals;

import lombok.Data;

/**
 * ユーザー登録フォームのデータを保持するクラス。
 * パスワードとパスワード確認フィールドが一致することを検証します。
 */
@Data
@PasswordEquals
public class UserForm {
    
    @NotEmpty
    @Size(max = 100)
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(max = 20)
    private String password;

    @NotEmpty
    @Size(max = 20)
    private String passwordConfirmation;

}
