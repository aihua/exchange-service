package biz.hengartner.euroexchange.app.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Provide date-conversion and date-formats.
 */
public class DateHelper {

    public static final String REQUEST_PARAM_ISO_DATE_FORMAT = "yyyy-MM-dd";

    public static final DateTimeFormatter DATE_TIME_FORMATTER_ISO_DATE = DateTimeFormatter.ISO_DATE;

    public static LocalDate parseIsoDate(String time) {
        return LocalDate.parse(time, DATE_TIME_FORMATTER_ISO_DATE);
    }

    public static String toIsoFormat(LocalDate date) {
        return DATE_TIME_FORMATTER_ISO_DATE.format(date);
    }
}
