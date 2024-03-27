package org.webCrawler.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.common.LettersTypes;
import org.webCrawler.common.MeetingStatuses;
import org.webCrawler.config.Selenium;
import org.webCrawler.dto.*;
import org.webCrawler.model.*;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@Service
public class MeetingService {
    @Autowired
    private WebDriver webDriver;
    @Autowired
    MongoGenericService<ExtraAssemblyDto> extraAssembly;
    @Autowired
    private JPAGenericService<Industry> industryJPAGenericService;
    @Autowired
    private JPAGenericService<IncomeStatement> incomeStatementService;
    @Autowired
    private JPAGenericService<Columns> columnsService;
    @Autowired
    private JPAGenericService<FinancialStatementsPeriod> financialStatementsPeriodService;
    @Autowired
    private JPAGenericService<IndustryColumn> industryColumnService;
    @Autowired
    private MongoGenericService<InterimStatementDto> interimStatementDtoMeetingService;
    @Autowired
    private JPAGenericService<Instrument> instrumentJPAGenericService;
    @Autowired
    private JPAGenericService<IncomeStatementDetail> incomeStatementDetailService;
    @Autowired
    private JPAGenericService<CodalShareholderMeeting> codalShareholderMeetingGenericService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JPAGenericService<InstrumentPriceDate> instrumentPriceDateJPAGenericService;
    @Autowired
    TSETMCService tsetmcService;

    //    private String webUrl = "https://www.codal.ir/ReportList.aspx?search&Childs=false";
    private String webUrl = "https://www.codal.ir/ReportList.aspx?search";
//    private String webUrl = "https://www.codal.ir/ReportList.aspx?search&Symbol=ومعلم";

    public void setMeetingService(String date, String endDate) {
//        this.webUrl = String.format("&FromDate=%s&ToDate=%s", date, endDate);
        this.webUrl += String.format("&FromDate=%s&ToDate=%s", date, endDate);
    }

    public MeetingService() {
    }

    public List<InterimStatementDto> getInterimStatementDto(String letterType) throws Exception {
        InterimStatementDto interimStatementDto = new InterimStatementDto();
        List<InterimStatementDto> interimStatementDtos = new ArrayList<>();
        this.webUrl += String.format("&LetterType=%s", letterType);
        WebDriver webDriverMain = new Selenium().webDriver();
        Thread.sleep(10000);
        webDriverMain.get(webUrl);
        Thread.sleep(10000);
        boolean flag = true;
        while (flag) {
            List<String> links = new ArrayList<>();
            List<WebElement> elements = webDriverMain.findElements(By.className("letter-title"));
            for (WebElement webElement : elements) {
                links.add(webElement.getDomProperty("href"));
            }
            for (String link : links) {
                interimStatementDto = new InterimStatementDto();
                interimStatementDto.setLink(link);
                WebDriver webDriver = new Selenium().webDriver();
                Thread.sleep(10000);
                webDriver.get(link);
                Thread.sleep(10000);
                WebElement span = CommonUtils.getWebElementById(webDriver, "ctl00_txbCompanyName");
                if (!CommonUtils.isNull(span)) interimStatementDto.setCompany(span.getText().trim());

                span = CommonUtils.getWebElementById(webDriver, "ctl00_lblDisplaySymbol");
                if (!CommonUtils.isNull(span.getText().trim())) {
                    interimStatementDto.setBourseAccount(span.getText().trim().replaceAll("[^[\\p{InArabic}&&\\PN]\\s.-]", ""));
                } else {
                    span = CommonUtils.getWebElementById(webDriver, "ctl00_txbSymbol");
                    if (!CommonUtils.isNull(span))
                        interimStatementDto.setBourseAccount(span.getText().trim().replaceAll("[^[\\p{InArabic}&&\\PN]\\s.-]", ""));
                }

                span = CommonUtils.getWebElementById(webDriver, "ctl00_lblISIC");
                if (!CommonUtils.isNull(span)) interimStatementDto.setISICCode(span.getText().trim());

                span = CommonUtils.getWebElementById(webDriver, "ctl00_lblYearEndToDate");
                span = span.findElement(By.tagName("bdo"));
                if (!CommonUtils.isNull(span)) interimStatementDto.setDate(span.getText().trim());

                span = CommonUtils.getWebElementById(webDriver, "ctl00_lblPeriodEndToDate");
                span = span.findElement(By.tagName("bdo"));
                if (!CommonUtils.isNull(span)) interimStatementDto.setEndDate(span.getText().trim());

                span = CommonUtils.getWebElementById(webDriver, "ctl00_lblListedCapital");
                if (!CommonUtils.isNull(span))
                    interimStatementDto.setRegisteredCapital(CommonUtils.longValue(span.getText().replace(",", "").trim()));

                span = CommonUtils.getWebElementById(webDriver, "ctl00_txbUnauthorizedCapital");
                if (!CommonUtils.isNull(span))
                    interimStatementDto.setNotRegisteredCapital(CommonUtils.longValue(span.getText().replace(",", "").trim()));

                span = CommonUtils.getWebElementById(webDriver, "ctl00_lblPeriod");
                if (!CommonUtils.isNull(span)) interimStatementDto.setPeriod(span.getText().trim());

                span = CommonUtils.getWebElementById(webDriver, "ctl00_lblIsAudited");
                if (!CommonUtils.isNull(span))
                    interimStatementDto.setFinancialStatementStatus(span.getText().trim().replaceAll("[^[\\p{InArabic}&&\\PN]\\s.-]", ""));

                span = CommonUtils.getWebElementById(webDriver, "ctl00_lblCompanyState");
                if (!CommonUtils.isNull(span)) interimStatementDto.setPublisherStatus(span.getText().trim());
                WebElement webElement = CommonUtils.getWebElementById(webDriver, "ddlTable");
                if (CommonUtils.isNull(webElement))
                    webElement = CommonUtils.getWebElementById(webDriver, "ctl00_ddlTable");
                Select select = new Select(webElement);
                for (int typeIndex = 0; typeIndex < select.getOptions().size(); typeIndex++) {
                    if (typeIndex > 0) {
                        webDriver.findElement(By.className("next_report")).click();
                    }
                    webElement = CommonUtils.getWebElementById(webDriver, "ddlTable");
                    if (CommonUtils.isNull(webElement))
                        webElement = CommonUtils.getWebElementById(webDriver, "ctl00_ddlTable");
                    select = new Select(webElement);
                    if (select.getFirstSelectedOption().getText().trim().equals("ترازنامه") || select.getFirstSelectedOption().getText().trim().equals("صورت سود و زیان") || select.getFirstSelectedOption().getText().trim().equals("جریان وجوه نقد")) {
                        WebElement table = CommonUtils.getWebElementByClass(webDriver, "rayanDynamicStatement");
                        String type = "rayanDynamicStatement";
                        if (CommonUtils.isNull(table)) {
                            type = "";
                            String tableId = "";
                            if (select.getFirstSelectedOption().getText().trim().equals("ترازنامه")) {
                                tableId = "ctl00_cphBody_ucSFinancialPosition_grdSFinancialPosition";
                            } else if (select.getFirstSelectedOption().getText().trim().equals("صورت سود و زیان")) {
                                tableId = "ctl00_cphBody_ucInterimStatement_grdInterimStatement";
                            } else if (select.getFirstSelectedOption().getText().trim().equals("جریان وجوه نقد")) {
                                tableId = "ctl00_cphBody_ucCashFlow1_gvCashFlow";
                                type = "rayanDynamicStatement";
                            }
                            table = CommonUtils.getWebElementById(webDriver, tableId);
                        }
                        if (select.getFirstSelectedOption().getText().trim().equals("ترازنامه")) {
                            interimStatementDto.setBalanceSheets(getBalanceSheets(table, type));
                        } else if (select.getFirstSelectedOption().getText().trim().equals("صورت سود و زیان")) {
                            interimStatementDto.setProfitAndStatement(getBalanceSheets(table, type));
                        } else if (select.getFirstSelectedOption().getText().trim().equals("جریان وجوه نقد")) {
                            interimStatementDto.setCashFlow(getBalanceSheets(table, type));
                        }
                    }
                }
                interimStatementDtos.add(interimStatementDto);
                webDriver.close();
            }
            WebElement li = CommonUtils.getWebElementByTitle(webDriverMain, "صفحه بعدی");
            flag = false;
            if (!CommonUtils.isNull(li)) {
                WebElement link = CommonUtils.getWebElement(li, "a");
                if (!CommonUtils.isNull(link)) {
                    if (!CommonUtils.isNull(link.getDomProperty("href"))) {
                        link.click();
                        flag = true;
                    }
                }
            }
        }
        webDriverMain.close();
        return interimStatementDtos;
    }

    private List<BalanceSheet> getBalanceSheets(WebElement table, String type) {
        BalanceSheet balanceSheet = new BalanceSheet();
        List<BalanceSheet> balanceSheets = new ArrayList<>();
        WebElement tbody = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));
        int defaultValue = 0;
        if (!type.equals("rayanDynamicStatement")) defaultValue = 1;
        for (int j = defaultValue; j < rows.size(); j++) {
            List<WebElement> columns = rows.get(j).findElements(By.tagName("td"));
            balanceSheet = new BalanceSheet();
            defaultValue = 0;
            if (!type.equals("rayanDynamicStatement")) {
                defaultValue = 1;
            }
            for (int i = defaultValue; i < columns.size(); i++) {
                if ((i == 0 && type.equals("rayanDynamicStatement")) || (i == 1) && !type.equals("rayanDynamicStatement")) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    if (CommonUtils.isNull(span)) span = columns.get(i);
                    balanceSheet.setDescription(span.getText().trim());
                } else if ((i == 1 && type.equals("rayanDynamicStatement")) || (i == 2) && !type.equals("rayanDynamicStatement")) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    String textVlaue = "";
                    if (CommonUtils.isNull(span)) {
                        WebElement input = CommonUtils.getWebElement(columns.get(i), "input");
                        if (CommonUtils.isNull(input)) {
                            span = columns.get(i);
                            textVlaue = span.getText().replace(",", "").trim();
                        } else {
                            textVlaue = input.getDomProperty("value").replace(",", "").trim();
                        }
                    } else {
                        textVlaue = span.getText().replace(",", "").trim();
                    }
                    boolean isNegative = false;
                    if (textVlaue.indexOf("(") != -1) {
                        isNegative = true;
                        textVlaue = textVlaue.replace("(", "").replace(")", "");
                    }
                    if (isNegative)
                        balanceSheet.setActualPerformance(CommonUtils.isNull(CommonUtils.longValue(textVlaue), 0L) * -1);
                    else balanceSheet.setActualPerformance(CommonUtils.longValue(textVlaue));
                } else if ((i == 2 && type.equals("rayanDynamicStatement")) || (i == 3) && !type.equals("rayanDynamicStatement")) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    String textVlaue = "";
                    if (CommonUtils.isNull(span)) {
                        WebElement input = CommonUtils.getWebElement(columns.get(i), "input");
                        if (CommonUtils.isNull(input)) {
                            span = columns.get(i);
                            textVlaue = span.getText().replace(",", "").trim();
                        } else {
                            textVlaue = input.getDomProperty("value").replace(",", "").trim();
                        }
                    } else {
                        textVlaue = span.getText().replace(",", "").trim();
                    }
                    boolean isNegative = false;
                    if (textVlaue.indexOf("(") != -1) {
                        isNegative = true;
                        textVlaue = textVlaue.replace("(", "").replace(")", "");
                    }
                    if (isNegative) balanceSheet.setEndOfOldYear(CommonUtils.longValue(textVlaue) * -1);
                    else balanceSheet.setEndOfOldYear(CommonUtils.longValue(textVlaue));
                } else if ((i == 3 && type.equals("rayanDynamicStatement")) || (i == 4) && !type.equals("rayanDynamicStatement")) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    String textVlaue = "";
                    if (CommonUtils.isNull(span)) {
                        WebElement input = CommonUtils.getWebElement(columns.get(i), "input");
                        if (CommonUtils.isNull(input)) {
                            span = columns.get(i);
                            textVlaue = span.getText().replace(",", "").trim();
                        } else {
                            textVlaue = input.getDomProperty("value").replace(",", "").trim();
                        }
                    } else {
                        textVlaue = span.getText().replace(",", "").trim();
                    }
                    boolean isNegative = false;
                    if (textVlaue.indexOf("(") != -1) {
                        isNegative = true;
                        textVlaue = textVlaue.replace("(", "").replace(")", "");
                    }
                    if (isNegative) balanceSheet.setChangePercent(CommonUtils.longValue(textVlaue) * -1);
                    else balanceSheet.setChangePercent(CommonUtils.longValue(textVlaue));
                } else if ((i == 4 && type.equals("rayanDynamicStatement")) || (i == 6) && !type.equals("rayanDynamicStatement")) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    if (CommonUtils.isNull(span)) span = columns.get(i);
                    balanceSheet.setDescription2(span.getText().trim());
                } else if ((i == 5 && type.equals("rayanDynamicStatement")) || (i == 7) && !type.equals("rayanDynamicStatement")) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    String textVlaue = "";
                    if (CommonUtils.isNull(span)) {
                        WebElement input = CommonUtils.getWebElement(columns.get(i), "input");
                        if (CommonUtils.isNull(input)) {
                            span = columns.get(i);
                            textVlaue = span.getText().replace(",", "").trim();
                        } else {
                            textVlaue = input.getDomProperty("value").replace(",", "").trim();
                        }
                    } else {
                        textVlaue = span.getText().replace(",", "").trim();
                    }
                    boolean isNegative = false;
                    if (textVlaue.indexOf("(") != -1) {
                        isNegative = true;
                        textVlaue = textVlaue.replace("(", "").replace(")", "");
                    }
                    if (isNegative) balanceSheet.setActualPerformance2(CommonUtils.longValue(textVlaue) * -1);
                    else balanceSheet.setActualPerformance2(CommonUtils.longValue(textVlaue));
                } else if ((i == 6 && type.equals("rayanDynamicStatement")) || (i == 8) && !type.equals("rayanDynamicStatement")) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    String textVlaue = "";
                    if (CommonUtils.isNull(span)) {
                        WebElement input = CommonUtils.getWebElement(columns.get(i), "input");
                        if (CommonUtils.isNull(input)) {
                            span = columns.get(i);
                            textVlaue = span.getText().replace(",", "").trim();
                        } else {
                            textVlaue = input.getDomProperty("value").replace(",", "").trim();
                        }
                    } else {
                        textVlaue = span.getText().replace(",", "").trim();
                    }
                    boolean isNegative = false;
                    if (textVlaue.indexOf("(") != -1) {
                        isNegative = true;
                        textVlaue = textVlaue.replace("(", "").replace(")", "");
                    }
                    if (isNegative) balanceSheet.setEndOfOldYear2(CommonUtils.longValue(textVlaue) * -1);
                    else balanceSheet.setEndOfOldYear2(CommonUtils.longValue(textVlaue));
                } else if ((i == 7 && type.equals("rayanDynamicStatement")) || (i == 9) && !type.equals("rayanDynamicStatement")) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    String textVlaue = "";
                    if (CommonUtils.isNull(span)) {
                        WebElement input = CommonUtils.getWebElement(columns.get(i), "input");
                        if (CommonUtils.isNull(input)) {
                            span = columns.get(i);
                            textVlaue = span.getText().replace(",", "").trim();
                        } else {
                            textVlaue = input.getDomProperty("value").replace(",", "").trim();
                        }
                    } else {
                        textVlaue = span.getText().replace(",", "").trim();
                    }
                    boolean isNegative = false;
                    if (textVlaue.indexOf("(") != -1) {
                        isNegative = true;
                        textVlaue = textVlaue.replace("(", "").replace(")", "");
                    }
                    if (isNegative) balanceSheet.setChangePercent2(CommonUtils.longValue(textVlaue) * -1);
                    else balanceSheet.setChangePercent2(CommonUtils.longValue(textVlaue));
                }
            }
            if (!CommonUtils.isNull(balanceSheet.getDescription()) || !CommonUtils.isNull(balanceSheet.getDescription2()))
                balanceSheets.add(balanceSheet);
        }
        return balanceSheets;
    }

    public List<PriorityOrBuyShareDto> getPriorityOrBuyShare(String letterType) throws Exception {
        PriorityOrBuyShareDto priorityOrBuyShare = new PriorityOrBuyShareDto();
        List<PriorityOrBuyShareDto> capitalIncreaseDto = new ArrayList<>();
        this.webUrl += String.format("&LetterType=%s", letterType);
        WebDriver webDriverMain = new Selenium().webDriver();
        Thread.sleep(10000);
        webDriverMain.get(webUrl);
        Thread.sleep(10000);
        boolean flag = true;
        while (flag) {
            List<String> links = new ArrayList<>();
            List<WebElement> elements = webDriverMain.findElements(By.className("letter-title"));
            for (WebElement webElement : elements) {
                links.add(webElement.getDomProperty("href"));
            }
            for (String link : links) {
                priorityOrBuyShare = new PriorityOrBuyShareDto();
                priorityOrBuyShare.setLink(link);
                WebDriver webDriver = new Selenium().webDriver();
                Thread.sleep(10000);
                webDriver.get(link);
                Thread.sleep(10000);
                WebElement span = CommonUtils.getWebElementById(webDriver, "lblLicenseDesc");
                if (!CommonUtils.isNull(span)) {
                    if (!CommonUtils.isNull(span.getText())) {
                        priorityOrBuyShare.setLicenseNumber(span.getText().split("مورخ")[0].trim());
                        priorityOrBuyShare.setAdvertisementDate(span.getText().split("مورخ")[1].replace("و", "").trim());
                    }
                }
                span = CommonUtils.getWebElementById(webDriver, "lblLastExtraAssembly");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setMeetingDate(span.getText().trim());
                }

                span = CommonUtils.getWebElementById(webDriver, "lblCompany");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setBourseAccount(span.getText().trim());
                    if (span.getText().trim().indexOf(":") != -1)
                        priorityOrBuyShare.setBourseAccount(span.getText().trim().split(":")[1].trim());
                }

                span = CommonUtils.getWebElementById(webDriver, "lblLastCapitalIncreaseSession");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setProceedingsDate(span.getText().trim());
                }

                span = CommonUtils.getWebElementById(webDriver, "lblPreviousCapital");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setFromAmount(CommonUtils.longValue(span.getText().trim().replace(",", "")));
                }
                span = CommonUtils.getWebElementById(webDriver, "lblNewCapital");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setToAmount(CommonUtils.longValue(span.getText().trim().replace(",", "")));
                }

                span = CommonUtils.getWebElementById(webDriver, "lblCashIncomingCaption1");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setType(span.getText().trim());
                }

                span = CommonUtils.getWebElementById(webDriver, "txbStartDate");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setStartDate(span.getText().trim());
                }

                span = CommonUtils.getWebElementById(webDriver, "txbEndDate");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setEndDate(span.getText().trim());
                }

                span = CommonUtils.getWebElementById(webDriver, "txbDate");
                if (!CommonUtils.isNull(span)) {
                    priorityOrBuyShare.setConfirmDate(span.getText().trim());
                }

                capitalIncreaseDto.add(priorityOrBuyShare);
                webDriver.close();
            }
            WebElement li = CommonUtils.getWebElementByTitle(webDriverMain, "صفحه بعدی");
            flag = false;
            if (!CommonUtils.isNull(li)) {
                WebElement link = CommonUtils.getWebElement(li, "a");
                if (!CommonUtils.isNull(link)) {
                    if (!CommonUtils.isNull(link.getDomProperty("href"))) {
                        link.click();
                        flag = true;
                    }
                }
            }
        }
        webDriverMain.close();
        return capitalIncreaseDto;
    }

    public List<CapitalIncreaseDto> getCapitalIncrease(String letterType) throws Exception {
        CapitalIncreaseDto capitalIncreaseDto = new CapitalIncreaseDto();
        List<CapitalIncreaseDto> capitalIncreaseDtos = new ArrayList<>();
        this.webUrl += String.format("&LetterType=%s", letterType);
        WebDriver webDriverMain = new Selenium().webDriver();
        Thread.sleep(10000);
        webDriverMain.get(webUrl);
        Thread.sleep(10000);
        List<String> links = new ArrayList<>();
        List<WebElement> elements = webDriverMain.findElements(By.className("letter-title"));
        for (WebElement webElement : elements) {
            links.add(webElement.getDomProperty("href"));
        }
        for (String link : links) {
            capitalIncreaseDto = new CapitalIncreaseDto();
            capitalIncreaseDto.setLink(link);
            WebDriver webDriver = new Selenium().webDriver();
            Thread.sleep(10000);
            webDriver.get(link);
            Thread.sleep(10000);

            String company = webDriver.findElement(By.id("lblCompany")).getText();
            capitalIncreaseDto.setBourseAccount(company);
            if (company.indexOf(":") != -1) {
                capitalIncreaseDto.setBourseAccount(webDriver.findElement(By.id("lblCompany")).getText().split(":")[1].trim());
            }

            WebElement span = CommonUtils.getWebElementById(webDriver, "lblLastBoardMemberInvitation");
            if (!CommonUtils.isNull(span)) {
                capitalIncreaseDto.setMeetingDate(span.getText().trim());
            }

            WebElement table = CommonUtils.getWebElementById(webDriver, "tblCapitalIncrease");
            if (!CommonUtils.isNull(table)) {
                capitalIncreaseDto.setDecisionsBoards(getDecisionsBoard(table));
            }
            capitalIncreaseDtos.add(capitalIncreaseDto);
            webDriver.close();
        }
        webDriverMain.close();
        return capitalIncreaseDtos;
    }

    private List<DecisionsBoard> getDecisionsBoard(WebElement table) {
        DecisionsBoard decisionsBoard = new DecisionsBoard();
        List<DecisionsBoard> decisionsBoards = new ArrayList<>();
        WebElement tbody = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));
        for (int j = 2; j < rows.size(); j++) {
            List<WebElement> columns = rows.get(j).findElements(By.tagName("td"));
            decisionsBoard = new DecisionsBoard();
            for (int i = 0; i < columns.size(); i++) {
                if (i == 0) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    WebElement input = CommonUtils.getWebElement(span, "input");
                    decisionsBoard.setUnitCount(CommonUtils.longValue(input.getDomProperty("value").replace(",", "").trim()));
                } else if (i == 1) {
                    WebElement input = CommonUtils.getWebElement(columns.get(i), "input");
                    decisionsBoard.setNominalValue(CommonUtils.longValue(input.getDomProperty("value").replace(",", "").trim()));
                } else if (i == 2) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    WebElement input = CommonUtils.getWebElement(span, "input");
                    decisionsBoard.setAmount(CommonUtils.longValue(input.getDomProperty("value").replace(",", "").trim()));
                } else if (i == 3) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    span = CommonUtils.getWebElement(span, "span");
                    decisionsBoard.setCash(CommonUtils.longValue(span.getText().replace(",", "").trim()));
                } else if (i == 4) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    span = CommonUtils.getWebElement(span, "span");
                    decisionsBoard.setAccumulatedProfit(CommonUtils.longValue(span.getText().replace(",", "").trim()));
                } else if (i == 5) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    span = CommonUtils.getWebElement(span, "span");
                    decisionsBoard.setSavedUp(CommonUtils.longValue(span.getText().replace(",", "").trim()));
                } else if (i == 6) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    span = CommonUtils.getWebElement(span, "span");
                    decisionsBoard.setAsset(CommonUtils.longValue(span.getText().replace(",", "").trim()));
                } else if (i == 7) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    span = CommonUtils.getWebElement(span, "span");
                    decisionsBoard.setStock(CommonUtils.longValue(span.getText().replace(",", "").trim()));
                } else if (i == 8) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    span = CommonUtils.getWebElement(span, "span");
                    decisionsBoard.setEntryCash(CommonUtils.longValue(span.getText().replace(",", "").trim()));
                } else if (i == 9) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    WebElement input = CommonUtils.getWebElement(span, "input");
                    decisionsBoard.setCapitalIncrease(CommonUtils.longValue(input.getDomProperty("value").replace(",", "").trim()));
                } else if (i == 10) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    WebElement input = CommonUtils.getWebElement(span, "input");
                    decisionsBoard.setPercentIncrease(CommonUtils.longValue(input.getDomProperty("value").replace(",", "").trim()));
                } else if (i == 11) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    WebElement input = CommonUtils.getWebElement(span, "input");
                    decisionsBoard.setUnitCountNotRegistered(CommonUtils.longValue(input.getDomProperty("value").replace(",", "").trim()));
                } else if (i == 12) {
                    WebElement span = CommonUtils.getWebElement(columns.get(i), "span");
                    WebElement input = CommonUtils.getWebElement(span, "input");
                    decisionsBoard.setNominalValueNotRegistered(CommonUtils.longValue(input.getDomProperty("value").replace(",", "").trim()));
                } else if (i == 13) {
                    WebElement input = CommonUtils.getWebElement(columns.get(i), "input");
                    decisionsBoard.setAmountNotRegistered(CommonUtils.longValue(input.getDomProperty("value").replace(",", "").trim()));
                }
            }
            decisionsBoards.add(decisionsBoard);
        }
        return decisionsBoards;
    }

    public List<DecisionDto> getDecisionList(String letterType) throws Exception {
        DecisionDto decisionDto = new DecisionDto();
        List<DecisionDto> decisionDtos = new ArrayList<>();
        this.webUrl += String.format("&LetterType=%s", letterType);
        WebDriver webDriverMain = new Selenium().webDriver();
        Thread.sleep(10000);
        webDriverMain.get(webUrl);
        Thread.sleep(10000);
        boolean flag = true;
        while (flag) {
            List<String> links = new ArrayList<>();
            List<WebElement> elements = webDriverMain.findElements(By.className("letter-title"));
            for (WebElement webElement : elements) {
                links.add(webElement.getDomProperty("href"));
            }

            for (String link : links) {
                decisionDto = new DecisionDto();
                decisionDto.setLink(link);
                WebDriver webDriver = new Selenium().webDriver();
                Thread.sleep(10000);
                webDriver.get(link);

                String company = webDriver.findElement(By.id("lblCompany")).getText();
                decisionDto.setBourseAccount(company);
                if (company.indexOf(":") != -1) {
                    decisionDto.setBourseAccount(webDriver.findElement(By.id("lblCompany")).getText().split(":")[1].trim());
                }

                List<WebElement> list = webDriver.findElements(By.tagName("bdo"));
                decisionDto.setMeetingDate(list.get(1).getText());

                Thread.sleep(10000);
                WebElement table = CommonUtils.getWebElementById(webDriver, "ucAssemblyPRetainedEarning_grdAssemblyProportionedRetainedEarning");
                if (!CommonUtils.isNull(table)) {
                    decisionDto.setAssemblyDecisions(getAssemblyDecisions(table));
                }
                decisionDtos.add(decisionDto);
                webDriver.close();
            }
            WebElement li = CommonUtils.getWebElementByTitle(webDriverMain, "صفحه بعدی");
            flag = false;
            if (!CommonUtils.isNull(li)) {
                WebElement link = CommonUtils.getWebElement(li, "a");
                if (!CommonUtils.isNull(link)) {
                    if (!CommonUtils.isNull(link.getDomProperty("href"))) {
                        link.click();
                        flag = true;
                    }
                }
            }
        }
        webDriverMain.close();
        return decisionDtos;
    }

    private List<AssemblyDecisions> getAssemblyDecisions(WebElement table) {
        AssemblyDecisions assemblyDecisions = new AssemblyDecisions();
        List<AssemblyDecisions> assemblyDecisionsList = new ArrayList<>();
        WebElement tbody = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));
        for (int j = 2; j < rows.size(); j++) {
            List<WebElement> columns = rows.get(j).findElements(By.tagName("td"));
            assemblyDecisions = new AssemblyDecisions();
            for (int i = 1; i < columns.size(); i++) {
                if (i == 1) {
                    WebElement span = columns.get(i).findElement(By.tagName("span"));
                    assemblyDecisions.setDescription(span.getText().trim());
                } else if (i == 2) {
                    WebElement input = columns.get(i).findElement(By.tagName("input"));
                    assemblyDecisions.setAmount(CommonUtils.longValue(input.getDomProperty("value").trim().replace(",", "")));
                }
            }
            assemblyDecisionsList.add(assemblyDecisions);
        }
        return assemblyDecisionsList;
    }

    public List<ExtraAssemblyDto> getExtraAssemblyList(String letterType) throws Exception {
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        ExtraAssemblyDto extraAssemblyDto = new ExtraAssemblyDto();
        this.webUrl += String.format("&LetterType=%s", letterType);
        WebDriver webDriverMain = new Selenium().webDriver();
        Thread.sleep(10000);
        webDriverMain.get(webUrl);
        Thread.sleep(10000);
        Boolean flag = true;
        while (flag) {
            List<WebElement> elements = webDriverMain.findElements(By.className("letter-title"));
            List<String> stringList = new ArrayList<>();
            for (WebElement webElement : elements) {
                stringList.add(webElement.getDomProperty("href"));
            }
            for (String ietm : stringList) {
                WebDriver webDriver = new Selenium().webDriver();
                Thread.sleep(10000);
                extraAssemblyDto = new ExtraAssemblyDto();
                extraAssemblyDto.setLink(ietm);
                System.out.println(ietm);
                webDriver.get(ietm);
                Thread.sleep(10000);
                String company = webDriver.findElement(By.id("lblCompany")).getText();
                if (company.indexOf(":") != -1) {
                    extraAssemblyDto.setBourseAccount(CommonUtils.getBourseAccount(webDriver.findElement(By.id("lblCompany")).getText().split(":")[1].trim()));
                    extraAssemblyDto.setCompany(webDriver.findElement(By.id("lblCompany")).getText().split(":")[0].trim().split("-")[0].trim());
                } else {
                    extraAssemblyDto.setBourseAccount(CommonUtils.getBourseAccount(company));
                }
                elements = webDriver.findElements(By.tagName("bdo"));
                extraAssemblyDto.setMeetingDate(elements.get(2).getText());
                WebElement table = CommonUtils.getWebElementById(webDriver, "ucAssemblyShareHolder1_gvAssemblyShareHolder");
                if (!CommonUtils.isNull(table))
                    extraAssemblyDto.setPeoplePrsentInMeetingList(getPeoplePrsentInMeetings(table));
                table = CommonUtils.getWebElementById(webDriver, "ucExtraAssemblyCapital1_tblCapitalIncrease");
                if (!CommonUtils.isNull(table)) extraAssemblyDto.setDecisionsMades(getDecisionsMades(table));
                extraAssemblyDtos.add(extraAssemblyDto);
                webDriver.close();
            }
            WebElement li = CommonUtils.getWebElementByTitle(webDriverMain, "صفحه بعدی");
            flag = false;
            if (!CommonUtils.isNull(li)) {
                WebElement link = CommonUtils.getWebElement(li, "a");
                if (!CommonUtils.isNull(link)) {
                    if (!CommonUtils.isNull(link.getDomProperty("href"))) {
                        link.click();
                        flag = true;
                    }
                }
            }
        }
        webDriverMain.close();
        return extraAssemblyDtos;
    }

    private List<DecisionsMade> getDecisionsMades(WebElement table) {
        List<DecisionsMade> decisionsMades = new ArrayList<>();
        DecisionsMade decisionsMade = new DecisionsMade();
        WebElement tbody = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));
        WebElement element;
        WebElement span;
        for (int j = 0; j < rows.size(); j++) {
            if (j > 1 && j < rows.size() - 1) {
                WebElement row = rows.get(j);
                List<WebElement> columns = row.findElements(By.tagName("td"));
                decisionsMade = new DecisionsMade();
                for (int i = 0; i < columns.size(); i++) {
                    if (j == 2 && i == 0) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        else span = CommonUtils.getWebElement(span, "input");
                        decisionsMade.setUnitCount(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));

                    } else if (j == 2 && i == 1) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        else span = CommonUtils.getWebElement(span, "input");
                        decisionsMade.setNominalValue(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));
                    } else if (j == 2 && i == 2) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        else span = CommonUtils.getWebElement(span, "input");
                        decisionsMade.setAmount(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));
                    } else if ((j == 2 && i == 3) || (j > 2 && i == 0)) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = CommonUtils.getWebElement(element, "span");
                            if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        } else {
                            element = span;
                            span = CommonUtils.getWebElement(span, "span");
                            if (CommonUtils.isNull(span)) span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setCash(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 4) || (j > 2 && i == 1)) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = CommonUtils.getWebElement(element, "span");
                            if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        } else {
                            element = span;
                            span = CommonUtils.getWebElement(span, "span");
                            if (CommonUtils.isNull(span)) span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setAccumulatedProfit(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 5) || (j > 2 && i == 2)) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = CommonUtils.getWebElement(element, "span");
                            if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        } else {
                            element = span;
                            span = CommonUtils.getWebElement(span, "span");
                            if (CommonUtils.isNull(span)) span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setSavedUp(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 6) || (j > 2 && i == 3)) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = CommonUtils.getWebElement(element, "span");
                            if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        } else {
                            element = span;
                            span = CommonUtils.getWebElement(span, "span");
                            if (CommonUtils.isNull(span)) span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setAsset(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 7) || (j > 2 && i == 4)) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = CommonUtils.getWebElement(element, "span");
                            if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        } else {
                            element = span;
                            span = CommonUtils.getWebElement(span, "span");
                            if (CommonUtils.isNull(span)) span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setStock(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 8) || (j > 2 && i == 5)) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = CommonUtils.getWebElement(element, "span");
                            if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        } else {
                            element = span;
                            span = CommonUtils.getWebElement(span, "span");
                            if (CommonUtils.isNull(span)) span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setEntryCash(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 9) || (j > 2 && i == 6)) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        else span = CommonUtils.getWebElement(span, "input");
                        decisionsMade.setCapitalIncrease(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));
                    } else if ((j == 2 && i == 10) || (j > 2 && i == 7)) {
                        element = columns.get(i);
                        span = CommonUtils.getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) span = CommonUtils.getWebElement(element, "input");
                        else span = CommonUtils.getWebElement(span, "input");
                        decisionsMade.setPercentIncrease(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));
                    } else if ((j == 2 && i == 11) || (j > 2 && i == 8)) {
                        decisionsMade.setApproveType(columns.get(i).getText().trim());
                    }
                }
                if (columns.size() > 0) decisionsMades.add(decisionsMade);
            }
        }
        for (int i = 0; i < decisionsMades.size(); i++) {
            decisionsMades.get(i).setUnitCount(decisionsMades.get(0).getUnitCount());
            decisionsMades.get(i).setNominalValue(decisionsMades.get(0).getNominalValue());
            decisionsMades.get(i).setAmount(decisionsMades.get(0).getAmount());
        }
        return decisionsMades;
    }

    private List<PeoplePrsentInMeeting> getPeoplePrsentInMeetings(WebElement table) {
        List<PeoplePrsentInMeeting> peoplePrsentInMeetingList = new ArrayList<>();
        PeoplePrsentInMeeting peoplePrsentInMeeting = new PeoplePrsentInMeeting();
        WebElement tbody = table.findElement(By.tagName("tbody"));
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            peoplePrsentInMeeting = new PeoplePrsentInMeeting();
            for (int i = 0; i < columns.size(); i++) {
                if (i == 0) peoplePrsentInMeeting.setShareholderName(columns.get(i).getText());
                else if (i == 1) peoplePrsentInMeeting.setUnitCount(CommonUtils.longValue(columns.get(i).getText()));
                else if (i == 2)
                    peoplePrsentInMeeting.setSharePercent(CommonUtils.floatValue(columns.get(i).getText().replace("%", "").trim()));
            }
            if (columns.size() > 0) peoplePrsentInMeetingList.add(peoplePrsentInMeeting);
        }
        return peoplePrsentInMeetingList;
    }

    @Transactional
    public void saveIncomeStatement() throws Exception {
        List<Columns> columnsList = columnsService.findAll(Columns.class);
        List<InterimStatementDto> interimStatementDtos = interimStatementDtoMeetingService.findAll(InterimStatementDto.class);
        List<Instrument> instrumentList = instrumentJPAGenericService.findAll(Instrument.class);
        for (InterimStatementDto interimStatementDto : interimStatementDtos) {
            IncomeStatement incomeStatement = new IncomeStatement();
            IncomeStatementDetail incomeStatementDetail = new IncomeStatementDetail();
            incomeStatement.setInstrumentID(instrumentList.stream().filter(a -> CommonUtils.replaceFarsiChars(a.getInstrumentName()).equals(CommonUtils.replaceFarsiChars(interimStatementDto.getBourseAccount()))).findFirst().get().getId());
            incomeStatement.setFiscalYear(interimStatementDto.getDate());
            switch (interimStatementDto.getPeriod().trim()) {
                case "9 ماهه" -> incomeStatement.setFinancialStatementsPeriodId(2L);
                case "6 ماهه" -> incomeStatement.setFinancialStatementsPeriodId(3L);
                case "12 ماهه" -> incomeStatement.setFinancialStatementsPeriodId(1L);
                case "3 ماهه" -> incomeStatement.setFinancialStatementsPeriodId(4L);
            }
            incomeStatement.setIndustryId(instrumentList.stream().filter(a -> CommonUtils.replaceFarsiChars(a.getInstrumentName()).equals(CommonUtils.replaceFarsiChars(interimStatementDto.getBourseAccount()))).findFirst().get().getIndustryId());

            if (interimStatementDto.getFinancialStatementStatus().equals("حسابرسی شده")) {
                incomeStatement.setIsAudited(true);
            } else {
                incomeStatement.setIsAudited(false);
            }
            incomeStatement.setEndTo(interimStatementDto.getEndDate());
            incomeStatementService.insert(incomeStatement);
            List<BalanceSheet> profitAndStatement = interimStatementDto.getProfitAndStatement();
            List<IndustryColumn> industryColumnList = industryColumnService.findAll(IndustryColumn.class);
            for (BalanceSheet item : profitAndStatement) {
                Optional<Columns> columns = columnsList.stream().filter(a -> CommonUtils.replaceFarsiChars(a.getCaption()).trim().equals(CommonUtils.replaceFarsiChars(item.getDescription()).trim())).findFirst();
                if (columns.isPresent()) {
                    Long columnId = columns.get().getId();
                    incomeStatementDetail = new IncomeStatementDetail();
                    incomeStatementDetail.setIncomeStatementId(incomeStatement.getId());
                    Long industryColumnId = industryColumnList.stream().filter(a -> a.getIndustryId().equals(incomeStatement.getIndustryId()) && a.getColumnId().equals(columnId)).findFirst().get().getId();
                    incomeStatementDetail.setValue(item.getActualPerformance());
                    incomeStatementDetail.setIndustryColumnId(industryColumnId);
                    incomeStatementDetailService.insert(incomeStatementDetail);
                }
            }
        }
    }

    @Transactional
    public List<CodalShareholderMeeting> saveCodalShareHolderMeeting() throws Exception {
        List<Instrument> instrumentList = instrumentJPAGenericService.findAll(Instrument.class);
        List<ExtraAssemblyDto> extraAssemblyDtos = extraAssembly.findAll(ExtraAssemblyDto.class);
        List<CodalShareholderMeeting> codalShareholderMeetings = new ArrayList<>();
        StringBuilder bourseAccount = new StringBuilder();
        for (ExtraAssemblyDto extraAssemblyDto : extraAssemblyDtos) {
            CodalShareholderMeeting codalShareholderMeeting = CommonUtils.convertTo(extraAssemblyDto, LettersTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING);
            Optional<Instrument> instrument = instrumentList.stream().filter(a -> CommonUtils.replaceFarsiChars(a.getInstrumentName()).equals(CommonUtils.replaceFarsiChars(extraAssemblyDto.getBourseAccount()))).findFirst();
            if (instrument.isPresent()) {
                codalShareholderMeeting.setInstrument(instrument.get());
                if (codalShareholderMeeting.getMeetingStatus().getId().equals(MeetingStatuses.NOT_HAVE_TABLE.getValue()) ||
                        codalShareholderMeeting.getMeetingStatus().getId().equals(MeetingStatuses.ZERO_SHAREHOLDER_MEETING.getValue()))
                    continue;
                theoreticalPrice(codalShareholderMeeting);
                codalShareholderMeetings.add(codalShareholderMeeting);
                codalShareholderMeetingGenericService.insert(codalShareholderMeeting);
            } else {
                bourseAccount.append(extraAssemblyDto.getBourseAccount()).append(" ,");
            }
        }
        System.out.println("have.not.instrument: " + bourseAccount);
        return codalShareholderMeetings;
    }

    public void theoreticalPrice(CodalShareholderMeeting codalShareholderMeeting) throws Exception {
        String hql = "select p from instrumentPriceDate p where p.instrument= :instrumentId and  p.priceDate in (SELECT max(priceDate)  FROM instrumentPriceDate o where o.priceDate<=:meetingDate and o.instrument=:instrumentId)";
        Query query = entityManager.createQuery(hql);
        Map<String, Object> param = new HashMap<>();
        param.put("meetingDate", codalShareholderMeeting.getMeetingDate());
        param.put("instrumentId", codalShareholderMeeting.getInstrument());
        List<InstrumentPriceDate> instrumentPriceDates = (List<InstrumentPriceDate>) instrumentPriceDateJPAGenericService.listByQuery(query, param);
        InstrumentPriceDate instrumentPriceDate = new InstrumentPriceDate();
        List<InstrumentData> instrumentDataList = new ArrayList<>();
        if (instrumentPriceDates.isEmpty()) {
            instrumentDataList = tsetmcService.getTradeDate(codalShareholderMeeting.getBourseAccount(), String.valueOf(codalShareholderMeeting.getMeetingDate()));
            for (InstrumentData instrumentData : instrumentDataList) {
                instrumentPriceDate = CommonUtils.convertTo(instrumentData);
            }
        } else {
            instrumentPriceDate = instrumentPriceDates.get(0);
            Long lsatDayPrice = instrumentPriceDate.getLastDayPrice();
            Long LastTradePrice = instrumentPriceDate.getLastTradePrice();
            Long shareProfit = 0L;
            double giftRecapPercent = 0D;
            double ipoRecapPercent = 0D;
            if (codalShareholderMeeting.getShareProfit() != null) {
                shareProfit = codalShareholderMeeting.getShareProfit();
            }
            if (codalShareholderMeeting.getGiftRecapPercent() != null) {
                giftRecapPercent = codalShareholderMeeting.getGiftRecapPercent();
            }
            if (codalShareholderMeeting.getIpoRecapPercent() != null) {
                ipoRecapPercent = codalShareholderMeeting.getIpoRecapPercent();
            }
            codalShareholderMeeting.setTheoreticalPriceLastDay((float) (((lsatDayPrice - shareProfit) + (ipoRecapPercent * 1000)) / (1 + (giftRecapPercent / 100) + (ipoRecapPercent / 100))));
            codalShareholderMeeting.setTheoreticalPriceLastTrade((float) (((LastTradePrice - shareProfit) + (ipoRecapPercent * 1000)) / (1 + (giftRecapPercent / 100) + (ipoRecapPercent / 100))));
        }
        System.out.println("have.not.price: " + codalShareholderMeeting.getBourseAccount());
    }
}


