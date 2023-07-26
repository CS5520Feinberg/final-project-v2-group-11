package com.example.numad23su_gourpv2_11.TourGuide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.numad23su_gourpv2_11.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private ArrayList<LocationClass> locations;

    // constructor
    public LocationAdapter(ArrayList<LocationClass> locations) {
        this.locations = locations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get element from your dataset at this position
        // replace the contents of the view with that element
        LocationClass location = locations.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nameTextView;
        public TextView latLongTextView;

        public ViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.nameTextView);
            latLongTextView = v.findViewById(R.id.latLongTextView);
        }

        public void bind(LocationClass location) {
            nameTextView.setText(location.getName());
            String latLongText = "Lat: " + location.getLatitude() + ", Long: " + location.getLongitude();
            latLongTextView.setText(latLongText);
        }
    }
}
