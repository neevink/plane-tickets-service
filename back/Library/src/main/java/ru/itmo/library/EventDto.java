package ru.itmo.library;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private Date date;
    private Integer minAge;
    private EventType eventType;
}

