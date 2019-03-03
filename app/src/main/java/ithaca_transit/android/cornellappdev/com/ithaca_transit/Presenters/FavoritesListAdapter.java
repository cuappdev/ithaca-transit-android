package ithaca_transit.android.cornellappdev.com.ithaca_transit.Presenters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import kotlin.TypeCastException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class FavoritesListAdapter extends Adapter {
    private String mQuery;
    private Context mContext;
    private final FavoritesListAdapter.ListAdapterOnClickHandler mListAdapterOnClickHandler;
    private Place[] mPlaceList;

    public final void setList(@NotNull Place[] list, @NotNull String query) {
        mQuery = query;
        mPlaceList = list;
        notifyDataSetChanged();
    }

    @Nullable
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.card_item_maps;

        View view = LayoutInflater.from(this.mContext).inflate(layoutId, parent, false);
        ViewHolder viewHolder = (ViewHolder)(new FavoritesListAdapter.TextAdapterViewHolder(view));
        return viewHolder;
    }

    public int getItemCount() {
        return this.mPlaceList.length;
    }

    public void onBindViewHolder(@Nullable ViewHolder holder, int position) {
        if (holder == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.cornellappdev.android.eatery.FavoritesFavoritesListAdapter.TextAdapterViewHolder");
        } else {
            FavoritesListAdapter.TextAdapterViewHolder holder2 = (FavoritesListAdapter.TextAdapterViewHolder)holder;
            holder2.getPlaceName().setText((CharSequence)this.mPlaceList[position].getName());
        }
    }

    public FavoritesListAdapter(@NotNull Context mContext, @NotNull FavoritesListAdapter.ListAdapterOnClickHandler mListAdapterOnClickHandler, @NotNull Place[] mPlaceList) {
        super();
        this.mContext = mContext;
        this.mListAdapterOnClickHandler = mListAdapterOnClickHandler;
        this.mPlaceList = mPlaceList;
    }


    public interface ListAdapterOnClickHandler {
        void onClick(int var1, @NotNull Place[] var2);
    }

    public final class TextAdapterViewHolder extends ViewHolder implements OnClickListener {
        @NotNull
        private TextView placeName;

        @NotNull
        public final TextView getPlaceName() {
            return this.placeName;
        }

        public void onClick(@NotNull View v) {
            int adapterPosition = this.getAdapterPosition();
            FavoritesListAdapter.this.mListAdapterOnClickHandler.onClick(adapterPosition, FavoritesListAdapter.this.mPlaceList);
        }

        public TextAdapterViewHolder(@NotNull View itemView) {
            super(itemView);
            this.placeName = (TextView) itemView.findViewById(R.id.place_name);
            itemView.setOnClickListener((OnClickListener)this);
        }
    }
}
