package ru.cepprice.maps.data.remote;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

import io.reactivex.disposables.Disposable;
import ru.cepprice.maps.data.local.InternalStorageHelper;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.utils.Utils;

import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;

public class MapsDownloadManager {

    private static final String BASE_URL =
            "http://download.osmand.net/download.php?standard=yes&file=";

    private static final String EXTERNAL_STORAGE_FOLDER_NAME = "OsmAnd/";

    private final Context context;
    private final Downloader listener;
    private final DownloadManager manager;
    private final DownloadProgressRetriever progressRetriever;

    private final BroadcastReceiver onCompleteReceiver;
    private final BroadcastReceiver onNotificationClickReceiver;

    private final Queue<Region> regions;

    private long lastDownload;
    private Disposable lastDisposable;

    public MapsDownloadManager(Context context) {
        this.context = context;
        regions = new ArrayDeque<>();
        listener = (Downloader) context;
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        progressRetriever = new DownloadProgressRetriever(manager, listener);

        onCompleteReceiver = setupOnCompleteReceiver();
        onNotificationClickReceiver = setupOnNotificationClickReceiver();
    }

    public BroadcastReceiver getOnCompleteReceiver() { return onCompleteReceiver; }

    public BroadcastReceiver getOnNotificationClickReceiver() { return onNotificationClickReceiver; }

    public void enqueueDownload(Region region) {
        regions.add(region);
        if (regions.size() == 1) performDownloadOperation(region);
    }

    private BroadcastReceiver setupOnCompleteReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Region downloadedRegion = regions.poll();
                if (downloadedRegion != null) {
                    listener.onDownloaded(downloadedRegion);
                    InternalStorageHelper.addDownloadedMap(context, downloadedRegion.getDownloadName());
                }
                lastDisposable.dispose();
                performDownloadOperation(regions.peek());
            }
        };
    }

    private BroadcastReceiver setupOnNotificationClickReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Nothing yet
            }
        };
    }

    private void performDownloadOperation(Region region) {
        if (region == null) return;
        createFolderIfNeeded();
        lastDownload = startDownload(region.getDownloadName());
        lastDisposable = progressRetriever.retrieve(lastDownload, region);
    }

    private void createFolderIfNeeded()  {
        if (!Utils.isExternalStorageAvailable()) {
            listener.onExternalStorageUnavailable();
            return;
        }

        File documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(documentsFolder, EXTERNAL_STORAGE_FOLDER_NAME);
        if (!file.exists() && !file.mkdirs()) listener.onExternalStorageUnavailable();
    }

    private long startDownload(String fileName) {
        int allowedNetworkTypes =
                DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI;
        Uri uri = Uri.parse(BASE_URL + fileName);
        String relativeFilePath = EXTERNAL_STORAGE_FOLDER_NAME + "/" + fileName;

        return manager.enqueue(new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(allowedNetworkTypes)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription("Downloading")
                .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, relativeFilePath)
        );
    }

}
