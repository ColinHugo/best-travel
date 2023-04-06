package com.curso.best.travel.infraestructure.services;

import com.curso.best.travel.api.models.request.TicketRequest;
import com.curso.best.travel.api.models.responses.FlyResponse;
import com.curso.best.travel.api.models.responses.TicketResponse;
import com.curso.best.travel.domain.entity.TicketEntity;
import com.curso.best.travel.domain.repositories.ICustomerRepository;
import com.curso.best.travel.domain.repositories.IFlyRepository;
import com.curso.best.travel.domain.repositories.ITickerRepository;
import com.curso.best.travel.infraestructure.abstract_services.ITicketService;
import com.curso.best.travel.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.curso.best.travel.infraestructure.helpers.BlackListHelper;
import com.curso.best.travel.infraestructure.helpers.CustomerHelper;
import com.curso.best.travel.infraestructure.helpers.EmailHelper;
import com.curso.best.travel.util.BestTravelUtil;
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
public class TicketService implements ITicketService {

    public static final BigDecimal CHARGER_PRICE_PERCETANGE = BigDecimal.valueOf( .25 );

    private final IFlyRepository flyRepository;
    private final ICustomerRepository customerRepository;
    private final ITickerRepository tickerRepository;
    private final CustomerHelper customerHelper;
    private BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper currencyConnectorHelper;
    private final EmailHelper emailHelper;

    @Override
    public TicketResponse create( TicketRequest ticketRequest ) {

        blackListHelper.isInBlackListCustomer( ticketRequest.getIdClient() );

        var fly = flyRepository.findById( ticketRequest.getIdFly() )
                .orElseThrow( () -> new IdNotFoundException( Tables.FLY.getTableName() ) );

        var customer = customerRepository.findById( ticketRequest.getIdClient() )
                .orElseThrow( () -> new IdNotFoundException( Tables.CUSTOMER.getTableName() ) );

        var ticketToPersist = TicketEntity.builder()
                .id( UUID.randomUUID() )
                .fly( fly )
                .customer( customer )
                .price( fly.getPrice().add( fly.getPrice().multiply( CHARGER_PRICE_PERCETANGE ) ) )
                .purchaseDate( LocalDate.now() )
                .departureDate( BestTravelUtil.getRandomSoon() )
                .arrivalDate( BestTravelUtil.getRandomLatter() )
                .build();

        var ticketPersisted = this.tickerRepository.save( ticketToPersist );

        customerHelper.increase( customer.getDni(), TicketService.class );

        if( Objects.nonNull( ticketRequest.getEmail() ) ) {
            this.emailHelper.sendMail( ticketRequest.getEmail(), customer.getFullName(), Tables.TICKET.getTableName() );
        }

        log.info( "Ticket saved with id: {}", ticketPersisted.getId() );

        return this.entityToResponse( ticketPersisted );
    }

    @Override
    public TicketResponse read( UUID id ) {

        var ticketFromDB = this.tickerRepository.findById( id )
                .orElseThrow( () -> new IdNotFoundException( Tables.TICKET.getTableName() ) );

        return this.entityToResponse( ticketFromDB );
    }

    @Override
    public TicketResponse update( TicketRequest ticketRequest, UUID id ) {

        var ticketToUpdate = tickerRepository.findById( id )
                .orElseThrow( () -> new IdNotFoundException( Tables.TICKET.getTableName() ) );

        var fly = flyRepository.findById( ticketRequest.getIdFly() )
                .orElseThrow( () -> new IdNotFoundException( Tables.FLY.getTableName() ) );

        ticketToUpdate.setFly( fly );
        ticketToUpdate.setPrice( fly.getPrice().add( fly.getPrice().multiply( CHARGER_PRICE_PERCETANGE ) ) );
        ticketToUpdate.setArrivalDate( BestTravelUtil.getRandomSoon() );
        ticketToUpdate.setDepartureDate( BestTravelUtil.getRandomLatter() );

        var ticketUpdated = this.tickerRepository.save( ticketToUpdate );
        log.info( "Ticket updated with id {} ", ticketUpdated.getId() );

        return this.entityToResponse( ticketUpdated );
    }

    @Override
    public void delete( UUID id ) {
        var ticketToDelete = tickerRepository.findById( id )
                .orElseThrow( () -> new IdNotFoundException( Tables.TICKET.getTableName() ) );
        this.tickerRepository.delete( ticketToDelete );
    }

    @Override
    public BigDecimal findPrice( Long flyId, Currency currency ) {

        var fly = this.flyRepository.findById( flyId )
                .orElseThrow( () -> new IdNotFoundException( Tables.FLY.getTableName() ) );
        if ( currency.equals( Currency.getInstance("USD" ) ) ) {
            return fly.getPrice().add( fly.getPrice().multiply( CHARGER_PRICE_PERCETANGE ) );
        }

        var currencyDTO = this.currencyConnectorHelper.getCurrency(currency);
        log.info("API currency in {}, response: {}", currencyDTO.getExchangeDate().toString(), currencyDTO.getRates());

        return fly.getPrice().add( fly.getPrice().multiply( CHARGER_PRICE_PERCETANGE ) )
                .multiply( currencyDTO.getRates().get( currency ) );
    }

    private TicketResponse entityToResponse( TicketEntity entity ) {

        var response = new TicketResponse();
        BeanUtils.copyProperties( entity, response );

        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties( entity.getFly(), flyResponse );

        response.setFly( flyResponse );

        return  response;
    }
}