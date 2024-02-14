package org.webCrawler.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webCrawler.common.CommonUtils;
import org.webCrawler.config.Selenium;
import org.webCrawler.dto.InstrumentDto;
import org.webCrawler.dto.InstrumentId;

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
}
