package se.ifmo.ru.service.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum View {
    STREET("street"),
    PARK("park"),
    BAD("bad"),
    GOOD("good"),
    TERRIBLE("terrible"),
    UNDEFINED("undefined");

    @Getter
    private final String value;

    @Override
    public String toString(){
        if (value == null) return "";
        return value;
    }

    public static View fromValue(String value){
        return Arrays.stream(View.values())
                .filter(e -> Objects.equals(e.getValue(), value))
                .findFirst()
                .orElse(null);
    }
}
