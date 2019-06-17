package org.odhs.happydinner.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateManager {

    public static final String DATE_FORMAT = "yyyyMMdd";

    private final Calendar cal = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);


    public DateManager() {
        cal.setTime(new Date());
    }

    public String getTodayDate() {
        return dateFormat.format(cal.getTime());
    }

    public String getTodayTime() {
        return cal.getTime().toString();
    }

    public static String changeDateFormat(String originalFormat, String newFormat, String date) throws Exception{
        SimpleDateFormat of = new SimpleDateFormat(originalFormat);
        SimpleDateFormat nf = new SimpleDateFormat(newFormat);

        Date originalDate = of.parse(date);
        return nf.format(originalDate);
    }

    /* return list of 7 dates (Monday - Sunday) */

    public List<String> getWeekDates() {
        int dayOfWeek = findDayOfWeek(this.cal);

        int frontStep;
        int backStep;

        List<String> dates = new ArrayList<>();

        switch (dayOfWeek) {
            case Calendar.MONDAY:
                frontStep = 0;
                backStep = 6;
                break;
            case Calendar.TUESDAY:
                frontStep = 1;
                backStep = 5;
                break;
            case Calendar.WEDNESDAY:
                frontStep = 2;
                backStep = 4;
                break;
            case Calendar.THURSDAY:
                frontStep = 3;
                backStep = 3;
                break;
            case Calendar.FRIDAY:
                frontStep = 4;
                backStep = 2;
                break;
            case Calendar.SATURDAY:
                frontStep = 5;
                backStep = 1;
                break;
            case Calendar.SUNDAY:
                frontStep = 6;
                backStep = 0;
                break;
            default:
                frontStep = 0;
                backStep = 0;
                break;
        }


        cal.add(Calendar.DATE, -1 * frontStep);
        for (int i=0; i<frontStep; i++) {
            dates.add(dateFormat.format(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }
        dates.add(dateFormat.format(cal.getTime()));
        for (int i=0; i<backStep; i++) {
            cal.add(Calendar.DATE, 1);
            dates.add(dateFormat.format(cal.getTime()));
        }

        cal.setTime(new Date());

        return dates;
    }

    private int findDayOfWeek(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
