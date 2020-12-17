package ru.cepprice.maps.data.remote;

import android.app.DownloadManager;
import android.database.Cursor;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BooleanSupplier;
import ru.cepprice.maps.data.model.Region;

public class DownloadProgressRetriever {

    private final DownloadManager downloadManager;
    private final Downloader listener;

    private BooleanSupplier downloading = () -> false;

    public DownloadProgressRetriever(DownloadManager downloadManager, Downloader listener) {
        this.downloadManager = downloadManager;
        this.listener = listener;
    }

    public Disposable retrieve(long id, Region region) {
        return makeObservable(id)
                .subscribeOn(AndroidSchedulers.mainThread())
                .delay(1, TimeUnit.SECONDS)
                .repeatUntil(downloading)
                .subscribe(progress -> {
                    listener.onProgressUpdated(region, progress);
                    Log.d("M_DownloadProgress", "Progress: " + progress);
                });
    }

    private Observable<Integer> makeObservable(long id) {
        return Observable.fromCallable(() -> {
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
            Cursor cursor = downloadManager.query(query);

            cursor.moveToFirst();
            int downloadedBytes = getDownloadedBytes(cursor);
            int totalBytes = getTotalBytes(cursor);

            if (isSuccessful(cursor) || isFailed(cursor)) downloading = () -> true;
            cursor.close();

            return (int) (downloadedBytes * 100f / totalBytes);
        });
    }

    private int getDownloadedBytes(Cursor cursor) {
        return cursor.getInt(getColumnIdx(cursor, DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
    }

    private int getTotalBytes(Cursor cursor) {
        return cursor.getInt(getColumnIdx(cursor, DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
    }

    private boolean isSuccessful(Cursor cursor) {
        return getCursorStatus(cursor) == DownloadManager.STATUS_SUCCESSFUL;
    }

    private boolean isFailed(Cursor cursor) {
        return getCursorStatus(cursor) == DownloadManager.STATUS_FAILED;
    }

    private int getCursorStatus(Cursor cursor) {
        return cursor.getInt(getColumnIdx(cursor, DownloadManager.COLUMN_STATUS));
    }

    private int getColumnIdx(Cursor cursor, String columnName) {
        return cursor.getColumnIndex(columnName);
    }
}
