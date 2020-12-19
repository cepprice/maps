package ru.cepprice.maps.ui.activity;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.cepprice.maps.R;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.databinding.ActivityRegionListBinding;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.utils.Constants;
import ru.cepprice.maps.utils.KotlinUtils;
import ru.cepprice.maps.utils.Utils;

public class RegionListActivity extends AppCompatActivity {

    private ActivityRegionListBinding binding;

    private  String parentRegionNameArg;
    private  List<Region> childRegionListArg;

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
            }
        };
    }

}
