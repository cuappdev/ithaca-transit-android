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

        View view = inflater.inflate(R.layout.route_options_fragment, container, false);
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

    public void setRouteCard(View view){
        Route route = Repository.getInstance().getSelectedRoute();
        TextView duration = view.findViewById(R.id.duration);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(route.getArrivalTime());
        int arrivalHour = calendar.get(Calendar.HOUR_OF_DAY);
        int arrivalMintues = calendar.get(Calendar.MINUTE);

        calendar.setTime(route.getDepartureTime());
        int departureHour = calendar.get(Calendar.HOUR_OF_DAY);
        int departureMinutes = calendar.get(Calendar.MINUTE);


        String arrivalAppend;
        String departureAppend;

        if(arrivalHour > 11){
            arrivalAppend = "PM";
        }
        else{
            arrivalAppend = "AM";
        }

        if(departureHour > 11){
            arrivalAppend = "PM";
        }
        else{
            arrivalAppend = "AM";
        }

        duration.setText(arrivalHour + ":" + arrivalMintues + "-" + departureHour + ":" + departureMinutes);

        // Boarding time for routes starting with bus, walk description for routes starting with
        // walk path
        TextView routeDescription = view.findViewById(R.id.duration);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ssz");
        Date currentDate = new Date(System.currentTimeMillis());
        calendar.setTime(currentDate);

        // if more than 24 hours, put day
        // if less than 24 hours but less than hour, put number of hours
        routeDescription.setText();


    }

    @NotNull
    public final TextView getAllRoutesText() {
        return allRoutesText;
    }

}
