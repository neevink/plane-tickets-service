package com.soa.mapper;


import com.soa.model.Coordinates;
import com.soa.model.EventDto;
import com.soa.model.Ticket;
import com.soa.model.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        dto.setRefundable(ticket.getRefundable());
        dto.setType(ticket.getType());
        if(ticket.getEvent() != null){
            dto.setEvent(EventDto.of(
                    ticket.getEvent().getId(),
                    ticket.getEvent().getName(),
                    ticket.getEvent().getDate(),
                    ticket.getEvent().getMinAge(),
                    ticket.getEvent().getEventType()
            ));
        }
        return dto;
    }
}
