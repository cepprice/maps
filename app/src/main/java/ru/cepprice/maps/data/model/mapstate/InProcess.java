package ru.cepprice.maps.data.model.mapstate;

import android.util.Log;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.data.remote.MapsDownloadManager;

public class InProcess extends MapState {

    @Override
    public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder, MapsDownloadManager downloadManager) {
        // TODO: Cancel downloading
        Log.d("M_MapState", "Cancel downloading");
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_import);
        hideProgressBar(holder.getDownloadProgressBar());
        region.setMapState(new NotDownloaded());
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder, int progress) {
        setImageDrawable(holder.getIvMap(), R.drawable.ic_map);
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_remove_dark);
        showProgressBar(holder.getDownloadProgressBar());
        holder.getDownloadProgressBar().setProgress(progress);
    }
}
