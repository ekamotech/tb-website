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

import com.example.website.validation.constraints.ImageByte;
import com.example.website.validation.constraints.ImageNotEmpty;
import com.example.website.validation.constraints.ValidTimeRange;

import lombok.Data;

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
    
    @NotNull(message = "日付を指定してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "未来の日付を指定してください")
    private LocalDate date;
    
    @NotNull(message = "開始時間を指定してください")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    
    @NotNull(message = "終了時間を指定してください")
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

}
