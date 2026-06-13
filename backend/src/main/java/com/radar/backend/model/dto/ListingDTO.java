package com.radar.backend.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ListingDTO {

    private Long id;
    private String url;
    private String imageUrl;
    private String title;
    private String district;
    private BigDecimal area;
    private LocalDateTime createdAt;
    private BigDecimal latestPrice;

}
