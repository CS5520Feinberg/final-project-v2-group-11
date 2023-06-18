package com.example.numad23su_gourpv2_11;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {
    private static final String API_KEY = "41233304fa73bb4fed2ae9064a5ab935";
    private EditText cityName;
    private ProgressBar progressBar;

    private ImageView errorImage, cityImage, temperatureImage, pressureImage, humidityImage,
            wspImage, tempMinImage, tempMaxImage, feelsLikeImage;

    private TextView cityNameResult, temperatureResult, humidityresult, pressureResult,
            windspeedResult, tempMinResult, tempMaxResult, feelsLikeResult;
    private String parsedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        cityName = findViewById(R.id.city_name);
        Button fetchButton = findViewById(R.id.fetch_button);
        cityNameResult = findViewById(R.id.cityNameResult);
        temperatureResult = findViewById(R.id.temperatureResult);
        humidityresult = findViewById(R.id.humidityResult);
        pressureResult = findViewById(R.id.pressureResult);
        windspeedResult = findViewById(R.id.windspeedResult);
        tempMinResult = findViewById(R.id.tempMinResult);
        tempMaxResult = findViewById(R.id.tempMaxResult);
        feelsLikeResult = findViewById(R.id.feelsLikeResult);

        errorImage = findViewById(R.id.errorImage);
        cityImage = findViewById(R.id.cityImages);
        temperatureImage = findViewById(R.id.temperatureImage);
        humidityImage = findViewById(R.id.humidityImage);
        wspImage = findViewById(R.id.wspImage);
        pressureImage = findViewById(R.id.pressureImage);
        tempMinImage = findViewById(R.id.tempminimage);
        tempMaxImage = findViewById(R.id.tempmaximage);
        feelsLikeImage = findViewById(R.id.feelslikeimage);

        progressBar = findViewById(R.id.progress_circular);

        //fetchButton.setOnClickListener(view -> fetchWeatherData());
        fetchButton.setOnClickListener(view -> {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            fetchWeatherData();
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("parsedString", this.parsedString);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        this.parsedString = savedInstanceState.getString("parsedString");
        parseJsonData(this.parsedString);
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
                this.parsedString = result.toString();
                parseJsonData(this.parsedString);
            } catch (Exception e) {
                Log.e("SecondActivity", "Error fetching weather data", e);

                runOnUiThread(() -> cityImage.setVisibility(View.GONE));
                runOnUiThread(() -> cityNameResult.setText(""));

                runOnUiThread(() -> temperatureImage.setVisibility(View.GONE));
                runOnUiThread(() -> temperatureResult.setText(""));

                runOnUiThread(() -> humidityImage.setVisibility(View.GONE));
                runOnUiThread(() -> humidityresult.setText(""));

                runOnUiThread(() -> pressureImage.setVisibility(View.GONE));
                runOnUiThread(() -> pressureResult.setText(""));

                runOnUiThread(() -> wspImage.setVisibility(View.GONE));
                runOnUiThread(() -> windspeedResult.setText(""));

                runOnUiThread(() -> tempMinImage.setVisibility(View.GONE));
                runOnUiThread(() -> tempMinResult.setText(""));

                runOnUiThread(() -> tempMaxImage.setVisibility(View.GONE));
                runOnUiThread(() -> tempMaxResult.setText(""));

                runOnUiThread(() -> feelsLikeImage.setVisibility(View.GONE));
                runOnUiThread(() -> feelsLikeResult.setText(""));

                runOnUiThread(() -> errorImage.setVisibility(View.VISIBLE));
                Snackbar.make(findViewById(R.id.linearLayout), "The city does not exist", Snackbar.LENGTH_LONG).show();
                //Toast.makeText(SecondActivity.this, "The city does not exist", Toast.LENGTH_LONG).show();
            } finally {
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            }
        });
        thread.start();
    }

    @SuppressLint("SetTextI18n")
    private void parseJsonData(String jsonData) {
        try {
            JSONObject obj = new JSONObject(jsonData);
            JSONObject main = obj.getJSONObject("main");
            final String temperature = main.getString("temp") + "°C";
            final String humidity = main.getString("humidity");
            final String pressure = main.getString("pressure");
            final String tempMin = main.getString("temp_min") + "°C";
            final String tempMax = main.getString("temp_max") + "°C";
            final String feelsLike = main.getString("feels_like") + "°C";

            JSONObject wind = obj.getJSONObject("wind");
            final String windSpeed = wind.getString("speed");

            final String cityName = obj.getString("name");

            runOnUiThread(() -> cityImage.setVisibility(View.VISIBLE));
            runOnUiThread(() -> cityNameResult.setText("City: " + cityName));

            runOnUiThread(() -> temperatureImage.setVisibility(View.VISIBLE));
            runOnUiThread(() -> temperatureResult.setText("Temperature: " + temperature));

            runOnUiThread(() -> tempMinImage.setVisibility(View.VISIBLE));
            runOnUiThread(() -> tempMinResult.setText("Minimum Temperature: " + tempMin));

            runOnUiThread(() -> tempMaxImage.setVisibility(View.VISIBLE));
            runOnUiThread(() -> tempMaxResult.setText("Maximum Temperature: " + tempMax));

            runOnUiThread(() -> feelsLikeImage.setVisibility(View.VISIBLE));
            runOnUiThread(() -> feelsLikeResult.setText("Feels Like: " + feelsLike));

            runOnUiThread(() -> humidityImage.setVisibility(View.VISIBLE));
            runOnUiThread(() -> humidityresult.setText("Humidity: " + humidity));

            runOnUiThread(() -> pressureImage.setVisibility(View.VISIBLE));
            runOnUiThread(() -> pressureResult.setText("Pressure: " + pressure));

            runOnUiThread(() -> wspImage.setVisibility(View.VISIBLE));
            runOnUiThread(() -> windspeedResult.setText("Wind Speed: " + windSpeed));

            runOnUiThread(() -> errorImage.setVisibility(View.GONE));
            //runOnUiThread(() -> resultTextView.setText("City: " + cityName + "\nTemperature: " + temperature + "\nHumidity: " + humidity + "\nPressure: " + pressure + "\nWind Speed: " + windSpeed));
        } catch (Exception e) {
            Log.e("SecondActivity", "Error parsing JSON", e);
        }
    }
}
