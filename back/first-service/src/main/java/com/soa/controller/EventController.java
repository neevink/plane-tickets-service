package com.soa.controller;

import com.soa.common.Endpoints;
import com.soa.model.CreateEventRequest;
import com.soa.model.EventDto;
import com.soa.model.TicketDto;
import com.soa.model.enums.EventType;
import com.soa.repository.FilterCriteria;
import com.soa.repository.SortCriteria;
import com.soa.service.EventService;
import com.soa.error.ErrorDescriptions;
import lombok.RequiredArgsConstructor;
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
    ) throws Exception  {
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

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        var allowedFilters = List.of(
                "id",
                "name",
                "date",
                "minAge",
                "eventType"
        );

        List<FilterCriteria> filters = new ArrayList<>();
        if (filter != null){
            try {
                for (String f : filter) {
                    var key = f.split("\\[", 2)[0];
                    if (!allowedFilters.contains(key)) {
                        throw new Exception("Недопустимое значение фильтра " + key + ", должно быть одно иззначений: " + allowedFilters);
                    }

                    var val = f.split("\\]", 2)[1];
                    val = val.substring(1);
                    if (key.equals("eventType")){
                        val = val.toUpperCase();
                        try {
                            EventType.valueOf(val);
                        } catch (Exception exc) {
                            throw new Exception("Недопустимое значение eventType: должно быть одно из значений: [CONCERT, BASEBALL, BASKETBALL, THEATRE_PERFORMANCE]");
                        }
                    } else if (key.equals("date")) {
                        Date date = formatter.parse(val);
                        System.out.println(date);
                        if (date == null){
                            throw new Exception("Недопустимое значение date: ожидается строка вида yyyy-MM-dd");
                        }
                    }

                    var op = f.split("\\[", 2)[1].split("\\]", 2)[0];

                    filters.add(
                            new FilterCriteria(
                                    key,
                                    op,
                                    key.equals("eventType") ? EventType.valueOf(val) : key.equals("date") ? formatter.parse(val) : val
                            )
                    );
                }
            } catch (IndexOutOfBoundsException exc){
                throw ErrorDescriptions.INCORRECT_FILTER.exception();
            }
        }

        List<SortCriteria> sc = new ArrayList<>();
        if (sort != null) {
            try {
                var listSorts = Arrays.asList(sort.split(","));
                for (String oneSort : listSorts) {
                    var descSort = oneSort.charAt(0) == '-';
                    var key = "";
                    if (descSort) {
                        key = oneSort.substring(1);
                    } else {
                        key = oneSort;
                    }
                    sc.add(new SortCriteria(key, !descSort));
                }
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
