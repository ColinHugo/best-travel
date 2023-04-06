package com.curso.best.travel.infraestructure.abstract_services;

import com.curso.best.travel.api.models.request.TicketRequest;
import com.curso.best.travel.api.models.responses.TicketResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface ITicketService extends ICrudService< TicketRequest, TicketResponse, UUID >{

    BigDecimal findPrice( Long flyId, Currency currency );

}
