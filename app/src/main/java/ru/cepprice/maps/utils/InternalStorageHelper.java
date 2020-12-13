package ru.cepprice.maps.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InternalStorageHelper {

    private static final String MAPS_FILE_NAME = "downloaded_maps.txt";

    private static File path = Environment.getDataDirectory();
    private static StatFs stat = new StatFs(path.getPath());

    public static void addDownloadedMap(Context context, String mapName) {
        try {
            FileOutputStream out = context.openFileOutput(MAPS_FILE_NAME, Context.MODE_APPEND);
            out.write((mapName + "\n").getBytes());
            out.flush();
            out.close();
        } catch (Exception ignored) {}
    }

    public static ArrayList<String> getDownloadedMaps(Context context) {
        ArrayList<String> downloadedMaps = new ArrayList<>();

        try {
            FileInputStream out = context.openFileInput(MAPS_FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(out));

            String line = reader.readLine();
            while (line != null) {
                downloadedMaps.add(line);
                line = reader.readLine();
            }
        } catch (Exception ignored) {}

        return downloadedMaps;
    }

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
