package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Models.Route;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.Singleton.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


public final class OptionsFragment extends Fragment {

    public TextView allRoutesText;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.route_options_holder, container, false);
        ((TextView)view.findViewById(R.id.allRoutes)).setOnClickListener((new OnClickListener() {
            public final void onClick(View view) {
                ExtendedFragment extendedFragment = new ExtendedFragment();
                FragmentTransaction fragmentTransaction = OptionsFragment.this.getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, (Fragment)extendedFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }));

        return view;
    }


    public final TextView getAllRoutesText() {
        return allRoutesText;
    }

}
