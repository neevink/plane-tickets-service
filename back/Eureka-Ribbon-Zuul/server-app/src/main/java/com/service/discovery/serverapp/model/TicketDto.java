package com.service.discovery.serverapp.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class TicketDto {
    @JsonIgnoreProperties
    private Integer id;
    private String name;
    private CoordinatesDto coordinates;
    private Date creationDate;
    private Long price;
    private Double discount;
    private Boolean refundable;
    private TicketType type;
    private Event discipline;
}
