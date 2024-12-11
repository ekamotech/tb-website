package com.example.website.form;

import com.example.website.entity.EventAttendee.ParticipationStatus;

import lombok.Data;

@Data
public class EventAttendeeForm {
    
    private Long id;
    
    private Long eventId;
    
    private Long userId;
    
    private ParticipationStatus participationStatus;

}
