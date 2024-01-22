package org.webCrawler.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.config.Selenium;
import org.webCrawler.dto.InstrumentInfo;
import org.webCrawler.dto.MarketStatusDetailDto;
import org.webCrawler.dto.MarketStatusDto;
import org.webCrawler.dto.MarketStatusPerBourseAccountDto;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InstrumentService {
    private String webUrl = "https://databourse.ir/symbol/";

    public InstrumentService(String bourseAccount) {
        this.webUrl += bourseAccount;
    }

    public InstrumentService() {
        this.webUrl = "https://databourse.ir/stats";
    }

    public InstrumentInfo getInstrumentInfo() throws Exception {
        InstrumentInfo instrumentInfo = new InstrumentInfo();
        WebDriver webDriverMain = new Selenium().webDriver();
        Thread.sleep(10000);
        webDriverMain.get(webUrl);
        Thread.sleep(10000);
        instrumentInfo.setDate(DateUtil.getJalaliDate(LocalDate.now()));
        WebElement div = CommonUtils.getWebElementByClass(webDriverMain, "symbol-info");
        if (!CommonUtils.isNull(div)) {
            int index = 0;
            List<WebElement> list = div.findElements(By.className("symbol-row"));
            for (WebElement element : list) {
                List<WebElement> elementList = element.findElements(By.tagName("div"));
                if (index == 0) {
                    instrumentInfo.setBourseAccount(elementList.get(0).findElement(By.className("txt")).getText().trim());
                    instrumentInfo.setMarket(elementList.get(1).findElement(By.className("txt")).getText().trim());
                    instrumentInfo.setGroupName(elementList.get(2).findElement(By.className("txt")).getText().trim());
                } else if (index == 1) {
                    if (CommonUtils.isNull(elementList.get(0).findElement(By.className("num")).getDomProperty("title")))
                        instrumentInfo.setStockCount(CommonUtils.longValue(elementList.get(0).findElement(By.className("num")).getText().trim()));
                    else
                        instrumentInfo.setStockCount(CommonUtils.longValue(elementList.get(0).findElement(By.className("num")).getDomProperty("title").trim()));
                    instrumentInfo.setFloatingStock(CommonUtils.longValue(elementList.get(1).findElement(By.className("num")).getText().trim()));
                    instrumentInfo.setBaseVolume(CommonUtils.longValue(elementList.get(2).findElement(By.className("num")).getText().trim()));
                } else if (index == 2) {
                    instrumentInfo.setProfit(CommonUtils.longValue(elementList.get(0).findElement(By.className("num")).getText().trim()));
                    instrumentInfo.setValue(CommonUtils.doubleValue(elementList.get(1).findElement(By.className("num")).getText().trim()));
                    instrumentInfo.setGroupValue(CommonUtils.doubleValue(elementList.get(2).findElement(By.className("num")).getAttribute("innerHTML").trim()));
                }
                index++;
            }
        }
        List<WebElement> list = CommonUtils.getWebElementsByClass(webDriverMain, "voltraders");
        if (!CommonUtils.isNull(list)) {
            List<WebElement> divs = list.get(0).findElements(By.tagName("div"));
            String value = divs.get(0).getText().trim();
            instrumentInfo.setPurchaseCount(CommonUtils.longValue(value.split(" ")[0]));
            instrumentInfo.setPurchasePercent(CommonUtils.longValue(value.split(" ")[1]));
            value = divs.get(1).getText().trim();
            instrumentInfo.setCompanyPurchaseCount(CommonUtils.longValue(value.split(" ")[0]));
            instrumentInfo.setCompanyPurchasePercent(CommonUtils.longValue(value.split(" ")[1]));


            divs = list.get(1).findElements(By.tagName("div"));
            value = divs.get(0).getText().trim();
            instrumentInfo.setSaleCount(CommonUtils.longValue(value.split(" ")[0]));
            instrumentInfo.setSalePercent(CommonUtils.longValue(value.split(" ")[1]));
            value = divs.get(1).getText().trim();
            instrumentInfo.setCompanySaleCount(CommonUtils.longValue(value.split(" ")[0]));
            instrumentInfo.setCompanySalePercent(CommonUtils.longValue(value.split(" ")[1]));
        }

        List<WebElement> elementList = webDriverMain.findElements(By.className("legal_real_info"));
        if (!CommonUtils.isNull(elementList) && elementList.size() > 0) {
            int index = 0;
            for (WebElement element : elementList) {
                List<WebElement> webElement = element.findElements(By.tagName("div"));
                if (index == 0) {
                    instrumentInfo.setWithdrawalMoney(CommonUtils.longValue(webElement.get(0).findElements(By.tagName("span")).get(1).getText()));
                    instrumentInfo.setPowerSellers(CommonUtils.doubleValue(webElement.get(1).findElements(By.tagName("span")).get(1).getText()));
                } else if (index == 1) {
                    instrumentInfo.setPurchasePerCapita(CommonUtils.longValue(webElement.get(0).findElements(By.tagName("span")).get(1).getText()));
                    instrumentInfo.setSalePerCapita(CommonUtils.longValue(webElement.get(1).findElements(By.tagName("span")).get(1).getText()));
                }
                index++;
            }
        }
        WebElement webElement = webDriverMain.findElement(By.className("legalbuy"));
        if (!CommonUtils.isNull(webElement)) {
            instrumentInfo.setChangeOwnership(CommonUtils.longValue(webElement.getAttribute("innerHTML")));
        }
        return instrumentInfo;
    }

    public List<MarketStatusPerBourseAccountDto> getMarketStatusPerBourseAccountDto() throws Exception {
        List<MarketStatusPerBourseAccountDto> marketStatusPerBourseAccountDtos = new ArrayList<>();
        MarketStatusPerBourseAccountDto marketStatusPerBourseAccountDto = new MarketStatusPerBourseAccountDto();
        WebDriver webDriverMain = new Selenium().webDriver();
//        Thread.sleep(10000);
        webDriverMain.get(webUrl);
//        Thread.sleep(10000);
        WebElement groupList = CommonUtils.getWebElementByClass(webDriverMain, "groupList");
        if (!CommonUtils.isNull(groupList)) {
            WebElement element = groupList.findElement(By.tagName("select"));
//            Select select = new Select(element);
            List<WebElement> allOptions = element.findElements(By.xpath(".//option"));
            for (WebElement option : allOptions) {
                String value = option.getDomAttribute("value");
                WebDriver webDrive = new Selenium().webDriver();
//                Thread.sleep(10000);
                if (!CommonUtils.isNull(value)) {
                    marketStatusPerBourseAccountDto = new MarketStatusPerBourseAccountDto();
                    webDrive.get(webUrl + "/" + option.getDomProperty("value"));
//                    Thread.sleep(10000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    marketStatusPerBourseAccountDto.setDate(DateUtil.getJalaliDate(LocalDate.now()));
                    marketStatusPerBourseAccountDto.setTime(dateFormat.format(calendar.getTime()));
                    marketStatusPerBourseAccountDto.setGroupName(option.getAttribute("innerHTML".trim()));
                    List<WebElement> statList = CommonUtils.getWebElementsByClass(webDrive, "statList");
                    int index = 0;
                    for (WebElement item : statList) {
                        WebElement barGraph = item.findElement(By.className("barGraph"));
                        List<WebElement> barRow = barGraph.findElements(By.className("barRow"));
                        if (index == 0) {
                            marketStatusPerBourseAccountDto.setEnteredMoney(getMarketStatusDetailDto(barRow));
                        } else if (index == 1) {
                            marketStatusPerBourseAccountDto.setOutMoney(getMarketStatusDetailDto(barRow));
                        }
                        index++;
                    }
                    marketStatusPerBourseAccountDtos.add(marketStatusPerBourseAccountDto);
                }
                webDrive.close();
            }
            webDriverMain.close();
        }
        return marketStatusPerBourseAccountDtos;
    }

    public MarketStatusDto getMarketStatusDto() throws Exception {
        MarketStatusDto marketStatusDto = new MarketStatusDto();
        WebDriver webDriverMain = new Selenium().webDriver();
        Thread.sleep(10000);
        webDriverMain.get(webUrl);
        Thread.sleep(10000);
        marketStatusDto.setDate(DateUtil.getJalaliDate(LocalDate.now()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        marketStatusDto.setTime(dateFormat.format(calendar.getTime()));
        WebElement div = CommonUtils.getWebElementById(webDriverMain, "daily-invest");
        List<WebElement> elementList = div.findElements(By.className("statList"));
        int index = 0;
        for (WebElement element : elementList) {
            WebElement barGraph = element.findElement(By.className("barGraph"));
            List<WebElement> barRow = barGraph.findElements(By.className("barRow"));
            if (index == 0) {
                marketStatusDto.setEnteredMoney(getMarketStatusDetailDto(barRow));
            } else if (index == 1) {
                marketStatusDto.setOutMoney(getMarketStatusDetailDto(barRow));
            } else if (index == 2) {
                marketStatusDto.setCompanyEnteredMoney(getMarketStatusDetailDto(barRow));
            } else if (index == 3) {
                marketStatusDto.setCompanyOutMoney(getMarketStatusDetailDto(barRow));
            }
            index++;
        }
        return marketStatusDto;
    }

    private List<MarketStatusDetailDto> getMarketStatusDetailDto(List<WebElement> barRow) {
        List<MarketStatusDetailDto> marketStatusDetailDtos = new ArrayList<>();
        for (WebElement item : barRow) {
            WebElement lable = item.findElement(By.className("label"));
            WebElement bar = item.findElement(By.className("bar"));
            bar = bar.findElement(By.tagName("div"));
            marketStatusDetailDtos.add(new MarketStatusDetailDto(lable.getDomProperty("title").trim(), CommonUtils.doubleValue(bar.getDomProperty("title"))));
        }
        return marketStatusDetailDtos;
    }
}
