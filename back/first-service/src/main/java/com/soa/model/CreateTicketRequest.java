package com.soa.model;

import com.soa.model.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private EventDto event;

    public void setRefundable(Object value) throws Exception {
        if (value instanceof Boolean) {
            refundable = (Boolean) value;
        } else {
            throw new Exception("refundable only boolean");
        }
    }

}
