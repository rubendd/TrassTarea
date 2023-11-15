package com.rdd.trasstarea.activities.listactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.CreateTaskActivity;
import com.rdd.trasstarea.activities.createtaskactivity.fragments.CreateSecondTaskFrag;
import com.rdd.trasstarea.activities.listactivity.dialogs.AboutDialog;
import com.rdd.trasstarea.activities.listactivity.dialogs.ExitDialog;
import com.rdd.trasstarea.activities.listactivity.recycler.CustomAdapter;
import com.rdd.trasstarea.comunicator.IComunicator;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

import java.util.List;

public class ListActivity extends AppCompatActivity implements IComunicator, CreateSecondTaskFrag.mandarTarea {


    private final ListController listController = new ListController();
    private final List<Task> listTareas = listController.getListTask();
    private View mensaje;
    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;
    private Task createTask;
    private boolean favorite = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_tareas);
        mensaje = findViewById(R.id.mensaje);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Configurar
        lanzarMensajeNoTareas();
        configureRecyclerView();
    }

    /**
     * Este método se comunica con el viewholder para borrar la tarea mediante una interfaz.
     */
    @Override
    public void deleteList(int position) {
        listTareas.remove(position); // Aunque hayamos borrado la tarea de la lista del viewholder, tenemos que borrarla de esta lista ya que no se guardaría los cambios
        customAdapter.notifyItemRemoved(position); //Notificamos que ha habido un cambio.
    }



    private void configureRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);

        // Configurar el layout manager, el adaptador y otros aspectos.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //TODO Cargar animacion
        // cargarAnimacion();

        initialList();

    }



    private void initialList(){
        customAdapter = new CustomAdapter(listTareas, this);
        recyclerView.setAdapter(customAdapter);
    }



    private void filtrarFavoritos(){
        // Filtra la lista de tareas utilizando el método filtarLista() de listController y crea un nuevo adaptador con los resultados
        customAdapter = new CustomAdapter(listController.filtarLista(), this);
        // Asigna el nuevo adaptador al RecyclerView para mostrar las tareas filtradas
        recyclerView.setAdapter(customAdapter);
    }


    private void lanzarMensajeNoTareas() {
        if (listTareas.isEmpty()) {
            mensaje.setVisibility(View.VISIBLE);
        } else {
            mensaje.setVisibility(View.INVISIBLE);
        }
    }



    private void cargarAnimacion() {
        int resId = R.anim.fall_down; // Ruta al recurso de animación
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú de la barra de herramientas usando el archivo de recursos toolbarmenu.xml
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbarmenu, menu);
        return true; // Indica que el menú ha sido creado exitosamente
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


    private void filtrarRecycler(@NonNull MenuItem item){
        // Cambiar el estado de la variable de bandera
        favorite = !favorite;

        // Realizar acciones según el estado actual de la variable favorite
        if (!favorite) {
            initialList();
        } else {
            filtrarFavoritos();
        }

        // Cambiar el icono del botón según el estado actual de la variable de bandera
        int iconResource = favorite ? R.drawable.baseline_stars_24 : R.drawable.baseline_stars_24_black;
        item.setIcon(iconResource);
    }

    ActivityResultLauncher<Intent> mlauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intentDevuelto = result.getData();
                if (intentDevuelto != null) {
                    Task task = intentDevuelto.getParcelableExtra("tareaNueva");
                    if (task != null) {
                        createTask = task;
                    } else {
                        Toast.makeText(ListActivity.this, "Tarea nueva es nula", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar el caso donde el Intent devuelto es nulo
                    Toast.makeText(ListActivity.this, "El intent es nulo", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });




    private void initCreateTask() {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        if (mlauncher != null) {
            mlauncher.launch(intent);
        }
    }


    @Override
    public void mandarTask() {
        listTareas.add(createTask);
        int position = listTareas.size() - 1; // Obtener la posición de la tarea recién agregada
        customAdapter.notifyItemInserted(position); // Not
    }
}
