package com.example.numad23su_gourpv2_11.TourGuide;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.numad23su_gourpv2_11.R;

import java.net.URI;
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
        public TextView descTextView;
        public TextView addressTextView;
        public TextView latitudeTextView;
        public TextView longitudeTextView;

        public TextView phoneTextView;

        public TextView urlTextView;


        public ViewHolder(View v) {
            super(v);
            nameTextView = v.findViewById(R.id.nameTextView);
            descTextView = v.findViewById(R.id.descriptionTextView);
            addressTextView = v.findViewById(R.id.addressTextView);
            latitudeTextView = v.findViewById(R.id.latitudeTextView);
            longitudeTextView = v.findViewById(R.id.longitudeTextView);
            phoneTextView = v.findViewById(R.id.phoneTextView);
            urlTextView = v.findViewById(R.id.urlTextView);

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
            descTextView.setText(location.getDescription());
            addressTextView.setText(location.getAddress());
            latitudeTextView.setText("Latitude :" + location.getLatitude());
            longitudeTextView.setText("Longitude: " + location.getLongitude());
            phoneTextView.setText("Phone: " + location.getPhone());

            urlTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = location.getURLlink();
                    //String url = ((TextView) view).getText().toString();
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    if (browserIntent.resolveActivity(view.getContext().getPackageManager()) != null) {
//                        view.getContext().startActivity(browserIntent);
//                    } else {
//                        Log.d("URL Handler", "No Intent available to handle action");
//                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setPackage("com.android.chrome");
                    try {
                        view.getContext().startActivity(browserIntent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed and open URL in default browser
                        browserIntent.setPackage(null);
                        view.getContext().startActivity(browserIntent);
                    }

                }
            });

            try {
                final double latitude = Double.parseDouble(location.getLatitude());
                final double longitude = Double.parseDouble(location.getLongitude());

                nameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                            v.getContext().startActivity(mapIntent);
                        } else {
                            Log.d("LocationAdapter", "No Intent available to handle action");
                        }
                    }
                });
            } catch (NumberFormatException e) {
                Log.e("LocationAdapter", "Could not parse latitude/longitude", e);
            }
        }
    }
}
