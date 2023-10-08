package ru.itmo.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.egormit.library.Coordinates;
import ru.itmo.library.enums.EventType;
import ru.itmo.library.enums.TicketType;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class EventDto {
    private Long id;
    private String name;
    private Date date;
    private Integer minAge;
    private EventType eventType;
}

