package com.curso.best.travel.infraestructure.helpers;

import com.curso.best.travel.util.exceptions.ForbiddenCustomerException;
import org.springframework.stereotype.Component;

@Component
public class BlackListHelper {

    public void isInBlackListCustomer( String customerId ) {
        if ( customerId.equals( "GOTW771012HMRGR087" ) ) {
            throw new ForbiddenCustomerException();
        }
    }

}
