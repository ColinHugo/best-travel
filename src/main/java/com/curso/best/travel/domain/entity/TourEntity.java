package com.curso.best.travel.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity( name = "tour")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "tour",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set< ReservationEntity > reservations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "tour",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set< TicketEntity > tickets;

    @ManyToOne
    @JoinColumn(
            name = "id_customer",
            nullable = true
    )
    private CustomerEntity customer;

    public void addTicket( TicketEntity ticket ) {

        if ( Objects.isNull( this.tickets ) ) {
            this.tickets = new HashSet<>();
        }

        this.tickets.add( ticket );
    }

    public void removeTicket( UUID id ) {

        if ( Objects.isNull( this.tickets ) ) {
            this.tickets = new HashSet<>();
        }

        this.tickets.removeIf( ticket -> ticket.getId().equals( id ) );
    }

    public void updateTickets() {
        this.tickets.forEach( ticket -> ticket.setTour( this ) );
    }

    public void addReservation( ReservationEntity reservation ) {
        if ( Objects.isNull( this.reservations ) ) {
            this.reservations = new HashSet<>();
        }

        this.reservations.add( reservation );
    }

    public void removeReservation( UUID idReservation ) {
        if ( Objects.isNull( this.reservations ) ) {
            this.reservations = new HashSet<>();
        }

        this.reservations.removeIf( r -> r.getId().equals( idReservation ) );
    }

    public void updateReservation() {
        this.reservations.forEach( r -> r.setTour( this ) );
    }
}