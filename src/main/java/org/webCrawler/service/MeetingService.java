package org.webCrawler.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.config.Selenium;
import org.webCrawler.dto.*;

import javax.lang.model.element.Element;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class MeetingService {
    private String date;
    private String webUrl = "https://www.codal.ir/ReportList.aspx?search&AuditorRef=-1&PageNumber=1&Audited&NotAudited&IsNotAudited=false&Childs&Mains&Publisher=false&CompanyState=-1&Category=-1&CompanyType=-1&Consolidatable&NotConsolidatable";

    public MeetingService(String date) {
        this.date = date;
        this.webUrl += String.format("&FromDate=%s&ToDate=%s", date, date);
    }

    public List<DecisionDto> getDecisionList() {
        DecisionDto decisionDto = new DecisionDto();
        List<DecisionDto> decisionDtos = new ArrayList<>();
        this.webUrl += String.format("&LetterType=%s", "20");
        WebDriver webDriverMain = new Selenium().webDriver();
        webDriverMain.get(webUrl);
        List<String> links = new ArrayList<>();
        List<WebElement> elements = webDriverMain.findElements(By.className("letter-title"));
        for (WebElement webElement : elements) {
            links.add(webElement.getDomProperty("href"));
        }
        for (String link : links) {
            decisionDto = new DecisionDto();
            decisionDto.setLink(link);
            WebDriver webDriver = new Selenium().webDriver();
            webDriver.get(link);
            WebElement table = getWebElementById(webDriver, "ucAssemblyPRetainedEarning_grdAssemblyProportionedRetainedEarning");
            if (!CommonUtils.isNull(table)) {
                decisionDto.setAssemblyDecisions(getAssemblyDecisions(table));
            }
            decisionDtos.add(decisionDto);
            webDriver.close();
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

    public List<ExtraAssemblyDto> getExtraAssemblyList() {
        List<ExtraAssemblyDto> extraAssemblyDtos = new ArrayList<>();
        ExtraAssemblyDto extraAssemblyDto = new ExtraAssemblyDto();
        this.webUrl += String.format("&LetterType=%s", "22");
        WebDriver webDriverMain = new Selenium().webDriver();
        webDriverMain.get(webUrl);
        List<WebElement> elements = webDriverMain.findElements(By.className("letter-title"));
        List<String> stringList = new ArrayList<>();
        for (WebElement webElement : elements) {
            stringList.add(webElement.getDomProperty("href"));
        }
        for (String ietm : stringList) {
            WebDriver webDriver = new Selenium().webDriver();
            extraAssemblyDto = new ExtraAssemblyDto();
            extraAssemblyDto.setLink(ietm);
            System.out.println(ietm);
            webDriver.get(ietm);
            extraAssemblyDto.setBourseAccount(webDriver.findElement(By.id("lblCompany")).getText().split(":")[1].trim());
            extraAssemblyDto.setCompany(webDriver.findElement(By.id("lblCompany")).getText().split(":")[0].trim().split("-")[0].trim());
            elements = webDriver.findElements(By.tagName("bdo"));
            extraAssemblyDto.setMeetingDate(elements.get(2).getText());
            WebElement table = getWebElementById(webDriver, "ucAssemblyShareHolder1_gvAssemblyShareHolder");
            if (!CommonUtils.isNull(table))
                extraAssemblyDto.setPeoplePrsentInMeetingList(getPeoplePrsentInMeetings(table));
            table = getWebElementById(webDriver, "ucExtraAssemblyCapital1_tblCapitalIncrease");
            if (!CommonUtils.isNull(table))
                extraAssemblyDto.setDecisionsMades(getDecisionsMades(table));
            extraAssemblyDtos.add(extraAssemblyDto);
            webDriver.close();
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
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span))
                            span = getWebElement(element, "input");
                        else
                            span = getWebElement(span, "input");
                        decisionsMade.setUnitCount(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));

                    } else if (j == 2 && i == 1) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span))
                            span = getWebElement(element, "input");
                        else
                            span = getWebElement(span, "input");
                        decisionsMade.setNominalValue(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));
                    } else if (j == 2 && i == 2) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span))
                            span = getWebElement(element, "input");
                        else
                            span = getWebElement(span, "input");
                        decisionsMade.setAmount(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));
                    } else if ((j == 2 && i == 3) || (j > 2 && i == 0)) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = getWebElement(element, "span");
                            if (CommonUtils.isNull(span))
                                span = getWebElement(element, "input");
                        } else {
                            element = span;
                            span = getWebElement(span, "span");
                            if (CommonUtils.isNull(span))
                                span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setCash(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 4) || (j > 2 && i == 1)) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = getWebElement(element, "span");
                            if (CommonUtils.isNull(span))
                                span = getWebElement(element, "input");
                        } else {
                            element = span;
                            span = getWebElement(span, "span");
                            if (CommonUtils.isNull(span))
                                span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setAccumulatedProfit(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 5) || (j > 2 && i == 2)) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = getWebElement(element, "span");
                            if (CommonUtils.isNull(span))
                                span = getWebElement(element, "input");
                        } else {
                            element = span;
                            span = getWebElement(span, "span");
                            if (CommonUtils.isNull(span))
                                span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setSavedUp(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 6) || (j > 2 && i == 3)) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = getWebElement(element, "span");
                            if (CommonUtils.isNull(span))
                                span = getWebElement(element, "input");
                        } else {
                            element = span;
                            span = getWebElement(span, "span");
                            if (CommonUtils.isNull(span))
                                span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setAsset(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 7) || (j > 2 && i == 4)) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = getWebElement(element, "span");
                            if (CommonUtils.isNull(span))
                                span = getWebElement(element, "input");
                        } else {
                            element = span;
                            span = getWebElement(span, "span");
                            if (CommonUtils.isNull(span))
                                span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setStock(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 8) || (j > 2 && i == 5)) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span)) {
                            span = getWebElement(element, "span");
                            if (CommonUtils.isNull(span))
                                span = getWebElement(element, "input");
                        } else {
                            element = span;
                            span = getWebElement(span, "span");
                            if (CommonUtils.isNull(span))
                                span = element;
                        }
                        String vlaue = CommonUtils.isNull(span.getText()) ? span.getDomProperty("value") : span.getText();
                        decisionsMade.setEntryCash(CommonUtils.longValue(vlaue.replace(",", "")));
                    } else if ((j == 2 && i == 9) || (j > 2 && i == 6)) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span))
                            span = getWebElement(element, "input");
                        else
                            span = getWebElement(span, "input");
                        decisionsMade.setCapitalIncrease(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));
                    } else if ((j == 2 && i == 10) || (j > 2 && i == 7)) {
                        element = columns.get(i);
                        span = getWebElement(element, "span");
                        if (CommonUtils.isNull(span))
                            span = getWebElement(element, "input");
                        else
                            span = getWebElement(span, "input");
                        decisionsMade.setPercentIncrease(CommonUtils.longValue(span.getDomProperty("value").replace(",", "")));
                    } else if ((j == 2 && i == 11) || (j > 2 && i == 8)) {
                        decisionsMade.setApproveType(columns.get(i).getText().trim());
                    }
                }
                if (columns.size() > 0)
                    decisionsMades.add(decisionsMade);
            }
        }
        for (int i = 0; i < decisionsMades.size(); i++) {
            decisionsMades.get(i).setUnitCount(decisionsMades.get(0).getUnitCount());
            decisionsMades.get(i).setNominalValue(decisionsMades.get(0).getNominalValue());
            decisionsMades.get(i).setAmount(decisionsMades.get(0).getAmount());
        }
        return decisionsMades;
    }

    private WebElement getWebElement(WebElement webElement, String tagName) {
        try {
            return webElement.findElement(By.tagName(tagName));
        } catch (Exception e) {
            return null;
        }
    }

    private WebElement getWebElementById(WebElement webElement, String id) {
        try {
            return webElement.findElement(By.id(id));
        } catch (Exception e) {
            return null;
        }
    }

    private WebElement getWebElementById(WebDriver webDriver, String id) {
        try {
            return webDriver.findElement(By.id(id));
        } catch (Exception e) {
            return null;
        }
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
                if (i == 0)
                    peoplePrsentInMeeting.setShareholderName(columns.get(i).getText());
                else if (i == 1)
                    peoplePrsentInMeeting.setUnitCount(CommonUtils.longValue(columns.get(i).getText()));
                else if (i == 2)
                    peoplePrsentInMeeting.setSharePercent(CommonUtils.floatValue(columns.get(i).getText().replace("%", "").trim()));
            }
            if (columns.size() > 0)
                peoplePrsentInMeetingList.add(peoplePrsentInMeeting);
        }
        return peoplePrsentInMeetingList;
    }

}

