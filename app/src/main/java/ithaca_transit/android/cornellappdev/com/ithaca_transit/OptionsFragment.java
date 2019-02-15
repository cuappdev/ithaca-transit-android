package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class OptionsFragment extends Fragment {
    @NotNull
    public TextView allRoutesText;

    @NotNull
    public final TextView getAllRoutesText() {
        return allRoutesText;
    }

    public final void setAllRoutesText(@NotNull TextView var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.allRoutesText = var1;
    }

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

}
