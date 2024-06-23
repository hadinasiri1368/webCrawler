package org.webCrawler.api.Codal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.common.LettersTypes;
import org.webCrawler.dto.CapitalIncreaseDto;
import org.webCrawler.dto.DecisionDto;
import org.webCrawler.dto.ExtraAssemblyDto;
import org.webCrawler.dto.PriorityOrBuyShareDto;
import org.webCrawler.model.CodalShareholderMeeting;
import org.webCrawler.service.JPAGenericService;
import org.webCrawler.service.MeetingService;
import org.webCrawler.service.MongoGenericService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ShareholderMeetingAPI {
    @Autowired
    MeetingService meetingService;
    @Autowired
    MongoGenericService<ExtraAssemblyDto> extraAssembly;
    @Autowired
    JPAGenericService<CodalShareholderMeeting> codalShareholderMeetingGenericService;
    @Autowired
    MongoGenericService<DecisionDto> decision;
    @Autowired
    MongoGenericService<CapitalIncreaseDto> capitalIncrease;
    @Autowired
    MongoGenericService<PriorityOrBuyShareDto> priorityOrBuyShare;

    @GetMapping(path = "/api/extraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        extraAssemblyDtos = meetingService.getExtraAssemblyList(LettersTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (ExtraAssemblyDto item : extraAssemblyDtos) {
            extraAssembly.add(item);
        }
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/summaryExtraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getSummaryExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        extraAssemblyDtos = meetingService.getExtraAssemblyList(LettersTypes.SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (ExtraAssemblyDto item : extraAssemblyDtos) {
            extraAssembly.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING));
        }
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/annualSaheholderMeeting")
    public List<DecisionDto> getAnnualSaheholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        decisionDtos = meetingService.getDecisionList(LettersTypes.ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.ANNUAL_SAHEHOLDER_MEETING));
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/capitalIncrease")
    public List<CapitalIncreaseDto> getCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<CapitalIncreaseDto> capitalIncreaseDtos = new ArrayList<>();
        capitalIncreaseDtos = meetingService.getCapitalIncrease(LettersTypes.CAPITAL_INCREASE_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (CapitalIncreaseDto item : capitalIncreaseDtos) {
            capitalIncrease.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.CAPITAL_INCREASE_SAHEHOLDER_MEETING));
        }
        return capitalIncreaseDtos;
    }

    @GetMapping(path = "/api/priorityOrBuyShare")
    public List<PriorityOrBuyShareDto> getPriorityOrBuyShare(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShareDtos = new ArrayList<>();
        priorityOrBuyShareDtos = meetingService.getPriorityOrBuyShare(LettersTypes.PRIORITYTIME_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (PriorityOrBuyShareDto item : priorityOrBuyShareDtos) {
            priorityOrBuyShare.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.PRIORITYTIME_SAHEHOLDER_MEETING));
        }
        return priorityOrBuyShareDtos;
    }

    @GetMapping(path = "/api/postulateDiscussionShareholderMeeting")
    public List<PriorityOrBuyShareDto> postulateDiscussionShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShareDtos = new ArrayList<>();
        priorityOrBuyShareDtos = meetingService.getPriorityOrBuyShare(LettersTypes.POSTULATEDISCUSSION_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (PriorityOrBuyShareDto item : priorityOrBuyShareDtos) {
            priorityOrBuyShare.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.POSTULATEDISCUSSION_SAHEHOLDER_MEETING));
        }
        return priorityOrBuyShareDtos;
    }

    @GetMapping(path = "/api/registerCapitalIncrease")
    public List<PriorityOrBuyShareDto> getRegisterCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShareDtos = new ArrayList<>();
        priorityOrBuyShareDtos = meetingService.getPriorityOrBuyShare(LettersTypes.CAPITAL_INCREASE_REGISTRATION.getLettersTypeValue().toString(), startDate, endDate);
        for (PriorityOrBuyShareDto item : priorityOrBuyShareDtos) {
            priorityOrBuyShare.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.CAPITAL_INCREASE_REGISTRATION));
        }
        return priorityOrBuyShareDtos;
    }

    @GetMapping(path = "/api/extraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        decisionDtos = meetingService.getDecisionList(LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING));
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/summaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        decisionDtos = meetingService.getDecisionList(LettersTypes.EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING));
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/summaryExtraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        CommonUtils.checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        decisionDtos = meetingService.getDecisionList(LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue().toString(), startDate, endDate);
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING));
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/ExtraAssemblyShareholderMeetingTheoreticalPrice")
    public List<CodalShareholderMeeting> getCodalShareholderMeeting() throws Exception {
        return meetingService.saveCodalShareHolderMeeting();
    }


}
