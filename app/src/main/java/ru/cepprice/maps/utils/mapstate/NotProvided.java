package ru.cepprice.maps.utils.mapstate;

import android.util.Log;
import android.widget.ImageView;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;

public class NotProvided extends MapState {

    @Override
    public void onImageButtonClick(Region region, ImageView ivAction) {
        // Do nothing
        Log.d("M_MapState", "Do nothing");
    }

    @Override
    public void renderImages(ImageView ivMap, ImageView ivAction) {
        setImageDrawable(ivMap, R.drawable.ic_map);
    }
}
