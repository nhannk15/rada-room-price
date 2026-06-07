package com.radar.backend.scheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.radar.backend.service.CrawlerService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CrawlerScheduler {

    private static final String PHONG_TRO_123 = "https://phongtro123.com/tinh-thanh/ho-chi-minh";

    @Autowired
    private CrawlerService crawlerService;

    /**
     * Production: runs at 3 a.m everyday.
     * Dev/Test: every 60s
     */
    @Scheduled(cron = "*/5 * * * * *")
    public void scheduledCrawl() {
        LocalDateTime now = LocalDateTime.now();
        String formatedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        log.info("=== SCHEDULED CRAWLER DAY {} STARTED ===", formatedDate);
        try {
            Document document = crawlerService.fetchPage(PHONG_TRO_123);
            if (document != null) {
                int count = crawlerService.parseListing(
                    document, 
            "ul.post__listing > li", 
            "h3 > a", 
            "span.text-green.fw-semibold.fs-6", 
            "div.mb-2 > span:nth-child(3)", 
            "div.mb-2 > a", 
            "h3 > a");
                
                log.info("SCHEDULED CRAWL dons: {} listing from {}", count, PHONG_TRO_123);
            }
        } catch (Exception ex) {
            log.error("SCHEDULED FAILED: {}", ex.getMessage());
        }
        log.info("=== SCHEDULED CRAWL FINISHED ===");
    }

}
