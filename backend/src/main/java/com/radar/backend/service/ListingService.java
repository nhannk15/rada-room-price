package com.radar.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radar.backend.model.dto.CreateListingRequest;
import com.radar.backend.model.dto.ListingDTO;
import com.radar.backend.model.dto.PriceSnapshotDTO;
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

    @Transactional
    public Listing addListing(Listing listing) {
        log.info("adding listing in database");
        Listing savedListing = listingRepo.save(listing);
        return savedListing;
    }

    @Transactional(readOnly = true) //--- Faster reading
    public Listing findById(Long id) {
        log.info("find by id: {}", id);
        Listing foundListing = listingRepo.findById(id).orElseThrow(() -> new RuntimeException("Can't find"));
        return foundListing;
    }

    @Transactional(readOnly = true) //--- Faster reading
    public Page<ListingDTO> getListings(String district, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Listing> listingPage;
        if (district != null && !district.isBlank()) {
            listingPage = listingRepo.findByDistrict(district, pageable);
        } else {
            listingPage = listingRepo.findAll(pageable);
        }
        return listingPage.map(this::toDTOWithLatestPrice);
    }

    @Transactional(readOnly = true)
    public ListingDTO getListingDetail(Long id) {
        Listing listing = findById(id);
        return toDTOWithLatestPrice(listing);
    }

    @Transactional(readOnly = true)
    public List<PriceSnapshotDTO> getPriceHistory(Long listingId) {
        List<PriceSnapshot> priceSnapshots = priceSnapshotRepo.findByListingIdOrderBySnapshotAtDesc(listingId);
        return priceSnapshots.stream()
                .map(listingMapper::toPriceSnapshotDTO)
                .collect(Collectors.toList());
    }

    /**
     * Helper method
     */
    public ListingDTO toDTOWithLatestPrice(Listing listing) {
        ListingDTO dto = listingMapper.toListingDTO(listing);
        List<PriceSnapshot> snapshots = priceSnapshotRepo.findByListingIdOrderBySnapshotAtDesc(listing.getId());
        if (!snapshots.isEmpty()) {
            dto.setLatestPrice(snapshots.get(0).getPrice());
        }
        return dto;
    }

    @Transactional
    public ListingDTO createListing(CreateListingRequest createListingRequest) {
        Listing newListing = Listing
                .builder()
                .url(createListingRequest.getUrl())
                .title(createListingRequest.getTitle())
                .district(createListingRequest.getDistrict())
                .area(createListingRequest.getArea())
                .build();

        Listing savedListing = listingRepo.save(newListing);

        PriceSnapshot priceSnapshot = PriceSnapshot
                .builder()
                .listing(savedListing)
                .price(createListingRequest.getPrice())
                .snapshotAt(LocalDateTime.now())
                .build();
        priceSnapshotRepo.save(priceSnapshot);

        return listingMapper.toListingDTO(savedListing);
        
    }

}
