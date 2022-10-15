package com.example.olxuzflatsscrapper.scraper;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

import com.example.olxuzflatsscrapper.dto.FlatDto;
import java.io.IOException;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FlatScraperTest {

    static MockWebServer mockTrustpilotBackEnd;

    @Autowired
    FlatScraper flatScraper;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) {
        r.add("olx.flats-path", () -> "http://localhost:" + mockTrustpilotBackEnd.getPort());
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockTrustpilotBackEnd = new MockWebServer();
        mockTrustpilotBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockTrustpilotBackEnd.shutdown();
    }

    @Test
    void getFlats() throws IOException {
        String path = "/mockwebserver/page1.html";
        String mockHtml = IOUtils.toString(requireNonNull(this.getClass().getResourceAsStream(path)), UTF_8);

        mockTrustpilotBackEnd.enqueue(new MockResponse()
            .setBody(mockHtml)
            .addHeader("Content-Type", "text/html; charset=utf-8"));

        List<FlatDto> flats = flatScraper.getFlats();
    }

}