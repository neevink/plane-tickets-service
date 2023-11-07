package com.soa.service;

import com.soa.mapper.EventModelMapper;
import com.soa.model.Event;
import com.soa.model.CreateEventRequest;
import com.soa.model.EventDto;
import com.soa.model.Ticket;
import com.soa.model.enums.EventType;
import com.soa.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import com.soa.error.ErrorDescriptions;


import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

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

    @Override
    public List<EventDto> getAllEvents(List<FilterCriteria> filterBy, List<SortCriteria> sortBy, Long limit, Long offset) throws Exception {
        for (var e : filterBy){
            System.out.println(e);
        }
        System.out.println(sortBy);

        try {
            EventSpecification spec = new EventSpecification(filterBy);
            var eventsStream = eventRepository.findAll(spec).stream();

            for (var f :filterBy){
                if (f.getKey().equals("date")){
                    if (f.getOperation().equals("eq")) {
                        eventsStream = eventsStream.filter(event -> event.getDate().equals((Date)f.getValue()));
                    } else if (f.getOperation().equals("ne")) {
                        eventsStream = eventsStream.filter(event -> !event.getDate().equals((Date)f.getValue()));
                    } else if (f.getOperation().equals("gt")) {
                        eventsStream = eventsStream.filter(event -> event.getDate().before((Date)f.getValue()));
                    } else {
                        eventsStream = eventsStream.filter(event -> event.getDate().after((Date)f.getValue()));
                    }
                } else if (f.getKey().equals("eventType")){
                    if (f.getOperation().equals("eq")) {
                        eventsStream = eventsStream.filter(event -> event.getEventType().equals(f.getValue()));
                    } else if (f.getOperation().equals("ne")) {
                        eventsStream = eventsStream.filter(event -> !event.getEventType().equals(f.getValue()));
                    } else if (f.getOperation().equals("gt")) {
                        eventsStream = eventsStream.filter(event -> (event.getEventType().compareTo((EventType) f.getValue()) < 0));
                    } else {
                        eventsStream = eventsStream.filter(event -> (event.getEventType().compareTo((EventType) f.getValue()) > 0));
                    }
                }
            }

            if (sortBy != null && sortBy.size() != 0) {
                Comparator<Event> c = null;
                for (SortCriteria sortCriteria : sortBy) {
                    Comparator<Event> currentComp;
                    var desc = !sortCriteria.getAscending();
                    switch (sortCriteria.getKey()) {
                        case "id" -> currentComp = Comparator.comparing(Event::getId);
                        case "name" -> currentComp = Comparator.comparing(Event::getName);
                        case "date" -> currentComp = Comparator.comparing(Event::getDate);
                        case "minAge" -> currentComp = Comparator.comparing(Event::getMinAge);
                        default -> throw ErrorDescriptions.INCORRECT_SORT.exception();
                    }
                    if (desc) currentComp = currentComp.reversed();
                    if (c == null) {
                        c = currentComp;
                    } else {
                        c = c.thenComparing(currentComp);
                    }
                }
                if (c != null) eventsStream = eventsStream.sorted(c);
            }

            return eventsStream
                    .skip(offset)
                    .limit(limit)
                    .map(eventModelMapper::map)
                    .collect(Collectors.toList());
        } catch (
            InvalidDataAccessApiUsageException exc){
                throw new Exception("В фильтре передано недопустимое значение для сравнения");
        }
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
    @Transactional
    public void deleteEventById(Long eventId) {
        if (!eventRepository.existsById(eventId)){
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }
        ticketRepository.deleteAllByEventId(eventId);
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
