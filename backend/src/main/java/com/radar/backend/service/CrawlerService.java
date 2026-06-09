package com.radar.backend.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.radar.backend.model.dto.CrawledRoom;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CrawlerService {

    private static final String USER_AGENT = "Mozila/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final int TIMEOUT_MS = 10_000;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2_000;

    public Document fetchPage(String url) {
        for (int attemp = 1; attemp <= MAX_RETRIES; attemp++) {
            try {
                Document document = Jsoup.connect(url)
                        .userAgent(USER_AGENT)
                        .timeout(TIMEOUT_MS)
                        .header("Accept", "text/html,application/xhtml+xml")
                        .header("Accept-Language", "vi-VN,vi;q=0.9")
                        .get();
                log.info("Fetched: {} - title: {}", url, document.title());
                return document;
            } catch (IOException ex) {
                log.warn("Attemp {}/{} failed for {} - {}", attemp, MAX_RETRIES, url, ex.getMessage());

                if (attemp == MAX_RETRIES) {
                    log.error("All {} attemps failed for {}", MAX_RETRIES, url);
                }

                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }
        return null;
    }

    public List<CrawledRoom> parseListing(
            Document document,
            String containerSelector,
            String titleSelector,
            String priceSelector,
            String areaSelector,
            String districtSelector,
            String urlSelector) {
        if (document == null) {
            log.warn("DOCUMENT IS NULL, SKIP PARSING");
            return List.of();
        }

        List<CrawledRoom> crawledRooms = new ArrayList<>();

        Elements items = document.select(containerSelector);
        log.info("Found {} items (selectors: {})", items.size(), containerSelector);

        int count = 0;

        for (Element item : items) {
            String title = extractText(item, titleSelector);
            String price = extractText(item, priceSelector);
            String area = extractText(item, areaSelector);
            String district = extractText(item, districtSelector);
            String url = extractAttribute(item, urlSelector, "href");

            if ("N/A".equals(title) | "N/A".equals("url")) {
                continue;
            }
            ++count;

            price = price
                    .replace("triệu", "")
                    .replace("/", "")
                    .replace("tháng", "")
                    .trim();
            area = area.substring(0, area.indexOf(" "));

            CrawledRoom crawledRoom = new CrawledRoom(title, BigDecimal.valueOf(Double.parseDouble(price) * 1_000_000),
                    BigDecimal.valueOf(Double.parseDouble(area)),
                    district, url);
            crawledRooms.add(crawledRoom);

            System.out.println("---------------------------------------------------------------");
            System.out.println("Title:      " + crawledRoom.getTitle());
            System.out.println("Price:      " + crawledRoom.getPrice());
            System.out.println("Area:       " + crawledRoom.getArea());
            System.out.println("District:   " + crawledRoom.getDistrict());
            System.out.println("Url:        " + crawledRoom.getUrl());
            System.out.println("---------------------------------------------------------------");
        }

        log.info("SUCCESSULLY PARSE {} VALID LISTINGS", count);
        return crawledRooms;
    }

    public String extractText(Element parent, String selector) {
        if (selector == null || selector.isBlank())
            return "N/A";
        Element element = parent.selectFirst(selector);
        return element != null ? element.text() : "N/A";
    }

    public String extractAttribute(Element parent, String selector, String attr) {
        Element element = parent.selectFirst(selector);
        return element != null ? element.attr("abs:" + attr) : "N/A";
    }

}
