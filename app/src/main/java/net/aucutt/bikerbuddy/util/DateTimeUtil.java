package net.aucutt.bikerbuddy.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//https://sunrise-sunset.org/api
public class DateTimeUtil {

    public static String UITToPST(String utc) {
        //2019-01-31T01:07:58+00:00
        DateFormat stupidFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        stupidFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = stupidFormat.parse(utc);
            DateFormat myFormat =new SimpleDateFormat("hh:mm:ss");
            return myFormat.format(date);

        } catch (ParseException e) {
            return e.toString();
        }

    }
}
