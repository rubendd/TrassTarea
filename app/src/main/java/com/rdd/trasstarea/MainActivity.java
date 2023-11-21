package com.rdd.trasstarea;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.rdd.trasstarea.activities.listactivity.ListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LottieAnimationView btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> start());
    }

    /**
     * Emepezar actividad.
     */
    private void start() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }










    
}