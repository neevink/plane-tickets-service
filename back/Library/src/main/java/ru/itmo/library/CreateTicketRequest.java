package ru.itmo.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itmo.library.Coordinates;
import ru.itmo.library.enums.TicketType;

/**
 * Модель запрса на саоздание Ticket.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateTicketRequest {
    private String name;
    private Coordinates coordinates;
    private Double price;
    private Double discount;
    private Boolean refundable;
    private TicketType type;
    private Long eventId;
}
