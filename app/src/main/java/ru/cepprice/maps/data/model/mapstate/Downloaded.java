package ru.cepprice.maps.data.model.mapstate;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.data.remote.MapsDownloadManager;

public class Downloaded extends MapState {

    public static final Parcelable.Creator<Downloaded> CREATOR = new Parcelable.Creator<Downloaded>() {
        @Override
        public Downloaded createFromParcel(Parcel source) {
            return new Downloaded(source);
        }

        @Override
        public Downloaded[] newArray(int size) {
            return new Downloaded[size];
        }
    };

    public Downloaded() {}

    protected Downloaded(Parcel in) {
        super(in);
    }

    @Override
    public void onImageButtonClick(
            Region region, RegionListAdapter.RegionViewHolder holder,
            MapsDownloadManager downloadManager
    ) {
        // Do nothing
        Log.d("M_Downloaded", "Do nothing");
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder, int progress) {
        // TODO: Change image color
        setImageDrawable(holder.getIvAction(), null);
    }
}
