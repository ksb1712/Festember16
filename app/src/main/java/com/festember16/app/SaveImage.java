package com.festember16.app;

/**
 * Created by bharath17 on 16/9/16.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class SaveImage {


    static String name;
    Bitmap img;

    public SaveImage(String a, Bitmap b) {
        name = a;
        img = b;
    }

    public static File getSavePath() {
        File path;
        if (hasSDCard()) { // SD card
            path = new File(getSDCardPath() + "/Festember/");
            path.mkdir();
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }

    public static String getCacheFilename() {
        File f = getSavePath();
        return f.getAbsolutePath() + "/" + name + ".png";
    }

    public static Bitmap loadFromFile(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists()) {
                return null;
            }
            Bitmap tmp = BitmapFactory.decodeFile(filename);
            return tmp;
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap loadFromCacheFile() {
        return loadFromFile(getCacheFilename());
    }

    public static void saveToCacheFile(Bitmap bmp) {
        saveToFile(getCacheFilename(), bmp);
    }

    public static void saveToFile(String filename, Bitmap bmp) {
        try {
            FileOutputStream out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
        }
    }

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardPath() {
        File path = Environment.getExternalStorageDirectory();
        return path.getAbsolutePath();
    }
}