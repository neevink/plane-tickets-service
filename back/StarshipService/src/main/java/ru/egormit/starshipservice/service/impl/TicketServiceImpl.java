package ru.egormit.starshipservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.egormit.starshipservice.domain.*;
import ru.egormit.starshipservice.error.ErrorDescriptions;
import ru.egormit.starshipservice.integration.FirstService;
import ru.egormit.starshipservice.service.TicketService;
import ru.egormit.starshipservice.utils.TicketModelMapper;
import ru.itmo.library.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса работы с starship.
 *
 * @author Egor Mitrofanov.
 */
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
     * {@link TicketModelMapper}.
     */
    private final TicketModelMapper ticketModelMapper;

    @Override
    public TicketDto createTicket(CreateTicketRequest request) {
        Ticket ticket = new Ticket();
        ticket.setName(request.getName());
        ticket.setCoordinateX(request.getCoordinates().getX());
        ticket.setCoordinateY(request.getCoordinates().getY());
        ticket.setCreationDate(ZonedDateTime.now());
        ticket.setPrice(request.getPrice());
        ticket.setDiscount(request.getDiscount());
        ticket.setRefundable(request.getRefundable());
        ticket.setType(request.getType());

        if (request.getEventId() != null) {
            if (!eventRepository.existsById(request.getEventId())) {
                throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
            }
            else {
                Optional<Event> event = eventRepository.findById(request.getEventId());
                ticket.setEvent(event.get());
            }
        }
        ticketRepository.save(ticket);

        TicketDto createdTicket = new TicketDto();
        createdTicket.setId(ticket.getId());
        createdTicket.setName(ticket.getName());
        createdTicket.setCoordinates(Coordinates.of(ticket.getCoordinateX(), ticket.getCoordinateY()));
        createdTicket.setCreationDate(ticket.getCreationDate());
        createdTicket.setPrice(ticket.getPrice());
        createdTicket.setDiscount(ticket.getDiscount());
        createdTicket.setRefundable(ticket.getRefundable());
        createdTicket.setType(ticket.getType());
//        createdTicket.setEvent(eve);
        return createdTicket;
    }

    @Override
    public List<TicketDto> getAllTickets(
            List<FilterCriteria> filterBy, SortCriteria sortBy, Long limit, Long offset
    ) {
        for (var e : filterBy){
            System.out.println(e);
        }

        TicketSpecification spec = new TicketSpecification(filterBy);
        return ticketRepository.findAll(spec)
                .stream()
                .map(ticketModelMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto getTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        Ticket ticket = ticketRepository.getById(ticketId);
        return ticketModelMapper.map(ticket);
    }

    @Override
    public void deleteTicketById(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)){
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
        }
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public void updateTicketById(Long ticketId, CreateTicketRequest request) {
        if (!ticketRepository.existsById(ticketId)) {
            throw ErrorDescriptions.TICKET_NOT_FOUND.exception();
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

        if (request.getEventId() != null) {
            if (!eventRepository.existsById(request.getEventId())) {
                throw ErrorDescriptions.EVENT_NOT_FOUND.exception();
            }
            else {
                Optional<Event> event = eventRepository.findById(request.getEventId());
                updatedTicket.setEvent(event.get());
            }
        }
        ticketRepository.save(updatedTicket);
    }

    @Override
    public long countTickets() {
        return ticketRepository.count();
    }
}
