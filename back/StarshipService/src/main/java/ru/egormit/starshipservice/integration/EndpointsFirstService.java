package ru.egormit.starshipservice.integration;

/**
 * Эндпоинты для взаимодействия с первым сервисом.
 *
 * @author Egor Mitrofanov.
 */
public interface EndpointsFirstService {
    String GET_SPACEMARINE = "/spacemarines/{id}";
    String UPDATE_SPACEMARINE = "/spacemarines/{id}";
}
