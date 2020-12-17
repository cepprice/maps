package ru.cepprice.maps.data.model.mapstate;

import android.util.Log;

import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.data.remote.MapsDownloadManager;

public class Downloaded extends MapState {

    @Override
    public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder, MapsDownloadManager downloadManager) {
        Log.d("M_MapState", "Removing");
        // Not specified in task
//        setImageViewSrc(holder.ivMap, R.drawable.ic_map);
//        setImageViewSrc(holder.ivAction, R.drawable.ic_action_import);
//        region.setMapState(new NotDownloaded());
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder, int progress) {
//        Context context = holder.getIvMap().getContext();
//        setImageDrawable(holder.getIvMap(), getColoredDrawable(context, R.drawable.ic_map));
        // TODO: Change image color
        setImageDrawable(holder.getIvAction(), null);
    }
}