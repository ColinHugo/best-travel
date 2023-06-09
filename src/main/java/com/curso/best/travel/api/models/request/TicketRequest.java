package com.curso.best.travel.api.models.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketRequest {

    @Size( min = 18, max = 20, message = "The size have to a length between 18 and 20 characters" )
    @NotBlank( message = "Id client is mandatory" )
    private String idClient;

    @Positive
    @NotNull( message = "Id ticket is mandatory" )
    private Long idFly;

    @Email( message = "Invalid email" )
    private String email;
}
