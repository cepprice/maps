package ru.cepprice.maps.data.remote;

import ru.cepprice.maps.data.model.Region;

public interface DownloadConsumer {
    void onProgressUpdated(Region region, int progress);
    void onDownloaded(Region region);
    void onCancelled(Region region);
    void onExternalStorageUnavailable();
}
