package com.soa.model.enums;

public enum EventType {
    CONCERT("Концерт"),
    BASEBALL("Бейсбол"),
    BASKETBALL("Баскетбол"),
    THEATRE_PERFORMANCE("Театр");

    private String ruValue;

    EventType (String ruValue) {
        this.ruValue = ruValue;
    }

    @Override
    public String toString(){
        return ruValue;
    }
}
