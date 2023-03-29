package com.curso.best.travel.domain.repositories;

import com.curso.best.travel.domain.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerRepository extends CrudRepository< CustomerEntity, String > {
}
