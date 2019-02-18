package net.aucutt.bikerbuddy.util;

import android.util.Pair;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateTimeAndTempUtil {

    private static final DateFormat timeFormat =new SimpleDateFormat("h:mm", Locale.US);
    private static final DateFormat dateFormat =new SimpleDateFormat("MMM dd", Locale.US);
    private static final DateFormat dateTimeFormat =new SimpleDateFormat("MMM dd yyyy  HH:mm", Locale.US);
    private static final DateFormat hourFormat = new SimpleDateFormat("HH", Locale.US);
    private static final DecimalFormat farnheitFormat = new DecimalFormat( "##");

    public static String UITToPST(String utc) {
        //2019-01-31T01:07:58+00:00
        DateFormat stupidFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        stupidFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = stupidFormat.parse(utc);
            return timeFormat.format(date);

        } catch (ParseException e) {
            return e.toString();
        }

    }

    public static  String getCurrentTimeFormatted() {
        Date myDate =  new Date(System.currentTimeMillis());
        return timeFormat.format(myDate);
    }

    public static String getCurrentDateFormatted() {
        return dateFormat.format( new Date(System.currentTimeMillis()));
    }

    public static Double kelvinToFarenheit(Double kelvin) {
         double what = kelvin * 9;
        double is =  what /5 - 459.67;
        return new Double( farnheitFormat.format(is)).doubleValue();
    }

    public static String getSecondsPastEpochFormatted(int seconds) {
        Long theDate = seconds * 1000l;
        Date myDate =  new Date(theDate);
        return dateTimeFormat.format(myDate);

    }

    public static String getHoursPastEpochFormatted(int seconds) {
        Long theDate = seconds * 1000l;
        Date myDate =  new Date(theDate);
        return timeFormat.format(myDate);

    }

    public static String getHourPastEpoch(int seconds) {
        Long theDate = seconds * 1000l;
        Date myDate =  new Date(theDate);
        return hourFormat.format(myDate);
    }
}
