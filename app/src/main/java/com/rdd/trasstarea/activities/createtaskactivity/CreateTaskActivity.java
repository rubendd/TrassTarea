package com.rdd.trasstarea.activities.createtaskactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.fragments.CreateSecondTaskFrag;
import com.rdd.trasstarea.activities.createtaskactivity.fragments.CreateTaskFragment;
import com.rdd.trasstarea.activities.listactivity.ListActivity;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

public class CreateTaskActivity extends AppCompatActivity implements CreateSecondTaskFrag.mandarTarea{


    public  String TITULO = "titulo";
    public   String DATE2 = "fecha2";
    public   String STATE = "estado";
    public  boolean PRIORITAIO = true;


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


    @Override
    public void mandarTask() {
        TITULO = comunicateFragments.getTitulo().getValue();
        DATE2 = comunicateFragments.getDate2().getValue();
        STATE = comunicateFragments.getState().getValue();
        PRIORITAIO = comunicateFragments.getPrioritario().getValue();

        Task task = new Task(TITULO, PRIORITAIO, ListController.convertirFecha(DATE2), Task.States.valueOf(STATE), "preuba");

        if (task != null) {
            Intent intent = new Intent();
            intent.putExtra("tareaNueva", task);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED); // Indica que la operación fue cancelada debido a un error
        }

        finish();
    }
}
