package ru.egormit.starshipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.egormit.starshipservice.common.Endpoints;
import ru.egormit.starshipservice.domain.FilterCriteria;
import ru.egormit.starshipservice.error.ErrorDescriptions;
import ru.egormit.starshipservice.service.EventService;
import ru.itmo.library.CreateEventRequest;
import ru.itmo.library.EventDto;

import java.util.ArrayList;
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
    public ResponseEntity<EventDto> createEvent(
            @RequestBody CreateEventRequest request
    ) {
        EventDto newEvent = eventService.createEvent(request);
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }

    /**
     * Получение списка событий.
     *
     * @return список событий.
     */
    @GetMapping(value = Endpoints.GET_ALL_EVENTS)
    public ResponseEntity<List<EventDto>> getAllEvents(
            @RequestParam(value = "filter", required = false) String[] filter,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset
    ) {
//        System.out.println("filter:");
//        System.out.println(filter);
//        System.out.println("sort:");
//        System.out.println(sort);

        List<FilterCriteria> filters = new ArrayList<>();
        if (filter != null){
            try {
                for (String f : filter) {
                    var key = f.split("\\[", 2)[0];
                    var val = f.split("\\]", 2)[1];
                    val = val.substring(1);
                    var op = f.split("\\[", 2)[1].split("\\]", 2)[0];

                    filters.add(
                            new FilterCriteria(
                                    key,
                                    op,
                                    val
                            )
                    );
                }
            } catch (Exception e) {
                throw ErrorDescriptions.INCORRECT_FILTER.exception();
            }
        }

        List<EventDto> events = eventService.getAllEvents(filters, null, limit, offset);
        return new ResponseEntity<>(events, HttpStatus.OK);
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

    // front needs this!!!!!!!!
    @GetMapping(value = Endpoints.GET_EVENTS_COUNT)
    public ResponseEntity<Long> countTickets() {
        return new ResponseEntity<>(eventService.countEvents(), HttpStatus.OK);
    }
}
