package com.radar.backend.service;

import java.util.List;

import com.radar.backend.model.dto.CrawledRoom;

public interface CrawlerService {

    String USER_AGENT = "Mozila/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    int TIMEOUT_MS = 10_000;
    int MAX_RETRIES = 3;
    int RETRY_DELAY_MS = 2_000;
    int MAX_PAGE = 10;

    List<CrawledRoom> crawl();

}
