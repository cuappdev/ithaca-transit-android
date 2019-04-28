package ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Place;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R;

public class RouteSwitcherAdapter extends ArrayAdapter<Place> {
    List<Place> mPlaceList;

    public RouteSwitcherAdapter(Context context, ArrayList<Place> places) {
        super(context, 0, places);
        mPlaceList = places;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Place place = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_list_item, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.rs_autocomplete_title);
        TextView details = (TextView) convertView.findViewById(R.id.rs_autocomplete_detail);
        title.setText(place.getName());
        details.setText(place.getDetail());
        return convertView;
    }

    public Place getItem(int position){
        return mPlaceList.get(position);
    }

}
