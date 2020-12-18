package ru.cepprice.maps.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import ru.cepprice.maps.data.model.mapstate.MapState;

public class Region implements Parcelable {

    public static final Parcelable.Creator<Region> CREATOR = new Parcelable.Creator<Region>() {
        @Override
        public Region createFromParcel(Parcel source) {
            return new Region(source);
        }

        @Override
        public Region[] newArray(int size) {
            return new Region[size];
        }
    };


    private String name;
    private String downloadName;
    private String innerDownloadPrefix;

    private MapState state;

    private int progress = 0;

    private final List<Region> childRegions = new ArrayList<>();

    private Region() {}

    private Region(Parcel in) {
        String[] strings = in.createStringArray();
        name = strings[0];
        downloadName = strings[1];
        innerDownloadPrefix = strings[2];
        progress = Integer.parseInt(strings[3]);
        state = in.readParcelable(MapState.class.getClassLoader());
        childRegions.addAll((List<Region>)in.readValue(Region.class.getClassLoader()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {name, downloadName, innerDownloadPrefix, String.valueOf(progress)});
        dest.writeParcelable(state, flags);
        dest.writeValue(childRegions);
    }

    public void setMapState(MapState state) {
        this.state = state;
    }

    public void setProgress(int progress) { this.progress = progress; }

    public String getName() {
        return name;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public String getInnerDownloadPrefix() {
        return innerDownloadPrefix;
    }

    public MapState getMapState() {
        return state;
    }

    public int getProgress() { return progress; }

    public List<Region> getChildRegions() {
        return childRegions;
    }

    public static Builder builder() {
        return new Region().new Builder();
    }

    public class Builder {

        private Builder() {}

        public Builder name(String name) {
            Region.this.name = name;
            return this;
        }

        public Builder downloadName(String downloadName) {
            Region.this.downloadName = downloadName;
            return this;
        }

        public Builder innerDownloadPrefix(String prefix) {
            Region.this.innerDownloadPrefix = prefix;
            return this;
        }

        public Builder mapState(MapState state) {
            Region.this.state = state;
            return this;
        }

        public Builder downloadProgress(int progress) {
            Region.this.progress = progress;
            return this;
        }

        public Builder childRegions(ArrayList<Region> regions) {
            Region.this.childRegions.addAll(regions);
            return this;
        }

        public Region build() {
            return Region.this;
        }

    }

}
