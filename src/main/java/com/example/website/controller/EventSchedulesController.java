package com.example.website.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.website.entity.Event;
import com.example.website.form.EventScheduleForm;
import com.example.website.repository.EventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * イベントスケジュールに関連するリクエストを処理するコントローラークラス。
 * カレンダーに表示するイベント情報を取得します。
 */
@RestController
@RequestMapping("/schedules")
public class EventSchedulesController {
    
    private final EventRepository eventRepository;
    
    public EventSchedulesController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    /**
    * カレンダーに表示するEvent情報を取得します。
    *
    * @return Event情報をjsonエンコードした文字列
    */
    @GetMapping("/all")
    public String getEventSchedules() {
        String jsonMsg = null;
        try {
            Iterable<Event> events = eventRepository.findAll();
            List<EventScheduleForm> form = new ArrayList<EventScheduleForm>();
            for (Event entity : events) {
                EventScheduleForm event = new EventScheduleForm();
                event.setTitle(entity.getTitle());
                LocalDate date = entity.getDate();
                String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                event.setStart(formattedDate);
                event.setUrl("/events/" + entity.getId());
                form.add(event);
            }
            ObjectMapper mapper = new ObjectMapper();
            jsonMsg = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(form);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }
        return jsonMsg;
    }

}
