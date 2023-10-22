package ru.egormit.starshipservice.service;

import ru.itmo.library.CreateTicketRequest;
import ru.itmo.library.TicketDto;

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
    List<TicketDto> getAllTickets();

    /**
     * Получение билета
     *
     * @return билет
     */
    TicketDto getTicketById(Long ticketId);

    void deleteTicketById(Long ticketId);

    void updateTicketById(Long ticketId, CreateTicketRequest request);

    long countTickets();
}
