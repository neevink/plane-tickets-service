package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.restrequest.CreateEventRestRequest;
import org.example.error.ErrorDescriptions;
import org.example.mapper.EventModelMapper;
import org.example.model.enums.EventType;
import org.example.model.model.Event;
import org.example.model.model.EventDto;
import org.example.repository.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final TicketRepository ticketRepository;


    private final EventModelMapper eventModelMapper;


    @Override
    public EventDto createEvent(CreateEventRestRequest request) {

        if (request.getName() == null) throw ErrorDescriptions.INCORRECT_EVENT.exception();
        if (request.getMinAge() == null) throw ErrorDescriptions.INCORRECT_EVENT.exception();

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
    public List<EventDto> getAllEvents(List<String> filter, String sort, Long limit, Long offset) throws Exception {
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

        List<EventDto> events = getAllEvents(filters, sc, limit, offset);
        return events;
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
                        case "id": currentComp = Comparator.comparing(Event::getId); break;
                        case "name": currentComp = Comparator.comparing(Event::getName); break;
                        case "date": currentComp = Comparator.comparing(Event::getDate); break;
                        case "minAge": currentComp = Comparator.comparing(Event::getMinAge); break;
                        default: throw ErrorDescriptions.INCORRECT_SORT.exception();
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
    @Transactional // removing this causes errors lol
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
    public EventDto updateEventById(Long eventId, EventDto eventDto) {
        if (!eventRepository.existsById(eventId)) {
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }

        Event updatedEvent = new Event();
        updatedEvent.setId(eventId);
        updatedEvent.setName(eventDto.getName());
        updatedEvent.setDate(eventDto.getDate());
        updatedEvent.setMinAge(eventDto.getMinAge());
        updatedEvent.setEventType(eventDto.getEventType());

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
