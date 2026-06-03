package com.radar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radar.backend.model.entity.Listing;

@Repository
public interface ListingRepo extends JpaRepository<Listing, Long> {
    
}
