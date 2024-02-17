package org.webCrawler.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.config.Selenium;
import org.webCrawler.dto.InstrumentData;
import org.webCrawler.dto.InstrumentDto;
import org.webCrawler.dto.InstrumentId;
import org.webCrawler.dto.Trades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TSETMCService {
    private String webUrl = "https://old.tsetmc.com/Loader.aspx?ParTree=15131F";

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
                webDriverMain.findElement(By.id("InputFilterCode")).sendKeys("(bvol)>=1");
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
                webDriverMain.findElement(By.id("InputFilterCode")).sendKeys("(bvol)<=-1");
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

    public List<InstrumentData> getInstrumentData(List<InstrumentDto> instrumentDtos) throws Exception {
        WebDriver webDriverMain = new Selenium().webDriver();
        List<InstrumentData> instrumentDataList = new ArrayList<>();
        for (InstrumentDto item : instrumentDtos) {
            webDriverMain.get(item.getInstrumentLink());
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
            for (int i = 1; i < pagelinks.size(); i++) {
                if (i > 1) {
                    String index = i + "";
                    paging = webDriverMain.findElement(By.id("paging"));
                    paging = paging.findElement(By.className("pagingBlock"));
                    pagelinks = paging.findElements(By.tagName("a"));
                    pagelinks.stream().filter(a -> a.getAttribute("innerHTML").equals(index)).findFirst().get().click();
                    Thread.sleep(10000);
                }
                WebElement table = webDriverMain.findElement(By.id("trade")).findElement(By.className("objbox")).findElement(By.tagName("table")).findElement(By.tagName("tbody"));
                List<WebElement> trs = table.findElements(By.tagName("tr"));
                for (int j = 1; j < trs.size(); j++) {
                    List<WebElement> tds = trs.get(j).findElements(By.tagName("td"));
                    InstrumentData instrumentData = new InstrumentData();
                    instrumentData.setBourseAccount(item.getBourseAccount());
                    instrumentData.setId(getId(item.getInstrumentLink()));
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
        WebDriver webDriverMain = new Selenium().webDriver();
        List<Trades> tradesList = new ArrayList<>();
        for (InstrumentData item : instrumentData) {
            LocalDate localDate = DateUtil.getGregorianDate(item.getDate());
            String link = String.format("https://cdn.tsetmc.com/History/%s/%s", instrumenId, localDate.getYear() + "" + (localDate.getMonthValue() < 10 ? "0" + localDate.getMonthValue() : localDate.getMonthValue()) + "" + (localDate.getDayOfMonth() < 10 ? "0" + localDate.getDayOfMonth() : localDate.getDayOfMonth()));
            try {
                webDriverMain.get(link);
                Thread.sleep(10000);
                List<WebElement> links = webDriverMain.findElement(By.className("menu2")).findElements(By.tagName("a"));
                links.stream().filter(a -> a.getAttribute("innerHTML").equals("معاملات")).findFirst().get().click();
                Thread.sleep(10000);
                WebElement div = webDriverMain.findElement(By.className("ag-center-cols-container"));
                List<WebElement> rows = div.findElements(By.className("ag-row"));
                for (int i = 0; i < rows.size(); i++) {
                    List<WebElement> tds = rows.get(i).findElements(By.tagName("div"));
                    Trades trades = new Trades();
                    trades.setDate(item.getDate());
                    trades.setBourseAccount(item.getBourseAccount());
                    for (int j = 0; j < tds.size(); j++) {
                        switch (j) {
                            case 1:
                                trades.setTime(tds.get(j).getText());
                                break;
                            case 2:
                                trades.setVolume(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(j).getText())));
                                break;
                            case 3:
                                trades.setPrice(CommonUtils.longValue(CommonUtils.cleanTextNumber(tds.get(j).getText())));
                                break;
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
}
