package ru.egormit.starshipservice.error;

import lombok.Getter;
import ru.egormit.library.ErrorResponse;

/**
 * Кастомные исключения.
 *
 * @author Egor Mitrofanov.
 */
@Getter
public class ApplicationException extends RuntimeException {

    /**
     * {@link ErrorResponse}, ошибка, возвращаемая на фронт.
     */
    private final ErrorResponse errorResponse;

    public ApplicationException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}
