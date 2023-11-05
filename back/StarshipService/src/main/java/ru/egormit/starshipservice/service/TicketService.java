package ru.egormit.starshipservice.service;

import ru.egormit.starshipservice.domain.FilterCriteria;
import ru.egormit.starshipservice.domain.SortCriteria;
import ru.itmo.library.CreateTicketRequest;
import ru.itmo.library.TicketDto;
import ru.itmo.library.enums.TicketType;

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
    List<TicketDto> getAllTickets(List<FilterCriteria> filterBy, SortCriteria sortBy, Long limit, Long offset);

    /**
     * Получение билета
     *
     * @return билет
     */
    TicketDto getTicketById(Long ticketId);

    TicketDto newVipTicketById(Long ticketId);

    void deleteTicketById(Long ticketId);

    TicketDto updateTicketById(Long ticketId, CreateTicketRequest request);

    long countTickets();

    List<Object> getTypes();

    Double sumOfDiscount();

    Object sumOfDiscountCount();

    Object getTicketsTypeCount(TicketType type);
}
