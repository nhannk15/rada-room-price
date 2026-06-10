package com.radar.backend.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.radar.backend.model.dto.CrawledRoom;
import com.radar.backend.model.dto.CreateListingRequest;
import com.radar.backend.service.CrawlerService;
import com.radar.backend.service.ListingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CrawlerScheduler {

    @Autowired
    private ListingService listingService;

    /**
     * Strategy Pattern.
     */
    @Autowired
    private List<CrawlerService> crawlerServices;

    // @Scheduled(cron = "*/45 * * * * *")
    public void scheduledCrawl() {
        for (CrawlerService crawlerService: crawlerServices) {
            try {
                List<CrawledRoom> crawledRooms = crawlerService.crawl();
                for (CrawledRoom crawledRoom: crawledRooms) {
                    CreateListingRequest request = CreateListingRequest
                    .builder()
                    .url(crawledRoom.getUrl())
                    .title(crawledRoom.getTitle())
                    .district(crawledRoom.getDistrict())
                    .price(crawledRoom.getPrice())
                    .area(crawledRoom.getArea())
                    .build();

                    listingService.createNewListing(request);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
