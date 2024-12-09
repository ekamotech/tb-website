package com.example.website.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CommentForm {
    
    private Long id;
    
    private Long userId;
    
    private Long eventId;
    
    @NotEmpty
    @Size(max = 1000)
    private String description;

}
