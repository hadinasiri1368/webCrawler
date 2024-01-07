package org.webCrawler.api;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.dto.*;
import org.webCrawler.service.MeetingService;

import java.util.List;

@RestController
public class API {

    @GetMapping(path = "/api/extraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = meetingService1.getExtraAssemblyList("22");
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/summaryExtraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getSummaryExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = meetingService1.getExtraAssemblyList("2222");
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/annualSaheholderMeeting")
    public List<DecisionDto> getAnnualSaheholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<DecisionDto> decisionDtos = meetingService1.getDecisionList("20");
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
        List<PriorityOrBuyShare> priorityOrBuyShares = meetingService1.getPriorityOrBuyShare("25");
        return priorityOrBuyShares;
    }

    @GetMapping(path = "/api/postulateDiscussionShareholderMeeting")
    public List<PriorityOrBuyShare> postulateDiscussionShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<PriorityOrBuyShare> priorityOrBuyShares = meetingService1.getPriorityOrBuyShare("27");
        return priorityOrBuyShares;
    }

    @GetMapping(path = "/api/registerCapitalIncrease")
    public List<PriorityOrBuyShare> getRegisterCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<PriorityOrBuyShare> priorityOrBuyShares = meetingService1.getPriorityOrBuyShare("28");
        return priorityOrBuyShares;
    }

    @GetMapping(path = "/api/extraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<DecisionDto> decisionDtos = meetingService1.getDecisionList("21");
        return decisionDtos;
    }

    @GetMapping(path = "/api/summaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<DecisionDto> decisionDtos = meetingService1.getDecisionList("2020");
        return decisionDtos;
    }

    @GetMapping(path = "/api/summaryExtraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<DecisionDto> decisionDtos = meetingService1.getDecisionList("2121");
        return decisionDtos;
    }

    @GetMapping(path = "/api/InterimStatementDto")
    public List<InterimStatementDto> getInterimStatementDto(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        MeetingService meetingService1 = new MeetingService(startDate);
        List<InterimStatementDto> interimStatementDtos = meetingService1.getInterimStatementDto("6");
        return interimStatementDtos;
    }
    }

