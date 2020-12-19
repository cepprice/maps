package ru.cepprice.maps.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;

import java.util.ArrayList;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.model.Region;
import ru.cepprice.maps.ui.activity.RegionListActivity;

public class Utils {

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void navigateToRegionListActivity(Activity context, Region region) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_REGION_NAME, region.getName());
        bundle.putParcelableArrayList(Constants.KEY_CHILD_REGIONS, (ArrayList<? extends Parcelable>) region.getChildRegions());

        Intent intent = new Intent(context, RegionListActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.forward_right_to_left, R.anim.forward_left_to_right);
    }

}
