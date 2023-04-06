package com.curso.best.travel.infraestructure.abstract_services;

import com.curso.best.travel.api.models.request.TourRequest;
import com.curso.best.travel.api.models.responses.TourResponse;

import java.util.UUID;

public interface ITourService extends ISimpleCrudService< TourRequest, TourResponse, Long > {

    void removeTicket( Long tourId, UUID ticketId );

    UUID addTicket( Long flyId, Long tourId );

    void removeReservation( Long tourId, UUID reservationId );

    UUID addReservation( Long flyId, Long tourId, Integer totalDays );
}
