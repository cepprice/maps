package ru.cepprice.maps.data;

import java.util.ArrayList;

import ru.cepprice.maps.utils.mapstate.MapState;

public class Region {

    private String name;
    private String downloadName;
    private String innerDownloadPrefix;

    private MapState state;

    private final ArrayList<Region> childRegions = new ArrayList<>();

    public void setMapState(MapState state) {
        this.state = state;
    }

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

    public ArrayList<Region> getChildRegions() {
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

        public Builder childRegions(ArrayList<Region> regions) {
            Region.this.childRegions.addAll(regions);
            return this;
        }

        public Region build() {
            return Region.this;
        }

    }

}
