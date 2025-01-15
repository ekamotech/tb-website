package com.example.website.form;

import lombok.Data;

/**
 * カレンダーに表示するイベント情報のデータを保持するクラス。
 */
@Data
public class EventScheduleForm {

    private String title;

    private String start;

    private String end;

    private String url;

}
