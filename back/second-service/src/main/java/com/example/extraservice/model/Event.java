package com.example.extraservice.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
public class Event {
    @JsonIgnoreProperties
    private Long id;
    private String name;
    private Date date;
    private Long minAge;
    private EventType eventType;
}
