package org.webCrawler.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.net.URL;
import java.util.List;

public class CommonUtils {
    private static final String CHROMEDRIVER_EXE = "D:\\java\\webCrawler\\chromedriver-win64\\chromedriver.exe";

    public static String findFile() {
        try {
            URL url = new File(CHROMEDRIVER_EXE).toURI().toURL();
            return url.getFile();
        } catch (Exception e) {
            return null;
        }
    }

    public static Long longValue(Object number) {
        if (isNull(number))
            return null;
        else if (number instanceof Number)
            return ((Number) number).longValue();
        else
            try {
                return Long.valueOf(cleanTextNumber(number.toString()));
            } catch (NumberFormatException e) {
                return null;
            }
    }

    public static boolean isNull(Object o) {
        if (o instanceof String) {
            if (o == null ||
                    ((String) o).isEmpty() ||
                    ((String) o).isBlank() ||
                    ((String) o).length() == 0 ||
                    ((String) o).toLowerCase().trim().equals("null")
            )
                return true;
            return false;
        }
        return o == null ? true : false;
    }

    public static <E> E isNull(E expr1, E expr2) {
        return (!isNull(expr1)) ? expr1 : expr2;
    }

    public static Double doubleValue(Object number) {
        if (isNull(number))
            return null;
        else if (number instanceof Number)
            return ((Number) number).doubleValue();
        else
            try {
                return Double.valueOf(cleanTextNumber(number.toString()));
            } catch (NumberFormatException e) {
                return null;
            }
    }

    public static Float floatValue(Object number) {
        if (isNull(number))
            return null;
        else if (number instanceof Number)
            return ((Number) number).floatValue();
        else
            try {
                return Float.valueOf(cleanTextNumber(number.toString()));
            } catch (NumberFormatException e) {
                return null;
            }
    }

    public static WebElement getById(WebDriver webDriver, String type, String id) {
        List<WebElement> list = webDriver.findElements(By.tagName(type));
        for (WebElement webElement : list) {
            if (webElement.getDomProperty("id").equals(id))
                return webElement;
            WebElement element = getById(webElement, type, id);
            if (!CommonUtils.isNull(element))
                return element;
        }
        return null;
    }

    public static WebElement getById(WebElement webElement, String type, String id) {
        List<WebElement> list = webElement.findElements(By.tagName(type));
        for (WebElement element : list) {
            if (element.getDomProperty("id").indexOf(id) != -1)
                return element;
            return getById(element, type, id);
        }
        return null;
    }

    public static WebElement getWebElement(WebElement webElement, String tagName) {
        try {
            return webElement.findElement(By.tagName(tagName));
        } catch (Exception e) {
            return null;
        }
    }

    public static WebElement getWebElementById(WebElement webElement, String id) {
        try {
            return webElement.findElement(By.id(id));
        } catch (Exception e) {
            return null;
        }
    }

    public static WebElement getWebElementById(WebDriver webDriver, String id) {
        try {
            return webDriver.findElement(By.id(id));
        } catch (Exception e) {
            return null;
        }
    }

    public static WebElement getWebElementByClass(WebDriver webDriver, String className) {
        try {
            return webDriver.findElement(By.className(className));
        } catch (Exception e) {
            return null;
        }
    }

    public static List<WebElement> getWebElementsByClass(WebDriver webDriver, String className) {
        try {
            return webDriver.findElements(By.className(className));
        } catch (Exception e) {
            return null;
        }
    }

    public static WebElement getWebElementByTitle(WebDriver webDriver, String title) {
        try {
            return webDriver.findElement(By.xpath("//*[@title='" + title + "']"));
        } catch (Exception e) {
            return null;
        }
    }

    public static String cleanTextNumber(String str) {
        str = str.trim();
        str = str.replace(" ", "");
        str = str.replace(",", "");
        str = str.replace("%", "");
        str = str.replace("(", "");
        str = str.replace(")", "");
        str = str.replaceAll("[^\\d.]", "");
        return str;
    }

}
