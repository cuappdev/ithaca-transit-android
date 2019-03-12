package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ithaca_transit.android.cornellappdev.com.ithaca_transit.Adapters.RoutesListAdapter;
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R.id;
import org.jetbrains.annotations.Nullable;

public final class ExtendedFragment extends Fragment {

    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_options_extended_fragment, container, false);
        return view;
    }

}
