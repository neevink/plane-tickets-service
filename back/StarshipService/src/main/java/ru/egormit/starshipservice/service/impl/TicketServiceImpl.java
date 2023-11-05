package ru.egormit.starshipservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.egormit.starshipservice.domain.*;
import ru.egormit.starshipservice.error.ErrorDescriptions;
import ru.egormit.starshipservice.integration.FirstService;
import ru.egormit.starshipservice.service.EventService;
import ru.egormit.starshipservice.service.TicketService;
import ru.egormit.starshipservice.utils.EventModelMapper;
import ru.egormit.starshipservice.utils.TicketModelMapper;
import ru.itmo.library.*;
import ru.itmo.library.enums.TicketType;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    /**
     * {@link FirstService}.
     */
    private final FirstService firstService;

    /**
     * {@link TicketRepository}.
     */
    private final TicketRepository ticketRepository;

    /**
     * {@link EventRepository}.
     */
    private final EventRepository eventRepository;

    /**
     * {@link EventRepository}.
     */
    private final EventService eventService;

    /**
     * {@link TicketModelMapper}.
     */
    private final TicketModelMapper ticketModelMapper;

    /**
     * {@link EventModelMapper}.
     */
    private final EventModelMapper eventModelMapper;

    @Override
    public TicketDto createTicket(CreateTicketRequest request) {
        if (request.getRefundable() == null) {
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }
        Ticket ticket = new Ticket();
        ticket.setName(request.getName());
        ticket.setCoordinateX(request.getCoordinates().getX());
        ticket.setCoordinateY(request.getCoordinates().getY());
        ticket.setCreationDate(ZonedDateTime.now());
        ticket.setPrice(request.getPrice());
        ticket.setDiscount(request.getDiscount());
        ticket.setRefundable(request.getRefundable());
        ticket.setType(request.getType());

        TicketDto createdTicket = new TicketDto();

        if (request.getEvent() != null) {
            if (request.getEvent().getId() != null) {
                if (!eventRepository.existsById(request.getEvent().getId())) {
                    throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
                }
                else {
                    Event event = eventRepository.findById(request.getEvent().getId()).get();
                    ticket.setEvent(event);
                    createdTicket.setEvent(event);
                }
            } else {  // create a new one
                EventDto newEvent = eventService.createEvent(CreateEventRequest.of(
                        request.getEvent().getName(),
                        request.getEvent().getDate(),
                        request.getEvent().getMinAge(),
                        request.getEvent().getEventType()
                ));
                System.out.println(newEvent.getId());
                Event event = eventRepository.findById(newEvent.getId()).get();
                ticket.setEvent(event);
                createdTicket.setEvent(event);
            }
        }
        ticketRepository.save(ticket);

        createdTicket.setId(ticket.getId());
        createdTicket.setName(ticket.getName());
        createdTicket.setCoordinates(Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()));
        createdTicket.setCreationDate(Date.from(ticket.getCreationDate().toInstant()));
        createdTicket.setPrice(ticket.getPrice());
        createdTicket.setDiscount(ticket.getDiscount());
        createdTicket.setRefundable(ticket.getRefundable());
        createdTicket.setType(ticket.getType());
        return createdTicket;
    }

    @Override
    public List<TicketDto> getAllTickets(
            List<FilterCriteria> filterBy, SortCriteria sortBy, Long limit, Long offset
    ) {
        for (var e : filterBy){
            System.out.println(e);
        }
        System.out.println(sortBy);

        TicketSpecification spec = new TicketSpecification(filterBy);
        var ticketsStream = ticketRepository.findAll(spec).stream();

        if (sortBy != null) {
            if (sortBy.getKey().equals("id")) {
                ticketsStream = ticketsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getId().compareTo(o2.getId()));
            }
            else if (sortBy.getKey().equals("name")) {
                ticketsStream = ticketsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getName().compareTo(o2.getName()));
            }
            else if (sortBy.getKey().equals("coordinateX")) {
                ticketsStream = ticketsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getCoordinateX().compareTo(o2.getCoordinateX()));
            }
            else if (sortBy.getKey().equals("coordinateY")) {
                ticketsStream = ticketsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getCoordinateY().compareTo(o2.getCoordinateY()));
            }
            else if (sortBy.getKey().equals("creationDate")) {
                ticketsStream = ticketsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getCreationDate().compareTo(o2.getCreationDate()));
            }
            else if (sortBy.getKey().equals("price")) {
                ticketsStream = ticketsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getPrice().compareTo(o2.getPrice()));
            }
            else if (sortBy.getKey().equals("discount")) {
                ticketsStream = ticketsStream.sorted((o1, o2) -> (sortBy.getAscending() ? 1 : -1) * o1.getDiscount().compareTo(o2.getDiscount()));
            }
            else {
                throw ErrorDescriptions.INCORRECT_SORT.exception();
            }
        }
        return ticketsStream
                .skip(offset)
                .limit(limit)
               .map(ticketModelMapper::map)
               .collect(Collectors.toList());
    }

    @Override
    public TicketDto getTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        Ticket ticket = ticketRepository.findById(ticketId).get();
        return ticketModelMapper.map(ticket);
    }

    @Override
    public TicketDto newVipTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        Ticket ticket = ticketRepository.findById(ticketId).get();
        System.out.println(ticket.getEvent());
        TicketDto newVipTicket = createTicket(CreateTicketRequest.of(
                ticket.getName(),
                Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()),
                ticket.getPrice() * 2,
                ticket.getDiscount(),
                ticket.getRefundable(),
                TicketType.VIP,
                eventModelMapper.map(ticket.getEvent())
        ));
        return newVipTicket;
    }

    @Override
    public TicketDto newDiscountTicketById(Long ticketId, Double discount) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        Ticket ticket = ticketRepository.findById(ticketId).get();
        System.out.println(ticket.getEvent());
        TicketDto newVipTicket = createTicket(CreateTicketRequest.of(
                ticket.getName(),
                Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()),
                ticket.getPrice() * (1 - discount / 100.0),
                discount,
                ticket.getRefundable(),
                ticket.getType(),
                eventModelMapper.map(ticket.getEvent())
        ));
        return newVipTicket;
    }

    @Override
    public void deleteTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)){
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public TicketDto updateTicketById(Long ticketId, CreateTicketRequest request) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }

        if (request.getRefundable() == null) {
            throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
        }
        Ticket updatedTicket = new Ticket();
        updatedTicket.setId(ticketId);
        updatedTicket.setName(request.getName());
        updatedTicket.setCoordinateX(request.getCoordinates().getX());
        updatedTicket.setCoordinateY(request.getCoordinates().getY());
        updatedTicket.setCreationDate(ZonedDateTime.now());
        updatedTicket.setPrice(request.getPrice());
        updatedTicket.setDiscount(request.getDiscount());
        updatedTicket.setRefundable(request.getRefundable());
        updatedTicket.setType(request.getType());

        TicketDto createdTicket = new TicketDto();

        if (request.getEvent() != null) {
            if (request.getEvent().getId() != null) {
                if (!eventRepository.existsById(request.getEvent().getId())) {
                    throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
                }
                else {
                    Event event = eventRepository.findById(request.getEvent().getId()).get();
                    updatedTicket.setEvent(event);
                    createdTicket.setEvent(event);
                }
            } else {  // create a new one
                EventDto newEvent = eventService.createEvent(CreateEventRequest.of(
                        request.getEvent().getName(),
                        request.getEvent().getDate(),
                        request.getEvent().getMinAge(),
                        request.getEvent().getEventType()
                ));
                System.out.println(newEvent.getId());
                Event event = eventRepository.findById(newEvent.getId()).get();
                updatedTicket.setEvent(event);
                createdTicket.setEvent(event);
            }
        }
        ticketRepository.save(updatedTicket);
        return ticketModelMapper.map(updatedTicket);
    }

    @Override
    public long countTickets() {
        return ticketRepository.count();
    }

    @Override
    public List<Object> getTypes() {
        var res = new ArrayList<>();
        for (var type : TicketType.values()) {
            HashMap<String, String> a = new HashMap<>();
            a.put("value", type.name());
            a.put("desc", type.toString());
            res.add(a);
        }
        return res;
    }

    @Override
    public Double sumOfDiscount() {
        var all = ticketRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false).map(Ticket::getDiscount).reduce((double) 0, Double::sum);
    }

    @Override
    public Object sumOfDiscountCount() {
        var all = ticketRepository.findAll();
//        [
//        {discount: 10
//         count: 100}
//        {discount: 11
//         count: 101}
//        ]
        var groupedByDiscount = StreamSupport.stream(all.spliterator(), false)
                .collect(groupingBy(
                        Ticket::getDiscount
                ));
        var res = new ArrayList<Map<String, Number>>();

        for(var t : groupedByDiscount.entrySet()){
            var hueta =
                    Map.of("discount", t.getKey(), "count", (Number) t.getValue().size());
            res.add(hueta);
        }
        return res;
    }

    @Override
    public Long getTicketsTypeCount(TicketType ticketType) {
        var all = ticketRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .filter(ticket -> ticket.getType().getValue() < ticketType.getValue())
                .count();
    }
}
