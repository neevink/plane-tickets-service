package ru.egormit.starshipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egormit.starshipservice.common.Endpoints;
import ru.egormit.starshipservice.service.TicketService;
import ru.itmo.library.CreateTicketRequest;
import ru.itmo.library.TicketDto;

import java.util.List;

/**
 * Обработчик запросов.
 */
@RestController
@RequiredArgsConstructor
public class TicketController {

    /**
     * {@link TicketService}.
     */
    private final TicketService ticketService;

    /**
     * Создание билета.
     *
     * @param request тело запроса.
     */
    @PostMapping(value = Endpoints.CREATE_TICKET)
    public ResponseEntity<TicketDto> createTicket(
            @RequestBody CreateTicketRequest request
    ) {
        TicketDto ticketDto = ticketService.createTicket(request);
        return new ResponseEntity<>(ticketDto, HttpStatus.OK);
    }

    /**
     * Получение списка билетов.
     *
     * @return список билетов.
     */
    @GetMapping(value = Endpoints.GET_ALL_TICKETS)
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }

    /**
     * Получение билет по id.
     *
     * @return список кораблей.
     */
    @GetMapping(value = Endpoints.GET_TICKET_BY_ID)
    public ResponseEntity<TicketDto> getTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        return new ResponseEntity<>(ticketService.getTicketById(ticketId), HttpStatus.OK);
    }

    /**
     * Удаление билета по id.
     */
    @DeleteMapping(value = Endpoints.DELETE_TICKET_BY_ID)
    public ResponseEntity<Object> deleteTicketById(
            @PathVariable("ticketId") Long ticketId
    ) {
        ticketService.deleteTicketById(ticketId);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    /**
     * Обновление билета по id.
     */
    @PutMapping(value = Endpoints.UPDATE_TICKET_BY_ID)
    public ResponseEntity<Object> updateTicketById(
            @PathVariable("ticketId") Long ticketId,
            @RequestBody CreateTicketRequest request
    ) {
        ticketService.updateTicketById(ticketId, request);
        return new ResponseEntity<>("updated", HttpStatus.OK);
    }
}
