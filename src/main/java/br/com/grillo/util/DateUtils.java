package br.com.grillo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    private DateUtils() {
        throw new AssertionError("Class nao pode ser instanciada");
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static LocalDateTime getLocalDateTimeFromString(String dateAsString) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT);
        Date dateISO8601 = inputFormat.parse(dateAsString);
        return dateISO8601.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
