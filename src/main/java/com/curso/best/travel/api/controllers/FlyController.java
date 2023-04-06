package com.curso.best.travel.api.controllers;

import com.curso.best.travel.api.models.responses.FlyResponse;
import com.curso.best.travel.infraestructure.abstract_services.IFlyService;
import com.curso.best.travel.util.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping( "fly" )
@AllArgsConstructor
@Tag( name = "Fly" )
public class FlyController {

    private final IFlyService flyService;

    @Operation( summary = "Return a page with flights can be sorted or not" )
    @GetMapping
    public ResponseEntity< Page< FlyResponse > > getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader( required = false ) SortType sortType ) {

        if ( Objects.isNull( sortType ) ){
            sortType = SortType.NONE;
        }

        var response = this.flyService.readAll( page, size, sortType );

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok( response );
    }

    @Operation( summary = "Return a list with flights with price less to price in parameter" )
    @GetMapping( "less-price" )
    public ResponseEntity< Set< FlyResponse > > getLessPrice( @RequestParam BigDecimal price ) {

        var response = this.flyService.readLessPrice( price );

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok( response );
    }

    @Operation( summary = "Return a list with flights with between prices in parameters" )
    @GetMapping( "between-price" )
    public ResponseEntity< Set< FlyResponse > > getBetweenPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice ) {

        var response = this.flyService.readBetweenPrice( minPrice, maxPrice );

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok( response );
    }

    @Operation( summary = "Return a list with flights with between origin and destiny in parameters" )
    @GetMapping( "origin-destiny" )
    public ResponseEntity< Set< FlyResponse > > getByOriginDestiny(
            @RequestParam String origin,
            @RequestParam String destiny ) {

        var response = this.flyService.readByOriginDestiny( origin, destiny );

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok( response );
    }

}
