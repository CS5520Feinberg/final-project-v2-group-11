package com.example.numad23su_gourpv2_11;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {
    private Button fetchButton;
    private ImageView resultImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        fetchButton = findViewById(R.id.fetch_button);
        resultImageView = findViewById(R.id.result_image_view);
        progressBar = findViewById(R.id.progress_circular);

        fetchButton.setOnClickListener(view -> fetchDogImage());
    }

    private void fetchDogImage() {
        Thread thread = new Thread(() -> {
            try {
                runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
                URL url = new URL("https://dog.ceo/api/breeds/image/random");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = reader.readLine();

                JSONObject response = new JSONObject(line);
                String imageUrl = response.getString("message");

                InputStream in = new URL(imageUrl).openStream();
                final Bitmap bmp = BitmapFactory.decodeStream(in);
                in.close();

                runOnUiThread(() -> {
                    resultImageView.setImageBitmap(bmp);
                    progressBar.setVisibility(View.GONE);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            }
        });
        thread.start();
    }



}
