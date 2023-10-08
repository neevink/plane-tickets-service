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
    public ResponseEntity<Object> createTicket(@RequestBody CreateTicketRequest request) {
        ticketService.createTicket(request);
        return ResponseEntity.ok().build();
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
}
