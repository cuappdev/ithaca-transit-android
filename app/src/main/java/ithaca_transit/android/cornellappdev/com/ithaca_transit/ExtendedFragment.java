package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public final class ExtendedFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_options_holder, container, false);
        return view;
    }

}
