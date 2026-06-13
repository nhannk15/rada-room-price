package com.radar.backend.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListingPriceSnapshotDTO {
    
    private Long id;
    private String title;
    private BigDecimal area;
    private List<PriceSnapshotDTO> priceSnapshots;
    private String url;
    private String imageUrl;
    private String district;
    private LocalDateTime createdAt;

}
