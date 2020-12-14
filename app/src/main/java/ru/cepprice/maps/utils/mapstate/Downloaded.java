package ru.cepprice.maps.utils.mapstate;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;

public class Downloaded extends MapState {

    @Override
    public void onImageButtonClick(Region region, ImageView ivAction) {
        Log.d("M_MapState", "Removing");
        // Not specified in task
//        setImageViewSrc(holder.ivMap, R.drawable.ic_map);
//        setImageViewSrc(holder.ivAction, R.drawable.ic_action_import);
//        region.setMapState(new NotDownloaded());
    }

    @Override
    public void renderImages(ImageView ivMap, ImageView ivAction) {
        Context context = ivMap.getContext();
        setImageDrawable(ivMap, getColoredDrawable(context, R.drawable.ic_map));
        setImageDrawable(ivAction, R.drawable.ic_action_remove_dark);
    }
}
