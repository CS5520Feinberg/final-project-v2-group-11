package com.example.numad23su_gourpv2_11;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.Button;

        import androidx.appcompat.app.AppCompatActivity;

        import com.example.numad23su_gourpv2_11.AtYourService.SecondActivity;
        import com.example.numad23su_gourpv2_11.StickItToEm.StickItToEm;
        import com.example.numad23su_gourpv2_11.TourGuide.TourGuide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openSecondActivityButton = findViewById(R.id.button_open_second_activity);
        Button stickItToEmButton = findViewById(R.id.stickItToEmButton);
        Button tourGuideButton = findViewById(R.id.tourGuide);

        openSecondActivityButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        stickItToEmButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StickItToEm.class);
            startActivity(intent);
        });

        tourGuideButton.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, TourGuide.class);
            startActivity(intent);
        });
    }
}