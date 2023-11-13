package com.rdd.trasstarea.activities.createtaskactivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.comunicator.IComunicator;
import com.rdd.trasstarea.model.Task;

import java.time.LocalDate;
import java.util.Calendar;

public class SecondCreateTaskActv extends AppCompatActivity  {

    String titulo;
    String date1, date2;
    private int estado;
    private boolean prioritaria;
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
        createBtn.setOnClickListener(v -> crearTarea());
        recibirDatos();
    }

    private Task crearTarea(){
        if (!CheckTask.checkEditText(editText)){

        }
        return null;
    }

    private void recibirDatos(){
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        titulo = intent.getStringExtra(CreateTaskActivity.TITULO);
        date1 = intent.getStringExtra(CreateTaskActivity.DATE1);
        date2 = intent.getStringExtra(CreateTaskActivity.DATE2);
        estado = intent.getIntExtra(CreateTaskActivity.STATE, 0);
        prioritaria = intent.getBooleanExtra(CreateTaskActivity.PRIORITAIO, false);
    }

}
