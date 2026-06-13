package com.radar.backend.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import lombok.Data;

@Configuration
public class CrawlerConfig {

    @Bean
    public Config crawlerConfigBean() throws IOException {
        Yaml yaml = new Yaml(new Constructor(Config.class, new LoaderOptions()));
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("crawler-config.yaml")) {
            return yaml.load(is);
        }
    }

    @Data
    public static class Config {
        private List<Site> sites;
        private Settings settings;
    }

    @Data
    public static class Site {
        private String name;
        private String basedUrl;
        private boolean enabled;
        private Selectors selectors;
    }

    @Data
    public static class Selectors {
        private String container;
        private String title;
        private String price;
        private String area;
        private String district;
        private String url;
    }

    @Data
    public static class Settings {
        private String userAgent;
        private int timeOutMs;
        private int retryMax;
        private int retryDelayMs;
        private int minPage;
        private int maxPage;
    }

}
