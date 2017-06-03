package tech.caols.infinitely.date;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatTest {

    public static void main(String[] args) {
        Date update = new Date();
        String format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA).format(update);
        System.out.println(format);
    }

}
