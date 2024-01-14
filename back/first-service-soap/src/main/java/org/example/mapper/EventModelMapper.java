package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.model.model.Event;
import org.example.model.model.EventDto;
import org.springframework.stereotype.Component;

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
