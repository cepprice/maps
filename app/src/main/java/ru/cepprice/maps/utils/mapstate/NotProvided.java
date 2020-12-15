package ru.cepprice.maps.utils.mapstate;

import android.util.Log;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;

public class NotProvided extends MapState {

    @Override
    public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder) {
        // Do nothing
        Log.d("M_MapState", "Do nothing");
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder) {
        setImageDrawable(holder.getIvMap(), R.drawable.ic_map);
    }
}
