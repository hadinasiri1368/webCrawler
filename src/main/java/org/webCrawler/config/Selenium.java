package org.webCrawler.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.webCrawler.common.CommonUtils;

import java.io.File;

@Configuration
public class Selenium {
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public WebDriver webDriver(){
        ChromeDriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(CommonUtils.findFile()))
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
