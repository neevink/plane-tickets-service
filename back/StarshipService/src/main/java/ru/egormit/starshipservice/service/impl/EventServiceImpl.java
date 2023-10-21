package ru.egormit.starshipservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.egormit.starshipservice.domain.EventRepository;
import ru.egormit.starshipservice.error.ErrorDescriptions;
import ru.egormit.starshipservice.integration.FirstService;
import ru.egormit.starshipservice.service.EventService;
import ru.egormit.starshipservice.utils.EventModelMapper;
import ru.itmo.library.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса работы с starship.
 *
 * @author Egor Mitrofanov.
 */
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

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
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

        eventRepository.deleteById(eventId);
    }

    @Override
    public void updateEventById(Long eventId, CreateEventRequest request) {
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
    }
}
