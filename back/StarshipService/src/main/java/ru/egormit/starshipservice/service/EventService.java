package ru.egormit.starshipservice.service;

import ru.egormit.starshipservice.domain.FilterCriteria;
import ru.egormit.starshipservice.domain.SortCriteria;
import ru.itmo.library.CreateEventRequest;
import ru.itmo.library.EventDto;

import java.util.List;

public interface EventService {
    EventDto createEvent(CreateEventRequest request);

    List<EventDto> getAllEvents(List<FilterCriteria> filterBy, SortCriteria sortBy, Long limit, Long offset);

    EventDto getEventById(Long ticketId);

    void deleteEventById(Long eventId);

    void updateEventById(Long eventId, CreateEventRequest request);
}
