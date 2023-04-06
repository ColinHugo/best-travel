package com.curso.best.travel.api.controllers;

import com.curso.best.travel.api.models.request.TicketRequest;
import com.curso.best.travel.api.models.responses.ErrorsResponse;
import com.curso.best.travel.api.models.responses.TicketResponse;
import com.curso.best.travel.infraestructure.abstract_services.ITicketService;
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
@RequestMapping( "ticket" )
@AllArgsConstructor
@Tag( name = "Ticket" )
public class TicketController {

    private final ITicketService ticketService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content( mediaType = "application/json", schema = @Schema( implementation = ErrorsResponse.class ) )
            }
    )
    @Operation( summary = "Save in system un ticket with the fly passed in parameter" )
    @PostMapping
    public ResponseEntity< TicketResponse > post( @Valid @RequestBody TicketRequest ticketRequest ) {
        return ResponseEntity.ok( ticketService.create( ticketRequest ) );
    }

    @Operation( summary = "Return a ticket with of passed" )
    @GetMapping( "/{id}" )
    public ResponseEntity< TicketResponse > get( @PathVariable UUID id ) {
        return ResponseEntity.ok( ticketService.read( id ) );
    }

    @Operation( summary = "Update ticket" )
    @PutMapping( "/{id}" )
    public ResponseEntity< TicketResponse > put( @Valid @PathVariable UUID id, @RequestBody TicketRequest ticketRequest ) {
        return ResponseEntity.ok( ticketService.update( ticketRequest, id ) );
    }

    @Operation( summary = "Delete a ticket with of passed" )
    @DeleteMapping( "/{id}" )
    public ResponseEntity< Void > delete( @PathVariable UUID id ) {
        this.ticketService.delete( id );
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity< Map< String, BigDecimal > > getFlyPrice(
            @RequestParam Long flyId,
            @RequestHeader( required = false ) Currency currency ) {

        if ( Objects.isNull( currency ) ) {
            currency = Currency.getInstance("USD" );
        }

        return ResponseEntity.ok(Collections.singletonMap("flyPrice", this.ticketService.findPrice( flyId, currency ) ) );
    }
}