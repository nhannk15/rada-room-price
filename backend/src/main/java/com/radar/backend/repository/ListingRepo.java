package com.radar.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.radar.backend.model.entity.Listing;
import java.util.List;
import java.util.Optional;

public interface ListingRepo extends JpaRepository<Listing, Long> {
    
    Page<Listing> findByDistrict(String district, Pageable pageable);

    Optional<Listing> findByUrl(String url);

    List<Listing> findByDistrictOrderByCreatedAtDesc(String district);

}
