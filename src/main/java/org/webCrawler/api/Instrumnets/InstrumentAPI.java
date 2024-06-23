package org.webCrawler.api.Instrumnets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webCrawler.dto.*;
import org.webCrawler.model.*;
import org.webCrawler.service.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class InstrumentAPI {

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
    JPAGenericService<Instrument> instrumentJPAGenericService;
    @Autowired
    TSETMCService tsetmcService;


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

    @GetMapping(path = "/api/getInstruments")
    public List<Instrument> getInstruments() throws Exception {
        return tsetmcService.saveInstruments();
    }

    @GetMapping(path = "/api/getInstrumentsPriceDate")
    public List<InstrumentPriceDate> getInstrumentsPriceDate() throws Exception {
        return tsetmcService.saveInstrumentPriceDate();
    }


    @GetMapping(path = "/api/getInstrumentId")
    public List<InstrumentId> getInstrumentId(@RequestParam("bourseAccount") String bourseAccount) throws Exception {
        return tsetmcService.getInstrumentId(bourseAccount);
    }

    @GetMapping(path = "/api/getTradeDate")
    public InstrumentData getTradeDate(@RequestParam("bourseAccount") String bourseAccount, @RequestParam("tradDate") String tradDate) throws Exception {
        return tsetmcService.getTradeDate(bourseAccount, tradDate);
    }

}

