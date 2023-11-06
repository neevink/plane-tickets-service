package com.soa.service;


import com.soa.model.CreateTicketRequest;
import com.soa.model.TicketDto;
import com.soa.model.enums.TicketType;
import com.soa.repository.FilterCriteria;
import com.soa.repository.SortCriteria;

import java.util.List;

/**
 * Интерфейс сервиса работы с ticket.
 *
 * @author Egor Mitrofanov.
 */
public interface TicketService {

    /**
     * Создание билета
     *
     * @param request билет
     * @return
     */
    TicketDto createTicket(CreateTicketRequest request);

    /**
     * Получение списка кораблей
     *
     * @return список всех билетов
     */
    List<TicketDto> getAllTickets(List<FilterCriteria> filterBy, List<SortCriteria> sortBy, Long limit, Long offset) throws Exception;

    /**
     * Получение билета
     *
     * @return билет
     */
    TicketDto getTicketById(Long ticketId);

    TicketDto newVipTicketById(Long ticketId);

    TicketDto newDiscountTicketById(Long ticketId, Double discount);

    void deleteTicketById(Long ticketId);

    TicketDto updateTicketById(Long ticketId, CreateTicketRequest request);

    long countTickets();

    List<Object> getTypes();

    Double sumOfDiscount();

    Object sumOfDiscountCount();

    Object getTicketsTypeCount(TicketType type);
}
