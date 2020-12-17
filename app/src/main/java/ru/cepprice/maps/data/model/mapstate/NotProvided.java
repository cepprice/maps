package ru.cepprice.maps.data.model.mapstate;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.data.remote.MapsDownloadManager;

public class NotProvided extends MapState {

    public static final Parcelable.Creator<NotProvided> CREATOR = new Parcelable.Creator<NotProvided>() {
        @Override
        public NotProvided createFromParcel(Parcel source) {
            return new NotProvided(source);
        }

        @Override
        public NotProvided[] newArray(int size) {
            return new NotProvided[size];
        }
    };

    public NotProvided() {}

    protected NotProvided(Parcel in) {
        super(in);
    }

    @Override
    public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder, MapsDownloadManager downloadManager) {
        // Do nothing
        Log.d("M_MapState", "Do nothing");
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder, int progress) {
        setImageDrawable(holder.getIvMap(), R.drawable.ic_map);
    }
}
