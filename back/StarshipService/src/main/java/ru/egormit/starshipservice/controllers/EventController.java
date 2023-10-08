package ru.egormit.starshipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egormit.starshipservice.common.Endpoints;
import ru.egormit.starshipservice.service.EventService;
import ru.itmo.library.CreateEventRequest;
import ru.itmo.library.EventDto;

import java.util.List;

/**
 * Обработчик запросов.
 */
@RestController
@RequiredArgsConstructor
public class EventController {

    /**
     * {@link EventService}.
     */
    private final EventService eventService;

    /**
     * Создание события.
     *
     * @param request тело запроса.
     */
    @PostMapping(value = Endpoints.CREATE_EVENT)
    public ResponseEntity<Object> createEvent(@RequestBody CreateEventRequest request) {
        eventService.createEvent(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение списка событий.
     *
     * @return список событий.
     */
    @GetMapping(value = Endpoints.GET_ALL_EVENTS)
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    /**
     * Получение события по id.
     *
     * @return список событий.
     */
    @GetMapping(value = Endpoints.GET_EVENT_BY_ID)
    public ResponseEntity<EventDto> getEventById(
            @PathVariable("ticketId") Long ticketId
    ) {
        return new ResponseEntity<>(eventService.getEventById(ticketId), HttpStatus.OK);
    }
}
