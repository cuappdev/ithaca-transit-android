package ithaca_transit.android.cornellappdev.com.ithaca_transit;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

import org.jetbrains.annotations.NotNull;


public final class RoutesRecyclerAdapter extends RecyclerView.Adapter<RoutesRecyclerAdapter.ViewHolder> {
    private final String[] durations = new String[]{"8:30 - 8:45", "8:50 - 9:10", "9:10 - 9:30"};
    private final String[] descriptions = new String[]{"via Campus Road", "Via University Ave", "via West Ave"};

    @NotNull
    public RoutesRecyclerAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoutesRecyclerAdapter.ViewHolder holder, int position) {
        holder.bindItems(durations[position], descriptions[position]);

    }

    public int getItemCount() {
        return durations.length;
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public final void bindItems(@NotNull String duration, @NotNull String description) {

            TextView dur = itemView.findViewById(R.id.duration);
            TextView desc = itemView.findViewById(R.id.route_description);
            dur.setText(duration);
            desc.setText(description);

        }

    }
}
