package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.appdev.futurenovajava.APIResponse;
import com.appdev.futurenovajava.Endpoint;
import com.appdev.futurenovajava.FutureNovaRequest;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Favorite;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.SectionedRoutes;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;
import java8.util.Optional;
import kotlin.TypeCastException;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public final class FavoritesListAdapter extends Adapter {

    // stores all routes for favorite at the position represented by the key
    private HashMap<Integer, SectionedRoutes>
            mAllRoutesToFavorites = new HashMap();

    private Context mContext;
    private ArrayList<Favorite> mFavList;
    private final TextAdapterOnClickHandler mListAdapterOnClickHandler;

    // stores most optimal route for favorite
    private Route[] mOptimalRoutes;

    public FavoritesListAdapter(@NotNull Context context, @NotNull TextAdapterOnClickHandler listAdapterOnClickHandler,
                                @NotNull ArrayList<Favorite> favorites) {
        super();
        mContext = context;
        mFavList = favorites;
        mListAdapterOnClickHandler = listAdapterOnClickHandler;
        mOptimalRoutes = new Route[favorites.size()];
    }

    public final void setList(@NotNull ArrayList<Favorite> list, @NotNull String query) {
        mFavList = list;
        mOptimalRoutes = new Route[list.size()];
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
        return this.mFavList.size();
    }

    public void onBindViewHolder(@Nullable ViewHolder holder, int position) {
        if (holder == null) {
            throw new TypeCastException("null cannot be cast to non-null type com.cornellappdev.android.eatery.FavoritesFavoritesListAdapter.TextAdapterViewHolder");
        } else {
            FavoritesListAdapter.TextAdapterViewHolder holder2 = (FavoritesListAdapter.TextAdapterViewHolder)holder;
            holder2.getFavoriteName().setText(mFavList.get(position).getStartPlace().getName() + "--" +
                    mFavList.get(position).getEndPlace().getName());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("\"America/NewYork\""));
            int secondsEpoch = (int) (calendar.getTimeInMillis()/1000L);

            // Getting the other route options
            HashMap<String, String> map = new HashMap();
            map.put("Content-Type", "application/json");
            JSONObject searchJSON = new JSONObject();
            try {
                searchJSON.put("start", mFavList.get(position).getStartPlace().toString());
                searchJSON.put("end", mFavList.get(position).getEndPlace().toString());
                searchJSON.put("destinationName", mFavList.get(position).getEndPlace().getName());
                searchJSON.put("arriveBy", false);
                searchJSON.put("time", secondsEpoch);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final RequestBody requestBody =
                    RequestBody.create(MediaType.get("application/json; charset=utf-8"), searchJSON.toString());

            Endpoint searchEndpoint2 = new Endpoint()
                    .path("v2/route")
                    .body(Optional.of(requestBody))
                    .headers(map)
                    .method(Endpoint.Method.POST);

            FutureNovaRequest.make(SectionedRoutes.class, searchEndpoint2).thenAccept((APIResponse<SectionedRoutes> response) -> {
                SectionedRoutes sectionedRoutes = response.getData();
                mAllRoutesToFavorites.put(position, sectionedRoutes);
                mOptimalRoutes[position] = sectionedRoutes.getOptRoute();
            });
        }
    }

    public interface TextAdapterOnClickHandler {
        void onFavoriteClick(int var1, @NotNull ArrayList<Favorite> var2);
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

    public HashMap<Integer, SectionedRoutes> getmAllRoutesToFavorites() {
        return mAllRoutesToFavorites;
    }
}
