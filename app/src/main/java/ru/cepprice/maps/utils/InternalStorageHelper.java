package ru.cepprice.maps.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class InternalStorageHelper {

    private static File path = Environment.getDataDirectory();
    private static StatFs stat = new StatFs(path.getPath());

    public static double getFreeGigabytes() {
        return fromBytesToGigabytes(stat.getAvailableBytes());
    }

    public static double getTotalGigabytes() {
        return fromBytesToGigabytes(stat.getTotalBytes());
    }

    private static double fromBytesToGigabytes(long bytes) {
        return bytes / Math.pow(1024, 3);
    }

}
