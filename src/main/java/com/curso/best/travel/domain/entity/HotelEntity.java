package com.curso.best.travel.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity( name = "hotel")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HotelEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( length = 20 )
    private String name;

    @Column( length = 20 )
    private String address;

    private Integer rating;

    private BigDecimal price;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "hotel",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set< ReservationEntity > reservation;
}
