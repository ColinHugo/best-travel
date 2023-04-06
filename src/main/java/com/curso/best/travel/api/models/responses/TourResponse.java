package com.curso.best.travel.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourResponse {

    private Long id;
    private Set< UUID > ticketIds;
    private Set< UUID > reservationIds;

}
