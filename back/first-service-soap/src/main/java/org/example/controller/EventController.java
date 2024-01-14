package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Endpoints;
import org.example.controller.restrequest.CreateEventRestRequest;
import org.example.error.ErrorDescriptions;
import org.example.model.enums.EventType;
import org.example.model.model.EventDto;
import org.example.repository.FilterCriteria;
import org.example.repository.SortCriteria;
import org.example.service.EventService;
import org.example.service.EventServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping(value = Endpoints.CREATE_EVENT)
    public ResponseEntity<EventDto> createEvent(
            @RequestBody CreateEventRestRequest request
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
    ) throws Exception  {

        var filters = filter == null ? null : Arrays.stream(filter).toList();
        var events = eventService.getAllEvents(filters, sort, limit, offset);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

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
            @RequestBody CreateEventRestRequest request
    ) {
        EventDto eventDto = new EventDto();
        eventDto.setEventType(request.getEventType());
        eventDto.setName(request.getName());
        eventDto.setDate(request.getDate());
        eventDto.setMinAge(request.getMinAge());
        var res = eventService.updateEventById(eventId, eventDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

//     front needs this!!!!!!!!
    @GetMapping(value = Endpoints.GET_EVENTS_COUNT)
    public ResponseEntity<Long> countTickets() {
        return new ResponseEntity<>(eventService.countEvents(), HttpStatus.OK);
    }


    @GetMapping(value = Endpoints.GET_EVENTS_TYPES)
    public ResponseEntity<List<Object>> eventsTypes() {
        return new ResponseEntity<>(eventService.getTypes(), HttpStatus.OK);
    }

}
