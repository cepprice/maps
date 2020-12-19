package ru.cepprice.maps.ui.activity;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.data.model.mapstate.Downloaded;
import ru.cepprice.maps.data.model.mapstate.NotDownloaded;
import ru.cepprice.maps.data.remote.DownloadConsumer;
import ru.cepprice.maps.data.remote.MapsDownloadManager;
import ru.cepprice.maps.databinding.ActivityRegionListBinding;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.utils.Constants;
import ru.cepprice.maps.utils.KotlinUtils;
import ru.cepprice.maps.utils.Utils;

public class RegionListActivity extends AppCompatActivity implements DownloadConsumer {

    private ActivityRegionListBinding binding;

    private String parentRegionNameArg;
    private List<Region> childRegionListArg;

    private MapsDownloadManager downloadManager;
    private RegionListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegionListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getExtras();
        setupToolbar();
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
        if (progress == 100) region.setMapState(new Downloaded());
        else updateListItem(region, progress);
    }

    @Override
    public void onDownloaded(Region region) {
        region.setMapState(new Downloaded());
        adapter.updateItem(region);
    }

    @Override
    public void onCancelled(Region region) {
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

    private void getExtras() {
        Bundle bundle = getIntent().getExtras();
        parentRegionNameArg = bundle.getString(Constants.KEY_REGION_NAME);
        childRegionListArg = bundle.getParcelableArrayList(Constants.KEY_CHILD_REGIONS);
    }

    private void setupToolbar() {
        binding.toolbar.setTitle(parentRegionNameArg);
        binding.toolbar.setNavigationOnClickListener(v -> super.onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new RegionListAdapter();
        adapter.attachCallback(getAdapterCallback());
        adapter.setList(KotlinUtils.INSTANCE.sortRegions(childRegionListArg));

        binding.rv.setHasFixedSize(true);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
    }

    private RegionListAdapter.Callback getAdapterCallback() {
        return new RegionListAdapter.Callback() {
            @Override
            public void onItemClick(Region region, View view) {
                if (region.getChildRegions().size() == 0) return;
                Utils.navigateToRegionListActivity(RegionListActivity.this, region);
            }

            @Override
            public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder) {
                region.getMapState().onImageButtonClick(region, holder, downloadManager);
            }
        };
    }

    private void updateListItem(Region region, int progress) {
        region.setProgress(progress);
        RegionListAdapter.RegionViewHolder holder = (RegionListAdapter.RegionViewHolder) binding.rv
                .findViewHolderForAdapterPosition(adapter.getItemPosition(region));
        if (holder != null) region.getMapState().renderViewHolder(holder, progress);
    }

}
