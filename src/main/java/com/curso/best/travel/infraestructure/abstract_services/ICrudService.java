package com.curso.best.travel.infraestructure.abstract_services;

public interface ICrudService < Request, Response, Id >{

    Response create( Request request );
    Response read( Id id );
    Response update( Request request, Id id );
    void delete( Id id );

}