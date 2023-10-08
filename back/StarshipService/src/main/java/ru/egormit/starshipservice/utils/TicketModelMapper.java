package ru.egormit.starshipservice.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.egormit.library.Coordinates;
import ru.egormit.library.SpaceMarineResponse;
import ru.egormit.library.SpaceMarineUpdateRequest;
import ru.itmo.library.Ticket;
import ru.itmo.library.TicketDto;

@Component
@RequiredArgsConstructor
public class TicketModelMapper {
    public TicketDto map(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setName(ticket.getName());
        dto.setCoordinates(Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()));
        dto.setCreationDate(ticket.getCreationDate());
        dto.setPrice(ticket.getPrice());
        dto.setDiscount(ticket.getDiscount());
        dto.setRefundable(dto.getRefundable());
        dto.setType(ticket.getType());
        dto.setEvent(ticket.getEvent());
        return dto;
    }

    public SpaceMarineUpdateRequest map(SpaceMarineResponse response) {
        SpaceMarineUpdateRequest request = new SpaceMarineUpdateRequest();
        request.setId(response.getId());
        request.setName(response.getName());
        request.setCoordinates(response.getCoordinates());
        request.setCategory(response.getCategory());
        request.setWeaponType(response.getWeaponType());
        request.setMeleeWeapon(response.getMeleeWeapon());
        request.setCreationDate(response.getCreationDate());
        request.setHealth(response.getHealth());
        return request;
    }

}
