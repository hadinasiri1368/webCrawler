package org.webCrawler.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.dto.*;
import org.webCrawler.service.MeetingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class API {

    @GetMapping(path = "/api/extraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            extraAssemblyDtos = meetingService1.getExtraAssemblyList("22");
            return extraAssemblyDtos;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                extraAssemblyDtos.addAll(meetingService1.getExtraAssemblyList("22"));
                fromDate = fromDate.plusDays(1L);
            }
            return extraAssemblyDtos;
        }
    }

    @GetMapping(path = "/api/summaryExtraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getSummaryExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            extraAssemblyDtos = meetingService1.getExtraAssemblyList("2222");
            return extraAssemblyDtos;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                extraAssemblyDtos.addAll(meetingService1.getExtraAssemblyList("2222"));
                fromDate = fromDate.plusDays(1L);
            }
            return extraAssemblyDtos;
        }
    }

    @GetMapping(path = "/api/annualSaheholderMeeting")
    public List<DecisionDto> getAnnualSaheholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            decisionDtos = meetingService1.getDecisionList("20");
            return decisionDtos;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                decisionDtos.addAll(meetingService1.getDecisionList("20"));
                fromDate = fromDate.plusDays(1L);
            }
            return decisionDtos;
        }
    }

    @GetMapping(path = "/api/capitalIncrease")
    public List<CapitalIncreaseDto> getCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<CapitalIncreaseDto> capitalIncreaseDtos = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            capitalIncreaseDtos = meetingService1.getCapitalIncrease();
            return capitalIncreaseDtos;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                capitalIncreaseDtos.addAll(meetingService1.getCapitalIncrease());
                fromDate = fromDate.plusDays(1L);
            }
            return capitalIncreaseDtos;
        }
    }

    @GetMapping(path = "/api/priorityOrBuyShare")
    public List<PriorityOrBuyShare> getPriorityOrBuyShare(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShare> priorityOrBuyShares = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            priorityOrBuyShares = meetingService1.getPriorityOrBuyShare("25");
            return priorityOrBuyShares;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                priorityOrBuyShares.addAll(meetingService1.getPriorityOrBuyShare("25"));
                fromDate = fromDate.plusDays(1L);
            }
            return priorityOrBuyShares;
        }
    }

    @GetMapping(path = "/api/postulateDiscussionShareholderMeeting")
    public List<PriorityOrBuyShare> postulateDiscussionShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShare> priorityOrBuyShares = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            priorityOrBuyShares = meetingService1.getPriorityOrBuyShare("27");
            return priorityOrBuyShares;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                priorityOrBuyShares.addAll(meetingService1.getPriorityOrBuyShare("27"));
                fromDate = fromDate.plusDays(1L);
            }
            return priorityOrBuyShares;
        }
    }

    @GetMapping(path = "/api/registerCapitalIncrease")
    public List<PriorityOrBuyShare> getRegisterCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShare> priorityOrBuyShares = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            priorityOrBuyShares = meetingService1.getPriorityOrBuyShare("28");
            return priorityOrBuyShares;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                priorityOrBuyShares.addAll(meetingService1.getPriorityOrBuyShare("28"));
                fromDate = fromDate.plusDays(1L);
            }
            return priorityOrBuyShares;
        }
    }

    @GetMapping(path = "/api/extraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            decisionDtos = meetingService1.getDecisionList("21");
            return decisionDtos;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                decisionDtos.addAll(meetingService1.getDecisionList("21"));
                fromDate = fromDate.plusDays(1L);
            }
            return decisionDtos;
        }
    }

    @GetMapping(path = "/api/summaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            decisionDtos = meetingService1.getDecisionList("2020");
            return decisionDtos;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                decisionDtos.addAll(meetingService1.getDecisionList("21"));
                fromDate = fromDate.plusDays(1L);
            }
            return decisionDtos;
        }
    }

    @GetMapping(path = "/api/summaryExtraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            decisionDtos = meetingService1.getDecisionList("2121");
            return decisionDtos;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                decisionDtos.addAll(meetingService1.getDecisionList("2121"));
                fromDate = fromDate.plusDays(1L);
            }
            return decisionDtos;
        }
    }

    @GetMapping(path = "/api/InterimStatementDto")
    public List<InterimStatementDto> getInterimStatementDto(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<InterimStatementDto> interimStatementDtos = new ArrayList<>();
        if (startDate.equals(endDate)) {
            MeetingService meetingService1 = new MeetingService(startDate);
            interimStatementDtos = meetingService1.getInterimStatementDto("6");
            return interimStatementDtos;
        } else {
            LocalDate fromDate = DateUtil.getGregorianDate(startDate);
            LocalDate toDate = DateUtil.getGregorianDate(endDate);
            while (fromDate.compareTo(toDate) <= 0) {

                MeetingService meetingService1 = new MeetingService(DateUtil.getJalaliDate(fromDate));
                interimStatementDtos.addAll(meetingService1.getInterimStatementDto("6"));
                fromDate = fromDate.plusDays(1L);
            }
            return interimStatementDtos;
        }
    }

    private void checkInputData(String startDate, String endDate) throws Exception {
        LocalDate fromDate = DateUtil.getGregorianDate(startDate);
        if (CommonUtils.isNull(fromDate))
            throw new RuntimeException("start.date.not.valid");
        LocalDate toDate = DateUtil.getGregorianDate(endDate);
        if (CommonUtils.isNull(toDate))
            throw new RuntimeException("end.date.not.valid");
        if (startDate.compareTo(endDate) > 0)
            throw new RuntimeException("start.date.must.bigger.than.end.date");
    }
}

