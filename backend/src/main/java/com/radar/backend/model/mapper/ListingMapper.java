package com.radar.backend.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.radar.backend.model.dto.ListingDTO;
import com.radar.backend.model.dto.PriceSnapshotDTO;
import com.radar.backend.model.entity.Listing;
import com.radar.backend.model.entity.PriceSnapshot;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ListingMapper {

    @Mapping(target = "latestPrice", ignore = true)
    ListingDTO toListingDTO(Listing listing);

    PriceSnapshotDTO toPriceSnapshotDTO(PriceSnapshot priceSnapshot);
}
