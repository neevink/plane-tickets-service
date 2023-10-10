package ru.egormit.starshipservice.common;

/**
 * Список эндпоинтов второго сервиса.
 *
 * @author Neevin Kirill.
 */
public interface Endpoints {
    String CREATE_TICKET = "/tickets";
    String GET_ALL_TICKETS = "/tickets";
    String GET_TICKET_BY_ID = "/tickets/{ticketId}";
    String DELETE_TICKET_BY_ID = "/tickets/{ticketId}";
    String UPDATE_TICKET_BY_ID = "/tickets/{ticketId}";


    String CREATE_EVENT = "/events";
    String GET_ALL_EVENTS = "/events";
    String GET_EVENT_BY_ID = "/events/{eventId}";
    String DELETE_EVENT_BY_ID = "/events/{eventId}";
    String UPDATE_EVENT_BY_ID = "/events/{eventId}";
}
