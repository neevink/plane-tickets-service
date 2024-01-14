package org.example.service;



import org.example.controller.restrequest.CreateEventRestRequest;
import org.example.model.model.EventDto;
import org.example.repository.FilterCriteria;
import org.example.repository.SortCriteria;

import java.util.List;

public interface EventService {
    EventDto createEvent(CreateEventRestRequest request);


    List<EventDto> getAllEvents(
            List<String> filter,
            String sort,
            Long limit,
            Long offset
    ) throws Exception;
    List<EventDto> getAllEvents(List<FilterCriteria> filterBy, List<SortCriteria> sortBy, Long limit, Long offset) throws Exception;

    EventDto getEventById(Long ticketId);

    void deleteEventById(Long eventId);

    EventDto updateEventById(Long eventId, EventDto request);

    long countEvents();

    List<Object> getTypes();
}
