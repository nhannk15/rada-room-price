package com.radar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radar.backend.model.dto.ListingDTO;
import com.radar.backend.service.ListingService;


@RestController
@RequestMapping("api/listings")
public class ListingController {

    private final ListingService listingService;

    @Autowired
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }
    
    @GetMapping
    public ResponseEntity<Page<ListingDTO>> getAllListing(
        @RequestParam(required = false) String district,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ListingDTO> responseBody = listingService.findAllListing(pageable);
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDTO> getListingDetail(@PathVariable Long id) {
        ListingDTO dto = listingService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/price-snapshots")
    public ResponseEntity<?> findByIdWithPriceSnapshots(@PathVariable Long id) {
        return ResponseEntity.ok().body(listingService.findByIdWithPriceSnapshots(id));
    }

}
