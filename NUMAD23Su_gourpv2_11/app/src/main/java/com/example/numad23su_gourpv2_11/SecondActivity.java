package com.example.numad23su_gourpv2_11;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {
    private static final String API_KEY = "41233304fa73bb4fed2ae9064a5ab935";
    private EditText cityName;
    private Button fetchButton;
    private TextView resultTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        cityName = findViewById(R.id.city_name);
        fetchButton = findViewById(R.id.fetch_button);
        resultTextView = findViewById(R.id.result_text_view);
        progressBar = findViewById(R.id.progress_circular);

        fetchButton.setOnClickListener(view -> fetchWeatherData());
    }

    private void fetchWeatherData() {
        String city = cityName.getText().toString();

        Thread thread = new Thread(() -> {
            try {
                runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + API_KEY);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder result = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                parseJsonData(result.toString());
            } catch (Exception e) {
                Log.e("SecondActivity", "Error fetching weather data", e);
            } finally {
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            }
        });
        thread.start();
    }

    private void parseJsonData(String jsonData) {
        try {
            JSONObject obj = new JSONObject(jsonData);
            JSONObject main = obj.getJSONObject("main");
            final String temperature = main.getString("temp") + "°C";
            final String humidity = main.getString("humidity");
            final String pressure = main.getString("pressure");

            JSONObject wind = obj.getJSONObject("wind");
            final String windSpeed = wind.getString("speed");

            final String cityName = obj.getString("name");

            runOnUiThread(() -> resultTextView.setText("City: " + cityName + "\nTemperature: " + temperature + "\nHumidity: " + humidity + "\nPressure: " + pressure + "\nWind Speed: " + windSpeed));
        } catch (Exception e) {
            Log.e("SecondActivity", "Error parsing JSON", e);
        }
    }
}
