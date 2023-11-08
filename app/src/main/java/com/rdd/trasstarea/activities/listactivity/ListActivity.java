package com.rdd.trasstarea.activities.listactivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.listactivity.recycler.CustomAdapter;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private final ListController listController = new ListController();
    private List<Task> listTareas;
    private View mensaje;

    private CustomAdapter customAdapter;
    private RecyclerView recyclerView;

    private boolean favorite = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_tareas);
        mensaje = findViewById(R.id.mensaje);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Configurar
        actualizarLista();
        lanzarMensajeNoTareas();
        configureRecyclerView();
    }

    private void actualizarLista(){
        listTareas = listController.getListTask();
    }

    private void lanzarMensajeNoTareas() {
        if (listTareas.isEmpty()) {
            mensaje.setVisibility(View.VISIBLE);
        } else {
            mensaje.setVisibility(View.INVISIBLE);
        }
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


    private void cargarAnimacion() {
        int resId = R.anim.fall_down; // Ruta al recurso de animación
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);
    }


    private void initialList(){
        customAdapter = new CustomAdapter(listTareas);
        recyclerView.scheduleLayoutAnimation(); // Lanzar la animación cuando se asigna el adaptador
        recyclerView.setAdapter(customAdapter);
    }

    private void filtrarFavoritos(){
        // Filtra la lista de tareas utilizando el método filtarLista() de listController y crea un nuevo adaptador con los resultados
        customAdapter = new CustomAdapter(listController.filtarLista());
        // Asigna el nuevo adaptador al RecyclerView para mostrar las tareas filtradas
        recyclerView.setAdapter(customAdapter);
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
            System.out.println("Hola"); // Imprime un mensaje en la consola cuando se selecciona "Añadir Tarea"
            return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_favorite) {
            filtrarRecycler(item); // Llama al método filtrarRecycler() cuando se selecciona "Favoritos"
            return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_acercade) {
            // Puedes agregar aquí las acciones que deseas realizar cuando se selecciona "Acerca de"
            return true; // Indica que el evento ha sido manejado
        }
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(this, "Adios", Toast.LENGTH_SHORT).show(); // Muestra un mensaje de despedida cuando se selecciona "Configuración"
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



}
