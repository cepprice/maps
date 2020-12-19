package ru.cepprice.maps.data.model.mapstate;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.data.remote.MapsDownloadManager;

public class InProcess extends MapState {

    public static final Parcelable.Creator<InProcess> CREATOR = new Parcelable.Creator<InProcess>() {
        @Override
        public InProcess createFromParcel(Parcel source) {
            return new InProcess(source);
        }

        @Override
        public InProcess[] newArray(int size) {
            return new InProcess[size];
        }
    };

    protected InProcess(Parcel in) {
        super(in);
    }

    public InProcess() {}

    @Override
    public void onImageButtonClick(Region region, RegionListAdapter.RegionViewHolder holder, MapsDownloadManager downloadManager) {
        Log.d("M_MapState", "Cancel downloading");
        downloadManager.cancelDownload(region);
        region.setMapState(new NotDownloaded());
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_import);
        hideProgressBar(holder.getDownloadProgressBar());
        resetProgress(holder.getDownloadProgressBar());
    }

    @Override
    public void renderViewHolder(RegionListAdapter.RegionViewHolder holder, int progress) {
        setImageDrawable(holder.getIvMap(), R.drawable.ic_map);
        setImageDrawable(holder.getIvAction(), R.drawable.ic_action_remove_dark);
        showProgressBar(holder.getDownloadProgressBar());
        holder.getDownloadProgressBar().setProgress(progress);
    }
}
