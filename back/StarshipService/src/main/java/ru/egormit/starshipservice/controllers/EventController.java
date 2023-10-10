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
    public ResponseEntity<Object> createEvent(
            @RequestBody CreateEventRequest request
    ) {
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
            @PathVariable("eventId") Long eventId
    ) {
        return new ResponseEntity<>(eventService.getEventById(eventId), HttpStatus.OK);
    }

    @DeleteMapping(value = Endpoints.DELETE_EVENT_BY_ID)
    public ResponseEntity<Object> deleteEventById(
            @PathVariable("eventId") Long eventId
    ) {
        eventService.deleteEventById(eventId);
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    @PutMapping(value = Endpoints.UPDATE_EVENT_BY_ID)
    public ResponseEntity<Object> updateEventById(
            @PathVariable("eventId") Long eventId,
            @RequestBody CreateEventRequest request
    ) {
        eventService.updateEventById(eventId, request);
        return new ResponseEntity<>("updated", HttpStatus.OK);
    }
}
