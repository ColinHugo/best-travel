package com.curso.best.travel.api.controllers.error_handler;

import com.curso.best.travel.api.models.responses.BaseErrorResponse;
import com.curso.best.travel.api.models.responses.ErrorResponse;
import com.curso.best.travel.api.models.responses.ErrorsResponse;
import com.curso.best.travel.util.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@ResponseStatus( HttpStatus.BAD_REQUEST )
public class BadRequestController {

    // @ExceptionHandler( { IdNotFoundException.class, UsernameNotFoundException.class } )
    @ExceptionHandler( IdNotFoundException.class )
    public BaseErrorResponse handleIdNotFound( RuntimeException exception ) {

        return ErrorResponse.builder()
                .error( exception.getMessage() )
                .status( HttpStatus.BAD_REQUEST.name() )
                .code( HttpStatus.BAD_REQUEST.value() )
                .build();
    }

    @ExceptionHandler( MethodArgumentNotValidException.class )
    public BaseErrorResponse handleIdNotFound( MethodArgumentNotValidException exception ) {

        List< String > errors = new ArrayList<>();

        exception.getAllErrors().forEach( error -> errors.add( error.getDefaultMessage() ) );

        return ErrorsResponse.builder()
                .errors( errors )
                .status( HttpStatus.BAD_REQUEST.name() )
                .code( HttpStatus.BAD_REQUEST.value() )
                .build();
    }
}