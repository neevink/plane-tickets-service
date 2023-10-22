package ru.itmo.library;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.library.enums.EventType;

import java.util.Date;

/**
 * Модель запрса на саоздание Event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateEventRequest {
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date date;
    private Integer minAge;
    private EventType eventType;
}
