package com.radar.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;

@Tag("wiremock")
public class CrawlerServiceTest {

    private static WireMockServer wireMock;
    private CrawlerService crawlerService;

    @BeforeAll
    static void startMockServer() {
        wireMock = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMock.start();
    }

    @AfterAll
    static void stopMockServer() {
        wireMock.stop();
    }

    @BeforeEach
    void setUp() {
        crawlerService = new CrawlerService();
        wireMock.resetAll();
    }

    private String mockServerUrl() {
        return "http://localhost:" + wireMock.port();
    }

    @Test
    @DisplayName("fetchPage - return Document and the Server return 200")
    void fetchPage_ShouldReturnDocument_WhenServerReturns200() {
        // Arrange.
        wireMock.stubFor(get(urlEqualTo("/rooms"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html; charset=UTF-8")
                        .withBody("<html><head><title>Phòng Trọ</title></head><body></body></html>")));

        // Act
        Document doc = crawlerService.fetchPage(mockServerUrl() + "/rooms");

        // Assert
        assertNotNull(doc);
        assertEquals(doc.title(), "Phòng Trọ");
    }

    @Test
    @DisplayName("fetchPage - retry when IOException and success the second time")
    void fetchPage_ShouldReturnDocument_WhenServerRetryTheSecondTime() {
        // Arange
        wireMock.stubFor(get(urlEqualTo("/retry-test"))
                .inScenario("Retry")
                .whenScenarioStateIs("Started")
                .willReturn(aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER))
                .willSetStateTo("FirstFail"));

        wireMock.stubFor(get(urlEqualTo("/retry-test"))
                .inScenario("Retry")
                .whenScenarioStateIs("FirstFail")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html; charset=UTF8")
                        .withBody("<html><head><title>Retry OK</title></head><body></body></html>")));

        // Act
        Document doc = crawlerService.fetchPage(mockServerUrl() + "/retry-test");

        // Assert
        assertNotNull(doc);
        assertEquals(doc.title(), "Retry OK");
    }

    @Test
    @DisplayName("parseListings - parse precisely fields from the HTML")
    void fetchPage_ShouldParsePrecisely() {
        // Arrange
        String html = """
                <html>
                    <body>
                        <div class="row gap-y-20">
                            <div>
                                <div class="hostel-item__title">
                                    <a href="/room/1">
                                        <h3>
                                            Phòng Quận 1.
                                        </h3>
                                    </a>
                                </div>

                                <div class="hostel-item__price--wrap">
                                    <span>
                                        5 triệu.
                                    </span>
                                </div>

                                <div>
                                    <div class="hostel-item__address">
                                        <p>
                                            Quận 1
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row gap-y-20">
                            <div>
                                <div class="hostel-item__title">
                                    <a href="/room/7">
                                        <h3>
                                            Phòng Quận 7.
                                        </h3>
                                    </a>
                                </div>

                                <div class="hostel-item__price--wrap">
                                    <span>
                                        10 triệu.
                                    </span>
                                </div>

                                <div>
                                    <div class="hostel-item__address">
                                        <p>
                                            Quận 7
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </body>
                </hmtl>
                """;
        
        wireMock.stubFor(get(urlEqualTo("/test-listings"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/html; charset=UTF-8")
                .withBody(html)));

        // Act
        Document document = crawlerService.fetchPage(mockServerUrl() + "/test-listings");
        int count = crawlerService.parseListing(
            document, 
            "div.row.gap-y-20 > div", 
            "div.hostel-item__title > a > h3", 
            "div.hostel-item__price--wrap > span", 
            "", 
            "div.hostel-item__address > p", 
            "div.hostel-item__title > a");

        assertEquals(count, 2);

    }

    @Test
    @DisplayName("fetchPage - skip the item without a title")
    void fetchPage_ShouldSkipTheItemWithoutATitle() {
        // Arrange
        String html = """
                <html>
                    <body>
                        <div class="row gap-y-20">
                            <div>

                                <div class="hostel-item__price--wrap">
                                    <span>
                                        5 triệu.
                                    </span>
                                </div>

                                <div>
                                    <div class="hostel-item__address">
                                        <p>
                                            Quận 1
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row gap-y-20">
                            <div>
                                <div class="hostel-item__title">
                                    <a href="/room/7">
                                        <h3>
                                            Phòng Quận 7.
                                        </h3>
                                    </a>
                                </div>

                                <div class="hostel-item__price--wrap">
                                    <span>
                                        10 triệu.
                                    </span>
                                </div>

                                <div>
                                    <div class="hostel-item__address">
                                        <p>
                                            Quận 7
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </body>
                </hmtl>
                """;
        
        wireMock.stubFor(get(urlEqualTo("/test-listings"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/html; charset=UTF-8")
                .withBody(html)));
        
        // Act
        Document document = crawlerService.fetchPage(mockServerUrl() + "/test-listings");
        int count = crawlerService.parseListing(
            document, 
            "div.row.gap-y-20 > div", 
            "div.hostel-item__title > a > h3", 
            "div.hostel-item__price--wrap > span", 
            "", 
            "div.hostel-item__address > p", 
            "div.hostel-item__title > a");
        
        // Assert
        assertEquals(count, 1);
    }

}
