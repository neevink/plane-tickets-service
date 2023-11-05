package com.soa.exception;

public class IncreaseNotAvailableException extends RuntimeException {

    public IncreaseNotAvailableException(int cnt) {
        super("Нельзя увеличить сложность на столько шагов. Увеличить можно не больше, чем на " + cnt);
    }

}
