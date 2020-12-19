package ru.cepprice.maps.data.model.mapstate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import ru.cepprice.maps.R;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.ui.adapter.RegionListAdapter;
import ru.cepprice.maps.data.remote.MapsDownloadManager;

public abstract class MapState implements Parcelable {

    protected MapState(Parcel in) {}

    protected MapState() {}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

    public abstract void onImageButtonClick(
            Region region,
            RegionListAdapter.RegionViewHolder holder,
            MapsDownloadManager downloadManager
    );

    public abstract void renderViewHolder(RegionListAdapter.RegionViewHolder holder, int progress);

    protected void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    protected void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        // TODO: Set progress?
    }

    protected void resetProgress(ProgressBar progressBar) {
        progressBar.setProgress(0);
    }

    protected void setImageDrawable(ImageView image, @DrawableRes int drawable) {
        Context context = image.getContext();
        image.setImageDrawable(ContextCompat.getDrawable(context, drawable));
    }

    protected void setImageDrawable(ImageView image, Drawable drawable) {
        image.setImageDrawable(drawable);
    }

    protected Drawable getColoredDrawable(Context context, @DrawableRes int drawable) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, drawable);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, R.color.icon_downloaded));
        return wrappedDrawable;
    }

}




