package org.webCrawler.service;

import jakarta.transaction.Transactional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.config.Selenium;
import org.webCrawler.dto.*;
import org.webCrawler.model.Industry;
import org.webCrawler.model.Instrument;
import org.webCrawler.model.InstrumentPriceDate;

import java.time.LocalDate;
import java.util.*;

@Service
public class TSETMCService {
    private String webUrl = "https://old.tsetmc.com/Loader.aspx?ParTree=15131F";
    @Autowired
    private MongoGenericService<InstrumentId> instrumentIdMongoGenericService;
    @Autowired
    private MongoGenericService<InstrumentDto> instrumentDtoMongoGenericService;
    @Autowired
    private JPAGenericService<Industry> industryJPAGenericService;
    @Autowired
    private JPAGenericService<Instrument> instrumentJPAGenericService;
    @Autowired
    private MongoGenericService<InstrumentData> instrumentDataMongoGenericService;
    @Autowired
    private JPAGenericService<InstrumentPriceDate> instrumentPriceDateJPAGenericService;
    @Autowired
    private WebDriver webDriver;

    public List<InstrumentDto> getInstrument() throws Exception {
        List<InstrumentDto> list = new ArrayList<>();
        InstrumentDto instrumentDto = new InstrumentDto();
        WebDriver webDriverMain = new Selenium().webDriver();
        Thread.sleep(10000);
        webDriverMain.get(webUrl);
        webDriverMain.findElement(By.className("MwQuery")).click();
        if (!CommonUtils.isNull(CommonUtils.getWebElementsByClass(webDriverMain, "awesome"))) {
            WebElement btnnew = webDriverMain.findElements(By.className("awesome")).stream().filter(a -> a.getAttribute("innerHTML").equals("فیلتر جدید")).findFirst().get();
            if (!CommonUtils.isNull(btnnew)) {
                btnnew.click();
                Thread.sleep(1000);
                btnnew = webDriverMain.findElements(By.className("awesome")).stream().filter(a -> a.getAttribute("innerHTML").contains("فیلتر شماره")).findFirst().get();
                btnnew.click();
                Thread.sleep(1000);
                webDriverMain.findElement(By.id("InputFilterCode")).clear();
                webDriverMain.findElement(By.id("InputFilterCode")).sendKeys("(l18).indexOf(\"ب\")==0");
                webDriverMain.findElement(By.id("InputFilterName")).clear();
                webDriverMain.findElement(By.id("InputFilterName")).sendKeys("کل نمادها");
                btnnew = webDriverMain.findElements(By.className("awesome")).stream().filter(a -> a.getAttribute("innerHTML").contains("ثبت")).findFirst().get();
                btnnew.click();
                Thread.sleep(1000);

                btnnew = webDriverMain.findElements(By.className("awesome")).stream().filter(a -> a.getAttribute("innerHTML").equals("فیلتر جدید")).findFirst().get();
                btnnew.click();
                Thread.sleep(1000);
                btnnew = webDriverMain.findElements(By.className("awesome")).stream().filter(a -> a.getAttribute("innerHTML").contains("فیلتر شماره")).findFirst().get();
                btnnew.click();
                Thread.sleep(1000);
                webDriverMain.findElement(By.id("InputFilterCode")).clear();
                webDriverMain.findElement(By.id("InputFilterCode")).sendKeys("(l18).indexOf(\"ب\")==0");
                webDriverMain.findElement(By.id("InputFilterName")).clear();
                webDriverMain.findElement(By.id("InputFilterName")).sendKeys("هیچ اطلاعات");
                btnnew = webDriverMain.findElements(By.className("awesome")).stream().filter(a -> a.getAttribute("innerHTML").contains("ثبت")).findFirst().get();
                btnnew.click();
                Thread.sleep(1000);

                webDriverMain.findElements(By.className("awesome")).stream().filter(a -> a.getAttribute("innerHTML").equals("هیچ اطلاعات")).findFirst().get().click();
                Thread.sleep(10000);
                webDriverMain.findElements(By.className("awesome")).stream().filter(a -> a.getAttribute("innerHTML").equals("کل نمادها")).findFirst().get().click();
                Thread.sleep(10000);
                WebElement webElement = webDriverMain.findElement(By.className("popup_close"));
                Thread.sleep(10000);
                webElement.click();
                Thread.sleep(10000);
            }
        }
        WebElement main = CommonUtils.getWebElementById(webDriverMain, "main");
        List<WebElement> elementList = CommonUtils.getWebElements(main, "div");
        String groupName = "";
        for (WebElement element : elementList) {
            if (CommonUtils.isNull(CommonUtils.getAttribute(element, "class")) ||
                    (!element.getAttribute("class").equals("secSep") &&
                            !element.getAttribute("class").equals("{c}")))
                continue;

            if (element.getAttribute("class").equals("secSep")) {
                groupName = element.getAttribute("innerHTML");
                System.out.println("================================================================");
                System.out.println(groupName);
                System.out.println("-----------------------------");
            } else {
                instrumentDto = new InstrumentDto();
                instrumentDto.setGroupName(groupName);
                instrumentDto.setBourseAccount(CommonUtils.getWebElementsByClass(element, "t0c").get(0).getText());
                instrumentDto.setInstrumentLink(CommonUtils.getWebElementsByClass(element, "t0c").get(0).findElement(By.tagName("a")).getDomProperty("href"));
                System.out.println(instrumentDto.getBourseAccount());
                instrumentDto.setName(CommonUtils.getWebElementsByClass(element, "t0c").get(1).getText());
                list.add(instrumentDto);
            }
        }
        webDriverMain.close();
        return list;
    }

    public List<InstrumentId> getInstrumentIds(List<InstrumentDto> instrumentDtos) throws Exception {
        WebDriver webDriverMain = new Selenium().webDriver();
        List<InstrumentId> instrumentIds = new ArrayList<>();
        for (InstrumentDto item : instrumentDtos) {
            try {
                webDriverMain.get(item.getInstrumentLink());
                Thread.sleep(10000);
                List<WebElement> links = webDriverMain.findElement(By.className("menu2")).findElements(By.tagName("a"));
                if (CommonUtils.isNull(links) || links.stream().filter(a -> a.getAttribute("innerHTML").equals("شناسه")).count() == 0) {
                    webDriverMain.get(item.getInstrumentLink());
                    Thread.sleep(10000);
                    links = webDriverMain.findElement(By.className("menu2")).findElements(By.tagName("a"));
                }
                links.stream().filter(a -> a.getAttribute("innerHTML").equals("شناسه")).findFirst().get().click();
                Thread.sleep(10000);
                WebElement div = webDriverMain.findElement(By.id("IdentityContent"));
                WebElement table = div.findElement(By.className("table1"));
                table = table.findElement(By.tagName("tbody"));
                List<WebElement> trs = table.findElements(By.tagName("tr"));
                for (WebElement element : trs) {
                    String caption = element.findElements(By.tagName("td")).get(0).getAttribute("innerHTML");
                    String value = element.findElements(By.tagName("td")).get(1).getAttribute("innerHTML");
                    instrumentIds.add(new InstrumentId(item.getBourseAccount(), caption, value));
                }
            } catch (Exception e) {
                System.out.println(item.getInstrumentLink());
                System.out.println("--------------------------------------------------------------------");
            }
        }
        webDriverMain.close();
        return instrumentIds;
    }

    public static String getId(String link) {
        return link.split("\\?")[1].split("&")[1].split("=")[1];
    }

//    public List<InstrumentData> getInstrumentDataNew(List<InstrumentDto> instrumentDtos) throws Exception {
//        List<InstrumentData> instrumentDataList = new ArrayList<>();
//        for (InstrumentDto item : instrumentDtos) {
//            webDriver.get(item.getInstrumentLink());
//            Thread.sleep(10000);
//            List<WebElement> links = webDriver.findElement(By.className("menu2")).findElements(By.tagName("a"));
//            webDriver.manage().window().maximize();
//            links.stream().filter(a -> a.getAttribute("innerHTML").equals("سابقه")).findFirst().get().click();
//            Thread.sleep(10000);
//            WebElement paging = webDriver.findElement(By.id("paging"));
//
//        }
//        return instrumentDataList;
//    }


    public List<InstrumentData> getInstrumentData(List<InstrumentDto> instrumentDtos) throws Exception {
        WebDriver webDriverMain = new Selenium().webDriver();
        List<InstrumentData> instrumentDataList = new ArrayList<>();
        webDriverMain.manage().window().maximize();
        for (InstrumentDto item : instrumentDtos) {
            webDriverMain.get(item.getInstrumentLink().replace("tsetmc.com", "old.tsetmc.com"));
            Thread.sleep(10000);
            List<WebElement> links = webDriverMain.findElement(By.className("menu2")).findElements(By.tagName("a"));
//            if (CommonUtils.isNull(links) || links.stream().filter(a -> a.getAttribute("innerHTML").equals("سابقه")).count() == 0) {
//                webDriverMain.get(item.getInstrumentLink());
//                Thread.sleep(10000);
//                links = webDriverMain.findElement(By.className("menu2")).findElements(By.tagName("a"));
//            }
            links.stream().filter(a -> a.getAttribute("innerHTML").equals("سابقه")).findFirst().get().click();
            Thread.sleep(10000);
            WebElement paging = webDriverMain.findElement(By.id("paging"));
            paging = paging.findElement(By.className("pagingBlock"));
            List<WebElement> pagelinks = paging.findElements(By.tagName("a"));
            int pagelinksSize = 35;
            for (int i = 1; i <= pagelinksSize + 1; i++) {
                if (i > 1) {
                    String index = i + "";
                    if (i % 35 == 1) {
                        pagelinks = paging.findElements(By.tagName("a"));
                        Optional<WebElement> element = pagelinks.stream().filter(a -> a.getAttribute("innerHTML").equals("&gt;")).findFirst();
                        if (!element.isPresent())
                            break;
                        element.get().click();
                        Thread.sleep(1000);
                        pagelinksSize += 35;
                    }
                    paging = webDriverMain.findElement(By.id("paging"));
                    paging = paging.findElement(By.className("pagingBlock"));
                    pagelinks = paging.findElements(By.tagName("a"));
                    Optional<WebElement> element = pagelinks.stream().filter(a -> CommonUtils.cleanTextNumber(a.getAttribute("innerHTML")).equals(index)).findFirst();
                    if (!element.isPresent())
                        break;
                    element.get().click();
                    Thread.sleep(1000);
                }
                WebElement table = webDriverMain.findElement(By.id("trade")).findElement(By.className("objbox")).findElement(By.tagName("table")).findElement(By.tagName("tbody"));
                List<WebElement> trs = table.findElements(By.tagName("tr"));
                for (int j = 1; j < trs.size(); j++) {
                    List<WebElement> tds = trs.get(j).findElements(By.tagName("td"));
                    InstrumentData instrumentData = new InstrumentData();
                    instrumentData.setBourseAccount(item.getBourseAccount());
                    instrumentData.setTsetmcId(getId(item.getInstrumentLink()));
                    for (int k = 0; k < tds.size(); k++) {
                        switch (k) {
                            case 15:
                                instrumentData.setDate(tds.get(k).getText());
                                break;
                            case 14:
                                instrumentData.setUnit(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                            case 13:
                                instrumentData.setVolume(CommonUtils.doubleValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                            case 12:
                                instrumentData.setValue(CommonUtils.doubleValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                            case 11:
                                instrumentData.setYesterdayPrice(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                            case 10:
                                instrumentData.setFirstPrice(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                            case 9:
                                instrumentData.setLastPrice(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                            case 6:
                                instrumentData.setFinalPrice(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                            case 3:
                                instrumentData.setMinPrice(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                            case 2:
                                instrumentData.setMaxPrice(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(k).getText())));
                                break;
                        }
                    }
                    instrumentDataList.add(instrumentData);
                }
            }
        }
        webDriverMain.close();
        return instrumentDataList;
    }

    public List<Trades> getTrades(List<InstrumentData> instrumentData, String instrumenId) throws Exception {
        WebDriver webDriverMain = webDriver;
        List<Trades> tradesList = new ArrayList<>();
        for (InstrumentData item : instrumentData) {
            LocalDate localDate = DateUtil.getGregorianDate(item.getDate());
            String link = String.format("https://cdn.tsetmc.com/History/%s/%s", instrumenId, localDate.getYear() + "" + (localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue()) + "" + (localDate.getDayOfMonth() < 10 ? "0" + localDate.getDayOfMonth() : localDate.getDayOfMonth()));
            try {
                webDriverMain.get(link);
                Thread.sleep(1000);
                List<WebElement> links = webDriverMain.findElement(By.className("menu2")).findElements(By.tagName("a"));
                links.stream().filter(a -> a.getAttribute("innerHTML").equals("معاملات")).findFirst().get().click();
                Thread.sleep(1000);
                WebElement div = webDriverMain.findElement(By.className("ag-center-cols-container"));
                List<WebElement> rows = div.findElements(By.className("ag-row"));
                int colIndex = 0;
                for (int i = 0; i < rows.size(); i++) {
                    List<WebElement> tds = rows.get(i).findElements(By.tagName("div"));
                    Trades trades = new Trades();
                    trades.setDate(item.getDate());
                    trades.setBourseAccount(item.getBourseAccount());
                    for (int j = 0; j < tds.size(); j++) {
                        if (j == 0) {
                            WebElement span = CommonUtils.getWebElement(tds.get(j), "span");
                            long count = CommonUtils.getWebElements(span, "div").stream()
                                    .filter(a -> !CommonUtils.isNull(a.getDomProperty("style")) && a.getDomProperty("style").contains("border-top"))
                                    .count();
                            colIndex = 0;
                            if (count > 0) {
                                trades.setIsCancel(true);
                                colIndex = 2;
                            }
                        } else if (1 + colIndex == j) {
                            trades.setTime(tds.get(j).getText());
                        } else if (2 + colIndex == j) {
                            trades.setVolume(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(j).getText())));
                        } else if (3 + colIndex == j) {
                            trades.setPrice(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(j).getText())));
                        }
                    }
                    tradesList.add(trades);
                }
            } catch (Exception e) {
                System.out.println(link);
                System.out.println("--------------------------------------------------------------------");
            }
        }
        webDriverMain.close();
        return tradesList;
    }

    public List<Instrument> saveInstruments() throws Exception {
        List<InstrumentDto> instrumentDtos = new ArrayList<>();
        List<InstrumentId> instrumentIds = new ArrayList<>();
        List<Industry> industries = new ArrayList<>();
        List<Instrument> instrumentList = instrumentJPAGenericService.findAll(Instrument.class);
        instrumentDtos = instrumentDtoMongoGenericService.findAll(InstrumentDto.class);
        instrumentIds = instrumentIdMongoGenericService.findAll(InstrumentId.class);
        industries = industryJPAGenericService.findAll(Industry.class);
        for (InstrumentDto instrumentDto : instrumentDtos) {
            if (instrumentList.stream().filter(a -> a.getInstrumentName().equals(instrumentDto.getBourseAccount())).findAny().isEmpty()) {
                Instrument instrument = new Instrument();
                List<InstrumentId> instrumentIdList = instrumentIds.stream().filter(a -> a.getBourseAccount().equals(instrumentDto.getBourseAccount())).toList();
                instrument.setInsMaxLcode(instrumentIdList.stream().filter(a -> a.getCaption().equals("کد 12 رقمی نماد")).findFirst().get().getValue());
                instrument.setInsMinLcode(instrumentIdList.stream().filter(a -> a.getCaption().equals("کد 5 رقمی نماد")).findFirst().get().getValue());
                instrument.setLatinName(instrumentIdList.stream().filter(a -> a.getCaption().equals("نام لاتین شرکت")).findFirst().get().getValue());
                instrument.setFourDigitCompanyCode(instrumentIdList.stream().filter(a -> a.getCaption().equals("کد 4 رقمی شرکت")).findFirst().get().getValue());
                instrument.setCompanyName(instrumentIdList.stream().filter(a -> a.getCaption().equals("نام شرکت")).findFirst().get().getValue());
                instrument.setInstrumentName(instrumentDto.getBourseAccount());
                instrument.setInstrumentNameThirty(instrumentIdList.stream().filter(a -> a.getCaption().equals("نماد 30 رقمی فارسی")).findFirst().get().getValue());
                instrument.setIsinCode(instrumentIdList.stream().filter(a -> a.getCaption().equals("کد 12 رقمی شرکت")).findFirst().get().getValue());
                instrument.setMarket(instrumentIdList.stream().filter(a -> a.getCaption().equals("بازار")).findFirst().get().getValue());
                instrument.setBoardCode(Long.valueOf(instrumentIdList.stream().filter(a -> a.getCaption().equals("کد تابلو")).findFirst().get().getValue()));
                instrument.setIndustrySubgroupCode(Long.valueOf(instrumentIdList.stream().filter(a -> a.getCaption().equals("کد زیر گروه صنعت")).findFirst().get().getValue()));
                instrument.setIndustrySubgroupName(instrumentIdList.stream().filter(a -> a.getCaption().equals("زیر گروه صنعت")).findFirst().get().getValue());
                instrument.setTsetmsId(getId(instrumentDto.getInstrumentLink()));
                instrument.setIsDeleted(Boolean.valueOf(instrumentDto.getIsDeleted()));
                String code = instrumentIdList.stream().filter(a -> a.getCaption().equals("کد گروه صنعت")).findFirst().get().getValue().trim();
                instrument.setIndustryId(industries.stream().filter(a -> a.getCode().equals(Integer.parseInt(code))).findFirst().get().getId());
                instrumentJPAGenericService.insert(instrument);
            }
        }
        return instrumentList;
    }

    @Transactional
    public List<InstrumentPriceDate> saveInstrumentPriceDate() throws Exception {
        List<InstrumentData> instrumentDataList = instrumentDataMongoGenericService.findAll(InstrumentData.class);
        List<Instrument> instrumentList = instrumentJPAGenericService.findAll(Instrument.class);
        List<InstrumentPriceDate> instrumentPriceDates = new ArrayList<>();
        for (InstrumentData instrumentData : instrumentDataList) {
            Instrument instrument = new Instrument();
            InstrumentPriceDate instrumentPriceDate = new InstrumentPriceDate();
            instrument.setId(instrumentList.stream().filter(a -> a.getTsetmsId().equals(instrumentData.getTsetmcId())).findFirst().get().getId());
            instrumentPriceDate.setInstrument(instrument);
            instrumentPriceDate.setPriceDate(instrumentData.getDate());
            instrumentPriceDate.setFirstPrice(instrumentData.getFirstPrice());
            instrumentPriceDate.setLastDayPrice(instrumentData.getFinalPrice());
            instrumentPriceDate.setLastTradePrice(instrumentData.getLastPrice());
            instrumentPriceDate.setLowestPrice(instrumentData.getMinPrice());
            instrumentPriceDate.setHighestPrice(instrumentData.getMaxPrice());
            instrumentPriceDate.setValue(instrumentData.getValue());
            instrumentPriceDate.setValuePrice(instrumentData.getVolume());
            instrumentPriceDate.setNumberOfTransactions(instrumentData.getUnit());
            instrumentPriceDates.add(instrumentPriceDate);
            instrumentPriceDateJPAGenericService.insert(instrumentPriceDate);

        }
        return instrumentPriceDates;
    }

    public List<InstrumentId> getInstrumentId(String instrumentName) throws Exception {
        Thread.sleep(5000);
        webDriver.get("https://old.tsetmc.com/Loader.aspx?ParTree=15");
        webDriver.findElement(By.id("search")).click();
        webDriver.findElement(By.id("SearchKey")).click();
        webDriver.findElement(By.id("SearchKey")).sendKeys(instrumentName);
        Thread.sleep(5000);
        WebElement table = webDriver.findElement(By.id("SearchResult")).findElement(By.className("table1")).findElement(By.tagName("tbody"));
        List<WebElement> trs = table.findElements(By.tagName("tr"));
        List<InstrumentId> instrumentIds = new ArrayList<>();
        List<InstrumentDto> instrumentDtos = new ArrayList<>();
        instrumentDtos = instrumentDtoMongoGenericService.findAll(InstrumentDto.class);
        List<Map> maps = new ArrayList<>();
        for (WebElement element : trs) {
            Map<String, String> stringMap = new HashMap<>();
            String href = element.findElement(By.tagName("td")).findElement(By.tagName("a")).getDomProperty("href");
            href = href.replace("old.", "");
            stringMap.put("link", href);
            String isDeleted = element.findElement(By.tagName("td")).findElement(By.tagName("a")).getAttribute("innerHTML");
            if (isDeleted.contains("(نماد قدیمی حذف شده)")) {
                stringMap.put("isdeleted", String.valueOf(true));
            } else {
                stringMap.put("isdeleted", String.valueOf(false));
            }
            maps.add(stringMap);

        }
        for (Map map : maps) {
            String link = map.get("link").toString();
            if (instrumentDtos.stream().filter(a -> a.getInstrumentLink().equals(link)).findAny().isEmpty()) {
                webDriver.get(link);
                Thread.sleep(1000);
                List<WebElement> links = webDriver.findElement(By.className("menu2")).findElements(By.tagName("a"));
                links.stream().filter(a -> a.getAttribute("innerHTML").equals("شناسه")).findFirst().get().click();
                Thread.sleep(10000);
                WebElement div = webDriver.findElement(By.id("IdentityContent"));
                WebElement table2 = div.findElement(By.className("table1"));
                table2 = table2.findElement(By.tagName("tbody"));
                List<WebElement> trs2 = table2.findElements(By.tagName("tr"));
                for (WebElement element2 : trs2) {
                    String caption = element2.findElements(By.tagName("td")).get(0).getAttribute("innerHTML");
                    String value = element2.findElements(By.tagName("td")).get(1).getAttribute("innerHTML");
                    instrumentIds.add(new InstrumentId(null, caption, value));
                }
                String bourseAccount = instrumentIds.stream().filter(a -> a.getCaption().equals("نماد فارسی") && !CommonUtils.isNull(a.getValue())).reduce((a, b) -> b).get().getValue().trim();
                String name = instrumentIds.stream().filter(a -> a.getCaption().equals("نام شرکت") && !CommonUtils.isNull(a.getValue())).reduce((a, b) -> b).get().getValue().trim();
                String groupName = instrumentIds.stream().filter(a -> a.getCaption().equals("گروه صنعت") && !CommonUtils.isNull(a.getValue())).reduce((a, b) -> b).get().getValue().trim();
                for (InstrumentId instrumentId : instrumentIds) {
                    instrumentId.setBourseAccount(bourseAccount);
                }
                InstrumentDto instrumentDto = new InstrumentDto(bourseAccount, name, groupName, map.get("link").toString(), map.get("isdeleted").toString());
                instrumentDtoMongoGenericService.add(instrumentDto);
                for (InstrumentId instrumentId : instrumentIds) {
                    instrumentIdMongoGenericService.add(instrumentId);
                }
            }
        }
        webDriver.close();
        return instrumentIds;
    }



}
