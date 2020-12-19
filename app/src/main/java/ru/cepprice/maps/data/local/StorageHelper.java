package ru.cepprice.maps.data.local;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.ArrayList;

import static ru.cepprice.maps.utils.Constants.EXTERNAL_STORAGE_FOLDER_NAME;

public class StorageHelper {

    private static File path = Environment.getDataDirectory();
    private static StatFs stat = new StatFs(path.getPath());

    public static ArrayList<String> getDownloadedMaps() {
        ArrayList<String> downloadedMaps = new ArrayList<>();

        File documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File srcFolder = new File(documentsFolder, EXTERNAL_STORAGE_FOLDER_NAME);
        File[] maps = srcFolder.listFiles();

        if (maps == null) return downloadedMaps;

        for (File f:
                srcFolder.listFiles()) {
            downloadedMaps.add(f.getName());
        }

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
