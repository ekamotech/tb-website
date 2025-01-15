package com.example.website.form;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * グループのデータを保持するクラス。
 */
@Data
public class GroupForm {
    
    private Long id;
    
    @NotEmpty
    @Size(max = 100)
    private String name;
    
    @NotEmpty
    @Size(max = 1000)
    private String description;
    
    private UserForm createdBy;
    
    private List<EventForm> events;
    
}
