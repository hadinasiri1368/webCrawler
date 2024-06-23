package org.webCrawler.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webCrawler.repository.MongoRepository;

@Service
public class TSEService {
    @Autowired
    private WebDriver webDriver;

    private String webUrl = "https://tse.ir/news/allNews?cat=announcement";




}
