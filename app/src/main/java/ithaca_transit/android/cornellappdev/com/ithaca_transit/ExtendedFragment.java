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
import ithaca_transit.android.cornellappdev.com.ithaca_transit.R.id;
import org.jetbrains.annotations.Nullable;

public final class ExtendedFragment extends Fragment {

    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_options_extended_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(id.routes);
        recyclerView.setLayoutManager((LayoutManager)(new LinearLayoutManager(this.getContext(), 1, false)));
        RoutesRecyclerAdapter adapter = new RoutesRecyclerAdapter();
        recyclerView.setAdapter((Adapter)adapter);
        return view;
    }

}
