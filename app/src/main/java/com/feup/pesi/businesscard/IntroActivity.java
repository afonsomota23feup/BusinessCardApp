package com.feup.pesi.businesscard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 4000; // Tempo de exibição da tela de introdução em milissegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);


                finish();
            }
        }, SPLASH_DELAY);
    }
}
