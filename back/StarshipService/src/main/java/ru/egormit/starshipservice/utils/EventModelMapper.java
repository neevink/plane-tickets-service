package ru.egormit.starshipservice.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itmo.library.Event;
import ru.itmo.library.EventDto;

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
}
