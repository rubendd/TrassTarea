package com.rdd.trasstarea.activities.createtaskactivity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rdd.trasstarea.R;

public class SecondCreateTaskActv extends AppCompatActivity {

    private EditText editText;
    private Button backBtn, createBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task2);
        findViewById(R.id.editTextDescription);
        createBtn = findViewById(R.id.create);
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(v -> finish());
    }

    private void crearTarea(){
        if (!CheckTask.checkEditText(editText)){

        }
    }

}
