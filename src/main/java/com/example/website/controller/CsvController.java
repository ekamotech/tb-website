package com.example.website.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.website.bean.EventCsv;
import com.example.website.entity.Event;
import com.example.website.service.EventService;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

/**
 * CSV コントローラークラス。
 * CSV ファイルの処理を行います。
 */
@Controller
public class CsvController {
    
    @Autowired
    private ModelMapper modelMapper;
    
    private final EventService eventService;
    
    public CsvController(EventService eventService) {
        this.eventService = eventService;
    }
    
    /**
     * イベントデータを CSV ファイルとしてダウンロードします。
     *
     * @return CSV 形式のイベントデータ
     * @throws IOException 入出力例外が発生した場合
     */
    @GetMapping(value = "/events/event.csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
            + "; charset=UTF-8; Content-Disposition: attachment")
    @ResponseBody
    public Object downloadCsv() throws IOException {
        // すべてのイベントを取得
        List<Event> events = eventService.getAllEvents();

        // EventCsvの型情報を取得
        Type listType = new TypeToken<List<EventCsv>>() {
        }.getType();

        // modelMapper を使用して、Event を EventCsv のリストに変換
        List<EventCsv> csv = modelMapper.map(events, listType);
        
        // JavaTimeModule を登録して、Java 8 の日付/時間型をサポート
        JavaTimeModule module = new JavaTimeModule();

        // 日付と時刻をフォーマットするためのシリアライザを追加
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm")));

        // CsvMapper オブジェクトを作成
        CsvMapper mapper = new CsvMapper();

        // JavaTimeModule を登録
        mapper.registerModule(module);

        // EventCsv クラスに基づいて CSV スキーマを作成し、ヘッダー行を含めるように設定
        CsvSchema schema = mapper.schemaFor(EventCsv.class).withHeader();
        
        // CSV 形式の文字列を生成して返す
        return mapper.writer(schema).writeValueAsString(csv);
    }

}
