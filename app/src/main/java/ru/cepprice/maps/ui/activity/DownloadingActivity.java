package ru.cepprice.maps.ui.activity;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;
import ru.cepprice.maps.data.local.StorageHelper;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.data.model.mapstate.Downloaded;
import ru.cepprice.maps.data.model.mapstate.NotDownloaded;
import ru.cepprice.maps.data.remote.DownloadConsumer;
import ru.cepprice.maps.data.remote.MapsDownloadManager;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.utils.Utils;

public abstract class DownloadingActivity
        extends AppCompatActivity implements DownloadConsumer {

    protected MapsDownloadManager downloadManager;
    protected RegionListAdapter adapter;

    abstract void setupRecyclerView();
    abstract void updateListItem(Region region, int progress);

    protected RegionListAdapter.Callback getAdapterCallback() {
        return new RegionListAdapter.Callback() {
            @Override
            public void onItemClick(Region region, View view) {
                if (region.getChildRegions().size() == 0) return;
                Utils.navigateToRegionListActivity(DownloadingActivity.this, region);
            }

            @Override
            public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder) {
                region.getMapState().onImageButtonClick(region, holder, downloadManager);
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        setupDownloadManager();
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceivers();
    }

    @Override
    public void onProgressUpdated(Region region, int progress) {
        if (progress == 100) region.setMapState(new Downloaded());
        else updateListItem(region, progress);
    }

    @Override
    public void onDownloaded(Region region) {
        Log.d("M_DownloadingActivity", "Downloaded: " + region.getName());
        region.setMapState(new Downloaded());
        adapter.updateItem(region);
    }

    @Override
    public void onCancelled(Region region) {
        Log.d("M_DownloadingActivity", "Cancelled: " + region.getName());
        region.setMapState(new NotDownloaded());
        updateListItem(region, 0);
    }

    @Override
    public void onExternalStorageUnavailable() {
        // TODO
    }

    private void setupDownloadManager() {
        downloadManager = new MapsDownloadManager(this);
        registerReceiver(downloadManager.getOnCompleteReceiver(),
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(downloadManager.getOnNotificationClickReceiver(),
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    private void unregisterReceivers() {
        unregisterReceiver(downloadManager.getOnCompleteReceiver());
        unregisterReceiver(downloadManager.getOnNotificationClickReceiver());
    }

}
