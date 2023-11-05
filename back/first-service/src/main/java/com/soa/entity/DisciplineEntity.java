package com.soa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "Discipline")
@Accessors(chain = true)
public class DisciplineEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "DISCIPLINE_NAME")
    private String name;
    @Column(name = "SELF_STUDY_HOURS")
    private int selfStudyHours;
    @OneToMany(mappedBy = "discipline")
    private List<LabWorkEntity> labworks;
}
