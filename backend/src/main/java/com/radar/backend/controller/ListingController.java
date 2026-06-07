package com.radar.backend.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radar.backend.model.dto.ListingDTO;
import com.radar.backend.model.dto.PriceSnapshotDTO;
import com.radar.backend.service.ListingService;

@RestController
@RequestMapping("api/listings")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @GetMapping
    public ResponseEntity<Page<ListingDTO>> getListings(
        @RequestParam(required = false) String district,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        Pageable pageable) {
        
        Page<ListingDTO> listingDto = listingService.getListings(district, minPrice, maxPrice, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listingDto);
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDTO> getListingDetail(@PathVariable Long id) {
        ListingDTO listingDTO = listingService.getListingDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(listingDTO);
    }

    @GetMapping("/{id}/price-history")
    public ResponseEntity<List<PriceSnapshotDTO>> getPriceHistory(@PathVariable Long id) {
        List<PriceSnapshotDTO> history = listingService.getPriceHistory(id);
        return ResponseEntity.status(HttpStatus.OK).body(history);
    }

}
