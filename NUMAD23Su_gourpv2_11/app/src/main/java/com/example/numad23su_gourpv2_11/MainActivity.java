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

        Button tourGuideButton = findViewById(R.id.tourGuide);

        tourGuideButton.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, TourGuide.class);
            startActivity(intent);
        });
    }
}