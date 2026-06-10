package com.radar.backend.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.radar.backend.model.dto.PriceSnapshotDTO;
import com.radar.backend.model.entity.PriceSnapshot;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PriceSnapshotMapper {
    PriceSnapshotDTO toPriceSnapshotDTO(PriceSnapshot priceSnapshot);

    List<PriceSnapshotDTO> toPriceSnapshotDTOs(List<PriceSnapshot> priceSnapshots);
}
