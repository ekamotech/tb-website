package com.example.website.form;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.website.entity.EventAttendee;
import com.example.website.validation.constraints.ImageByte;
import com.example.website.validation.constraints.ImageNotEmpty;
import com.example.website.validation.constraints.ValidTimeRange;

import lombok.Data;

/**
 * イベントのデータを保持するクラス。
 * 開始日時と終了日時の範囲が正しいことを検証します。
 */
@Data
@ValidTimeRange
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
    
    @NotNull(message = "{com.example.website.form.EventForm.date.NotNull.message}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "{com.example.website.form.EventForm.date.Future.message}")
    private LocalDate date;
    
    @NotNull(message = "{com.example.website.form.EventForm.startTime.NotNull.message}")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    
    @NotNull(message = "{com.example.website.form.EventForm.endTime.NotNull.message}")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    
    @ImageNotEmpty
    @ImageByte(max = 2000000)
    private MultipartFile image;

    private String imageData;

    private String path;
    
    @NotEmpty
    @Size(max = 100)
    private String address;
    
    private Double latitude;
    
    private Double longitude;

    private UserForm user;
    
    private GroupForm group;
    
    private List<FavoriteForm> favorites;
    
    private FavoriteForm favorite;
    
    private List<CommentForm> comments;
    
    private List<EventAttendee> eventAttendees;

}
