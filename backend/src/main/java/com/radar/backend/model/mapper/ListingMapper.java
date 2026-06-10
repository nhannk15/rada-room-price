package com.radar.backend.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.radar.backend.model.dto.ListingDTO;
import com.radar.backend.model.dto.ListingPriceSnapshotDTO;
import com.radar.backend.model.entity.Listing;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ListingMapper {

    @Mapping(target = "latestPrice", ignore = true)
    ListingDTO toListingDTO(Listing listing);

    List<ListingDTO> toListingDTOs(List<Listing> listings);

    ListingPriceSnapshotDTO toListingPriceSnapshotDTO(Listing listing);

}
