package ru.egormit.starship.logic.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.egormit.library.ErrorResponse;

/**
 * Ошибки с комментариями.
 *
 * @author Egor Mitrofanov.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorDescriptions {
    HANDLER_NOT_FOUND("Не найден обработчик", 404),
    SPACEMACS_IS_BUSY("Этот космодесантник уже на другом корабле", 400),
    STARSHIP_IS_EMPTY("Этот космический корабль уже пустой", 400),
    STARSHIP_NOT_FOUND("Космический корабль не найден", 404),
    MARINE_NOT_FOUND("Десантник не найден", 404);

    /**
     * Сообщение ошибки.
     */
    private final String message;

    /**
     * Статус ошибки.
     */
    private final Integer status;

    /**
     * Метод выбрасывает исключение приложения.
     *
     * @throws ApplicationException исключение приложения
     */
    public void throwException() throws ApplicationException {
        throw exception();
    }

    /**
     * Метод выбрасывает ислючение если объект == null.
     *
     * @param obj объект для проверки
     */
    public void throwIfNull(Object obj) {
        if (obj == null) {
            throw exception();
        }
    }

    /**
     * Метод выбрасывает ислючение если условие истинно.
     *
     * @param condition условие для проверки
     */
    public void throwIfTrue(Boolean condition) {
        if (condition) {
            throw exception();
        }
    }

    /**
     * Метод выбрасывает ислючение если условие ложно.
     *
     * @param condition условие для проверки
     */
    public void throwIfFalse(Boolean condition) {
        if (!condition) {
            throw exception();
        }
    }

    public ErrorResponse applicationError() {
        return ErrorResponse.of(this.status, this.message);
    }


    public ApplicationException exception() {
        return new ApplicationException(applicationError());
    }
}
