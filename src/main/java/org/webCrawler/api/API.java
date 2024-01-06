package org.webCrawler.api;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.dto.CapitalIncreaseDto;
import org.webCrawler.dto.DecisionDto;
import org.webCrawler.dto.ExtraAssemblyDto;
import org.webCrawler.dto.PriorityOrBuyShare;
import org.webCrawler.service.MeetingService;

import java.util.List;

@RestController
public class API {

    @GetMapping(path = "/api/extraAssembly")
    public List<ExtraAssemblyDto> getExtraAssemblyList(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = meetingService1.getExtraAssemblyList();
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/decision")
    public List<DecisionDto> getDecisionList(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<DecisionDto> decisionDtos = meetingService1.getDecisionList();
        return decisionDtos;
    }

    @GetMapping(path = "/api/capitalIncrease")
    public List<CapitalIncreaseDto> getCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<CapitalIncreaseDto> capitalIncreaseDtos = meetingService1.getCapitalIncrease();
        return capitalIncreaseDtos;
    }

    @GetMapping(path = "/api/priorityOrBuyShare")
    public List<PriorityOrBuyShare> getPriorityOrBuyShare(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<PriorityOrBuyShare> priorityOrBuyShares = meetingService1.getPriorityOrBuyShare();
        return priorityOrBuyShares;
    }
}
