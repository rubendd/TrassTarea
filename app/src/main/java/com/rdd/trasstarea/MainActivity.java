package com.rdd.trasstarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.rdd.trasstarea.activities.listactivity.ListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.button);
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