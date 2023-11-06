package com.soa.error;

import com.soa.model.common.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDescriptions {
    SPACEMACS_IS_BUSY("Этот космодесантник уже на другом корабле", HttpStatus.BAD_REQUEST),
    STARSHIP_IS_EMPTY("Этот космический корабль уже пустой", HttpStatus.BAD_REQUEST),
    STARSHIP_NOT_FOUND("Этот космический корабль уже пустой", HttpStatus.NOT_FOUND),

    HANDLER_NOT_FOUND("Не найден обработчик", HttpStatus.NOT_FOUND),

    TICKET_NOT_FOUND("Билет с заданным ID не найден", HttpStatus.NOT_FOUND),

    EVENT_NOT_FOUND("Событие с заданным ID не найдено", HttpStatus.NOT_FOUND),
    CANT_DELETE_EVENT("Нельзя удалить событие, пока на него существует хотя бы один билет", HttpStatus.BAD_REQUEST),
    REFUNDABLE_MUST_PRESENT("Refundable должен быть либо true либо false", HttpStatus.BAD_REQUEST),
    DISCOUNT_MUST_PRESENT("Скидка должна быть от 1 до 100", HttpStatus.BAD_REQUEST),

    COORDINATES_MUST_PRESENT("Координаты X Y должны присутствовать", HttpStatus.BAD_REQUEST),
    X_BAD("Координата x должна быть > -686", HttpStatus.BAD_REQUEST),

    INCORRECT_FILTER("Некорректный формат фильтра, ожидается формат вида: ПОЛЕ[eq|ne|gt|lt]=ЗНАЧЕНИЕ", HttpStatus.BAD_REQUEST),
    INCORRECT_SORT("Некорректный формат сортировки", HttpStatus.BAD_REQUEST),

    INCORRECT_LIMIT("Некорректный лимит (> 0)", HttpStatus.BAD_REQUEST),
    INCORRECT_OFFSET("Некорректное смещение (>= 0)", HttpStatus.BAD_REQUEST)
    ;

    /**
     * Сообщение ошибки.
     */
    private final String message;

    /**
     * Статус ошибки.
     */
    private final HttpStatus status;

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
        return ErrorResponse.of(this.status.value(), this.message);
    }


    public ApplicationException exception() {
        return new ApplicationException(applicationError());
    }
}
