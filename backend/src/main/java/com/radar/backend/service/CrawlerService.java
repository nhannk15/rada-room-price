package com.radar.backend.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CrawlerService {

    private static final String USER_AGENT = "Mozila/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final int TIMEOUT_MS = 10_000;

    public Document fetchPage(String url) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MS)
                    .header("Accept", "text/html,application/xhtml+xml")
                    .header("Accept-Language", "vi-VN,vi;q=0.9")
                    .get();
            log.info("Fetched: {} - title: {}", url, document.title());
            return document;
        } catch (Exception ex) {
            log.error("Failed to fetch {} - {}", url, ex.getMessage());
            return null;
        }
    }

    public void parseListing(
            Document document,
            String containerSelector,
            String titleSelector,
            String priceSelector,
            String areaSelector,
            String districtSelector,
            String urlSelector) {
        if (document == null) {
            log.warn("Document is null, skip parsing");
            return;
        }

        Elements items = document.select(containerSelector);
        log.info("Found {} items (selectors: {})", items.size(), containerSelector);

        for(Element item: items) {
            String title = extractText(item, titleSelector);
            String price = extractText(item, priceSelector);
            String area = extractText(item, areaSelector);
            String district = extractText(item, districtSelector);
            String url = extractAttribute(item, urlSelector, "href");

            System.out.println("---------------------------------------------------------------");
            System.out.println("Title:      " + title);
            System.out.println("Price:      " + price);
            System.out.println("Area:       " + area);
            System.out.println("District:   " + district);
            System.out.println("Url:        " + url);

            System.out.println("---------------------------------------------------------------");
        }

    }

    public String extractText(Element parent, String selector) {
        Element element = parent.selectFirst(selector);
        return element != null ? element.text() : "N/A";
    }

    public String extractAttribute(Element parent, String selector, String attr) {
        Element element = parent.selectFirst(selector);
        return element != null ? element.attr(attr) : "N/A";
    }

}
