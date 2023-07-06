package com.example.numad23su_gourpv2_11;

        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.Button;

        import androidx.appcompat.app.AppCompatActivity;

        import com.example.numad23su_gourpv2_11.AtYourService.SecondActivity;
        import com.example.numad23su_gourpv2_11.StickItToEm.StickItToEm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openSecondActivityButton = findViewById(R.id.button_open_second_activity);
        Button stickItToEmBtn = findViewById(R.id.stickItToEmButton);

        openSecondActivityButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        stickItToEmBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, StickItToEm.class);
            startActivity(intent1);
        });
    }
}