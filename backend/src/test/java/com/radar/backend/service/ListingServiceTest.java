package com.radar.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.radar.backend.model.entity.Listing;
import com.radar.backend.repository.ListingRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * Trigger Mockito for JUnit 5
 */
@ExtendWith(MockitoExtension.class)
public class ListingServiceTest {
    
    /**
     * Create a Mock Object
     */
    @Mock
    ListingRepo listingRepo;

    /**
     * Inject Mock into the real object we need to test.
     */
    @InjectMocks
    ListingService listingService;

    /**
     * - We use @Mock to test the business logic we wrote in service.
     * - We use @DataJptTest to test query we wrote in the Repository.
     * - JPA generate the query we didn't write, we don't need to test.
     */
    @Test
    void findById_shouldReturnListing() {
        // Given
        Listing mockListing = new Listing();
        mockListing.setId(1L);
        mockListing.setTitle("Phòng quận 1");
        when(listingRepo.findById(1L)).thenReturn(Optional.of(mockListing));

        // When
        Listing result = listingService.findById(1L);

        // Then
        assertEquals(result.getTitle(), "Phòng quận 1");
    }

}
