package se.ifmo.ru.firstservice.storage.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "coordinates")
@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "x")
    private Integer x;

    @Column(name = "y")
    private Float y;

}
