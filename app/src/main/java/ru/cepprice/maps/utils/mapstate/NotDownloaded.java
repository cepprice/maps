package ru.cepprice.maps.utils.mapstate;

import android.util.Log;
import android.widget.ImageView;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;

public class NotDownloaded extends MapState {

    @Override
    public void onImageButtonClick(Region region, ImageView ivAction) {
        // TODO: Start downloading map
        Log.d("M_MapState", "Starting map downloading");
        setImageDrawable(ivAction, R.drawable.ic_action_remove_dark);
        region.setMapState(new InProcess());
    }

    @Override
    public void renderImages(ImageView ivMap, ImageView ivAction) {
        setImageDrawable(ivMap, R.drawable.ic_map);
        setImageDrawable(ivAction, R.drawable.ic_action_import);
    }
}
