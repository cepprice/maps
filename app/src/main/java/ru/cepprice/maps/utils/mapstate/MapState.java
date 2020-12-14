package ru.cepprice.maps.utils.mapstate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;

public abstract class MapState {

    public abstract void onImageButtonClick(Region region, ImageView ivAction);
    public abstract void renderImages(ImageView ivMap, ImageView ivAction);

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




