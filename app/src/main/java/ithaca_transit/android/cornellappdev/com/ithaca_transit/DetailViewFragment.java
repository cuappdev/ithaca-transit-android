package ithaca_transit.android.cornellappdev.com.ithaca_transit;
import static ithaca_transit.android.cornellappdev.com.ithaca_transit.MainActivity.mMapsPresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.checkerframework.checker.linear.qual.Linear;

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
    private TextView busNumber;
    private Route mRoute;
    private SlidingUpPanelLayout mSlidingPanel;
    private RelativeLayout mRelativeLayout;


    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_view_fragment, container, false);

        mContext = view.getContext();
        mRoute = mMapsPresenter.mRepo.getSelectedRoute();
        mDetailDirections = mRoute.getDetailDirections();
        mLayoutInflater = (LayoutInflater) ((MainActivity)mContext).getSystemService(
                 Context.LAYOUT_INFLATER_SERVICE);

        mRelativeLayout = view.findViewById(R.id.detailView);
        mSlidingPanel = view.findViewById(R.id.sliding_panel);

        String timeText = String.format("Depart at <b>%s</b> from <b>%s</b>",
                mRoute.getBusArrival(),
                mDetailDirections.get(0).getName());
        headerText = view.findViewById(R.id.header_direction);
        //TODO: add bold formatting
        headerText.setText(Html.fromHtml(timeText));

        busNumber = view.findViewById(R.id.tv_bus_number_two);
        busNumber.setText(mDetailDirections.get(1).getRouteNumber().toString());
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
                Log.d("dddetail", mDetailDirections.toString());
                return false;
            }
        });
        listView.setAdapter(listViewAdapter);

//        mSlidingPanel.setPanelHeight(600);

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
