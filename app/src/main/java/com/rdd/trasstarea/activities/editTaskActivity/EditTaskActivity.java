package com.rdd.trasstarea.activities.editTaskActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.fragments.ComunicateFragments;
import com.rdd.trasstarea.fragments.CreateSecondTaskFrag;
import com.rdd.trasstarea.fragments.CreateTaskFragment;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

public class EditTaskActivity extends AppCompatActivity implements CreateSecondTaskFrag.mandarTarea{

    public  String TITULO = "titulo";
    public   String DATE2 = "fecha2";
    public   String STATE = "estado";
    public  boolean PRIORITAIO = true;

    public String DESCRIPTION = "";


    private CreateTaskFragment createTaskFragment;
    private CreateSecondTaskFrag createSecondTaskFrag;
    private ComunicateFragments comunicateFragments;
    private Task editTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_create_task);

        comunicateFragments = new ViewModelProvider(this).get(ComunicateFragments.class);
        createTaskFragment = new CreateTaskFragment();
        createSecondTaskFrag = new CreateSecondTaskFrag();


        editTask = (Task) getIntent().getSerializableExtra("tareaEditar");
        comunicateFragments.setTask(editTask);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, createTaskFragment).addToBackStack(null).commit();

    }

    @Override
    public void mandarTask() {
        TITULO = comunicateFragments.getTitulo().getValue();
        DATE2 = comunicateFragments.getDate2().getValue();
        STATE = comunicateFragments.getState().getValue();
        PRIORITAIO = Boolean.TRUE.equals(comunicateFragments.getPrioritario().getValue());
        DESCRIPTION = comunicateFragments.getDescription().getValue();

        Task task = new Task(TITULO, PRIORITAIO, ListController.convertirFecha(DATE2), Task.States.valueOf(STATE), DESCRIPTION);

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
