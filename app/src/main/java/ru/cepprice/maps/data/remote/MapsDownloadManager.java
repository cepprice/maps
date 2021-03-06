package ru.cepprice.maps.data.remote;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

import io.reactivex.disposables.Disposable;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.data.model.mapstate.NotDownloaded;

import static android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED;
import static ru.cepprice.maps.utils.Constants.EXTERNAL_STORAGE_FOLDER_NAME;

public class MapsDownloadManager {

    private static final String BASE_URL =
            "http://download.osmand.net/download.php?standard=yes&file=";

    private static final Queue<Region> regions = new ArrayDeque<>();

    private static String lastDownloadName;
    private static long lastDownloadId;
    private static Disposable lastDisposable;

    private final DownloadConsumer downloadConsumer;
    private final DownloadManager downloadManager;
    private final DownloadProgressRetriever progressRetriever;

    private BroadcastReceiver onCompleteReceiver;
    private BroadcastReceiver onNotificationClickReceiver;


    public MapsDownloadManager(Context context) {
        downloadConsumer = (DownloadConsumer) context;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        progressRetriever = new DownloadProgressRetriever(downloadManager, downloadConsumer);

        setupOnCompleteReceiver();
        setupOnNotificationClickReceiver();
    }

    public BroadcastReceiver getOnCompleteReceiver() { return onCompleteReceiver; }

    public BroadcastReceiver getOnNotificationClickReceiver() { return onNotificationClickReceiver; }

    public void enqueueDownload(Region region) {
        regions.add(region);
        if (regions.size() == 1) performDownloadOperation(region);
    }

    public void cancelDownload(Region region) {
        if (region.getName().equals(lastDownloadName)) {
            downloadManager.remove(lastDownloadId);
            if (lastDisposable != null) lastDisposable.dispose();
            downloadConsumer.onCancelled(region);
        }
    }

    private void setupOnCompleteReceiver() {
        onCompleteReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) return;

                Region downloadedRegion = regions.poll();
                long id = lastDownloadId;

                if (lastDisposable != null) lastDisposable.dispose();
                pollAllCancelledDownloads();
                performDownloadOperation(regions.peek());

                if (downloadedRegion == null) {
                    Log.d("M_MapsDownloadManager", "Polling empty queue");
                    return;
                }

                if (isDownloadSuccessful(id)) {
                    downloadConsumer.onDownloaded(downloadedRegion);
                }
                else {
                    logErrorReason(id);
                    downloadConsumer.onCancelled(downloadedRegion);
                }
            }
        };
    }

    private boolean isDownloadSuccessful(long id) {
        Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(id));
        if (!cursor.moveToFirst()) return false;
        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        return status == DownloadManager.STATUS_SUCCESSFUL;
    }

    private void logErrorReason(long id) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);

        if (!cursor.moveToFirst()) {
            Log.d("M_MapsDownloadManager", "User cancelled download");
            return;
        }

        int columnReasonIdx = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReasonIdx);

        switch (reason) {
            case DownloadManager.ERROR_CANNOT_RESUME:
                Log.d("M_MapsDownloadManager", "ERROR_CANNOT_RESUME");
                break;
            case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                Log.d("M_MapsDownloadManager", "ERROR_DEVICE_NOT_FOUND");
                break;
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                Log.d("M_MapsDownloadManager", "ERROR_FILE_ALREADY_EXISTS");
                break;
            case DownloadManager.ERROR_FILE_ERROR:
                Log.d("M_MapsDownloadManager", "ERROR_FILE_ERROR");
                break;
            case DownloadManager.ERROR_HTTP_DATA_ERROR:
                Log.d("M_MapsDownloadManager", "ERROR_HTTP_DATA_ERROR");
                break;
            case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                Log.d("M_MapsDownloadManager", "ERROR_INSUFFICIENT_SPACE");
                break;
            case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                Log.d("M_MapsDownloadManager", "ERROR_TOO_MANY_REDIRECTS");
                break;
            case DownloadManager.ERROR_UNKNOWN:
                Log.d("M_MapsDownloadManager", "ERROR_UNKNOWN");
                break;
        }

    }

    private void setupOnNotificationClickReceiver() {
        onNotificationClickReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Nothing yet
            }
        };
    }

    private void pollAllCancelledDownloads() {
        while (regions.peek() != null && regions.peek().getMapState() instanceof NotDownloaded) {
            downloadConsumer.onCancelled(regions.poll());
        }
    }

    private void performDownloadOperation(Region region) {
        if (region == null) return;
        createFolderIfNeeded();
        lastDownloadId = startDownload(region.getDownloadName());
        Log.d("M_MapsDownloadManager", "Downloading now | name: " + region.getName() +  " id: " + lastDownloadId);
        lastDisposable = progressRetriever.retrieve(lastDownloadId, region);
        lastDownloadName = region.getName();
    }

    private void createFolderIfNeeded()  {
        File documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(documentsFolder, EXTERNAL_STORAGE_FOLDER_NAME);
        if (!file.exists() && !file.mkdirs()) downloadConsumer.onExternalStorageUnavailable();
    }

    private long startDownload(String fileName) {
        int allowedNetworkTypes =
                DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI;
        Uri uri = Uri.parse(BASE_URL + fileName);
        String relativeFilePath = EXTERNAL_STORAGE_FOLDER_NAME + "/" + fileName;

        return downloadManager.enqueue(new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(allowedNetworkTypes)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription("Downloading")
                .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, relativeFilePath)
        );
    }

}
