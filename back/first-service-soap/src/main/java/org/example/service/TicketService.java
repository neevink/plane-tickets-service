package org.example.service;

import org.example.controller.restrequest.CreateTicketRestRequest;
import org.example.model.enums.TicketType;
import org.example.model.model.EventDto;
import org.example.model.model.TicketDto;
import org.example.repository.FilterCriteria;
import org.example.repository.SortCriteria;

import java.util.List;

public interface TicketService {

    TicketDto createTicket(CreateTicketRestRequest request);


    List<TicketDto> getAllTickets(
            List<String> filter,
            String sort,
            Long limit,
            Long offset
    ) throws Exception;

    List<TicketDto> getAllTickets(List<FilterCriteria> filterBy, List<SortCriteria> sortBy, Long limit, Long offset) throws Exception;

    TicketDto getTicketById(Long ticketId);

    TicketDto newVipTicketById(Long ticketId);

    TicketDto newDiscountTicketById(Long ticketId, Double discount);

    void deleteTicketById(Long ticketId);

    TicketDto updateTicketById(Long ticketId, TicketDto ticketDto);

    long countTickets();

    List<Object> getTypes();

    Double sumOfDiscount();

    Object sumOfDiscountCount();

    Long getTicketsTypeCount(TicketType type);
}
