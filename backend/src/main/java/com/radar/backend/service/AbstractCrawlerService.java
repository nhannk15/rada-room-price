package com.radar.backend.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.radar.backend.config.CrawlerConfig;
import com.radar.backend.model.dto.CrawledRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCrawlerService implements CrawlerService {

    protected final CrawlerConfig.Site siteConfig;
    protected final CrawlerConfig.Settings settings;

    public AbstractCrawlerService(CrawlerConfig.Site siteConfig, CrawlerConfig.Settings settings) {
        this.siteConfig = siteConfig;
        this.settings = settings;
    }

    @Override
    public List<CrawledRoom> crawl() {
        List<CrawledRoom> crawledRooms = new ArrayList<>();
        CrawlerConfig.Selectors selectors = siteConfig.getSelectors();

        for (int page = settings.getMinPage(); page <= settings.getMaxPage(); page++) {
            int attemp = 1;
            for (attemp = 1; attemp <= settings.getRetryMax(); attemp++) {
                String pageUrl = siteConfig.getBasedUrl() + "?page=" + page;
                try {
                    Document document = Jsoup.connect(pageUrl)
                            .userAgent(settings.getUserAgent())
                            .timeout(settings.getTimeOutMs())
                            .get();
                    log.info("Fetching page {}: {}", page, document.title());

                    for (Element element : document.select(selectors.getContainer())) {
                        try {
                            CrawledRoom crawledRoom = parseElement(element, selectors);
                            if (crawledRoom != null) {
                                crawledRooms.add(crawledRoom);
                            }
                        } catch (Exception ex) {
                            log.warn("Fail to parse element: " + ex.getMessage());
                        }
                    }
                    break;
                } catch (IOException ex) {
                    log.warn("Attemp {}/{} faild for {}: {}", attemp, settings.getRetryMax(), pageUrl, ex.getMessage());
                    if (attemp < settings.getRetryDelayMs()) {
                        try {
                            Thread.sleep(settings.getRetryDelayMs());
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                if (attemp > settings.getRetryMax()) {
                    throw new RuntimeException("Failed to fetch data from " + siteConfig.getBasedUrl());
                }
            }
        }
        return crawledRooms;
    }

    protected abstract CrawledRoom parseElement(Element element, CrawlerConfig.Selectors selectors);

    protected abstract BigDecimal parsePrice(String rawPrice);

    protected abstract BigDecimal parseArea(String rawArea);

}
