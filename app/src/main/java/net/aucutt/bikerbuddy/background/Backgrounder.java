package net.aucutt.bikerbuddy.background;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.graphics.Palette;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

import io.reactivex.annotations.Nullable;

import static net.aucutt.bikerbuddy.MainActivity.TAG;

public class Backgrounder {


    private File[] webps;  //i decided not to load all webps in memory at once.
    Bitmap currentBitmap;


    public void loadImagesFromFile() {

        FilenameFilter webpFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            if (lowercaseName.endsWith(".webp")) {
                return true;
            } else {
                return false;
            }
        };
       String path ="/sdcard/Pictures";
       Log.d(TAG," le path " + path);
       File dirHandle = new File(path);
       webps= dirHandle.listFiles(webpFilter);
       for (File webp : webps ) {
           Log.d(TAG, "file "  + webp );
       }

    }

    @Nullable
    public Bitmap fetchNewBackground() {
        File newBackground = getRandomWebp();
        if (newBackground == null ) {
            return null;
        }
        Log.d(TAG, " newfie file "  + newBackground.getName());
        currentBitmap = BitmapFactory.decodeFile(newBackground.getAbsolutePath());
        return currentBitmap;
    }

    @Nullable
    public Palette getPalete() {
        return Palette.from(currentBitmap).generate();
    }

    @Nullable
    private File getRandomWebp() {
        if (webps == null || webps.length == 0) {
            return null;
        }
        Random randmomGenerator = new Random();  //should  this be a member variable?
        int image = randmomGenerator.nextInt( webps.length);
        return webps[image];


    }

    @ColorInt
    public static int getContrastColor(@ColorInt int color) {
        // Counting the perceptive luminance - human eye favors green color...
        double a = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

        int d;
        if (a < 0.5) {
            d = 0; // bright colors - black font
        } else {
            d = 255; // dark colors - white font
        }

        return Color.rgb(d, d, d);
    }
}
