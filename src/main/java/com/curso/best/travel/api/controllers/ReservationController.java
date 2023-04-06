package com.curso.best.travel.api.controllers;

import com.curso.best.travel.api.models.request.ReservationRequest;
import com.curso.best.travel.api.models.responses.ErrorsResponse;
import com.curso.best.travel.api.models.responses.ReservationResponse;
import com.curso.best.travel.infraestructure.abstract_services.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping( "reservation" )
@AllArgsConstructor
@Tag( name = "Reservation" )
public class ReservationController {

    private final IReservationService reservationService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content( mediaType = "application/json", schema = @Schema( implementation = ErrorsResponse.class ) )
            }
    )
    @Operation( summary = "Save in system un reservation with the fly passed in parameter" )
    @PostMapping
    public ResponseEntity< ReservationResponse > post( @Valid @RequestBody ReservationRequest request ) {
        return ResponseEntity.ok( reservationService.create( request ) );
    }

    /* public ResponseEntity< ReservationResponse > get( @PathVariable @NotNull  @Min( 32 ) String id ) {
        var UUIDValid = UUID.fromString(id);
        return ResponseEntity.ok(reservationService.read(UUIDValid));
    } */

    @Operation( summary = "Return a reservation with of passed" )
    @GetMapping( "/{id}" )
    public ResponseEntity< ReservationResponse > get( @PathVariable UUID id ) {
        return ResponseEntity.ok( reservationService.read( id ) );
    }

    @Operation( summary = "Update reservation" )
    @PutMapping( "/{id}" )
    public ResponseEntity< ReservationResponse > put( @Valid @PathVariable UUID id, @RequestBody ReservationRequest reservationRequest ) {
        return ResponseEntity.ok( reservationService.update( reservationRequest, id ) );
    }

    @Operation( summary = "Delete a reservation with of passed" )
    @DeleteMapping( "/{id}" )
    public ResponseEntity< Void > delete( @PathVariable UUID id ) {
        this.reservationService.delete( id );
        return ResponseEntity.noContent().build();
    }

    @Operation( summary = "Return a reservation price given a hotel id" )
    @GetMapping
    public ResponseEntity< Map< String, BigDecimal > > getReservationPrice(
            @RequestParam Long hotelId,
            @RequestHeader Currency currency ) {

        if ( Objects.isNull( currency ) ) {
            currency = Currency.getInstance( "USD" );
        }

        return ResponseEntity.ok(
                Collections.singletonMap( "ticketPrice", this.reservationService.findPrice( hotelId, currency ) ) );
    }
}
