package ru.egormit.starshipservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.egormit.starshipservice.domain.EventRepository;
import ru.egormit.starshipservice.domain.TicketRepository;
import ru.egormit.starshipservice.error.ErrorDescriptions;
import ru.egormit.starshipservice.integration.FirstService;
import ru.egormit.starshipservice.service.TicketService;
import ru.egormit.starshipservice.utils.TicketModelMapper;
import ru.itmo.library.CreateTicketRequest;
import ru.itmo.library.Event;
import ru.itmo.library.Ticket;
import ru.itmo.library.TicketDto;

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
    public void createTicket(CreateTicketRequest request) {
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
    }

    @Override
    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll()
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
}
