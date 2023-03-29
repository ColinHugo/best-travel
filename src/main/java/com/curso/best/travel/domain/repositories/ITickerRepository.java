package com.curso.best.travel.domain.repositories;

import com.curso.best.travel.domain.entity.TicketEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ITickerRepository extends CrudRepository< TicketEntity, UUID > {
}
