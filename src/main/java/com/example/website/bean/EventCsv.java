package com.example.website.bean;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

/**
 * イベントデータを表すクラス。
 * CSV ファイルからイベントデータを読み込むためのデータモデルとして使用されます。
 */
@JsonPropertyOrder({ "ID", "ユーザーID", "グループID", "タイトル", "説明文", "開催日", "開始時間", "終了時間", "画像パス", "開催場所", "緯度", "経度" })
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EventCsv {
    
    @JsonProperty("ID")
    private Long id;
    
    @JsonProperty("ユーザーID")
    private Long userId;
    
    @JsonProperty("グループID")
    private Long groupId;
    
    @JsonProperty("タイトル")
    private String title;
    
    @JsonProperty("説明文")
    private String description;
    
    @JsonProperty("開催日")
    private LocalDate date;
    
    @JsonProperty("開始時間")
    private LocalTime startTime;
    
    @JsonProperty("終了時間")
    private LocalTime endTime;
    
    @JsonProperty("画像パス")
    private String path;
    
    @JsonProperty("開催場所")
    private String address;
    
    @JsonProperty("緯度")
    private Double latitude;
    
    @JsonProperty("経度")
    private Double longitude;

}
