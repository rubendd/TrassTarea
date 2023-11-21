package com.rdd.trasstarea.activities.createtaskactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.editTaskActivity.EditTaskActivity;
import com.rdd.trasstarea.fragments.ComunicateFragments;
import com.rdd.trasstarea.fragments.CreateSecondTaskFrag;
import com.rdd.trasstarea.fragments.CreateTaskFragment;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity implements CreateSecondTaskFrag.mandarTarea{


    public  String TITULO = "titulo";
    public   String DATE2 = "fecha2";
    public String DATE1;
    public   String STATE = "estado";
    public  boolean PRIORITAIO = true;

    public String DESCRIPTION = "";


    private ComunicateFragments comunicateFragments;


    public CreateTaskActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_create_task);

        comunicateFragments = new ViewModelProvider(this).get(ComunicateFragments.class);
        CreateTaskFragment createTaskFragment = new CreateTaskFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, createTaskFragment).addToBackStack(null).commit();

    }


    @Override
    public void mandarTask() {
        TITULO = comunicateFragments.getTitulo().getValue();
        DATE1 = comunicateFragments.getDate1().getValue();
        DATE2 = comunicateFragments.getDate2().getValue();
        STATE = comunicateFragments.getState().getValue();
        PRIORITAIO = Boolean.TRUE.equals(comunicateFragments.getPrioritario().getValue());
        DESCRIPTION = comunicateFragments.getDescription().getValue();

        Task task = new Task(TITULO, PRIORITAIO, ListController.convertirFecha(DATE2), Task.States.valueOf(STATE), DESCRIPTION, ListController.convertirFecha(String.valueOf(DATE1)));

        Intent intent = new Intent();
        intent.putExtra(EditTaskActivity.TAREA_NUEVA, task);
        setResult(RESULT_OK, intent);

        finish();
    }
}
