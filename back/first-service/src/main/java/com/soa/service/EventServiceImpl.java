package com.soa.service;

import com.soa.mapper.EventModelMapper;
import com.soa.model.Event;
import com.soa.model.CreateEventRequest;
import com.soa.model.EventDto;
import com.soa.model.enums.EventType;
import com.soa.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import com.soa.error.ErrorDescriptions;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public List<EventDto> getAllEvents(List<FilterCriteria> filterBy, SortCriteria sortBy, Long limit, Long offset) throws Exception {
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
                }
            }

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
