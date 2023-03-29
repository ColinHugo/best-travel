package com.curso.best.travel.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity( name = "customer")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerEntity {

    @Id
    private String dni;

    @Column( length = 50 )
    private String fullName;

    @Column( length = 20 )
    private String creditCard;

    @Column( length = 12 )
    private String phoneNumber;

    private Integer totalFlights;

    private Integer totalLodgings;

    private Integer totalTours;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set< TicketEntity > tickets;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set< ReservationEntity > reservations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private Set< TourEntity > tours;
}
