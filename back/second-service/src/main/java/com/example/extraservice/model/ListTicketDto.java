package com.example.extraservice.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ListTicketDto {
    private List<TicketDto> tickets;
}
