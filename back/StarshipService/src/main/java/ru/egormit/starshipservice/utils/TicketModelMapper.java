package ru.egormit.starshipservice.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itmo.library.Coordinates;
import ru.itmo.library.Ticket;
import ru.itmo.library.TicketDto;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TicketModelMapper {
    public TicketDto map(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setName(ticket.getName());
        dto.setCoordinates(Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()));
        dto.setCreationDate(Date.from(ticket.getCreationDate().toInstant()));
        dto.setPrice(ticket.getPrice());
        dto.setDiscount(ticket.getDiscount());
        dto.setRefundable(dto.getRefundable());
        dto.setType(ticket.getType());
        if (ticket.getEvent() != null){
            dto.setEventId(ticket.getEvent().getId());
        }
        return dto;
    }
}
