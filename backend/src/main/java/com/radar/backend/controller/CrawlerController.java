package com.radar.backend.controller;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radar.backend.service.CrawlerService;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {
    
    private final CrawlerService crawlerService;

    @Autowired
    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/test")
    public String testCrawl(@RequestParam String url) {
        Document doc = crawlerService.fetchPage(url);

        if (doc == null) {
            return "Fail to fetched";
        }

        int count = crawlerService.parseListing(
            doc, 
            "ul.post__listing > li", 
            "h3 > a > span", 
            "span.text-green.fw-semibold.fs-6", 
            "div.mb-2 > span:nth-child(3)", 
            "div.mb-2 > a", 
            "h3 > a");

        return "Fetched: " + url + " - parsed " + count + " listings. Check console for details";
    }

}
