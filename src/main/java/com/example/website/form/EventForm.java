package com.example.website.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class EventForm {
    
    private Long id;
    
    // ユーザーIDとグループIDを直接保持
    private Long userId;
    
    private Long groupId;
    
    @NotEmpty
    @Size(max = 100)
    private String title;
    
    @NotEmpty
    @Size(max = 1000)
    private String description;
    
//    private UserForm user;
//    
//    private GroupForm group;

}
