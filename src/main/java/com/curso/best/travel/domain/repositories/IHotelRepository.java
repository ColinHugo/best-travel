package com.curso.best.travel.domain.repositories;

import com.curso.best.travel.domain.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IHotelRepository extends JpaRepository< HotelEntity, Long > {

    Set< HotelEntity > findByPriceLessThan( BigDecimal price );
    Set< HotelEntity > findByPriceIsBetween( BigDecimal min, BigDecimal max );
    Set< HotelEntity > findByRatingGreaterThan( Integer rating );
    Optional< HotelEntity > findByReservationId( UUID id );
}
