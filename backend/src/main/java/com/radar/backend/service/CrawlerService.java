package com.radar.backend.service;

import java.util.List;

import com.radar.backend.model.dto.CrawledRoom;

public interface CrawlerService {   

    List<CrawledRoom> crawl();

}
