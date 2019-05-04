package ithaca_transit.android.cornellappdev.com.ithaca_transit;
import static ithaca_transit.android.cornellappdev.com.ithaca_transit.MainActivity.mMapsPresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.annotation.Nullable;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.ExpandableListViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;

public class DetailViewFragment extends Fragment {

    private Context mContext;
    private ArrayList<Direction> mDetailDirections;
    private LayoutInflater mLayoutInflater;
    private TextView headerText;
    private Route mRoute;


    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_view_fragment, container, false);

        mContext = view.getContext();
        mRoute = mMapsPresenter.mRepo.getSelectedRoute();
        mDetailDirections = mRoute.getDetailDirections();
        mLayoutInflater = (LayoutInflater) ((MainActivity)mContext).getSystemService(
                 Context.LAYOUT_INFLATER_SERVICE);

        String timeText = String.format("Depart at %s from %s", mRoute.getBusArrival(),
                mDetailDirections.get(mDetailDirections.size()-1).getName());
        headerText = view.findViewById(R.id.header_direction);
        //TODO: add bold formatting
        headerText.setText(timeText);
        return view;
    }

    public interface OnDetailViewFragmentListener {
        void changeRoutes(String start, String end, String name);
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

//        if(mRoute.isWalkOnlyRoute()){
//            LinearLayout linearLayout = ((MainActivity) mContext).findViewById(
//                    R.id.bus_container);
//            linearLayout.setVisibility(View.VISIBLE);
//
//            int count = 0;
//            Direction direction;
//            while(count < mRoute.getDirections().length){
//                direction = mRoute.getDirections()[count];
//                if(direction.equals("depart")){
//                    direction =
//                }
//            }
//            TextView busNumber = linearLayout.findViewById(R.id.tv_bus_number_header);
//            busNumber.setText();
//        }


    }
}
