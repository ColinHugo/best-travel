package com.curso.best.travel.domain.repositories;

import com.curso.best.travel.domain.entity.TourEntity;
import org.springframework.data.repository.CrudRepository;

public interface ITourRepository extends CrudRepository< TourEntity, Long > {
}
