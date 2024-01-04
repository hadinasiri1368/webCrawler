package org.webCrawler.common;

import java.io.File;
import java.net.URL;

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
                return Long.valueOf(number.toString().trim());
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

    public static Double doubleValue(Object number) {
        if (isNull(number))
            return null;
        else if (number instanceof Number)
            return ((Number) number).doubleValue();
        else
            try {
                return Double.valueOf(number.toString().trim());
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
                return Float.valueOf(number.toString().trim());
            } catch (NumberFormatException e) {
                return null;
            }
    }
}
