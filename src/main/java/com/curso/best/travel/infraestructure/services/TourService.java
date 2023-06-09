package com.curso.best.travel.infraestructure.services;

import com.curso.best.travel.api.models.request.TourRequest;
import com.curso.best.travel.api.models.responses.TourResponse;
import com.curso.best.travel.domain.entity.*;
import com.curso.best.travel.domain.repositories.ICustomerRepository;
import com.curso.best.travel.domain.repositories.IFlyRepository;
import com.curso.best.travel.domain.repositories.IHotelRepository;
import com.curso.best.travel.domain.repositories.ITourRepository;
import com.curso.best.travel.infraestructure.abstract_services.ITourService;
import com.curso.best.travel.infraestructure.helpers.BlackListHelper;
import com.curso.best.travel.infraestructure.helpers.CustomerHelper;
import com.curso.best.travel.infraestructure.helpers.EmailHelper;
import com.curso.best.travel.infraestructure.helpers.TourHelper;
import com.curso.best.travel.util.enums.Tables;
import com.curso.best.travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class TourService implements ITourService {

    private final ITourRepository tourRepository;
    private final IFlyRepository flyRepository;
    private final IHotelRepository hotelRepository;
    private final ICustomerRepository customerRepository;
    private final TourHelper tourHelper;
    private final CustomerHelper customerHelper;
    private BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;

    @Override
    public TourResponse create( TourRequest request ) {

        blackListHelper.isInBlackListCustomer( request.getCustomerId() );

        var customer = customerRepository.findById( request.getCustomerId() )
                .orElseThrow( () -> new IdNotFoundException( Tables.CUSTOMER.getTableName() ) );

        var flights = new HashSet< FlyEntity >();
        request.getFlights().forEach( fly -> flights.add( this.flyRepository.findById( fly.getId() )
                .orElseThrow( () -> new IdNotFoundException( Tables.FLY.getTableName() ) ) ) );

        var hotels = new HashMap< HotelEntity, Integer >();
        request.getHotels().forEach( hotel -> hotels.put( this.hotelRepository.findById( hotel.getId() )
                .orElseThrow(), hotel.getTotalDays() ) );

        var tourToSave = TourEntity.builder()
                .tickets( this.tourHelper.createTickets( flights, customer ) )
                .reservations( this.tourHelper.createReservations( hotels, customer ) )
                .customer( customer )
                .build();

        var tourSaved = this.tourRepository.save( tourToSave );

        this.customerHelper.increase( customer.getDni(), TourService.class );

        if( Objects.nonNull( request.getEmail() ) ) {
            this.emailHelper.sendMail( request.getEmail(), customer.getFullName(), Tables.TOUR.getTableName() );
        }

        return TourResponse.builder()
                .reservationIds( tourSaved.getReservations().stream().map( ReservationEntity::getId ).collect( Collectors.toSet() ) )
                .ticketIds( tourSaved.getTickets().stream().map( TicketEntity::getId ).collect( Collectors.toSet() ) )
                .id( tourSaved.getId() )
                .build();
    }

    @Override
    public TourResponse read( Long id ) {
        var tourFromDb = this.tourRepository.findById( id )
                .orElseThrow( () -> new IdNotFoundException( Tables.TOUR.getTableName() ) );

        return TourResponse.builder()
                .reservationIds( tourFromDb.getReservations().stream().map( ReservationEntity::getId ).collect( Collectors.toSet() ) )
                .ticketIds( tourFromDb.getTickets().stream().map( TicketEntity::getId ).collect( Collectors.toSet() ) )
                .id( tourFromDb.getId() )
                .build();
    }

    @Override
    public void delete( Long id ) {
        var tourToDelete = this.tourRepository.findById( id )
                .orElseThrow( () -> new IdNotFoundException( Tables.TOUR.getTableName() ) );

        this.tourRepository.delete( tourToDelete );
    }

    @Override
    public void removeTicket( Long tourId, UUID ticketId )  {
        var tourUpdate = this.tourRepository.findById( tourId )
                .orElseThrow( () -> new IdNotFoundException( Tables.TOUR.getTableName() ) );
        tourUpdate.removeTicket( ticketId );

        this.tourRepository.save( tourUpdate );
    }

    @Override
    public UUID addTicket( Long tourId, Long flyId ) {

        var tourUpdate = this.tourRepository.findById( tourId )
                .orElseThrow( () -> new IdNotFoundException( Tables.TOUR.getTableName() ) );

        var fly = this.flyRepository.findById( flyId )
                .orElseThrow(() -> new IdNotFoundException( Tables.FLY.getTableName() ) );

        var ticket = this.tourHelper.createTicket( fly, tourUpdate.getCustomer() );

        tourUpdate.addTicket( ticket );
        this.tourRepository.save( tourUpdate );

        return ticket.getId();
    }

    @Override
    public void removeReservation(Long tourId, UUID reservationId) {

        var tourUpdate = this.tourRepository.findById( tourId )
                .orElseThrow( () -> new IdNotFoundException( Tables.TOUR.getTableName() ) );

        tourUpdate.removeReservation( reservationId );
        this.tourRepository.save( tourUpdate );
    }

    @Override
    public UUID addReservation( Long tourId, Long hotelId, Integer totalDays ) {

        var tourUpdate = this.tourRepository.findById( tourId )
                .orElseThrow( () -> new IdNotFoundException( Tables.TOUR.getTableName() ) );

        var hotel = this.hotelRepository.findById(hotelId)
                .orElseThrow( () -> new IdNotFoundException( Tables.HOTEL.getTableName() ) );

        var reservation = this.tourHelper.createReservation( hotel, tourUpdate.getCustomer(), totalDays );

        tourUpdate.addReservation( reservation );
        this.tourRepository.save( tourUpdate );

        return reservation.getId();
    }
}