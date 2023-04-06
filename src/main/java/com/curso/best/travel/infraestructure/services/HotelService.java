package com.curso.best.travel.infraestructure.services;

import com.curso.best.travel.api.models.responses.HotelResponse;
import com.curso.best.travel.domain.entity.HotelEntity;
import com.curso.best.travel.domain.repositories.IHotelRepository;
import com.curso.best.travel.infraestructure.abstract_services.IHotelService;
import com.curso.best.travel.util.constants.CacheConstants;
import com.curso.best.travel.util.enums.SortType;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional( readOnly = true )
@Service
@AllArgsConstructor
public class HotelService implements IHotelService {

    private final IHotelRepository hotelRepository;

    @Override
    public Page<HotelResponse> readAll( Integer page, Integer size, SortType sortType ) {

        PageRequest pageRequest = null;

        switch ( sortType ) {
            case NONE -> pageRequest = PageRequest.of( page, size );
            case LOWER -> pageRequest = PageRequest.of( page, size, Sort.by( FIELD_BY_SORT ).ascending() );
            case UPPER -> pageRequest = PageRequest.of( page, size, Sort.by( FIELD_BY_SORT ).descending() );
        }

        return this.hotelRepository.findAll( pageRequest ).map( this::entityToResponse );
    }

    @Cacheable( CacheConstants.HOTEL_CACHE_NAME )
    @Override
    public Set< HotelResponse > readLessPrice( BigDecimal price ) {
        return this.hotelRepository.findByPriceLessThan( price )
                .stream()
                .map( this::entityToResponse )
                .collect( Collectors.toSet() );
    }

    @Cacheable( CacheConstants.HOTEL_CACHE_NAME )
    @Override
    public Set< HotelResponse > readBetweenPrice( BigDecimal minPrice, BigDecimal maxPrice ) {
        return this.hotelRepository.findByPriceIsBetween( minPrice, maxPrice )
                .stream()
                .map( this::entityToResponse )
                .collect( Collectors.toSet() );
    }

    @Cacheable( CacheConstants.HOTEL_CACHE_NAME )
    @Override
    public Set<HotelResponse> readByRating( Integer rating ) {
        return this.hotelRepository.findByRatingGreaterThan( rating )
                .stream()
                .map( this::entityToResponse )
                .collect( Collectors.toSet() );
    }

    private HotelResponse entityToResponse( HotelEntity entity ) {

        HotelResponse response = new HotelResponse();
        BeanUtils.copyProperties( entity, response );

        return response;
    }
}
