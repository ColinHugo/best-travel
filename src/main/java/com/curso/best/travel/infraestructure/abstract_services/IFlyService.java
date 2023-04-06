package com.curso.best.travel.infraestructure.abstract_services;

import com.curso.best.travel.api.models.responses.FlyResponse;

import java.util.Set;

public interface IFlyService extends ICatalogService< FlyResponse > {

    Set< FlyResponse > readByOriginDestiny( String origin, String destiny );
}
