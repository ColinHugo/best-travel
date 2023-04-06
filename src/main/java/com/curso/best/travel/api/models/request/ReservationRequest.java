package com.curso.best.travel.api.models.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReservationRequest {

    @Size( min = 18, max = 20, message = "The size have to length between 18 and 20 characters" )
    @NotBlank( message = "Id client is mandatory" )
    private String idClient;

    @Positive
    @NotNull( message = "Id hotel is mandatory" )
    private Long idHotel;

    @Min( value = 1, message = "Min one day to make reservation" )
    @Max( value = 30, message = "Max 30 days to make reservation" )
    @NotNull( message = "Total days is mandatory" )
    private Integer totalDays;

    @Email( message = "Invalid email" )
    private String email;
}
