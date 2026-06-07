package com.radar.backend.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.radar.backend.model.entity.Listing;

@Disabled
@DataJpaTest
@TestPropertySource(properties = { "spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=create-drop" })
public class ListingRepoTest {

    // @Autowired
    // private ListingRepo listingRepo;

    // private Listing q1Listing;
    // private Listing q7Listing;

    // @BeforeEach
    // void setup() {
    //     /**
    //      * Arange: create sample data.
    //      */
    //     q1Listing = new Listing(
    //             "https://example.com/room-q1",
    //             "Phòng đẹp quận 1",
    //             "Quận 1",
    //             new BigDecimal("25.5"));

    //     q7Listing = new Listing(
    //             "https://example.com/room-q7",
    //             "Phòng đẹp quận 7",
    //             "Quận 7",
    //             new BigDecimal("30.0"));
    //     listingRepo.save(q1Listing);
    //     listingRepo.save(q7Listing);
    // }

    // @Test
    // @DisplayName("findByDistrict - Return Listing")
    // void findByDistrict_ShouldReturnMatchingLists() {

    //     /**
    //      * Act: trigger actions.
    //      */
    //     List<Listing> result = listingRepo.findByDistrict("Quận 1");

    //     /**
    //      * Assert: test.
    //      */
    //     assertEquals(1, result.size());
    //     assertEquals(result.get(0).getDistrict(), "Quận 1");
    //     assertEquals(result.get(0).getTitle(), "Phòng đẹp quận 1");
    // }

    // @Test
    // @DisplayName("findByDistrict - Empty")
    // void findByUrl_ShoudReturnEmpty_WhenNoMatch() {
    //     /**
    //      * Act
    //      */
    //     List<Listing> result = listingRepo.findByDistrict("Quận 99");

    //     /**
    //      * Assert
    //      */
    //     assertTrue(result.isEmpty());
    // }

    // @Test
    // @DisplayName("findByUrl - return Listing")
    // void findByUrl_ShouldReturnMatchingListing() {
    //     /**
    //      * Act.
    //      */
    //     Optional<Listing> result = listingRepo.findByUrl("https://example.com/room-q1");

    //     /**
    //      * Assert.
    //      */
    //     assertTrue(result.isPresent());
    //     assertEquals(result.get().getTitle(), "Phòng đẹp quận 1");
    // }

    // @Test
    // @DisplayName("findByUrl - return empty")
    // void findByUrl_ShouldReturnEmpty() {
    //     /**
    //      * Act.
    //      */
    //     Optional<Listing> result = listingRepo.findByUrl("https://example.com/room-q99");

    //     /**
    //      * Assert
    //      */
    //     assertTrue(result.isEmpty());
    // }

    // @Test
    // @DisplayName("findAll - shouldReturnAllListing")
    // void findAll_ShouldReturnAllListing() {
    //     /**
    //      * Act
    //      */
    //     List<Listing> result = listingRepo.findAll();

    //     /**
    //      * Assert
    //      */
    //     assertEquals(result.size(), 2);
    // }

    // @Test
    // @DisplayName("addListing - shouldPersistNewListingSuccessfully")
    // void addNewListing_ShouldReturnNewPersistedListing() {
    //     /**
    //      * Act
    //      */
    //     Listing binhThanhListing = new Listing("https://example.com/room-qbinhthanh", "Phòng đẹp quận Bình Thạnh",
    //             "Quận Bình Thạnh", new BigDecimal("35.5"));
    //     Listing saved = listingRepo.save(binhThanhListing);

    //     /**
    //      * Assert
    //      */
    //     assertNotNull(saved.getId());
    //     assertEquals(saved.getTitle(), binhThanhListing.getTitle());

    //     /**
    //      * Verify
    //      */
    //     Optional<Listing> found = listingRepo.findById(saved.getId());
    //     assertTrue(found.isPresent());
    // }

    /**
     * 
     * Setup data giả cho mock.
     * when(listingRepo.findById(1L)).thenReturn(Optional.of(mockListing));
     * 
     * Đảm bảo method được gọi đúng số lần.
     * verify(listingRepo, times(1)).save(any());
     * verify(emailService, never()).sendAlert(any());
     * 
     * Test các trường hợp lỗi / exception.
     * assertThrows(RuntimeException.class, () -> listingService.findById(99L));
     * 
     * Kiểm tra object được truyền vào có đúng không.
     * verify(listingRepo).save(listingCaptor.capture());
     * assertEquals("Quận 1", listingCaptor.getValue().getDistrict());
     * 
     */
}
