package org.webCrawler.common;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webCrawler.dto.*;
import org.webCrawler.model.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
                return Long.valueOf(cleanTextNumber(arabicToDecimal(number.toString())));
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

    public static WebElement getWebElement(WebElement webElement, String tagName) {
        try {
            return webElement.findElement(By.tagName(tagName));
        } catch (Exception e) {
            return null;
        }
    }

    public static List<WebElement> getWebElements(WebElement webElement, String tagName) {
        try {
            return webElement.findElements(By.tagName(tagName));
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

    public static List<WebElement> getWebElementsById(WebElement webElement, String id) {
        try {
            return webElement.findElements(By.id(id));
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

    public static List<WebElement> getWebElementsById(WebDriver webDriver, String id) {
        try {
            return webDriver.findElements(By.id(id));
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

    public static List<WebElement> getWebElementsByClass(WebElement webElement, String className) {
        try {
            return webElement.findElements(By.className(className));
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

    public static String getAttribute(WebElement element, String attribute) {
        try {
            return element.getAttribute(attribute);
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
        if (!CommonUtils.isNull(extraAssemblyDto.getDecisionsMades()) && !extraAssemblyDto.getDecisionsMades().isEmpty()) {
            codalShareholderMeeting.setLastAssetAmount(extraAssemblyDto.getDecisionsMades().get(0).getAmount());
            codalShareholderMeeting.setIpoRecapAmount(extraAssemblyDto.getDecisionsMades().get(0).getCash());
            codalShareholderMeeting.setGiftRecapAmount(extraAssemblyDto.getDecisionsMades().get(0).getAccumulatedProfit());
            codalShareholderMeeting.setSavedRecap(extraAssemblyDto.getDecisionsMades().get(0).getSavedUp());
            codalShareholderMeeting.setExtraReErvalRecap(extraAssemblyDto.getDecisionsMades().get(0).getAsset());
            codalShareholderMeeting.setStockDifferenceRecap(extraAssemblyDto.getDecisionsMades().get(0).getStock());
            codalShareholderMeeting.setDeprivationRight(extraAssemblyDto.getDecisionsMades().get(0).getEntryCash());
            LocalDate md = DateUtil.getGregorianDate(codalShareholderMeeting.getMeetingDate()).plusMonths(2);
            codalShareholderMeeting.setIpoDate(DateUtil.getJalaliDate(md));
            codalShareholderMeeting.setRenewedIpoDate(DateUtil.getJalaliDate(md));
            recapPercent(codalShareholderMeeting);
        } else
            codalShareholderMeeting.setMeetingStatus(MeetingStatus.builder().id(MeetingStatuses.NOT_HAVE_TABLE.getValue()).build());

        codalShareholderMeeting.setLetterType(LetterType.builder().id(lettersTypes.getLettersTypeValue()).build());
        codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.ANNUAL_SAHEHOLDER_MEETING.getValue()).build());
        if (lettersTypes.getLettersTypeValue().equals(LettersTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue()) ||
                lettersTypes.getLettersTypeValue().equals(LettersTypes.SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue()) ||
                lettersTypes.getLettersTypeValue().equals(LettersTypes.CAPITAL_INCREASE_SAHEHOLDER_MEETING.getLettersTypeValue()))
            codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getValue()).build());
        return codalShareholderMeeting;
    }

    public static CodalShareholderMeeting convertTo(DecisionDto decisionDto, LettersTypes lettersTypes) {
        CodalShareholderMeeting codalShareholderMeeting = new CodalShareholderMeeting();
        codalShareholderMeeting.setBourseAccount(decisionDto.getBourseAccount());
        codalShareholderMeeting.setUrl(decisionDto.getLink());
        codalShareholderMeeting.setMeetingDate(decisionDto.getMeetingDate());
        codalShareholderMeeting.setMeetingStatus(MeetingStatus.builder().id(MeetingStatuses.TRUE_SHAREHOLDER_MEETING.getValue()).build());
        if (!CommonUtils.isNull(decisionDto.getAssemblyDecisions()) && !decisionDto.getAssemblyDecisions().isEmpty()) {
            codalShareholderMeeting.setShareProfit(decisionDto.getAssemblyDecisions().stream().filter(a -> a.getDescription().equals("سود نقدی هر سهم (ریال)")).findFirst().get().getAmount());
        } else
            codalShareholderMeeting.setMeetingStatus(MeetingStatus.builder().id(MeetingStatuses.NOT_HAVE_TABLE.getValue()).build());

        codalShareholderMeeting.setLetterType(LetterType.builder().id(lettersTypes.getLettersTypeValue()).build());
        codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getValue()).build());
        if (lettersTypes.getLettersTypeValue().equals(LettersTypes.SUMMARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue()) ||
                lettersTypes.getLettersTypeValue().equals(LettersTypes.EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue()) ||
                lettersTypes.getLettersTypeValue().equals(LettersTypes.SUMMARY_EXTRAORDINARY_ANNUAL_SAHEHOLDER_MEETING.getLettersTypeValue()))
            codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.ANNUAL_SAHEHOLDER_MEETING.getValue()).build());

        return codalShareholderMeeting;
    }

    public static CodalShareholderMeeting convertTo(CapitalIncreaseDto capitalIncreaseDto, LettersTypes lettersTypes) {
        CodalShareholderMeeting codalShareholderMeeting = new CodalShareholderMeeting();
        codalShareholderMeeting.setBourseAccount(capitalIncreaseDto.getBourseAccount());
        codalShareholderMeeting.setUrl(capitalIncreaseDto.getLink());
        codalShareholderMeeting.setMeetingDate(capitalIncreaseDto.getMeetingDate());
        codalShareholderMeeting.setMeetingStatus(MeetingStatus.builder().id(MeetingStatuses.TRUE_SHAREHOLDER_MEETING.getValue()).build());
        if (!CommonUtils.isNull(capitalIncreaseDto.getDecisionsBoards()) && !capitalIncreaseDto.getDecisionsBoards().isEmpty()) {
            codalShareholderMeeting.setLastAssetAmount(capitalIncreaseDto.getDecisionsBoards().get(0).getAmount());
            codalShareholderMeeting.setIpoRecapAmount(capitalIncreaseDto.getDecisionsBoards().get(0).getCash());
            codalShareholderMeeting.setGiftRecapAmount(capitalIncreaseDto.getDecisionsBoards().get(0).getAccumulatedProfit());
            codalShareholderMeeting.setSavedRecap(capitalIncreaseDto.getDecisionsBoards().get(0).getSavedUp());
            codalShareholderMeeting.setExtraReErvalRecap(capitalIncreaseDto.getDecisionsBoards().get(0).getStock());
            codalShareholderMeeting.setStockDifferenceRecap(capitalIncreaseDto.getDecisionsBoards().get(0).getEntryCash());
            codalShareholderMeeting.setDeprivationRight(capitalIncreaseDto.getDecisionsBoards().get(0).getCapitalIncrease());
            LocalDate md = DateUtil.getGregorianDate(codalShareholderMeeting.getMeetingDate()).plusMonths(2);
            codalShareholderMeeting.setIpoDate(DateUtil.getJalaliDate(md));
            codalShareholderMeeting.setRenewedIpoDate(DateUtil.getJalaliDate(md));
            recapPercent(codalShareholderMeeting);
        } else
            codalShareholderMeeting.setMeetingStatus(MeetingStatus.builder().id(MeetingStatuses.NOT_HAVE_TABLE.getValue()).build());

        codalShareholderMeeting.setLetterType(LetterType.builder().id(lettersTypes.getLettersTypeValue()).build());
        codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.ANNUAL_SAHEHOLDER_MEETING.getValue()).build());
        if (lettersTypes.getLettersTypeValue().equals(LettersTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue()) ||
                lettersTypes.getLettersTypeValue().equals(LettersTypes.SUMMARY_EXTRAASSEMBLY_SAHEHOLDER_MEETING.getLettersTypeValue()) ||
                lettersTypes.getLettersTypeValue().equals(LettersTypes.CAPITAL_INCREASE_SAHEHOLDER_MEETING.getLettersTypeValue()))
            codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getValue()).build());
        return codalShareholderMeeting;
    }

    public static CodalShareholderMeeting convertTo(PriorityOrBuyShareDto priorityOrBuyShareDto, LettersTypes lettersTypes) {
        CodalShareholderMeeting codalShareholderMeeting = new CodalShareholderMeeting();
        codalShareholderMeeting.setBourseAccount(priorityOrBuyShareDto.getBourseAccount());
        codalShareholderMeeting.setUrl(priorityOrBuyShareDto.getLink());
        codalShareholderMeeting.setMeetingDate(priorityOrBuyShareDto.getMeetingDate());
        codalShareholderMeeting.setMeetingStatus(MeetingStatus.builder().id(MeetingStatuses.TRUE_SHAREHOLDER_MEETING.getValue()).build());

        if (lettersTypes.getLettersTypeValue().equals(LettersTypes.PRIORITYTIME_SAHEHOLDER_MEETING.getLettersTypeValue())) {
            codalShareholderMeeting.setIpoDate(priorityOrBuyShareDto.getEndDate());
            LocalDate md = DateUtil.getGregorianDate(priorityOrBuyShareDto.getEndDate()).plusDays(1);
            codalShareholderMeeting.setIpoDate(DateUtil.getJalaliDate(md));
            codalShareholderMeeting.setRenewedIpoDate(DateUtil.getJalaliDate(md));
        } else if (lettersTypes.getLettersTypeValue().equals(LettersTypes.POSTULATEDISCUSSION_SAHEHOLDER_MEETING.getLettersTypeValue())) {
            codalShareholderMeeting.setIpoDate(priorityOrBuyShareDto.getEndDate());
            LocalDate md = DateUtil.getGregorianDate(priorityOrBuyShareDto.getEndDate()).plusDays(1);
            codalShareholderMeeting.setRenewedIpoDate(DateUtil.getJalaliDate(md));
        } else if (lettersTypes.getLettersTypeValue().equals(LettersTypes.CAPITAL_INCREASE_REGISTRATION.getLettersTypeValue())) {
            codalShareholderMeeting.setConfirmDate(priorityOrBuyShareDto.getConfirmDate());
        }
        codalShareholderMeeting.setLetterType(LetterType.builder().id(lettersTypes.getLettersTypeValue()).build());
        codalShareholderMeeting.setMeetingType(MeetingType.builder().id(MeetingTypes.EXTRAASSEMBLY_SAHEHOLDER_MEETING.getValue()).build());
        return codalShareholderMeeting;
    }

    public static InstrumentPriceDate convertTo(InstrumentData instrumentData) {
        List<InstrumentPriceDate> instrumentPriceDates = new ArrayList<>();
//        Instrument instrument = new Instrument();
        InstrumentPriceDate instrumentPriceDate = new InstrumentPriceDate();
//        instrument.setId(instrumentList.stream().filter(a -> a.getTsetmsId().equals(instrumentData.getTsetmcId())).findFirst().get().getId());
//        instrumentPriceDate.setInstrument(instrument);
        instrumentPriceDate.setPriceDate(instrumentData.getDate());
        instrumentPriceDate.setFirstPrice(instrumentData.getFirstPrice());
        instrumentPriceDate.setLastDayPrice(instrumentData.getFinalPrice());
        instrumentPriceDate.setLastTradePrice(instrumentData.getLastPrice());
        instrumentPriceDate.setLowestPrice(instrumentData.getMinPrice());
        instrumentPriceDate.setHighestPrice(instrumentData.getMaxPrice());
        instrumentPriceDate.setValue(instrumentData.getValue());
        instrumentPriceDate.setValuePrice(instrumentData.getVolume());
        instrumentPriceDate.setNumberOfTransactions(instrumentData.getUnit());
        instrumentPriceDates.add(instrumentPriceDate);
        return instrumentPriceDate;
    }

    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static String replaceFarsiChars(String word) {
        word = word.replaceAll("ك", "ک");
        word = word.replaceAll("ي", "ی");
        return word;
    }

    public static String reverse(String word) {
        String nstr = "";
        char ch;

        for (int i = 0; i < word.length(); i++) {
            ch = word.charAt(i); //extracts each character
            nstr = ch + nstr; //adds each character in front of the existing string
        }
        return nstr;
    }

    public static boolean isUTF8(String word) {
        try {
            byte[] bytes = word.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return true;
    }

    public static CodalShareholderMeeting recapPercent(CodalShareholderMeeting codalShareholderMeeting) {
        codalShareholderMeeting.setGiftRecapPercent((double) ((codalShareholderMeeting.getGiftRecapAmount()  + codalShareholderMeeting.getDeprivationRight() +
                codalShareholderMeeting.getExtraReErvalRecap() + codalShareholderMeeting.getSavedRecap()) * 100) / codalShareholderMeeting.getLastAssetAmount());
        codalShareholderMeeting.setIpoRecapPercent((double) ((codalShareholderMeeting.getIpoRecapAmount() * 100) / codalShareholderMeeting.getLastAssetAmount()));
        return codalShareholderMeeting;
    }

    public static void checkInputData(String startDate, String endDate) throws Exception {
        LocalDate fromDate = DateUtil.getGregorianDate(startDate);
        if (CommonUtils.isNull(fromDate)) throw new RuntimeException("start.date.not.valid");
        LocalDate toDate = DateUtil.getGregorianDate(endDate);
        if (CommonUtils.isNull(toDate)) throw new RuntimeException("end.date.not.valid");
        if (startDate.compareTo(endDate) > 0) throw new RuntimeException("start.date.must.bigger.than.end.date");
    }
}

