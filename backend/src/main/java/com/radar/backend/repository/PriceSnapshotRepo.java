package com.radar.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.radar.backend.model.entity.PriceSnapshot;

public interface PriceSnapshotRepo extends JpaRepository<PriceSnapshot, Long> {
    
    List<PriceSnapshot> findByListingIdOrderBySnapshotAtDesc(Long listingId);

}
