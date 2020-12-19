package ru.cepprice.maps.ui.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.cepprice.maps.R;
import ru.cepprice.maps.data.local.InternalStorageHelper;
import ru.cepprice.maps.data.local.RegionProvider;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.data.model.mapstate.Downloaded;
import ru.cepprice.maps.data.remote.Downloader;
import ru.cepprice.maps.data.remote.MapsDownloadManager;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.utils.KotlinUtils;
import ru.cepprice.maps.databinding.ActivityMainBinding;
import ru.cepprice.maps.utils.Utils;

public class MainActivity extends AppCompatActivity implements Downloader {

    private ActivityMainBinding binding;

    private RegionListAdapter adapter;
    private MapsDownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("M_CountryListActivity", "Permission is granted");
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        setupDeviceMemoryInfo();
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupDownloadManager();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceivers();
    }

    @Override
    public void onProgressUpdated(Region region, int progress) {
        region.setProgress(progress);
        adapter.updateItem(region);
    }

    @Override
    public void onDownloaded(Region region) {
        region.setMapState(new Downloaded());
        adapter.updateItem(region);
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

    private void setupDeviceMemoryInfo() {
        double free = InternalStorageHelper.getFreeGigabytes();
        double total = InternalStorageHelper.getTotalGigabytes();

        String formattedGigabytes = String.format("%.2f", free).replace(',', '.');
        String text = getString(R.string.countries_label_free_gigabytes, formattedGigabytes);
        binding.tvFreeGb.setText(text);

        int totalProgress = 100;
        int progress = totalProgress - (int) (free / total * 100);
        binding.progressFreeSpace.setProgress(progress);
    }

    private void setupRecyclerView() {
        adapter = new RegionListAdapter();
        adapter.setList(KotlinUtils.INSTANCE.sortRegions(RegionProvider.getRegions(this)));
        adapter.attachCallback(getAdapterCallback());

        binding.rv.setHasFixedSize(true);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
    }

    private RegionListAdapter.Callback getAdapterCallback() {
        return new RegionListAdapter.Callback() {
            @Override
            public void onItemClick(Region region, View view) {
                if (region.getChildRegions().size() == 0) return;
                Utils.navigateToRegionListActivity(MainActivity.this, region);
            }

            @Override
            public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder) {
                region.getMapState().onImageButtonClick(region, holder, downloadManager);
            }
        };
    }

}
