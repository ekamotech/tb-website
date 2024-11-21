package com.example.website.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

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
    
}
