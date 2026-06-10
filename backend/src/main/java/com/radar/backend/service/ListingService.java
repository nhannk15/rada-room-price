package com.radar.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radar.backend.model.dto.CreateListingRequest;
import com.radar.backend.model.dto.ListingDTO;
import com.radar.backend.model.entity.Listing;
import com.radar.backend.model.entity.PriceSnapshot;
import com.radar.backend.model.mapper.ListingMapper;
import com.radar.backend.repository.ListingRepo;
import com.radar.backend.repository.PriceSnapshotRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ListingService {

    @Autowired
    private ListingRepo listingRepo;

    @Autowired
    private PriceSnapshotRepo priceSnapshotRepo;

    @Autowired
    private ListingMapper listingMapper;

    @Transactional(readOnly = true)
    public ListingDTO findById(Long id) {
        Listing listing = listingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found by id: " + id));
        return listingMapper.toListingDTO(listing);
    }

    @Transactional(readOnly = true)
    public Page<ListingDTO> findAllListing(Pageable pageable) {
        Page<Listing> allListings = listingRepo.findAll(pageable);
        return allListings.map(listingMapper::toListingDTO);
    }

    @Transactional
    public ListingDTO createNewListing(CreateListingRequest request) {

        Optional<Listing> optionalListing = listingRepo.findByUrl(request.getUrl());
        if (optionalListing.isPresent()) {
            Listing persistedListing = optionalListing.get();
            PriceSnapshot latestPriceSnapshot = priceSnapshotRepo
                    .findByListingIdOrderBySnapshotAtDesc(persistedListing.getId()).get(0);

            if (!(request.getPrice().compareTo(latestPriceSnapshot.getPrice()) == 0)) {

                PriceSnapshot newPriceSnapshot = PriceSnapshot
                        .builder()
                        .listing(persistedListing)
                        .price(request.getPrice())
                        .build();
                        priceSnapshotRepo.save(newPriceSnapshot);
            }
            ListingDTO response = listingMapper.toListingDTO(persistedListing);
            response.setLatestPrice(request.getPrice());
            return response;
        }

        Listing newListing = Listing
                .builder()
                .url(request.getUrl())
                .title(request.getTitle())
                .district(request.getDistrict())
                .area(request.getArea())
                .build();
        Listing savedListing = listingRepo.save(newListing);

        PriceSnapshot newPriceSnapshot = PriceSnapshot
                .builder()
                .listing(savedListing)
                .price(request.getPrice())
                .build();
        priceSnapshotRepo.save(newPriceSnapshot);
        ListingDTO response = listingMapper.toListingDTO(savedListing);
        response.setLatestPrice(newPriceSnapshot.getPrice());
        return response;
    }

}
