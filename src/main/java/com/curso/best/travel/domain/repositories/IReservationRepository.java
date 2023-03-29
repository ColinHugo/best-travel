package com.curso.best.travel.domain.repositories;

import com.curso.best.travel.domain.entity.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IReservationRepository extends CrudRepository< ReservationEntity, UUID> {
}
