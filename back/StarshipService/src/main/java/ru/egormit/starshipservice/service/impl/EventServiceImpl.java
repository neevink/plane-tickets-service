package ru.egormit.starshipservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egormit.starshipservice.domain.*;
import ru.egormit.starshipservice.error.ErrorDescriptions;
import ru.egormit.starshipservice.integration.FirstService;
import ru.egormit.starshipservice.service.EventService;
import ru.egormit.starshipservice.utils.EventModelMapper;
import ru.itmo.library.*;
import ru.itmo.library.enums.EventType;
import ru.itmo.library.enums.TicketType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    /**
     * {@link FirstService}.
     */
    private final FirstService firstService;

    /**
     * {@link EventRepository}.
     */
    private final EventRepository eventRepository;

    /**
     * {@link EventRepository}.
     */
    private final TicketRepository ticketRepository;


    /**
     * {@link EventModelMapper}.
     */
    private final EventModelMapper eventModelMapper;

    @Override
    public EventDto createEvent(CreateEventRequest request) {
        Event event = new Event();
        event.setName(request.getName());
        event.setDate(request.getDate());
        event.setMinAge(request.getMinAge());
        event.setEventType(request.getEventType());
        eventRepository.save(event);
        EventDto createdEvent = new EventDto();
        createdEvent.setId(event.getId());
        createdEvent.setName(event.getName());
        createdEvent.setDate(event.getDate());
        createdEvent.setMinAge(event.getMinAge());
        createdEvent.setEventType(event.getEventType());
        return createdEvent;
    }

    public List<EventDto> getAllEvents(List<FilterCriteria> filterBy, SortCriteria sortBy, Long limit, Long offset) {
        for (var e : filterBy){
            System.out.println(e);
        }
        System.out.println(sortBy);

        EventSpecification spec = new EventSpecification(filterBy);
        var eventsStream = eventRepository.findAll(spec).stream();

        if (sortBy != null) {
            if (sortBy.getKey().equals("id")) {
                eventsStream = eventsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getId().compareTo(o2.getId()));
            }
            else if (sortBy.getKey().equals("name")) {
                eventsStream = eventsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getName().compareTo(o2.getName()));
            }
            else if (sortBy.getKey().equals("date")) {
                eventsStream = eventsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getDate().compareTo(o2.getDate()));
            }
            else if (sortBy.getKey().equals("minAge")) {
                eventsStream = eventsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getMinAge().compareTo(o2.getMinAge()));
            } else {
                throw ErrorDescriptions.INCORRECT_SORT.exception();
            }
        }
        return eventsStream
                .skip(offset)
                .limit(limit)
                .map(eventModelMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)){
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }

        Event event = eventRepository.getById(eventId);
        return eventModelMapper.map(event);
    }

    @Override
    public void deleteEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)){
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }

        var ticketsToDelete = ticketRepository.allTicketsByEventId(eventId);
        ticketsToDelete.forEach(ticket -> ticketRepository.deleteById(ticket.getId()));
        System.out.println("hello im here");

        eventRepository.deleteById(eventId);
    }

    @Override
    public EventDto updateEventById(Long eventId, CreateEventRequest request) {
        if (!eventRepository.existsById(eventId)) {
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }

        Event updatedEvent = new Event();
        updatedEvent.setId(eventId);
        updatedEvent.setName(request.getName());
        updatedEvent.setDate(request.getDate());
        updatedEvent.setMinAge(request.getMinAge());
        updatedEvent.setEventType(request.getEventType());

        eventRepository.save(updatedEvent);
        return eventModelMapper.map(updatedEvent);
    }

    @Override
    public long countEvents() {
        return eventRepository.count();
    }

    @Override
    public List<Object> getTypes() {
        var res = new ArrayList<>();
        for (var type : EventType.values()) {
            HashMap<String, String> a = new HashMap<>();
            a.put("value", type.name());
            a.put("desc", type.toString());
            res.add(a);
        }
        return res;
    }
}
