package com.rdd.trasstarea.activities.listactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.CreateTaskActivity;
import com.rdd.trasstarea.activities.editTaskActivity.EditTaskActivity;
import com.rdd.trasstarea.activities.listactivity.dialogs.AboutDialog;
import com.rdd.trasstarea.activities.listactivity.dialogs.ExitDialog;
import com.rdd.trasstarea.activities.listactivity.recycler.CustomAdapter;
import com.rdd.trasstarea.activities.settings.SettingsActivity;
import com.rdd.trasstarea.comunicator.IComunicator;
import com.rdd.trasstarea.database.AppDataBase;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;




public class ListActivity extends AppCompatActivity {

    /**
     * -------------------------------Variables--------------------------------------
     */

    AppDataBase appDataBase;

    // Constantes para las claves de Bundle
    public static final String TASK_LIST = "taskList";
    public static final String TAREA_NUEVA_ES_NULA = "Tarea nueva es nula";
    public static final String EL_INTENT_ES_NULO = "El intent es nulo";

    // Variables configuración.
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_THEME = "theme";
    private SharedPreferences preferences;

    // Controlador de la lista de tareas
    private final ListController listController = new ListController();

    // Lista de tareas y otros miembros de la actividad

    private List<Task> listTareas = new ArrayList<>();
    private View mensaje;
    private MenuItem item;
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    private int positionTask;

    // Tarea a crear/editar y bandera de favoritos
    private Task createTask;
    private boolean favorite = false;

    //Bandera para recreado
    private boolean isRecreado = false;

    /**
     * -------------------------------Interfaz--------------------------------------
     */

    // Interfaz de comunicación con el adaptador
    private final IComunicator comunicator = new IComunicator() {
        @Override
        public void deleteList(Task task) {
            // Eliminar tarea y notificar al adaptador
            listTareas.remove(task);
            customAdapter.updateData(listTareas);
            customAdapter.notifyItemRemoved(task.getId());
            if (favorite) {
                filtrarFavoritos();
            }
            lanzarMensajeNoTareas();
        }

        @Override
        public void createTask() {
            // Añadir nueva tarea y notificar al adaptador
            listTareas.add(createTask);
            insertarUsuario(createTask);
            customAdapter.updateData(listTareas);
            customAdapter.notifyItemInserted(customAdapter.getItemCount());
            if (favorite) {
                filtrarFavoritos();
            }
            lanzarMensajeNoTareas();
        }

        @Override
        public void editTask(Task task, int position) {
            // Iniciar la edición de la tarea
            positionTask = position;
            initEditTask(task);
            lanzarMensajeNoTareas();
        }
    };

    /**
     * -----------------------------ESTADOS-------------------------------------------------
     */

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDataBase = AppDataBase.getInstance(getApplicationContext());
        setContentView(R.layout.listado_tareas);
        mensaje = findViewById(R.id.mensaje);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cargar las settings
        setSettings();

        // Configurar la actividad
        if (savedInstanceState != null) {
            configureSaveStance(savedInstanceState);
        } else {
            // Llamada a obtenerLista() con subscribe
            obtenerLista().subscribe((tasks) -> {
                // Configurar el RecyclerView y otras acciones dependientes de la lista
                listTareas.addAll(tasks);
                configureRecyclerView();
                lanzarMensajeNoTareas();
            }, throwable -> {
                Log.e("error", "No se ha podido recuperar");
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (appDataBase != null){
            appDataBase.close();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        setSettings();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Guardar el estado de la lista y la configuración de favoritos
        outState.putBoolean("favorite", favorite);
        outState.putSerializable(TASK_LIST, (Serializable) listTareas);
        // Guardar el recurso del icono
        int iconResource = favorite ? R.drawable.baseline_stars_24 : R.drawable.baseline_stars_24_black;
        outState.putInt("iconResource", iconResource);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restaurar el estado de favoritos al recrear la actividad
        favorite = savedInstanceState.getBoolean("favorite");
    }

    /**
     * Este método se comunica con el viewholder para borrar la tarea mediante una interfaz.
     */
    private void configureSaveStance(Bundle savedInstanceState) {
        // Restaurar el estado de la variable 'favorite'
        favorite = savedInstanceState.getBoolean("favorite");
        // Restaurar la lista de tareas y configurar el RecyclerView
        listTareas = (List<Task>) savedInstanceState.getSerializable(TASK_LIST);
        configureRecyclerView();
        // Filtrar por favoritos si es necesario
        if (favorite) {
            filtrarFavoritos();
        }
    }

    /**
     * ------------------------------------------------------------------------------
     */
    private void configureRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        // Configurar el layout manager, el adaptador y otros aspectos.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // Configurar el adaptador y la animación
        setearLista();
    }

    private void setearLista() {
        customAdapter = new CustomAdapter(listTareas, comunicator);
        recyclerView.setAdapter(customAdapter);
    }

    private void lanzarMensajeNoTareas() {
        // Mostrar o ocultar mensaje según si la lista de tareas está vacía
        if (customAdapter.getTasksDataSet().isEmpty()) {
            mensaje.setVisibility(View.VISIBLE);
        } else {
            mensaje.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú de la barra de herramientas usando el archivo de recursos toolbarmenu.xml
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        item = menu.findItem(R.id.action_favorite);
        // Configurar el ícono de favoritos según el estado actual
        int iconResource = favorite ? R.drawable.baseline_stars_24 : R.drawable.baseline_stars_24_black;
        item.setIcon(iconResource);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Manejar las opciones del menú seleccionadas
        if (item.getItemId() == R.id.action_addTarea) {
            // Iniciar la creación de una nueva tarea
            initCreateTask();
            return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_favorite) {
            // Filtrar la lista por favoritos al seleccionar/deseleccionar la opción
            filtrarRecycler(item);
            return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_acercade) {
            // Mostrar el cuadro de diálogo "Acerca de"
            new AboutDialog(this, "Rubén Díaz Dugo" + "\n" + "IES TRASSIERRA 2023");
            return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_settings){
            initSettingConfigure();
        }
        if (item.getItemId() == R.id.action_exit) {
            // Mostrar el cuadro de diálogo de salida
            new ExitDialog(this);
            return true; // Indica que el evento ha sido manejado
        }
        return super.onOptionsItemSelected(item); // Delega el manejo del evento al comportamiento predeterminado
    }

    private void filtrarRecycler(MenuItem item) {
        // Alternar el estado de filtrado y cambiar el ícono
        checkFiltrado();
        int iconResource = favorite ? R.drawable.baseline_stars_24 : R.drawable.baseline_stars_24_black;
        item.setIcon(iconResource);
    }

    private void checkFiltrado() {
        // Cambiar el estado de la variable de filtrado
        favorite = !favorite;
        // Realizar acciones según el estado actual de la variable favorite
        if (!favorite) {
            setearLista();
            lanzarMensajeNoTareas();
        } else {
            filtrarFavoritos();
        }
    }

    private void filtrarFavoritos() {
        // Filtrar la lista por favoritos y actualizar el adaptador
        List<Task> filteredList = listTareas.stream().filter(Task::isPrioritaria).collect(Collectors.toList());
        customAdapter.updateData(filteredList);
        customAdapter.notifyDataSetChanged();
        lanzarMensajeNoTareas();
    }

    // Lanzadores de actividades para crear y editar tareas
    ActivityResultLauncher<Intent> createTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Manejar el resultado de la creación de la tarea
                if (result.getResultCode() == RESULT_OK) {
                    handleTaskCreationResult(result);
                }
            });

    ActivityResultLauncher<Intent> editTaskLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Manejar el resultado de la edición de la tarea
                if (result.getResultCode() == RESULT_OK) {
                    handleTaskEditResult(result);
                }
            });

    private void handleTaskCreationResult(ActivityResult result) {
        // Obtener la tarea creada del Intent devuelto
        Intent intentDevuelto = result.getData();
        if (intentDevuelto != null) {
            Task task = (Task) Objects.requireNonNull(intentDevuelto.getExtras()).get(EditTaskActivity.TAREA_NUEVA);
            if (task != null) {
                // Notificar al adaptador sobre la nueva tarea creada
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

    private void handleTaskEditResult(@NonNull ActivityResult result) {
        // Obtener la tarea editada del Intent devuelto
        Intent intentDevuelto = result.getData();
        if (intentDevuelto != null) {
            Task task = (Task) Objects.requireNonNull(intentDevuelto.getExtras()).get(EditTaskActivity.TAREA_NUEVA);
            if (task != null) {
                // Actualizar la tarea editada en la lista y notificar al adaptador
                int posicionTarea = findTaskPositionById(task.getId());
                createTask = task;
                listTareas.set(posicionTarea, createTask);
                customAdapter.updateData(listTareas);
                if (favorite) {
                    filtrarFavoritos();
                }
                customAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ListActivity.this, TAREA_NUEVA_ES_NULA, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Manejar el caso donde el Intent devuelto es nulo
            Toast.makeText(ListActivity.this, EL_INTENT_ES_NULO, Toast.LENGTH_SHORT).show();
        }
    }

    // Método para encontrar la posición de la tarea por ID en la lista actual
    private int findTaskPositionById(int taskId) {
        for (int i = 0; i < listTareas.size(); i++) {
            if (listTareas.get(i).getId() == taskId) {
                return i;
            }
        }
        // Devolver -1 si no se encuentra la tarea en la lista actual
        return -1;
    }

    private void initCreateTask() {
        // Iniciar la actividad para crear una nueva tarea
        Intent intent = new Intent(this, CreateTaskActivity.class);
        if (createTaskLauncher != null) {
            createTaskLauncher.launch(intent);
        }
    }

    private void initEditTask(Task task) {
        // Iniciar la actividad para editar una tarea existente
        Intent intent = new Intent(this, EditTaskActivity.class);
        if (editTaskLauncher != null) {
            intent.putExtra(EditTaskActivity.TAREA_EDITAR, task);
            editTaskLauncher.launch(intent);
        }
    }

    //------------------------------DataBase -------------------------------------------

    /**
     * En este ejemplo, Schedulers.io()
     * y AndroidSchedulers.mainThread() se utilizan para
     * realizar la operación de inserción en un hilo de fondo
     * y para manejar el resultado en el hilo principal, respectivamente.
     * @param task
     */
    @SuppressLint("CheckResult")
    private void insertarUsuario(Task task) {
        appDataBase.daoTask().insertTask(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> System.out.println("bien"),
                        throwable -> System.out.println("ml"));
    }


    private Single<List<Task>> obtenerLista() {
        return obtenerSingle()
                .doOnSuccess(tasks -> {
                    // Actualizar la listaTareas en caso de éxito
                    listTareas.clear();
                    listTareas.addAll(tasks);
                });
    }

    private Single<List<Task>> obtenerSingle() {
        return appDataBase.daoTask().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    //------------------------------Configuracion-------------------------------------------

    private void initSettingConfigure(){
        Intent intent = new Intent(this, SettingsActivity.class);
        isRecreado = true;
        startActivity(intent);
    }

    private void setSettings(){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Tema oscuro.
        boolean temaClaro = preferences.getBoolean("claro", true);
        AppCompatDelegate.setDefaultNightMode(temaClaro ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);


        Configuration configuration = getResources().getConfiguration();

        //Tamaño fuente
        String fuente = preferences.getString("fuente","b");
        if (fuente.equals("a")) configuration.fontScale = 0.8f;
        if (fuente.equals("b")) configuration.fontScale = 1.0f;
        if (fuente.equals("c")) configuration.fontScale = 1.3f;

        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());


        //TODO mejorar la parte del asc, ya que no se guarda bien.

        if (!listTareas.isEmpty()){
            String criterio = preferences.getString("criterio","b");
            if (criterio.equals("a")) {
                ListController.orderByAlfabetico(listTareas);
            }
            if (criterio.equals("b")) {
                ListController.orderByDate(listTareas);
            }
            if (criterio.equals("c")) {
                ListController.orderByDaysLeft(listTareas);
            }
            if (criterio.equals("d")) {
                ListController.orderByProgress(listTareas);
            }


            //Ordenar lista by asc.
            boolean asc = preferences.getBoolean("asc",true);
            listTareas = ListController.orderByAsc(listTareas, asc);

        }
        //Ordenar lista


        if (isRecreado){
            recreate();
            isRecreado = !isRecreado;
        }


    }

}

