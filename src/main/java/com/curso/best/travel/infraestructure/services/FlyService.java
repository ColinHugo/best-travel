package com.curso.best.travel.infraestructure.services;

import com.curso.best.travel.api.models.responses.FlyResponse;
import com.curso.best.travel.domain.entity.FlyEntity;
import com.curso.best.travel.domain.repositories.IFlyRepository;
import com.curso.best.travel.infraestructure.abstract_services.IFlyService;
import com.curso.best.travel.util.constants.CacheConstants;
import com.curso.best.travel.util.enums.SortType;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional( readOnly = true )
@AllArgsConstructor
@Service
public class FlyService implements IFlyService {

    private final IFlyRepository flyRepository;
    // private final WebClient webClient;

    /* public FlyService(IFlyRepository flyRepository, @Qualifier( value = "base" ) WebClient webClient) {
        this.flyRepository = flyRepository;
        this.webClient = webClient;
    } */

    @Override
    public Page<FlyResponse> readAll( Integer page, Integer size, SortType sortType ) {

        PageRequest pageRequest = null;

        switch ( sortType ) {
            case NONE -> pageRequest = PageRequest.of( page, size );
            case LOWER -> pageRequest = PageRequest.of( page, size, Sort.by( FIELD_BY_SORT ).ascending() );
            case UPPER -> pageRequest = PageRequest.of( page, size, Sort.by( FIELD_BY_SORT ).descending() );
        }

        return this.flyRepository.findAll( pageRequest ).map( this::entityToResponse );
    }

    @Cacheable( CacheConstants.FLY_CACHE_NAME )
    @Override
    public Set<FlyResponse> readLessPrice( BigDecimal price ) {
        return this.flyRepository.selectLessPrice( price )
                .stream()
                .map( this::entityToResponse )
                .collect( Collectors.toSet() );
    }

    @Cacheable( CacheConstants.FLY_CACHE_NAME )
    @Override
    public Set<FlyResponse> readBetweenPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return this.flyRepository.selectBetweenPrice( minPrice, maxPrice )
                .stream()
                .map( this::entityToResponse )
                .collect( Collectors.toSet() );
    }

    @Cacheable( CacheConstants.FLY_CACHE_NAME )
    @Override
    public Set<FlyResponse> readByOriginDestiny( String origin, String destiny ) {
        return this.flyRepository.selectOriginDestiny( origin, destiny )
                .stream()
                .map( this::entityToResponse )
                .collect( Collectors.toSet() );
    }

    private FlyResponse entityToResponse( FlyEntity entity ) {

        FlyResponse response = new FlyResponse();
        BeanUtils.copyProperties( entity, response );

        return response;
    }
}
