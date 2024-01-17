package com.rdd.trasstarea.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.rdd.trasstarea.R;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;
public class CreateSecondTaskFrag extends Fragment {

    // Variables de instancia
    private String date1;  // Variable para almacenar la fecha 1
    private Task task = new Task();  // Instancia de la clase Task para almacenar datos de la tarea
    private EditText descripcion;  // Campo de texto para la descripción de la tarea
    private LottieAnimationView btnCreateTask;  // Vista de animación para el botón de crear tarea

    private ComunicateFragments comunicateFragments;  // ViewModel compartido entre fragmentos

    // Interfaz para la comunicación con la actividad contenedora
    public interface mandarTarea {
        void mandarTask();
    }

    private mandarTarea comunicador;  // Objeto que implementa la interfaz de comunicación

    // Método llamado cuando el fragmento se adjunta a la actividad
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Verifica si la actividad implementa la interfaz de comunicación
        if (context instanceof mandarTarea) {
            comunicador = (mandarTarea) context;
        }
    }

    // Método llamado para guardar el estado del fragmento antes de ser destruido y recreado
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tarea", task);  // Guarda la tarea en el Bundle
    }

    // Método llamado al crear el fragmento
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtiene una referencia del ViewModel compartido
        comunicateFragments = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);
    }

    // Método para obtener datos de la tarea y actualizar la interfaz de usuario
    private void putDataTask() {
        if (comunicateFragments.getTaskLiveData().isInitialized()) {
            comunicateFragments.getTaskLiveData().observe(getViewLifecycleOwner(), task -> descripcion.setText(task.getDescription()));
        }
    }

    // Método para recibir datos de la tarea
    private void recibirTask() {
        if (comunicateFragments.getTaskLiveData().isInitialized()) {
            comunicateFragments.getTaskLiveData().observe(getViewLifecycleOwner(), task -> this.task = task);
        }
    }

    // Método llamado para crear la vista del fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmento2 = inflater.inflate(R.layout.create_task2, container, false);


        // Inicializa los componentes y maneja los datos
        initComponents(fragmento2);
        if (savedInstanceState != null) {
            recibirTask();
        } else {
            putDataTask();
            recuperarDatos();
        }
        return fragmento2;
    }

    // Método para inicializar los componentes de la interfaz de usuario
    private void initComponents(View fragmento2) {
        descripcion = fragmento2.findViewById(R.id.editTextDescription);

        //Botones
        Button btnSalir = fragmento2.findViewById(R.id.back);
        Button documento = fragmento2.findViewById(R.id.documento);
        Button video = fragmento2.findViewById(R.id.video);
        Button audio = fragmento2.findViewById(R.id.audio);
        Button imagen = fragmento2.findViewById(R.id.imagen);
        btnCreateTask = fragmento2.findViewById(R.id.create);

        //Listener
        btnSalir.setOnClickListener(this::backFragment);
        documento.setOnClickListener(this::onSelectDocumentClick);
        audio.setOnClickListener(this::onSelectAudioClick);
        imagen.setOnClickListener(this::onSelectImageClick);
        video.setOnClickListener(this::onSelectVideoClick);

        btnCreateTask.setOnClickListener(v -> {
            sendData();
            comunicador.mandarTask();
        });
    }

    // Método llamado al hacer clic en el botón para volver al fragmento anterior
    private void backFragment(View view) {
        // Intenta encontrar la instancia existente del CreateTaskFragment por su tag
        CreateTaskFragment existingFragment = (CreateTaskFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("CreateTaskFragment");

        // Si no se encuentra, crea una nueva instancia
        if (existingFragment == null) {
            existingFragment = new CreateTaskFragment();
        }

        // Reemplaza el fragmento actual con el fragmento anterior y lo agrega a la pila de retroceso
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, existingFragment, "CreateTaskFragment").commit();
    }

    // Método para recuperar datos del ViewModel compartido
    private void recuperarDatos() {
        if (comunicateFragments.getTitulo().isInitialized()) {
            comunicateFragments.getTitulo().observe(getViewLifecycleOwner(), task::setTitulo);
            comunicateFragments.getDate1().observe(getViewLifecycleOwner(), da -> {
                date1 = da;
                System.out.println(date1);
            });
            comunicateFragments.getDate2().observe(getViewLifecycleOwner(), da -> task.setDateEnd(da));
            comunicateFragments.getPrioritario().observe(getViewLifecycleOwner(), task::setPrioritaria);
            comunicateFragments.getState().observe(getViewLifecycleOwner(), da -> task.setStatesNumber(Task.States.valueOf(String.valueOf(da))));
        }
    }

    // Método para enviar datos al ViewModel compartido
    private void sendData() {
        comunicateFragments.setDescription(descripcion.getText().toString());
    }


    public void onSelectDocumentClick(View view) {
        openFileChooser("application/*");
    }

    public void onSelectImageClick(View view) {
        openFileChooser("image/*");
    }

    public void onSelectAudioClick(View view) {
        openFileChooser("audio/*");
    }

    public void onSelectVideoClick(View view) {
        openFileChooser("video/*");
    }

    private void openFileChooser(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
    }
}
