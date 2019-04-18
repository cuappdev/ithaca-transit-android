package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class DotsListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Direction> mDirections;

    public DotsListAdapter(@NonNull Context context, ArrayList<Direction> directions) {
        mContext = context;
        mDirections = directions;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.dots_list_item;

        View view = LayoutInflater.from(this.mContext).inflate(layoutId, parent, false);
        RecyclerView.ViewHolder viewHolder =
                (RecyclerView.ViewHolder) (new DotsListAdapter.ImageViewHolder(view));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            throw new ClassCastException(
                    "null cannot be cast to non-null type com.cornellappdev.android.eatery"
                            + ".DotsListAdapter.ImageAdapterViewHolder");
        } else {
            DotsListAdapter.ImageViewHolder holder2 =
                    (DotsListAdapter.ImageViewHolder) holder;

            //last direction
            Direction direction = mDirections.get(position);

            // In this case, we walk from start to end location
            if (position == 0 && direction.getType().equals("walk")) {
                holder2.iconImageDot.setImageResource(R.drawable.walkstart);
                holder2.iconImageLine.setImageResource(R.drawable.walkonlyend);
            }
            // Departing but not arriving
            else if (direction.getType().equals("depart")) {
                holder2.iconImageDot.setImageResource(R.drawable.busstop);

                if (position + 1 < mDirections.size() && mDirections.get(
                        position + 1).getType().equals("depart")) {
                    holder2.iconImageLine.setImageResource(R.drawable.line);
                }
            }
            // Arriving
            else if (position == mDirections.size() - 1) {
                if (direction.getType().equals("walk")) {
                    holder2.iconImageDot.setImageResource(R.drawable.walkfromstop);
                } else {
                    holder2.iconImageDot.setImageResource(R.drawable.busstop);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDirections.size();
    }

    public final class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImageDot;
        private ImageView iconImageLine;
        private View rootView;

        public ImageView getIconImage() {
            return iconImageDot;
        }

        public View getRootView() {
            return rootView;
        }

        public ImageViewHolder(@NotNull View itemView) {
            super(itemView);
            this.iconImageDot = (ImageView) itemView.findViewById(
                    R.id.icon_image_one);
            this.iconImageLine = (ImageView) itemView.findViewById(
                    R.id.icon_image_two);
            this.rootView = itemView;
        }
    }
}
