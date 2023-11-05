package com.soa.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.soa.model.Event;
import com.soa.model.EventDto;

@Component
@RequiredArgsConstructor
public class EventModelMapper {
    public EventDto map(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setMinAge(event.getMinAge());
        dto.setEventType(event.getEventType());
        return dto;
    }

    public Event map(EventDto event) {
        Event dto = new Event();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        dto.setMinAge(event.getMinAge());
        dto.setEventType(event.getEventType());
        return dto;
    }
}
