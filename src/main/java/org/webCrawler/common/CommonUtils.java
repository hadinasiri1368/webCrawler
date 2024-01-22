package org.webCrawler.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webCrawler.dto.ExtraAssemblyDto;
import org.webCrawler.model.CodalShareholderMeeting;
import org.webCrawler.model.LetterType;
import org.webCrawler.model.MeetingStatus;
import org.webCrawler.model.MeetingType;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public static String getStringQuery(String query, Map<String, Object> param) {
        String returnValue = query + " where 1=1 ";
        for (String key : param.keySet()) {
            returnValue += String.format(" and %s = :%s", key, key);
            param.get(key);
        }
        return returnValue;
    }

    public static void setNull(Object entity) throws Exception {
        Class cls = Class.forName(entity.getClass().getName());
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1, field.getName().length());
            Method m = entity.getClass().getMethod("get" + name);
            Object o = m.invoke(entity);
            if (CommonUtils.isNull(o)) {
                Method method = entity.getClass().getMethod("set" + name, field.getType());
                method.invoke(entity, field.getType().cast(null));
            }
        }
    }

    public static String getBourseAccount(String bourseAccount) {
        if (bourseAccount.indexOf("(") != -1) {
            return bourseAccount.split("\\(")[0];
        }
        return bourseAccount;
    }

    public static CodalShareholderMeeting convertTo(ExtraAssemblyDto extraAssemblyDto, LettersTypes lettersTypes) {
        CodalShareholderMeeting codalShareholderMeeting = new CodalShareholderMeeting();
        codalShareholderMeeting.setBourseAccount(extraAssemblyDto.getBourseAccount());
        codalShareholderMeeting.setUrl(extraAssemblyDto.getLink());
        codalShareholderMeeting.setMeetingDate(extraAssemblyDto.getMeetingDate());
        codalShareholderMeeting.setMeetingStatus(MeetingStatus.builder().id(MeetingStatuses.TRUE_SHAREHOLDER_MEETING.getValue()).build());
        if (!CommonUtils.isNull(extraAssemblyDto.getDecisionsMades()) && extraAssemblyDto.getDecisionsMades().size() > 0) {
            codalShareholderMeeting.setLastAssetAmount(extraAssemblyDto.getDecisionsMades().get(0).getAmount());
            codalShareholderMeeting.setIpoRecapAmount(extraAssemblyDto.getDecisionsMades().get(0).getCash());
            codalShareholderMeeting.setGiftRecapAmount(extraAssemblyDto.getDecisionsMades().get(0).getAccumulatedProfit());
            codalShareholderMeeting.setSavedRecap(extraAssemblyDto.getDecisionsMades().get(0).getSavedUp());
            codalShareholderMeeting.setExtraReErvalRecap(extraAssemblyDto.getDecisionsMades().get(0).getStock());
            codalShareholderMeeting.setStockDifferenceRecap(extraAssemblyDto.getDecisionsMades().get(0).getEntryCash());
            codalShareholderMeeting.setDeprivationRight(extraAssemblyDto.getDecisionsMades().get(0).getCapitalIncrease());
            LocalDate md = DateUtil.getGregorianDate(codalShareholderMeeting.getMeetingDate()).plusMonths(2);
            codalShareholderMeeting.setIpoDate(DateUtil.getJalaliDate(md));
            codalShareholderMeeting.setRenewedIpoDate(DateUtil.getJalaliDate(md));
        } else
            codalShareholderMeeting.setMeetingStatus(MeetingStatus.builder().id(MeetingStatuses.NOT_HAVE_TABLE.getValue()).build());

        codalShareholderMeeting.setLetterType(LetterType.builder().id(lettersTypes.getLettersTypeValue()).build());
        codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.ANNUAL_SAHEHOLDER_MEETING.getValue()).build());
        if (lettersTypes.getLettersTypeValue() == LettersTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue() ||
                lettersTypes.getLettersTypeValue() == LettersTypes.SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue() ||
                lettersTypes.getLettersTypeValue() == LettersTypes.CAPITAL_INCREASE_SAHEHOLDER_MEETING.getLettersTypeValue())
            codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getValue()).build());
        return codalShareholderMeeting;
    }
}
