package com.soa.entity;

import com.soa.dto.Difficulty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "LabWork")
@Accessors(chain = true)
public class LabWorkEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "LABWORK_NAME")
    private String name;
    @Embedded
    @JoinColumn(name = "COORDINATES_ID")
    private CoordinatesEntity coordinates;
    @CreationTimestamp
    @Column(name = "CREATION_DATE")
    private Instant creationDate;
    @Column(name = "MINIMAL_POINT")
    private int minimalPoint = 0;
    @Column(name = "DIFFICULTY")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id")
    private DisciplineEntity discipline;
}
