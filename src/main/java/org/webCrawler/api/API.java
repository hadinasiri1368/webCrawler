package org.webCrawler.api;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.common.LettersTypes;
import org.webCrawler.dto.*;
import org.webCrawler.model.*;
import org.webCrawler.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//1402/07/14
@RestController
public class API {

    @Autowired
    MongoGenericService<ExtraAssemblyDto> extraAssembl;
    @Autowired
    MongoGenericService<DecisionDto> decision;
    @Autowired
    MongoGenericService<CapitalIncreaseDto> capitalIncrease;
    @Autowired
    MongoGenericService<PriorityOrBuyShareDto> priorityOrBuyShare;
    @Autowired
    MongoGenericService<InterimStatementDto> interimStatementService;
    @Autowired
    MongoGenericService<InstrumentInfo> instrumentInfoService;
    @Autowired
    MongoGenericService<MarketStatusDto> marketStatusService;
    @Autowired
    MongoGenericService<MarketStatusPerBourseAccountDto> marketStatusPerBourseAccount;
    @Autowired
    MongoGenericService<InstrumentDto> instrumentDtoMongoGenericService;
    @Autowired
    MongoGenericService<InstrumentId> instrumentId;
    @Autowired
    MongoGenericService<InstrumentData> instrumentData;
    @Autowired
    MongoGenericService<Trades> Trades;
    @Autowired
    JPAGenericService<CodalShareholderMeeting> codalShareholderMeetingGenericService;
    @Autowired
    JPAGenericService<Instrument> instrumentJPAGenericService;
    @Autowired
    TSETMCService tsetmcService;
    @Autowired
    MeetingService meetingService;

    @GetMapping(path = "/api/extraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        extraAssemblyDtos = meetingService.getExtraAssemblyList(LettersTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        CodalShareholderMeeting codalShareholderMeeting = new CodalShareholderMeeting();
        for (ExtraAssemblyDto item : extraAssemblyDtos) {
            extraAssembl.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING));
        }
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/summaryExtraAssemblyShareholderMeeting")
    public List<ExtraAssemblyDto> getSummaryExtraAssemblyShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        extraAssemblyDtos = meetingService.getExtraAssemblyList(LettersTypes.SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        for (ExtraAssemblyDto item : extraAssemblyDtos) {
            extraAssembl.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING));
        }
        return extraAssemblyDtos;
    }

    @GetMapping(path = "/api/annualSaheholderMeeting")
    public List<DecisionDto> getAnnualSaheholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        decisionDtos = meetingService.getDecisionList(LettersTypes.ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.ANNUAL_SAHEHOLDER_MEETING));
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/capitalIncrease")
    public List<CapitalIncreaseDto> getCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<CapitalIncreaseDto> capitalIncreaseDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        capitalIncreaseDtos = meetingService.getCapitalIncrease(LettersTypes.CAPITAL_INCREASE_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        for (CapitalIncreaseDto item : capitalIncreaseDtos) {
            capitalIncrease.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.CAPITAL_INCREASE_SAHEHOLDER_MEETING));
        }
        return capitalIncreaseDtos;
    }

    @GetMapping(path = "/api/priorityOrBuyShare")
    public List<PriorityOrBuyShareDto> getPriorityOrBuyShare(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShareDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        priorityOrBuyShareDtos = meetingService.getPriorityOrBuyShare(LettersTypes.PRIORITYTIME_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        for (PriorityOrBuyShareDto item : priorityOrBuyShareDtos) {
            priorityOrBuyShare.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.PRIORITYTIME_SAHEHOLDER_MEETING));
        }
        return priorityOrBuyShareDtos;
    }

    @GetMapping(path = "/api/postulateDiscussionShareholderMeeting")
    public List<PriorityOrBuyShareDto> postulateDiscussionShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShareDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        priorityOrBuyShareDtos = meetingService.getPriorityOrBuyShare(LettersTypes.POSTULATEDISCUSSION_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        for (PriorityOrBuyShareDto item : priorityOrBuyShareDtos) {
            priorityOrBuyShare.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.POSTULATEDISCUSSION_SAHEHOLDER_MEETING));
        }
        return priorityOrBuyShareDtos;
    }

    @GetMapping(path = "/api/registerCapitalIncrease")
    public List<PriorityOrBuyShareDto> getRegisterCapitalIncrease(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<PriorityOrBuyShareDto> priorityOrBuyShareDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        priorityOrBuyShareDtos = meetingService.getPriorityOrBuyShare(LettersTypes.CAPITAL_INCREASE_REGISTRATION.getLettersTypeValue().toString());
        for (PriorityOrBuyShareDto item : priorityOrBuyShareDtos) {
            priorityOrBuyShare.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.CAPITAL_INCREASE_REGISTRATION));
        }
        return priorityOrBuyShareDtos;
    }

    @GetMapping(path = "/api/extraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        decisionDtos = meetingService.getDecisionList(LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING));
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/summaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        decisionDtos = meetingService.getDecisionList(LettersTypes.EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING));
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/summaryExtraordinaryAnnualShareholderMeeting")
    public List<DecisionDto> getSummaryExtraordinaryAnnualShareholderMeeting(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<DecisionDto> decisionDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        decisionDtos = meetingService.getDecisionList(LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue().toString());
        for (DecisionDto item : decisionDtos) {
            decision.add(item);
            codalShareholderMeetingGenericService.insert(CommonUtils.convertTo(item, LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING));
        }
        return decisionDtos;
    }

    @GetMapping(path = "/api/interimStatementDto")
    public List<InterimStatementDto> getInterimStatementDto(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws Exception {
        checkInputData(startDate, endDate);
        List<InterimStatementDto> interimStatementDtos = new ArrayList<>();
        meetingService.setMeetingService(startDate, endDate);
        interimStatementDtos.addAll(meetingService.getInterimStatementDto("6"));
        for (InterimStatementDto item : interimStatementDtos) {
            interimStatementService.add(item);
        }
        return interimStatementDtos;
    }

    @GetMapping(path = "/api/instrumentInfo")
    public InstrumentInfo getInstrumentInfo(@RequestParam("bourseAccount") String bourseAccount) throws Exception {
        InstrumentInfo instrumentInfo = new InstrumentInfo();
        InstrumentService instrumentService = new InstrumentService(bourseAccount);
        instrumentInfo = instrumentService.getInstrumentInfo();
        instrumentInfoService.add(instrumentInfo);
        return instrumentInfo;
    }

    @GetMapping(path = "/api/marketStatus")
    public MarketStatusDto getMarketStatus() throws Exception {
        MarketStatusDto marketStatusDto = new MarketStatusDto();
        InstrumentService instrumentService = new InstrumentService();
        marketStatusDto = instrumentService.getMarketStatusDto();
        marketStatusService.add(marketStatusDto);
        return marketStatusDto;
    }

    @GetMapping(path = "/api/marketStatusPerBourseAccountGroup")
    public List<MarketStatusPerBourseAccountDto> getMarketStatusPerBourseAccount() throws Exception {
        List<MarketStatusPerBourseAccountDto> marketStatusDto = new ArrayList<>();
        InstrumentService instrumentService = new InstrumentService();
        marketStatusDto = instrumentService.getMarketStatusPerBourseAccountDto();
        for (MarketStatusPerBourseAccountDto marketStatusPerBourseAccountDto : marketStatusDto) {
            marketStatusPerBourseAccount.add(marketStatusPerBourseAccountDto);
        }
        return marketStatusDto;
    }

    @GetMapping(path = "/api/getInstrument")
    public List<InstrumentDto> getInstrument() throws Exception {
        List<InstrumentDto> instrumentDtos = new ArrayList<>();
        List<InstrumentId> instrumentIds = new ArrayList<>();
        TSETMCService tsetmcService = new TSETMCService();
        instrumentDtos = tsetmcService.getInstrument();
        for (InstrumentDto instrumentDto : instrumentDtos) {
            instrumentDtoMongoGenericService.add(instrumentDto);
        }
        return instrumentDtos;
    }

    @GetMapping(path = "/api/getInstrumentIds")
    public List<InstrumentId> getInstrumentIds() throws Exception {
        List<InstrumentId> instrumentIds = new ArrayList<>();
        TSETMCService tsetmcService = new TSETMCService();
        instrumentIds = tsetmcService.getInstrumentIds(instrumentDtoMongoGenericService.findAll(InstrumentDto.class));
        for (InstrumentId item : instrumentIds) {
            instrumentId.add(item);
        }
        return instrumentIds;
    }

    @GetMapping(path = "/api/getInstrumentData")
    public List<InstrumentData> getInstrumentData() throws Exception {
        List<InstrumentData> instrumentDataList = new ArrayList<>();
        TSETMCService tsetmcService = new TSETMCService();
        instrumentDataList = tsetmcService.getInstrumentData(instrumentDtoMongoGenericService.findAll(InstrumentDto.class));
        for (InstrumentData item : instrumentDataList) {
            instrumentData.add(item);
        }
        return instrumentDataList;
    }

    @GetMapping(path = "/api/getTrades")
    public List<Trades> getTrades() throws Exception {
        List<Trades> tradesList = new ArrayList<>();
        TSETMCService tsetmcService = new TSETMCService();
        List<InstrumentDto> instrumentDtos = instrumentDtoMongoGenericService.findAll(InstrumentDto.class);

        for (InstrumentDto item : instrumentDtos) {
            List<InstrumentData> instrumentDataList = instrumentData.list(InstrumentData.class, "bourseAccount", item.getBourseAccount());
            tradesList = tsetmcService.getTrades(instrumentDataList, TSETMCService.getId(item.getInstrumentLink()));
            for (Trades trades : tradesList) {
                Trades.add(trades);
            }
            break;
        }
        return tradesList;
    }

    private void checkInputData(String startDate, String endDate) throws Exception {
        LocalDate fromDate = DateUtil.getGregorianDate(startDate);
        if (CommonUtils.isNull(fromDate)) throw new RuntimeException("start.date.not.valid");
        LocalDate toDate = DateUtil.getGregorianDate(endDate);
        if (CommonUtils.isNull(toDate)) throw new RuntimeException("end.date.not.valid");
        if (startDate.compareTo(endDate) > 0) throw new RuntimeException("start.date.must.bigger.than.end.date");
    }

    @GetMapping(path = "/api/getInstruments")
    public List<Instrument> getInstruments() throws Exception {
        List<Instrument> instruments = new ArrayList<>();
        tsetmcService.saveInstruments();
        return instruments;
    }

    //ToDo
    @GetMapping(path = "/api/getIncomeStatement")
    public List<IncomeStatement> getIncomeStatement() throws Exception{
        List<IncomeStatement> incomeStatements = new ArrayList<>();
        meetingService.saveIncomeStatement();
        return incomeStatements;

    }

}

