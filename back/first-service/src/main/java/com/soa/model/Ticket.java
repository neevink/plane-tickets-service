package com.soa.model;


import com.soa.model.enums.TicketType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность Ticket.
 */
@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {

    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "ticket_seq")
    private Long id;

    /**
     * Название.
     */
    @Column(name = "name")
    @Lob
    private String name;

    /**
     * Координата X.
     */
    @Column(name = "coordinate_x")
    private Integer coordinateX;

    /**
     * Координата Y.
     */
    @Column(name = "coordinate_y")
    private Float coordinateY;

    /**
     * Дата создания.
     */
    @Column(name = "creation_date")
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    /**
     * Цена.
     */
    @Column(name = "price")
    private Double price; //Значение поля должно быть больше 0

    /**
     * Скидка.
     */
    @Column(name = "discount")
    private Double discount; //Поле не может быть null, Значение поля должно быть больше 0, Максимальное значение поля: 100

    /**
     *
     */
    @Column(name = "refundable")
    private Boolean refundable; //Поле не может быть null


    /**
     * Тип билета.
     */
    @Column(name = "type")
    private TicketType type; //Поле может быть null

    /**
     * На кокое событие билет.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="event_id")
    private Event event;  //Поле не может быть null
}
