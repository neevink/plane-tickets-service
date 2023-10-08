package ru.egormit.starshipservice.common;

/**
 * Список эндпоинтов второго сервиса.
 *
 * @author Egor Mitrofanov.
 */
public interface Endpoints {
    String CREATE_STARSHIP = "/starships";
    String GET_ALL_STARSHIPS = "/starships";
    String ADD_MARINE_TO_STARSHIP = "/starships/{starship-id}/load/{spacemarine-id}";
    String CLEAN_STARSHIP = "/starships/{id}/unload";

    String CREATE_TICKET = "/tickets";
    String GET_ALL_TICKETS = "/tickets";
    String GET_TICKET_BY_ID = "/tickets/{ticketId}";

    String CREATE_EVENT = "/events";
    String GET_ALL_EVENTS = "/events";
    String GET_EVENT_BY_ID = "/events/{ticketId}";
}
