package com.radar.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radar.backend.model.entity.Listing;
import com.radar.backend.repository.ListingRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ListingService {
    
    @Autowired
    private ListingRepo listingRepo;

    public Listing addListing(Listing listing) {
        log.info("adding listing in database");
        Listing savedListing = listingRepo.save(listing);
        return savedListing;
    }

    public Listing findById(Long id) {
        log.info("find by id: {}", id);
        Listing foundListing = listingRepo.findById(id).orElseThrow(() -> new RuntimeException("Can't find"));
        return foundListing;
    }

}
