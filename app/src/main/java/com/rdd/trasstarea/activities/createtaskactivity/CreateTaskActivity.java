package com.rdd.trasstarea.activities.createtaskactivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.fragments.CreateSecondTaskFrag;
import com.rdd.trasstarea.activities.createtaskactivity.fragments.CreateTaskFragment;

public class CreateTaskActivity extends AppCompatActivity {


    public static final String TITULO = "titulo";
    public static final String DATE1 = "fecha1";
    public static final String DATE2 = "fecha2";
    public static final String STATE = "estado";
    public static final String PRIORITAIO = "prioritario";


    private CreateTaskFragment createTaskFragment;
    private CreateSecondTaskFrag createSecondTaskFrag;
    private ComunicateFragments comunicateFragments;


    public CreateTaskActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_create_task);

        comunicateFragments = new ViewModelProvider(this).get(ComunicateFragments.class);

        createTaskFragment = new CreateTaskFragment();
        createSecondTaskFrag = new CreateSecondTaskFrag();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, createTaskFragment).addToBackStack(null).commit();

    }








}
