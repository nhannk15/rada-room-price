package com.radar.backend.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.jsoup.nodes.Document;
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

    private static final String PHONG_TRO_123 = "https://phongtro123.com/tinh-thanh/ho-chi-minh";

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private ListingService listingService;

    /**
     * Production: runs at 3 a.m everyday.
     * Dev/Test: every 60s
     */
    @Scheduled(cron = "*/30 * * * * *")
    public void scheduledCrawl() {
        LocalDateTime now = LocalDateTime.now();
        String formatedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        log.info("=== SCHEDULED CRAWLER {} STARTED ===", formatedDate);

        try {
            Document document = crawlerService.fetchPage(PHONG_TRO_123);
            if (document != null) {
                List<CrawledRoom> rooms = crawlerService.parseListing(
                        document,
                        "ul.post__listing > li",
                        "h3 > a",
                        "span.text-green.fw-semibold.fs-6",
                        "div.mb-2 > span:nth-child(3)",
                        "div.mb-2 > a",
                        "h3 > a");
                for (CrawledRoom crawledRoom : rooms) {
                    try {
                        CreateListingRequest request = CreateListingRequest
                            .builder()
                            .url(crawledRoom.getUrl())
                            .title(crawledRoom.getTitle())
                            .district(crawledRoom.getDistrict())
                            .price(crawledRoom.getPrice())
                            .area(crawledRoom.getArea())
                            .build();
                    listingService.createListing(request);
                    } catch (Exception ex) {
                        log.warn("Skip listing {} for {}", crawledRoom.getTitle(), ex.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            log.error("SCHEDULED FAILED: {}", ex.getMessage(), ex);
        }
        log.info("=== SCHEDULED CRAWL FINISHED ===");
    }

}
