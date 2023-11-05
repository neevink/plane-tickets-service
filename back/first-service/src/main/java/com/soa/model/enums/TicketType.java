package com.soa.model.enums;

public enum TicketType {
    CHEAP("Дешевый", 1),
    BUDGETARY("Бюджетный", 2),

    USUAL("Обычный", 3),
    VIP("Випка", 4);

    private String ruValue;
    private int value;

    TicketType(String ruValue, int value) {
        this.ruValue = ruValue;
        this.value = value;
    }

    @Override
    public String toString(){
        return ruValue;
    }

    public int getValue() {
        return value;
    }
}
