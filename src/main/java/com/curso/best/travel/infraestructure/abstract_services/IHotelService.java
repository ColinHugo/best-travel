package com.curso.best.travel.infraestructure.abstract_services;

import com.curso.best.travel.api.models.responses.HotelResponse;

import java.util.Set;

public interface IHotelService extends ICatalogService< HotelResponse > {

    Set< HotelResponse > readByRating( Integer rating );
}
