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

@Slf4j
@Service
public class PhongTro123CrawlerService implements CrawlerService {

    private static final String PHONG_TRO_123 = "https://phongtro123.com/tinh-thanh/ho-chi-minh?page=";
    private static final String WEBSITE_NAME = "phongtro123.com";

    @Override
    public List<CrawledRoom> crawl() {
        List<CrawledRoom> crawledRooms = new ArrayList<>();
        for (int page = 1; page <= MAX_PAGE; page++) {
            int attemp = 1;
            for (attemp = 1; attemp <= MAX_RETRIES; attemp++) {
                String websiteUrl = PHONG_TRO_123 + page;
                try {
                    Document document = Jsoup.connect(websiteUrl)
                            .userAgent(USER_AGENT)
                            .get();
                    System.out.println("===============" + document.title() + "===============");
                    Elements elements = document.select("ul.post__listing > li");
                    for (Element element : elements) {
                        String title = element.select("h3 > a").text();
                        String price = element.select("span.text-green.fw-semibold.fs-6").text();
                        String area = element.select("div.mb-2 > span:nth-child(3)").text();
                        String district = element.select("div.mb-2 > a").text();
                        String url = element.select("h3 > a").attr("abs:href");

                        String cleanedPrice = price
                                .replace("triệu", "")
                                .replace("/tháng", "")
                                .replace("đồng", "")
                                .trim();
                        String cleanedArea = area
                                .substring(0, area.indexOf(" "));

                        BigDecimal finalPrice = BigDecimal.valueOf(Double.parseDouble(cleanedPrice));
                        if (price.contains("triệu")) {
                            finalPrice = finalPrice.multiply(BigDecimal.valueOf(1_000_000));
                        } else {
                            finalPrice = finalPrice.multiply(BigDecimal.valueOf(1_000));
                        }

                        CrawledRoom crawledRoom = CrawledRoom
                                .builder()
                                .title(title)
                                .price(finalPrice)
                                .area(BigDecimal.valueOf(Double.parseDouble(cleanedArea)))
                                .district(district)
                                .url(url)
                                .build();
                        
                        crawledRooms.add(crawledRoom);
                        log.info("Added to crawled rooms: ", crawledRoom.getTitle());
                    }
                    log.info("Successfully fetch {} data from {}", elements.size(), websiteUrl);
                    break;
                } catch (IOException e) {
                    log.warn("Attemp {}/{} fetching website", attemp, MAX_RETRIES);
                }
            }
            if (attemp == 3) {
                throw new RuntimeException("Server error fetching data from Website: " + WEBSITE_NAME);
            }
        }
        return crawledRooms;
    }

}
