package ru.itmo.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.egormit.library.Coordinates;
import ru.itmo.library.enums.TicketType;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TicketDto {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Double price;
    private Double discount;
    private Boolean refundable;
    private TicketType type;
    Event event;

}

