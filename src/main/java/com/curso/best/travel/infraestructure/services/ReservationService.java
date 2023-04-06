package com.curso.best.travel.infraestructure.services;

import com.curso.best.travel.api.models.request.ReservationRequest;
import com.curso.best.travel.api.models.responses.HotelResponse;
import com.curso.best.travel.api.models.responses.ReservationResponse;
import com.curso.best.travel.domain.entity.ReservationEntity;
import com.curso.best.travel.domain.repositories.ICustomerRepository;
import com.curso.best.travel.domain.repositories.IHotelRepository;
import com.curso.best.travel.domain.repositories.IReservationRepository;
import com.curso.best.travel.infraestructure.abstract_services.IReservationService;
import com.curso.best.travel.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.curso.best.travel.infraestructure.helpers.BlackListHelper;
import com.curso.best.travel.infraestructure.helpers.CustomerHelper;
import com.curso.best.travel.infraestructure.helpers.EmailHelper;
import com.curso.best.travel.util.enums.Tables;
import com.curso.best.travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {

    public static final BigDecimal CHARGES_PRICE_PERCENTAGE = BigDecimal.valueOf( .20 );

    private final IHotelRepository hotelRepository;
    private final ICustomerRepository customerRepository;
    private final IReservationRepository reservationRepository;
    private final CustomerHelper customerHelper;
    private BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper currencyConnectorHelper;
    private final EmailHelper emailHelper;

    @Override
    public ReservationResponse create( ReservationRequest request ) {

        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var hotel = this.hotelRepository.findById( request.getIdHotel() )
                .orElseThrow( () -> new IdNotFoundException( Tables.HOTEL.getTableName() ) );

        var customer = this.customerRepository.findById( request.getIdClient() )
                .orElseThrow( () -> new IdNotFoundException( Tables.CUSTOMER.getTableName() ) );

        var reservationToPersist = ReservationEntity.builder()
                .id( UUID.randomUUID() )
                .hotel( hotel )
                .customer( customer )
                .totalDays( request.getTotalDays() )
                .dateTimeReservation( LocalDateTime.now() )
                .dateStart( LocalDate.now() )
                .dateEnd( LocalDate.now().plusDays( request.getTotalDays() ) )
                .price(hotel.getPrice().add( hotel.getPrice().multiply( CHARGES_PRICE_PERCENTAGE ) ) )
                .build();

        var reservationPersisted = reservationRepository.save( reservationToPersist );

        this.customerHelper.increase( customer.getDni(), ReservationService.class );

        if( Objects.nonNull( request.getEmail() ) ) {
            this.emailHelper.sendMail( request.getEmail(), customer.getFullName(), Tables.RESERVATION.getTableName() );
        }

        return this.entityToResponse( reservationPersisted );
    }

    @Override
    public ReservationResponse read( UUID id ) {

        var reservationFromDB = this.reservationRepository.findById( id )
                .orElseThrow( () -> new IdNotFoundException( Tables.RESERVATION.getTableName() ) );

        return this.entityToResponse(reservationFromDB);
    }

    @Override
    public ReservationResponse update( ReservationRequest request, UUID id ) {

        var hotel = this.hotelRepository.findById( request.getIdHotel() )
                .orElseThrow( () -> new IdNotFoundException( Tables.HOTEL.getTableName() ) );

        var reservationToUpdate = this.reservationRepository.findById( id )
                .orElseThrow( () -> new IdNotFoundException( Tables.RESERVATION.getTableName() ) );

        reservationToUpdate.setHotel( hotel );
        reservationToUpdate.setTotalDays( request.getTotalDays() );
        reservationToUpdate.setDateTimeReservation( LocalDateTime.now() );
        reservationToUpdate.setDateStart( LocalDate.now() );
        reservationToUpdate.setDateEnd( LocalDate.now().plusDays( request.getTotalDays() ) );
        reservationToUpdate.setPrice( hotel.getPrice().add( hotel.getPrice().multiply( CHARGES_PRICE_PERCENTAGE ) ) );

        var reservationUpdated = this.reservationRepository.save( reservationToUpdate );
        log.info( "Reservation updated with id {}", reservationUpdated.getId() );

        return this.entityToResponse( reservationUpdated );
    }

    @Override
    public void delete( UUID id ) {
        var reservationToDelete = reservationRepository.findById( id )
                .orElseThrow( () -> new IdNotFoundException( Tables.RESERVATION.getTableName() ) );

        this.reservationRepository.delete( reservationToDelete );
    }

    @Override
    public BigDecimal findPrice( Long hotelId, Currency currency ) {

        var hotel = this.hotelRepository.findById( hotelId )
                .orElseThrow( () -> new IdNotFoundException( Tables.HOTEL.getTableName() ) );

        var priceInDollars = hotel.getPrice().add( hotel.getPrice().multiply( CHARGES_PRICE_PERCENTAGE ) );

        if ( currency.equals( Currency.getInstance( "USD" ) ) ){
            return priceInDollars;
        }

        var currencyDTO = this.currencyConnectorHelper.getCurrency( currency );

        log.info( "API Currency in {}, response: {}", currencyDTO.getExchangeDate().toString(), currencyDTO.getRates() );

        return priceInDollars.multiply( currencyDTO.getRates().get( currency ) );
    }

    private ReservationResponse entityToResponse( ReservationEntity entity ) {

        var response = new ReservationResponse();
        BeanUtils.copyProperties( entity, response );

        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties( entity.getHotel(), hotelResponse );

        response.setHotel( hotelResponse );

        return response;
    }
}
