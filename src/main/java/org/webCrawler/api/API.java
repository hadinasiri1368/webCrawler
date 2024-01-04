package org.webCrawler.api;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.dto.ExtraAssemblyDto;
import org.webCrawler.service.MeetingService;

import java.util.List;

@RestController
public class API {

    @GetMapping(path = "/api/doAction")
    public List<ExtraAssemblyDto> doAction() throws Exception {
        try {
            MeetingService meetingService1 = new MeetingService("1402/10/11");
            List<ExtraAssemblyDto> extraAssemblyDtos = meetingService1.getExtraAssemblyList();
            return extraAssemblyDtos;
        } catch (Exception e) {
            throw e;
        }
    }
}
