package ru.cepprice.maps.data.model.mapstate;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.data.remote.MapsDownloadManager;
import ru.cepprice.maps.ui.adapter.RegionListAdapter.RegionViewHolder;
import ru.cepprice.maps.utils.Utils;

public class NotDownloaded extends MapState {

    public static final Parcelable.Creator<NotDownloaded> CREATOR = new Parcelable.Creator<NotDownloaded>() {
        @Override
        public NotDownloaded createFromParcel(Parcel source) {
            return new NotDownloaded(source);
        }

        @Override
        public NotDownloaded[] newArray(int size) {
            return new NotDownloaded[size];
        }
    };

    protected NotDownloaded(Parcel in) {
        super(in);
    }

    public NotDownloaded() {}

    @Override
    public void onImageButtonClick(
            Region region, RegionViewHolder holder, MapsDownloadManager downloadManager)
    {
        if (Utils.isExternalStorageAvailable()) {
            downloadManager.enqueueDownload(region);
            setImageDrawable(holder.getIvAction(), R.drawable.ic_action_remove_dark);
            showProgressBar(holder.getDownloadProgressBar());
            region.setMapState(new InProcess());
            Log.d("M_MapState", "Enqueue map download");
        } else {
            Log.d("M_NotDownloaded", "External storage unavailable");
        }
    }

    @Override
    public void renderViewHolder(RegionViewHolder holder, int progress) {
        setImageDrawable(holder.getIvMap(), R.drawable.ic_map);
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_import);
    }
}
