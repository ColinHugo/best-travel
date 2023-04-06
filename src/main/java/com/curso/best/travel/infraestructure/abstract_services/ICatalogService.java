package com.curso.best.travel.infraestructure.abstract_services;

import com.curso.best.travel.util.enums.SortType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Set;

public interface ICatalogService < Response >{

    String FIELD_BY_SORT = "price";

    Page< Response > readAll(Integer page, Integer size, SortType sortType );

    Set< Response > readLessPrice( BigDecimal price );

    Set< Response > readBetweenPrice( BigDecimal minPrice, BigDecimal maxPrice );
}
