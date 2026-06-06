package com.radar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.radar.backend.model.entity.Listing;
import java.util.List;
import java.util.Optional;


@Repository
public interface ListingRepo extends JpaRepository<Listing, Long> {
    
    List<Listing> findByDistrict(String district);

    Optional<Listing> findByUrl(String url);

    List<Listing> findByDistrictOrderByCreatedAtDesc(String district);

}
