package com.example.numad23su_gourpv2_11.TourGuide;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.numad23su_gourpv2_11.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private final ArrayList<LocationClass> locations;

    // constructor
    public LocationAdapter(ArrayList<LocationClass> locations) {
        this.locations = locations;
    }

    @NonNull
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
        public TextView descTextView;
        public TextView addressTextView;

        public TextView phoneTextView;

        public TextView urlTextView;

        public TextView checkCoordinates;


        public ViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.nameTextView);
            descTextView = v.findViewById(R.id.descriptionTextView);
            addressTextView = v.findViewById(R.id.addressTextView);
            phoneTextView = v.findViewById(R.id.phoneTextView);
            urlTextView = v.findViewById(R.id.urlTextView);
            checkCoordinates = v.findViewById(R.id.checkCoordinates);

            phoneTextView.setOnClickListener(v1 -> {
                String phoneNumberWithPrefix = ((TextView) v1).getText().toString();
                String phoneNumber = phoneNumberWithPrefix.replace("Phone: ", "");
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(Uri.parse("tel:" + phoneNumber));
                v1.getContext().startActivity(dial);
            });

        }

        @SuppressLint({"SetTextI18n", "QueryPermissionsNeeded"})
        public void bind(LocationClass location) {
            nameTextView.setText(location.getName());
            descTextView.setText(location.getDescription());
            addressTextView.setText("Address: " + location.getAddress());
            phoneTextView.setText("Phone: " + location.getPhone());

            urlTextView.setOnClickListener(view -> {
                String url = location.getURLlink();
                //String url = ((TextView) view).getText().toString();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.setPackage("com.android.chrome");
                try {
                    view.getContext().startActivity(browserIntent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed and open URL in default browser
                    browserIntent.setPackage(null);
                    view.getContext().startActivity(browserIntent);
                }

            });

            try {
                final double latitude = Double.parseDouble(location.getLatitude());
                final double longitude = Double.parseDouble(location.getLongitude());

                nameTextView.setOnClickListener(v -> {
                    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                        v.getContext().startActivity(mapIntent);
                    } else {
                        Log.d("LocationAdapter", "No Intent available to handle action");
                    }
                });
            } catch (NumberFormatException e) {
                Log.e("LocationAdapter", "Could not parse latitude/longitude", e);
            }

            checkCoordinates.setOnClickListener(view -> new MaterialAlertDialogBuilder(view.getContext())
                    .setTitle("Coordinates")
                    .setMessage("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude())
                    .setPositiveButton("OK", /* listener = */ null)
                    .show());
        }
    }
}
