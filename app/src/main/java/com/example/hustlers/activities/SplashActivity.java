package com.example.hustlers.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hustlers.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        Intent main = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(main);
                    }else {
                        Intent intent = new Intent(getApplicationContext(),LoginSignupActivity.class);
                        startActivity(intent);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}