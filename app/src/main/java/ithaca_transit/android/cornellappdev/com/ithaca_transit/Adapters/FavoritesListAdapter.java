package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.appdev.futurenovajava.Endpoint;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Favorite;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Utils.FutureUtilities;
import kotlin.TypeCastException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class FavoritesListAdapter extends Adapter {
    private Endpoint.Config mConfig;
    private Context mContext;
    private Favorite[] mFavList;
    private final FavoritesListAdapter.ListAdapterOnClickHandler mListAdapterOnClickHandler;
    private Route[] mOptimalRoutes;


    public FavoritesListAdapter(@NotNull Context context, @NotNull FavoritesListAdapter.ListAdapterOnClickHandler listAdapterOnClickHandler,
                                @NotNull Favorite[] favorites, Endpoint.Config config) {
        super();
        mConfig = config;
        mContext = context;
        mFavList = favorites;
        mListAdapterOnClickHandler = listAdapterOnClickHandler;
        mOptimalRoutes = new Route[favorites.length];
    }


    public final void setList(@NotNull Favorite[] list, @NotNull String query) {
        mFavList = list;
        mOptimalRoutes = new Route[list.length];
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
        return this.mFavList.length;
    }

    public void onBindViewHolder(@Nullable ViewHolder holder, int position) {
        if (holder == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.cornellappdev.android.eatery.FavoritesFavoritesListAdapter.TextAdapterViewHolder");
        } else {
            FavoritesListAdapter.TextAdapterViewHolder holder2 = (FavoritesListAdapter.TextAdapterViewHolder)holder;
            holder2.getFavoriteName().setText(mFavList[position].getStartPlace().getName() + "--" +
                    mFavList[position].getEndPlace().getName());

            // Grabbing route associated with favorite
            Route[] routesList = FutureUtilities.getRoute(mConfig, mFavList[position].getStartPlace(),
                    mFavList[position].getEndPlace());
            Route optimalRoute = routesList[0];
            Repository.ourInstance.setRoutesList(routesList);

            mOptimalRoutes[position] = optimalRoute;
        }
    }

    public interface ListAdapterOnClickHandler {
        void onFavoriteClick(int var1, @NotNull Favorite[] var2);
    }

    public final class TextAdapterViewHolder extends ViewHolder implements OnClickListener {
        @NotNull
        private TextView favoriteName;

        @NotNull
        public final TextView getFavoriteName() {
            return this.favoriteName;
        }

        public void onClick(@NotNull View v) {
            int adapterPosition = this.getAdapterPosition();
            FavoritesListAdapter.this.mListAdapterOnClickHandler.onFavoriteClick(adapterPosition,
                    FavoritesListAdapter.this.mFavList);
        }

        public TextAdapterViewHolder(@NotNull View itemView) {
            super(itemView);
            this.favoriteName = (TextView) itemView.findViewById(R.id.place_name);
            itemView.setOnClickListener((OnClickListener)this);
        }
    }

    public Route[] getOptimalRoutes() {
        return mOptimalRoutes;
    }
}
