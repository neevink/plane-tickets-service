package ru.itmo.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.egormit.library.Coordinates;
import ru.itmo.library.enums.EventType;
import ru.itmo.library.enums.TicketType;

import java.util.Date;

/**
 * Модель запрса на саоздание Event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateEventRequest {
    private String name;
    private Date date;
    private Integer minAge;
    private EventType eventType;
}
