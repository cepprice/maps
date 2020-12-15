package ru.cepprice.maps.utils.mapstate;

import android.util.Log;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;

public class NotDownloaded extends MapState {

    @Override
    public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder) {
        // TODO: Start downloading map
        Log.d("M_MapState", "Starting map downloading");
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_remove_dark);
        showProgressBar(holder.getDownloadProgressBar());
        region.setMapState(new InProcess());
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder) {
        setImageDrawable(holder.getIvMap(), R.drawable.ic_map);
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_import);
    }
}
