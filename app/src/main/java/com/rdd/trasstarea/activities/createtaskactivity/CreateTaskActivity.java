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
public class CreateTaskActivity extends AppCompatActivity implements CreateSecondTaskFrag.mandarTarea {

    // Variables de instancia para los datos de la tarea
    public String TITULO = "titulo";
    public String DATE2 = "fecha2";
    public String DATE1;
    public String STATE = "estado";
    public boolean PRIORITAIO = true;
    public String DESCRIPTION = "";

    // ViewModel para la comunicación entre fragmentos
    private ComunicateFragments comunicateFragments;

    // Constructor vacío
    public CreateTaskActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_create_task);

        // Inicialización del ViewModel para la comunicación entre fragmentos
        comunicateFragments = new ViewModelProvider(this).get(ComunicateFragments.class);

        // Creación del fragmento de creación de tarea
        CreateTaskFragment createTaskFragment = new CreateTaskFragment();

        // Reemplaza el contenido del contenedor con el fragmento y agrega la transacción a la pila de retroceso
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_task_create, createTaskFragment)
                .addToBackStack(null)
                .commit();
    }

    // Método de la interfaz para enviar la tarea creada al fragmento anterior
    @Override
    public void mandarTask() {
        // Obtiene los valores del formulario a través del ViewModel
        TITULO = comunicateFragments.getTitulo().getValue();
        DATE1 = comunicateFragments.getDate1().getValue();
        DATE2 = comunicateFragments.getDate2().getValue();
        STATE = comunicateFragments.getState().getValue();
        PRIORITAIO = Boolean.TRUE.equals(comunicateFragments.getPrioritario().getValue());
        DESCRIPTION = comunicateFragments.getDescription().getValue();

        // Crea una nueva tarea con los valores proporcionados
        Task task = new Task(TITULO, PRIORITAIO, DATE2,
                Task.States.valueOf(STATE), DESCRIPTION, DATE1);

        // Prepara la intención para devolver la tarea al fragmento anterior
        Intent intent = new Intent();
        intent.putExtra(EditTaskActivity.TAREA_NUEVA, task);
        setResult(RESULT_OK, intent);

        // Finaliza la actividad
        finish();
    }
}
