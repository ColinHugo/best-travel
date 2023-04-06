package com.curso.best.travel.infraestructure.helpers;

import com.curso.best.travel.domain.entity.*;
import com.curso.best.travel.domain.repositories.IReservationRepository;
import com.curso.best.travel.domain.repositories.ITickerRepository;
import com.curso.best.travel.infraestructure.services.ReservationService;
import com.curso.best.travel.infraestructure.services.TicketService;
import com.curso.best.travel.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Transactional
@Component
@AllArgsConstructor
public class TourHelper {

    private final ITickerRepository ticketRepository;
    private final IReservationRepository reservationRepository;

    public Set< TicketEntity > createTickets( Set< FlyEntity > flights, CustomerEntity customer ) {

        var response = new HashSet< TicketEntity >( flights.size() );

        flights.forEach( fly -> {

            var ticketToPersist = TicketEntity.builder()
                    .id(UUID.randomUUID())
                    .fly(fly)
                    .customer( customer )
                    .price(fly.getPrice().add(fly.getPrice().multiply( TicketService.CHARGER_PRICE_PERCETANGE ) ) )
                    .purchaseDate(LocalDate.now())
                    .departureDate( BestTravelUtil.getRandomSoon() )
                    .arrivalDate( BestTravelUtil.getRandomLatter() )
                    .arrivalDate( BestTravelUtil.getRandomLatter() )
                    .build();

            response.add( this.ticketRepository.save( ticketToPersist ) );
        });
        return response;
    }

    public Set< ReservationEntity > createReservations( HashMap< HotelEntity, Integer > hotels, CustomerEntity customer ) {

        var response = new HashSet< ReservationEntity >( hotels.size() );

        hotels.forEach( ( hotel, totalDays ) -> {

            var reservationToPersist = ReservationEntity.builder()
                    .id(UUID.randomUUID())
                    .hotel(hotel)
                    .customer( customer )
                    .totalDays( totalDays )
                    .dateTimeReservation(LocalDateTime.now() )
                    .dateStart( LocalDate.now() )
                    .dateEnd( LocalDate.now().plusDays( totalDays ) )
                    .price( hotel.getPrice().add( hotel.getPrice().multiply( ReservationService.CHARGES_PRICE_PERCENTAGE ) ) )
                    .build();

            response.add( this.reservationRepository.save( reservationToPersist ) );
        });

        return response;
    }

    public TicketEntity createTicket( FlyEntity fly, CustomerEntity customer ) {

        var ticketToPersist = TicketEntity.builder()
                .id( UUID.randomUUID() )
                .fly( fly )
                .customer( customer )
                .price( fly.getPrice().add( fly.getPrice().multiply( TicketService.CHARGER_PRICE_PERCETANGE ) ) )
                .purchaseDate( LocalDate.now() )
                .departureDate( BestTravelUtil.getRandomSoon() )
                .arrivalDate( BestTravelUtil.getRandomLatter() )
                .arrivalDate( BestTravelUtil.getRandomLatter() )
                .build();

        return this.ticketRepository.save( ticketToPersist );
    }

    public ReservationEntity createReservation( HotelEntity hotel, CustomerEntity customer, Integer totalDays ) {

        var reservationToPersist = ReservationEntity.builder()
                .id( UUID.randomUUID() )
                .hotel( hotel )
                .customer( customer )
                .totalDays( totalDays )
                .dateTimeReservation( LocalDateTime.now() )
                .dateStart( LocalDate.now() )
                .dateEnd( LocalDate.now().plusDays( totalDays ) )
                .price(hotel.getPrice().add( hotel.getPrice().multiply( ReservationService.CHARGES_PRICE_PERCENTAGE ) ) )
                .build();

        return this.reservationRepository.save( reservationToPersist );
    }

}
