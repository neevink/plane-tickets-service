package ru.egormit.library;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

/**
 * Сущность Starship.
 *
 * @author Egor Mitrofanov.
 */
@Getter
@Setter
@Entity
@Table(name = "starship")
public class StarShip {

    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", allocationSize = 1, sequenceName = "starship_seq")
    private Long id;

    /**
     * Имя.
     */
    @Column(name = "name")
    private String name;

    /**
     * Флотн.
     */
    @Column(name = "fleet")
    private String fleet;

    /**
     * Координата X.
     */
    @Column(name = "coordinate_x")
    private Integer coordinateX;

    /**
     * Координата Y.
     */
    @Column(name = "coordinate_y")
    private Long coordinateY;

    /**
     * Десетники в корабле.
     */
    @OneToMany(mappedBy = "starShip", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<SpaceMarine> spaceMarines;
}
