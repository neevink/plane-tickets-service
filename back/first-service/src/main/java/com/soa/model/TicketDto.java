package com.soa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soa.model.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TicketDto {
    private Long id;
    private String name;
    private Coordinates coordinates;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date creationDate;
    private Double price;
    private Double discount;
    private Boolean refundable;
    private TicketType type;
    private EventDto event;
}

