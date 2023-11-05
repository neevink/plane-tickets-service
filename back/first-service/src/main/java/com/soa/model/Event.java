package com.soa.model;


import com.soa.model.enums.EventType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность Event.
 */
@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "event_seq")
    private Long id;

    @Column(name = "name")
    @Lob
    private String name;

    @Column(name = "date")
    private Date date;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "event_type")
    private EventType eventType;
}
