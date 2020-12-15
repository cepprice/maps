package ru.cepprice.maps.utils.mapstate;

import android.util.Log;
import android.view.View;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;

public class InProcess extends MapState {

    @Override
    public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder) {
        // TODO: Cancel downloading
        Log.d("M_MapState", "Cancel downloading");
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_import);
        hideProgressBar(holder.getDownloadProgressBar());
        region.setMapState(new NotDownloaded());
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder) {
        setImageDrawable(holder.getIvMap(), R.drawable.ic_map);
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_remove_dark);
        showProgressBar(holder.getDownloadProgressBar());
        // TODO: how to set progress?
    }
}
