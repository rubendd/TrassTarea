package com.rdd.trasstarea.activities.editTaskActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.fragments.ComunicateFragments;
import com.rdd.trasstarea.fragments.CreateSecondTaskFrag;
import com.rdd.trasstarea.fragments.CreateTaskFragment;
import com.rdd.trasstarea.model.Task;

public class EditTaskActivity extends AppCompatActivity implements CreateSecondTaskFrag.mandarTarea {

    // Definición de constantes y variables de clase
    public static final String TAREA_NUEVA = "tareaNueva";
    public static final String TAREA_EDITAR = "tareaEditar";
    public String DESCRIPTION;
    public String URL_DOC = "";
    public String URL_IMG = "";
    public String URL_AUDIO = "";
    public String URL_VIDEO = "";
    // Variables de instancia
    private ComunicateFragments comunicateFragments;
    private Task editTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_create_task);

        // Inicialización de la comunicación entre fragmentos
        comunicateFragments = new ViewModelProvider(this).get(ComunicateFragments.class);
        CreateTaskFragment createTaskFragment = new CreateTaskFragment();

        // Verifica si la actividad se está recreando debido a un cambio de configuración
        if (savedInstanceState == null) {
            // Obtiene la tarea a editar de los extras de la intención
            editTask = (Task) getIntent().getSerializableExtra(TAREA_EDITAR);
        }

        // Establece la tarea en el ViewModel para que los fragmentos puedan acceder a ella
        comunicateFragments.setTask(editTask);

        // Agrega el fragmento de creación de tarea al contenedor si aún no está presente
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_task_create, createTaskFragment, "CreateTaskFragmentTag")
                    .addToBackStack(null)
                    .commit();
    }

    // Guarda el estado de la actividad, incluida la tarea editada
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("task", editTask);
    }

    // Restaura el estado de la actividad, incluida la tarea editada
    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (savedInstanceState != null) {
            editTask = (Task) savedInstanceState.getSerializable("task");
        }
    }

    // Implementación del método de la interfaz para enviar la tarea creada o editada
    @Override
    public void mandarTask() {
        // Obtiene los valores del formulario a través del ViewModel
        DESCRIPTION = comunicateFragments.getDescription().getValue();

        // Crea una nueva tarea
        editTask = comunicateFragments.getTaskLiveData().getValue();

        URL_AUDIO = comunicateFragments.getUrl_audio().getValue();
        URL_DOC = comunicateFragments.getUrl_doc().getValue();
        URL_IMG = comunicateFragments.getUrl_img().getValue();
        URL_VIDEO = comunicateFragments.getUrl_video().getValue();

        if (editTask != null) {
            editTask.setDescription(DESCRIPTION);
            editTask.setURL_aud(URL_AUDIO);
            editTask.setURL_doc(URL_DOC);
            editTask.setURL_img(URL_IMG);
            editTask.setURL_vid(URL_VIDEO);
        }

        // Prepara la intención para devolver la tarea al fragmento anterior
        Intent intent = new Intent();
        intent.putExtra(TAREA_NUEVA, editTask);
        setResult(RESULT_OK, intent);

        // Finaliza la actividad
        finish();
    }
}
