package com.example.website.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class EventUpdateForm {
    
    private Long id;
    
    @NotEmpty
    @Size(max = 100)
    private String title;
    
    @NotEmpty
    @Size(max = 1000)
    private String description;

}
