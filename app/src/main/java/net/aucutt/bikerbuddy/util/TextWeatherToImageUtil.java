package net.aucutt.bikerbuddy.util;

import android.util.Log;

import net.aucutt.bikerbuddy.R;

import static net.aucutt.bikerbuddy.MainActivity.TAG;

public class TextWeatherToImageUtil {

    public static Integer getDrawableId(String text) {
        Integer drawable;
        switch (text) {
            case "Rain":
                drawable = R.mipmap.ic_rain;
                break;
            case "Clouds":
                drawable = R.mipmap.ic_cloudy;
                break;
            default:
                Log.e(TAG, "no mapping for " + text);
                drawable = R.mipmap.ic_rain;

        }
        return drawable;
    }
}
