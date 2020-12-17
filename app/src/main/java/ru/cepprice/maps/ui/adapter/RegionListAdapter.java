package ru.cepprice.maps.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.cepprice.maps.R;
import ru.cepprice.maps.data.model.Region;

public class RegionListAdapter extends RecyclerView.Adapter<RegionListAdapter.RegionViewHolder> {

    protected ArrayList<Region> regions = new ArrayList<>();
    private Callback callback = null;

    public RegionListAdapter() {
    }

    public void attachCallback(Callback callback) {
        this.callback = callback;
    }

    public void setList(List<Region> dataList) {
        this.regions.clear();
        this.regions.addAll(dataList);
        notifyDataSetChanged();
    }

    public void updateItem(Region region) {
        int idxOfChangedRegion = regions.indexOf(region);
        notifyItemChanged(idxOfChangedRegion);
    }

    public interface Callback {
        void onItemClick(Region region, View view);
        void onImageButtonClick(Region region, RegionViewHolder holder);
    }

    public class RegionViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRegionName;

        private final ImageView ivAction;
        private final ImageView ivMap;

        private final View divider;

        private final ProgressBar downloadProgress;

        public RegionViewHolder(View itemView) {
            super(itemView);
            tvRegionName = itemView.findViewById(R.id.tv_region_name);
            ivAction = itemView.findViewById(R.id.ic_btn);
            ivMap = itemView.findViewById(R.id.ic_globe);
            divider = itemView.findViewById(R.id.divider);
            downloadProgress = itemView.findViewById(R.id.progress_downloading);
        }

        public void bind(RegionViewHolder holder, int position) {
            Region region = regions.get(position);

            resetViews();
            tvRegionName.setText(region.getName());

            region.getMapState().renderViewHolder(holder, region.getProgress());
            ivAction.setOnClickListener(v -> callback.onImageButtonClick(region, holder));

            // Remove divider of last element
            if (position == regions.size() - 1) divider.setVisibility(View.GONE);
        }

        public TextView getTvRegionName() {
            return tvRegionName;
        }

        public ImageView getIvAction() {
            return ivAction;
        }

        public ImageView getIvMap() {
            return ivMap;
        }

        public View getDivider() {
            return divider;
        }

        public ProgressBar getDownloadProgressBar() {
            return downloadProgress;
        }

        private void resetViews() {
            ivMap.setImageDrawable(null);
            ivAction.setImageDrawable(null);
            downloadProgress.setVisibility(View.GONE);
            downloadProgress.setProgress(0);
            divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return regions.size();
    }

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_region, parent, false);
        return new RegionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int position) {
        Region region = regions.get(position);
        holder.bind(holder, position);
        holder.itemView.setOnClickListener(view -> callback.onItemClick(region, view));
    }
}
