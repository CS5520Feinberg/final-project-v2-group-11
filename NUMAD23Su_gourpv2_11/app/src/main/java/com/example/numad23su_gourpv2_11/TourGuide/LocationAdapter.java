package com.example.numad23su_gourpv2_11.TourGuide;

import android.content.Intent;
import android.net.Uri;
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
        // get element from the dataset at this position
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
        public TextView latitudeTextView;
        public TextView longitudeTextView;

        public TextView phoneTextView;


        public ViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.nameTextView);
            latitudeTextView = v.findViewById(R.id.latitudeTextView);
            longitudeTextView = v.findViewById(R.id.longitudeTextView);
            phoneTextView = v.findViewById(R.id.phoneTextView);

            phoneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumberWithPrefix = ((TextView) v).getText().toString();
                    String phoneNumber = phoneNumberWithPrefix.replace("Phone: ", "");
                    Intent dial = new Intent(Intent.ACTION_DIAL);
                    dial.setData(Uri.parse("tel:" + phoneNumber));
                    v.getContext().startActivity(dial);
                }
            });
        }

        public void bind(LocationClass location) {
            nameTextView.setText(location.getName());
            latitudeTextView.setText("Latitude :" + location.getLatitude());
            longitudeTextView.setText("Longitude: " + location.getLongitude());
            phoneTextView.setText("Phone: " + location.getPhone());
        }
    }
}
