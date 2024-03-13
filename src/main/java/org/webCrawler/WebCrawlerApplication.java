package org.webCrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WebCrawlerApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(WebCrawlerApplication.class, args);
    }

}
