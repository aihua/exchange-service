package biz.hengartner.euroexchange.api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    public static final String REQUEST_PARAM_ISO_DATE_FORMAT = "yyyy-MM-dd";

    public static final DateTimeFormatter DATE_TIME_FORMATTER_ISO_DATE = DateTimeFormatter.ISO_DATE;

    public static LocalDate parseIsoDate(String time) {
        return LocalDate.parse(time, DATE_TIME_FORMATTER_ISO_DATE);
    }
}
