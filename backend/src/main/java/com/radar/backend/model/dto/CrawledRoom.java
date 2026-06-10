package com.radar.backend.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CrawledRoom {

    private String title;
    private BigDecimal price;
    private BigDecimal area;
    private String district;
    private String url;

}
