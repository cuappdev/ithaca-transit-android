package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;



import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class IconsListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Direction> mDirections;

    public IconsListAdapter(@NonNull Context context, ArrayList<Direction> directions) {
        mContext = context;
        mDirections = directions;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.icon_list_item;

        View view = LayoutInflater.from(this.mContext).inflate(layoutId, parent, false);
        RecyclerView.ViewHolder viewHolder =
                (RecyclerView.ViewHolder) (new IconsListAdapter.ImageViewHolder(view));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            throw new ClassCastException(
                    "null cannot be cast to non-null type com.cornellappdev.android.eatery"
                            + ".IconsListAdapter.ImageAdapterViewHolder");
        } else {
            IconsListAdapter.ImageViewHolder holder2 =
                    (IconsListAdapter.ImageViewHolder) holder;
            Direction direction = mDirections.get(position);
            if (((Direction) direction).getType().equals("walk")) {
                holder2.iconImage.setImageResource(R.drawable.walk);
                holder2.containerImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDirections.size();
    }

    public final class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImage;
        private View rootView;
        private View containerImage;

        public ImageView getIconImage() {
            return iconImage;
        }

        public View getRootView() {
            return rootView;
        }

        public ImageViewHolder(View itemView) {
            super(itemView);
            this.iconImage = (ImageView) itemView.findViewById(
                    R.id.icon_image);
            this.rootView = itemView;
            this.containerImage = (RelativeLayout) itemView.findViewById(
                    R.id.container_bus_number);
        }
    }
}
