package ru.itmo.library.enums;

public enum TicketType {
    VIP("Випка"),
    USUAL("Обычный"),
    BUDGETARY("Бюджетный"),
    CHEAP("Дешевый");

    private String ruValue;

    TicketType(String ruValue) {
        this.ruValue = ruValue;
    }

    @Override
    public String toString(){
        return ruValue;
    }
}
