package com.rdd.trasstarea.activities.listactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.CreateTaskActivity;
import com.rdd.trasstarea.activities.editTaskActivity.EditTaskActivity;
import com.rdd.trasstarea.activities.listactivity.dialogs.AboutDialog;
import com.rdd.trasstarea.activities.listactivity.dialogs.ExitDialog;
import com.rdd.trasstarea.activities.listactivity.recycler.CustomAdapter;
import com.rdd.trasstarea.comunicator.IComunicator;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity{

    public static final String TASK_LIST = "taskList";
    public static final String TAREA_NUEVA_ES_NULA = "Tarea nueva es nula";
    public static final String EL_INTENT_ES_NULO = "El intent es nulo";
    private final ListController listController = new ListController();
    private List<Task> listTareas = listController.getListTask();
    private View mensaje;
    private MenuItem item;
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    private int positionTask;

    private final IComunicator comunicator = new IComunicator() {
        @Override
        public void deleteList(int position) {
            listTareas.remove(position);
            customAdapter.updateData(listTareas);
            customAdapter.notifyItemRemoved(position);
            lanzarMensajeNoTareas();
        }

        @Override
        public void createTask() {
            listTareas.add(createTask);
            customAdapter.updateData(listTareas);
            customAdapter.notifyItemInserted(customAdapter.getItemCount());
        }

        @Override
        public void editTask(Task task, int position) {
            positionTask = position;
            initEditTask(task);
        }
    };

    private Task createTask;
    private boolean favorite = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_tareas);
        mensaje = findViewById(R.id.mensaje);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Configure
        lanzarMensajeNoTareas();
        if (savedInstanceState != null){
            configureSaveStance(savedInstanceState,toolbar);
        } else {
            configureRecyclerView();
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar si la lista está filtrada por favoritos
        outState.putBoolean("favorite", favorite);
        outState.putSerializable(TASK_LIST, (Serializable) listTareas);
        // Guardar el resource del icono
        int iconResource = favorite ? R.drawable.baseline_stars_24 : R.drawable.baseline_stars_24_black;
        outState.putInt("iconResource", iconResource);
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        favorite = savedInstanceState.getBoolean("favorite");
    }




    /**
     * Este método se comunica con el viewholder para borrar la tarea mediante una interfaz.
     */


    private void configureSaveStance(Bundle savedInstanceState, Toolbar toolbar){
            // Restaurar el estado de la variable 'favorite'
            favorite = savedInstanceState.getBoolean("favorite");
            listTareas = (List<Task>) savedInstanceState.getSerializable(TASK_LIST);
            configureRecyclerView();
            if (favorite){
                filtrarFavoritos();
            }
    }


    private void configureRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);

        // Configurar el layout manager, el adaptador y otros aspectos.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //TODO Cargar animacion
        // cargarAnimacion();
        setearLista();

    }



    private void setearLista(){
        customAdapter = new CustomAdapter(listTareas, comunicator);
        recyclerView.setAdapter(customAdapter);
    }



    private void lanzarMensajeNoTareas() {
        if (listTareas.isEmpty()) {
            mensaje.setVisibility(View.VISIBLE);
        } else {
            mensaje.setVisibility(View.INVISIBLE);
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú de la barra de herramientas usando el archivo de recursos toolbarmenu.xml
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        item = menu.findItem(R.id.action_favorite);
        int iconResource = favorite ? R.drawable.baseline_stars_24 : R.drawable.baseline_stars_24_black;
        item.setIcon(iconResource);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Verifica qué opción del menú ha sido seleccionada
        if (item.getItemId() == R.id.action_addTarea) {
            initCreateTask();
            return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_favorite) {
            filtrarRecycler(item); // Llama al método filtrarRecycler() cuando se selecciona "Favoritos"
            return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_acercade) {
                new AboutDialog(this, "Rubén Díaz Dugo"+"\n"+"IES TRASSIERRA 2023");
                return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_settings) {
            new ExitDialog(this);
            return true; // Indica que el evento ha sido manejado
        }
        return super.onOptionsItemSelected(item); // Delega el manejo del evento al comportamiento predeterminado
    }


    private void filtrarRecycler(MenuItem item){
        checkFiltrado();
        // Cambiar el icono del botón según el estado actual de la variable de bandera
        int iconResource = favorite ? R.drawable.baseline_stars_24 : R.drawable.baseline_stars_24_black;
        item.setIcon(iconResource);

    }


    private void checkFiltrado(){
        // Cambiar el estado de la variable de bandera
        favorite = !favorite;

        // Realizar acciones según el estado actual de la variable favorite
        if (!favorite) {
            setearLista();
        } else {
            filtrarFavoritos();
        }
    }

    private void filtrarFavoritos(){
        List<Task> filteredList = listController.filtarLista();
        customAdapter.updateData(filteredList);
        customAdapter.notifyDataSetChanged();

    }


    ActivityResultLauncher<Intent> createTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intentDevuelto = result.getData();
                if (intentDevuelto != null) {
                    Task task = (Task) Objects.requireNonNull(intentDevuelto.getExtras()).get(EditTaskActivity.TAREA_NUEVA);
                    if (task != null) {
                        createTask = task;
                        comunicator.createTask();
                    } else {
                        Toast.makeText(ListActivity.this, TAREA_NUEVA_ES_NULA, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar el caso donde el Intent devuelto es nulo
                    Toast.makeText(ListActivity.this, EL_INTENT_ES_NULO, Toast.LENGTH_SHORT).show();
                }
            }
        }
    });


    ActivityResultLauncher<Intent> editTaskLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intentDevuelto = result.getData();
                if (intentDevuelto != null) {
                    Task task = (Task) Objects.requireNonNull(intentDevuelto.getExtras()).get(EditTaskActivity.TAREA_NUEVA);
                    if (task != null) {
                        createTask = task;
                        listTareas.set(positionTask, createTask);
                        customAdapter.updateData(listTareas);
                        customAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ListActivity.this, TAREA_NUEVA_ES_NULA, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar el caso donde el Intent devuelto es nulo
                    Toast.makeText(ListActivity.this, EL_INTENT_ES_NULO, Toast.LENGTH_SHORT).show();
                }
            }
        }
    });


    private void initCreateTask() {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        if (createTaskLauncher != null) {
            createTaskLauncher.launch(intent);
        }
    }

    private void initEditTask(Task task){
        Intent intent = new Intent(this, EditTaskActivity.class);
        if (editTaskLauncher != null){
            intent.putExtra(EditTaskActivity.TAREA_EDITAR, task);
            editTaskLauncher.launch(intent);
        }
    }


}
