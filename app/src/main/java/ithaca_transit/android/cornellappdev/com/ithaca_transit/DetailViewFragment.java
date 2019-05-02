package ithaca_transit.android.cornellappdev.com.ithaca_transit;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.ExpandableListViewAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Direction;

public class DetailViewFragment extends Fragment {

    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_view_fragment, container, false);

        ArrayList<Direction> detailDirections =
                (ArrayList<Direction>) getArguments().get("directions");
        LayoutInflater layoutInflater =
                (LayoutInflater) getArguments().get("inflater");

        ExpandableListView listView = view.findViewById(
                R.id.directions_list_view);
        ExpandableListViewAdapter listViewAdapter = new ExpandableListViewAdapter(layoutInflater,
                detailDirections);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i,
                    long l) {
                return detailDirections.get(i).getStops().length > 0;
            }
        });
        listView.setAdapter(listViewAdapter);
        return view;
    }
}
