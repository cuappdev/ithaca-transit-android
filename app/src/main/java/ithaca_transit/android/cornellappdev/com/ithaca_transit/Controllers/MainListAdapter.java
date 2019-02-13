package ithaca_transit.android.cornellappdev.com.ithaca_transit.Controllers;

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
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class MainListAdapter extends Adapter {
    private String mQuery;
    private Context mContext;
    private final MainListAdapter.ListAdapterOnClickHandler mListAdapterOnClickHandler;
    private Place[] mPlaceList;

    public final void setList(@NotNull Place[] list, @NotNull String query) {
        mQuery = query;
        mPlaceList = list;
        notifyDataSetChanged();
    }

    @Nullable
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.card_item_maps;
        System.out.println("context" + this.mContext);

        View view = LayoutInflater.from(this.mContext).inflate(layoutId, parent, false);
        ViewHolder viewHolder = (ViewHolder)(new MainListAdapter.TextAdapterViewHolder(view));
        return viewHolder;
    }

    public int getItemCount() {
        return this.mPlaceList.length;
    }

    public void onBindViewHolder(@Nullable ViewHolder holder, int position) {
        if (holder == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.cornellappdev.android.eatery.MainListAdapter.TextAdapterViewHolder");
        } else {
            MainListAdapter.TextAdapterViewHolder holder2 = (MainListAdapter.TextAdapterViewHolder)holder;
            holder2.getPlaceName().setText((CharSequence)this.mPlaceList[position].getName());
        }
    }

    public MainListAdapter(@NotNull Context mContext, @NotNull MainListAdapter.ListAdapterOnClickHandler mListAdapterOnClickHandler, @NotNull Place[] mPlaceList) {
        super();
        this.mContext = mContext;
        this.mListAdapterOnClickHandler = mListAdapterOnClickHandler;
        this.mPlaceList = mPlaceList;
    }

    // $FF: synthetic method
    public static final void access$setMPlaceList$p(MainListAdapter $this, @NotNull Place[] var1) {
        $this.mPlaceList = var1;
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

        public final void setPlaceName(@NotNull TextView var1) {
            Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
            this.placeName = var1;
        }

        public void onClick(@NotNull View v) {
            Intrinsics.checkParameterIsNotNull(v, "v");
            int adapterPosition = this.getAdapterPosition();
            MainListAdapter.this.mListAdapterOnClickHandler.onClick(adapterPosition, MainListAdapter.this.mPlaceList);
        }

        public TextAdapterViewHolder(@NotNull View itemView) {
            super(itemView);
            this.placeName = (TextView) itemView.findViewById(R.id.place_name);
            itemView.setOnClickListener((OnClickListener)this);
        }
    }
}
