package ru.cepprice.maps.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import ru.cepprice.maps.R;
import ru.cepprice.maps.data.local.RegionProvider;
import ru.cepprice.maps.data.local.StorageHelper;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.databinding.ActivityMainBinding;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.utils.KotlinUtils;

public class MainActivity extends DownloadingActivity {

    private ActivityMainBinding binding;

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
    public void onDownloaded(Region region) {
        super.onDownloaded(region);
        setupDeviceMemoryInfo();
    }

    @Override
    void setupRecyclerView() {
        adapter = new RegionListAdapter();
        adapter.setList(KotlinUtils.INSTANCE.sortRegions(RegionProvider.getRegions(this)));
        adapter.attachCallback(getAdapterCallback());

        binding.rv.setHasFixedSize(true);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(adapter);
    }

    @Override
    void updateListItem(Region region, int progress) {
        region.setProgress(progress);
        RegionListAdapter.RegionViewHolder holder = (RegionListAdapter.RegionViewHolder) binding.rv
                .findViewHolderForAdapterPosition(adapter.getItemPosition(region));
        if (holder != null) region.getMapState().renderViewHolder(holder, progress);
    }

    private void setupDeviceMemoryInfo() {
        double free = StorageHelper.getFreeGigabytes();
        double total = StorageHelper.getTotalGigabytes();

        String formattedGigabytes = String.format("%.2f", free).replace(',', '.');
        String text = getString(R.string.countries_label_free_gigabytes, formattedGigabytes);
        binding.tvFreeGb.setText(text);

        int totalProgress = 100;
        int progress = totalProgress - (int) (free / total * 100);
        binding.progressFreeSpace.setProgress(progress);
    }

}
