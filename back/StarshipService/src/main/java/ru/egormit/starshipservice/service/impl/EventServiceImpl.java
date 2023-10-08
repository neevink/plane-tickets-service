package ru.egormit.starshipservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.egormit.starshipservice.domain.EventRepository;
import ru.egormit.starshipservice.domain.TicketRepository;
import ru.egormit.starshipservice.integration.FirstService;
import ru.egormit.starshipservice.service.EventService;
import ru.egormit.starshipservice.utils.EventModelMapper;
import ru.egormit.starshipservice.utils.TicketModelMapper;
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
    public void createEvent(CreateEventRequest request) {
        Event event = new Event();
        event.setName(request.getName());
        event.setDate(request.getDate());
        event.setMinAge(request.getMinAge());
        event.setEventType(request.getEventType());
        eventRepository.save(event);
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventModelMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getEventById(Long eventId) {
        Event event = eventRepository.getById(eventId);
        return eventModelMapper.map(event);
    }
}
