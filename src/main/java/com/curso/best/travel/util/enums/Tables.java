package com.curso.best.travel.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tables {

    CUSTOMER( "customer" ),
    FLY( "fly" ),
    HOTEL( "hotel" ),
    TICKET( "ticket" ),
    TOUR( "tour" ),
    RESERVATION( "reservation" );

    private final String tableName;

}
