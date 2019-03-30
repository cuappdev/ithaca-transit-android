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
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


public final class OptionsFragment extends Fragment {
    @NotNull
    public TextView allRoutesText;

    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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


    @NotNull
    public final TextView getAllRoutesText() {
        return allRoutesText;
    }

}
