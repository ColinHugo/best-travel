package com.curso.best.travel.api.controllers.error_handler;

import com.curso.best.travel.api.models.responses.BaseErrorResponse;
import com.curso.best.travel.api.models.responses.ErrorResponse;
import com.curso.best.travel.util.exceptions.ForbiddenCustomerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus( HttpStatus.FORBIDDEN )
public class ForbiddenCustomerHandler {

    @ExceptionHandler( ForbiddenCustomerException.class )
    public BaseErrorResponse handleIdNotFound( ForbiddenCustomerException exception ) {

        return ErrorResponse.builder()
                .error( exception.getMessage() )
                .status( HttpStatus.FORBIDDEN.name() )
                .code( HttpStatus.FORBIDDEN.value() )
                .build();
    }
}