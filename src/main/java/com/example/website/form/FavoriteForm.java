package com.example.website.form;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class FavoriteForm {

    @NotNull
    private Long userId;
    
    @NotNull
    private Long eventId;
    
    private EventForm event;
}
