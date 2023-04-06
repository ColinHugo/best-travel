package com.curso.best.travel.infraestructure.helpers;

import com.curso.best.travel.domain.repositories.ICustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@AllArgsConstructor
public class CustomerHelper {

    private final ICustomerRepository customerRepository;

    public void increase( String customerId, Class< ? > type ) {

        var customerToUpdate = this.customerRepository.findById( customerId ).orElseThrow();

        switch ( type.getSimpleName() ) {
            case "TourService" -> customerToUpdate.setTotalTours( customerToUpdate.getTotalTours() + 1 );
            case "TicketService" -> customerToUpdate.setTotalFlights( customerToUpdate.getTotalFlights() + 1 );
            case "ReservationService" -> customerToUpdate.setTotalLodgings( customerToUpdate.getTotalLodgings() + 1 );
        }

        this.customerRepository.save( customerToUpdate );
    }
}