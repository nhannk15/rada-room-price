package com.radar.backend.service;

import java.math.BigDecimal;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radar.backend.config.CrawlerConfig;
import com.radar.backend.config.CrawlerConfig.Selectors;
import com.radar.backend.model.dto.CrawledRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PhongTro123CrawlerService extends AbstractCrawlerService {

    @Autowired
    public PhongTro123CrawlerService(CrawlerConfig.Config config) {
        /**
         * Java < 25. You can't call the super() after any statements...
         */
        super(
                config.getSites().stream().filter(s -> s.getName().equals("phongtro123")).findFirst()
                        .orElseThrow(() -> new RuntimeException("phongtro123 confuguration not found")),
                config.getSettings());
    }

    @Override
    protected CrawledRoom parseElement(Element element, Selectors selectors) {
        String title = element.select(selectors.getTitle()).text();
        String rawPrice = element.select(selectors.getPrice()).text();
        String rawArea = element.select(selectors.getArea()).text();
        String district = element.select(selectors.getDistrict()).text();
        String url = element.select(selectors.getUrl()).attr("abs:href");

        BigDecimal price = parsePrice(rawPrice);
        BigDecimal area = parseArea(rawArea);

        CrawledRoom crawledRoom = CrawledRoom
                .builder()
                .title(title)
                .price(price)
                .area(area)
                .district(district)
                .url(url)
                .build();

        return crawledRoom;
    }

    @Override
    protected BigDecimal parsePrice(String rawPrice) {
        String cleanedPrice = rawPrice
                .replace("triệu", "")
                .replace("/tháng", "")
                .replace("đồng", "")
                .trim();

        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(cleanedPrice));
        if (rawPrice.contains("triệu")) {
            price = price.multiply(BigDecimal.valueOf(1_000_000L));
        } else if (rawPrice.contains("trăm")) {
            price = price.multiply(BigDecimal.valueOf(100_000L));
        }

        return price;
    }

    @Override
    protected BigDecimal parseArea(String rawArea) {
        if (rawArea == null || rawArea.trim().isBlank()) {
            return BigDecimal.ZERO;
        }

        String cleanedArea = rawArea.substring(0, rawArea.indexOf(" ")).trim();
        return BigDecimal.valueOf(Double.parseDouble(cleanedArea));
    }

}
