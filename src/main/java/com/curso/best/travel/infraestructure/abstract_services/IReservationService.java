package com.curso.best.travel.infraestructure.abstract_services;

import com.curso.best.travel.api.models.request.ReservationRequest;
import com.curso.best.travel.api.models.responses.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface IReservationService extends ICrudService< ReservationRequest, ReservationResponse, UUID > {

    BigDecimal findPrice(Long hotelId, Currency currency );

}
