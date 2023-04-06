package com.curso.best.travel.infraestructure.abstract_services;

public interface ISimpleCrudService< Request, Response, Id > {

    Response create( Request request );

    Response read( Id id );

    void delete( Id id );

}
