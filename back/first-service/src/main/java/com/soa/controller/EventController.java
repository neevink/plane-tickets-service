package com.soa.controller;

import com.soa.common.Endpoints;
import com.soa.model.CreateEventRequest;
import com.soa.model.EventDto;
import com.soa.model.enums.EventType;
import com.soa.repository.FilterCriteria;
import com.soa.repository.SortCriteria;
import com.soa.service.EventService;
import com.soa.error.ErrorDescriptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping(value = Endpoints.CREATE_EVENT)
    public ResponseEntity<EventDto> createEvent(
            @RequestBody CreateEventRequest request
    ) {
        EventDto newEvent = eventService.createEvent(request);
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }

    @GetMapping(value = Endpoints.GET_ALL_EVENTS)
    public ResponseEntity<List<EventDto>> getAllEvents(
            @RequestParam(value = "filter", required = false) String[] filter,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Long offset
    ) {
        if (limit != null) {
            if (limit <= 0) {
                throw ErrorDescriptions.INCORRECT_LIMIT.exception();
            }
        }

        if (offset != null) {
            if (offset < 0) {
                throw ErrorDescriptions.INCORRECT_OFFSET.exception();
            }
        }

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
                                    key.equals("eventType") ? EventType.valueOf(val) : val
                            )
                    );
                }
            } catch (Exception e) {
                throw ErrorDescriptions.INCORRECT_FILTER.exception();
            }
        }

        SortCriteria sc = null;
        if (sort != null) {
            try {
                var descSort = sort.charAt(0) == '-';
                var key = "";
                if (descSort) {
                    key = sort.substring(1);
                } else {
                    key = sort;
                }
                sc = new SortCriteria(key, !descSort);

            } catch (Exception e) {
                throw ErrorDescriptions.INCORRECT_SORT.exception();
            }
        }
        List<EventDto> events = eventService.getAllEvents(filters, sc, limit, offset);
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
        var res = eventService.updateEventById(eventId, request);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // front needs this!!!!!!!!
    @GetMapping(value = Endpoints.GET_EVENTS_COUNT)
    public ResponseEntity<Long> countTickets() {
        return new ResponseEntity<>(eventService.countEvents(), HttpStatus.OK);
    }


    @GetMapping(value = Endpoints.GET_EVENTS_TYPES)
    public ResponseEntity<List<Object>> eventsTypes() {
        return new ResponseEntity<>(eventService.getTypes(), HttpStatus.OK);
    }

}
