package com.company.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    ImageView image;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        image = findViewById(R.id.imageViewSplash);
        title = findViewById(R.id.textViewSplash);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_anim);
        title.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(Splash_Screen.this, Login_Page.class);
                startActivity(i);
                finish();

            }
        }, 5000);

    }
}
