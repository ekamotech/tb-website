package com.example.website.form;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * お気に入りのデータを保持するクラス。
 */
@Data
public class FavoriteForm {

    @NotNull
    private Long userId;
    
    @NotNull
    private Long eventId;
    
    private EventForm event;
}
