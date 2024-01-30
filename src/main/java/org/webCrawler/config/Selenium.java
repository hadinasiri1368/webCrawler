package org.webCrawler.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.webCrawler.common.CommonUtils;

import java.io.File;

public class Selenium {
    public WebDriver webDriver() {
        String driverFile = CommonUtils.findFile();
        if (driverFile == null)
            return null;
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(driverFile))
                .build();
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--no-sandbox"); // Bypass OS security model, MUST BE THE VERY FIRST OPTION
//        options.addArguments("--headless");
//        options.setExperimentalOption("useAutomationExtension", false);
//        options.addArguments("--start-minimized"); // open Browser in minimized mode
//        options.addArguments("start-maximized"); // open Browser in maximized mode
//        options.addArguments("disable-infobars"); // disabling infobars
//        options.addArguments("--disable-extensions"); // disabling extensions
//        options.addArguments("--disable-gpu"); // applicable to windows os only
//        options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--ignore-certificate-errors");
        return new ChromeDriver(service, options);
    }
}
