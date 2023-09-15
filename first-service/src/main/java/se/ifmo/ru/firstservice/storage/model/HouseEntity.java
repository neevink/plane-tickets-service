package se.ifmo.ru.firstservice.storage.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "house")
@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private Long year;

    @Column(name = "number_of_floors")
    private Integer numberOfFloors;
}
