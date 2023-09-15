package se.ifmo.ru.firstservice.storage.model;

import lombok.*;
import se.ifmo.ru.firstservice.service.model.View;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "flat")
@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "flat")
public class FlatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id", nullable = false)
    private CoordinatesEntity coordinates;

    @Column(name = "area")
    private Integer area;

    @Column(name = "number_of_rooms")
    private Long numberOfRooms;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "time_to_metro_on_foot")
    private Integer timeToMetroOnFoot;

    @Column(name = "balcony")
    private Boolean balcony;

    @Column(name = "view")
    @Enumerated(EnumType.STRING)
    private View view;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    private HouseEntity house;

    @Column(name = "price")
    private Double price;
}
