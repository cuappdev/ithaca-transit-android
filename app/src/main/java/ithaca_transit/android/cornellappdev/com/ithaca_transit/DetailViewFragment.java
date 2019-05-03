package ithaca_transit.android.cornellappdev.com.ithaca_transit;
import static ithaca_transit.android.cornellappdev.com.ithaca_transit.MainActivity.mMapsPresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import javax.annotation.Nullable;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.ExpandableListViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;

public class DetailViewFragment extends Fragment {

    private Context mContext;
    private ArrayList<Direction> mDetailDirections;
    private LayoutInflater mLayoutInflater;


    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_view_fragment, container, false);

        mContext = view.getContext();

        Route route = mMapsPresenter.mRepo.getSelectedRoute();
        mDetailDirections = route.getDetailDirections();
        mLayoutInflater = (LayoutInflater) ((MainActivity)mContext).getSystemService(
                 Context.LAYOUT_INFLATER_SERVICE);

        return view;
    }

    public void setUpList(){
        ExpandableListView listView = ((MainActivity) mContext).findViewById(
                R.id.directions_list_view);
        ExpandableListViewAdapter listViewAdapter = new ExpandableListViewAdapter(mLayoutInflater,
                mDetailDirections);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i,
                    long l) {
                return mDetailDirections.get(i).getStops().length > 0;
            }
        });
        listView.setAdapter(listViewAdapter);
    }
}
