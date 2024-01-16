package org.webCrawler.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.dto.*;
import org.webCrawler.service.GenericService;
import org.webCrawler.service.InstrumentService;
import org.webCrawler.service.MeetingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//1402/07/14
@RestController
public class API {

    @Autowired
    GenericService<ExtraAssemblyDto> extraAssembl;

    @Autowired
    GenericService<DecisionDto> decision;

    @Autowired
    GenericService<CapitalIncreaseDto> capitalIncrease;

    @Autowired
    GenericService<PriorityOrBuyShareDto> priorityOrBuyShare;
    @Autowired
    GenericService<InterimStatementDto> InterimStatement;

    @GetMapping(path = "/api/extraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        extraAssemblyDtos = meetingService1.getExtraAssemblyList("22");
        for (ExtraAssemblyDto item : extraAssemblyDtos) {
            extraAssembl.add(item);
        }
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/summaryExtraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getSummaryExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        extraAssemblyDtos.addAll(meetingService1.getExtraAssemblyList("2222"));
        for (ExtraAssemblyDto item : extraAssemblyDtos) {
            extraAssembl.add(item);
        }
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/annualSaheholderMeeting")
    public List<DecisionDto> getAnnualSaheholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        decisionDtos.addAll(meetingService1.getDecisionList("20"));
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/capitalIncrease")
    public List<CapitalIncreaseDto> getCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<CapitalIncreaseDto> capitalIncreaseDtos = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        capitalIncreaseDtos.addAll(meetingService1.getCapitalIncrease());
        for (CapitalIncreaseDto item : capitalIncreaseDtos) {
            capitalIncrease.add(item);
        }
        return capitalIncreaseDtos;
    }

    @GetMapping(path = "/api/priorityOrBuyShare")
    public List<PriorityOrBuyShareDto> getPriorityOrBuyShare(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShares = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        priorityOrBuyShares.addAll(meetingService1.getPriorityOrBuyShare("25"));
        for (PriorityOrBuyShareDto item : priorityOrBuyShares) {
            priorityOrBuyShare.add(item);
        }
        return priorityOrBuyShares;
    }

    @GetMapping(path = "/api/postulateDiscussionShareholderMeeting")
    public List<PriorityOrBuyShareDto> postulateDiscussionShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShares = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        priorityOrBuyShares.addAll(meetingService1.getPriorityOrBuyShare("27"));
        for (PriorityOrBuyShareDto item : priorityOrBuyShares) {
            priorityOrBuyShare.add(item);
        }
        return priorityOrBuyShares;
    }

    @GetMapping(path = "/api/registerCapitalIncrease")
    public List<PriorityOrBuyShareDto> getRegisterCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShares = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        priorityOrBuyShares.addAll(meetingService1.getPriorityOrBuyShare("28"));
        for (PriorityOrBuyShareDto item : priorityOrBuyShares) {
            priorityOrBuyShare.add(item);
        }
        return priorityOrBuyShares;
    }

    @GetMapping(path = "/api/extraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        decisionDtos.addAll(meetingService1.getDecisionList("21"));
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/summaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        decisionDtos.addAll(meetingService1.getDecisionList("21"));
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/summaryExtraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        decisionDtos.addAll(meetingService1.getDecisionList("2121"));
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/interimStatementDto")
    public List<InterimStatementDto> getInterimStatementDto(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<InterimStatementDto> interimStatementDtos = new ArrayList<>();
        MeetingService meetingService1 = new MeetingService(startDate, endDate);
        interimStatementDtos.addAll(meetingService1.getInterimStatementDto("6"));
        for (InterimStatementDto item : interimStatementDtos) {
            InterimStatement.add(item);
        }
        return interimStatementDtos;
    }

    @GetMapping(path = "/api/instrumentInfo")
    public InstrumentInfo getInstrumentInfo(@RequestParam("bourseAccount") String bourseAccount) throws Exception {
        InstrumentInfo instrumentInfo = new InstrumentInfo();
        InstrumentService instrumentService = new InstrumentService(bourseAccount);
        instrumentInfo = instrumentService.getInstrumentInfo();
        return instrumentInfo;
    }

    private void checkInputData(String startDate, String endDate) throws Exception {
        LocalDate fromDate = DateUtil.getGregorianDate(startDate);
        if (CommonUtils.isNull(fromDate)) throw new RuntimeException("start.date.not.valid");
        LocalDate toDate = DateUtil.getGregorianDate(endDate);
        if (CommonUtils.isNull(toDate)) throw new RuntimeException("end.date.not.valid");
        if (startDate.compareTo(endDate) > 0) throw new RuntimeException("start.date.must.bigger.than.end.date");
    }
}

