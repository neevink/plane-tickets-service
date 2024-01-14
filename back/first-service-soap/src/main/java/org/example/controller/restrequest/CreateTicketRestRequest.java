package org.example.controller.restrequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.TicketType;
import org.example.model.model.Coordinates;
import org.example.model.model.EventDto;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateTicketRestRequest {
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
