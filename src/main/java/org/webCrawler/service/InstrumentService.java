package org.webCrawler.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.common.DateUtil;
import org.webCrawler.config.Selenium;
import org.webCrawler.dto.InstrumentInfo;

import java.time.LocalDate;
import java.util.List;

public class InstrumentService {
    private String webUrl = "https://databourse.ir/symbol/";

    public InstrumentService(String bourseAccount) {
        this.webUrl += bourseAccount;
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
}
