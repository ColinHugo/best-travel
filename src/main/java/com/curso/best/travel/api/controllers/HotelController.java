package com.curso.best.travel.api.controllers;

import com.curso.best.travel.api.models.responses.HotelResponse;
import com.curso.best.travel.infraestructure.abstract_services.IHotelService;
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
@RequestMapping( "hotel" )
@AllArgsConstructor
@Tag( name = "Hotel" )
public class HotelController {

    private final IHotelService hotelService;

    @Operation( summary = "Return a page with hotels can be sorted or not" )

    @GetMapping
    public ResponseEntity< Page< HotelResponse > > getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader( required = false ) SortType sortType ) {

        if ( Objects.isNull( sortType ) ){
            sortType = SortType.NONE;
        }

        var response = this.hotelService.readAll( page, size, sortType );

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok( response );
    }

    @Operation( summary = "Return a list with hotels with price less to price in parameter" )
    @GetMapping( "less-price" )
    public ResponseEntity< Set< HotelResponse > > getLessPrice( @RequestParam BigDecimal price ) {

        var response = this.hotelService.readLessPrice( price );

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok( response );
    }

    @Operation( summary = "Return a list with hotels with between prices in parameters" )
    @GetMapping( "between-price" )
    public ResponseEntity< Set< HotelResponse > > getBetweenPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice ) {

        var response = this.hotelService.readBetweenPrice( minPrice, maxPrice );

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok( response );
    }

    @Operation( summary = "Return a list with hotels with ratting greater a parameter" )
    @GetMapping( "rating" )
    public ResponseEntity< Set< HotelResponse > > getByRating( @RequestParam Integer rating ) {

        if ( rating > 4 ) {
            rating = 4;
        }

        if ( rating < 1 ) {
            rating = 1;
        }

        var response = this.hotelService.readByRating( rating );

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok( response );
    }



}
