package ru.egormit.starshipservice.common;

/**
 * Список эндпоинтов второго сервиса.
 *
 * @author Neevin Kirill.
 */
public interface Endpoints {
    String CREATE_TICKET = "/tickets";
    String GET_ALL_TICKETS = "/tickets";

    String GET_TICKETS_COUNT = "/tickets/count";
    String GET_TICKET_BY_ID = "/tickets/{ticketId}";
    String DELETE_TICKET_BY_ID = "/tickets/{ticketId}";
    String UPDATE_TICKET_BY_ID = "/tickets/{ticketId}";


    String CREATE_EVENT = "/events";
    String GET_ALL_EVENTS = "/events";
    String GET_EVENT_BY_ID = "/events/{eventId}";
    String DELETE_EVENT_BY_ID = "/events/{eventId}";
    String UPDATE_EVENT_BY_ID = "/events/{eventId}";
    String GET_EVENTS_COUNT = "/events/count";


    String GET_EVENTS_TYPES = "/events/types";
    String GET_TICKETS_TYPES = "/tickets/types";
    String GET_TICKETS_DISCOUNT_SUM = "/tickets/discount/sum";
    String GET_TICKETS_DISCOUNT_COUNT = "/tickets/discount/count";
    String GET_TICKETS_TYPE_COUNT = "/tickets/type/count";
}
