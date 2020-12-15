package ru.cepprice.maps.utils.mapstate;

import android.content.Context;
import android.util.Log;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;

public class Downloaded extends MapState {

    @Override
    public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder) {
        Log.d("M_MapState", "Removing");
        // Not specified in task
//        setImageViewSrc(holder.ivMap, R.drawable.ic_map);
//        setImageViewSrc(holder.ivAction, R.drawable.ic_action_import);
//        region.setMapState(new NotDownloaded());
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder) {
        Context context = holder.getIvMap().getContext();
        setImageDrawable(holder.getIvMap(), getColoredDrawable(context, R.drawable.ic_map));
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_remove_dark);
    }
}
