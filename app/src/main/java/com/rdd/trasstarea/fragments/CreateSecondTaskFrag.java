package com.rdd.trasstarea.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.rdd.trasstarea.R;
import com.rdd.trasstarea.model.Task;
import com.rdd.trasstarea.database.tarjetasd.SdManager;

public class CreateSecondTaskFrag extends Fragment {

    // Variables de instancia
    private String date1;  // Variable para almacenar la fecha 1
    private String video = "";
    private String audio = "";
    private String imagen = "";
    private String documento = "";



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
            comunicateFragments.getUrl_audio().observe(getViewLifecycleOwner(), s -> {
                audio = s;
                System.out.println(audio);
            });
            comunicateFragments.getUrl_doc().observe(getViewLifecycleOwner(), s -> {
                documento = s;
                System.out.println(documento);
            });
            comunicateFragments.getUrl_video().observe(getViewLifecycleOwner(), s -> {
                video = s;
                System.out.println(video);
            });
            comunicateFragments.getUrl_img().observe(getViewLifecycleOwner(), s -> {
                imagen = s;
                System.out.println(imagen);
            });

        }
    }

    // Método para enviar datos al ViewModel compartido
    private void sendData() {
        comunicateFragments.setDescription(descripcion.getText().toString());
        comunicateFragments.setUrl_doc(documento);
        comunicateFragments.getUrl_doc().observe(getViewLifecycleOwner(),s -> {
            //TODO hacer esto
        });
        comunicateFragments.getUrl_img().observe(getViewLifecycleOwner(),s -> {
            imagen = s;
        });
        comunicateFragments.getUrl_audio().observe(getViewLifecycleOwner(),s -> {
            audio = s;
        });
        comunicateFragments.setUrl_audio(audio);
        comunicateFragments.setUrl_video(video);
        comunicateFragments.setUrl_img(imagen);
        System.out.println(documento);
        System.out.println(audio);
        System.out.println(video);
        System.out.println(imagen);
    }


    // Lanzadores de resultados de actividad
    private final ActivityResultLauncher<String> pickDocumentLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::onDocumentSelected);

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::onImageSelected);

    private final ActivityResultLauncher<String> pickAudioLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::onAudioSelected);

    private final ActivityResultLauncher<String> pickVideoLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::onVideoSelected);


    // Métodos para manejar la selección de documentos, imágenes, audio y video
    public void onSelectDocumentClick(View view) {
        pickDocumentLauncher.launch("application/*");
    }


    public void onSelectImageClick(View view) {
        pickImageLauncher.launch("image/*");
    }

    public void onSelectAudioClick(View view) {
        pickAudioLauncher.launch("audio/*");
    }

    public void onSelectVideoClick(View view) {
        pickVideoLauncher.launch("video/*");
    }


    // Métodos para manejar la selección de documentos, imágenes, audio y video


    private void onDocumentSelected(Uri documentUri) {
        if (documentUri != null) {
            documento = SdManager.getPathFromUri(getContext(), documentUri);
        }
    }


    private void onImageSelected(Uri imageUri) {
        if (imageUri != null) {
            imagen = SdManager.getPathFromUri(getContext(), imageUri);
        }
    }


    private void onAudioSelected(Uri audioUri) {
        if (audioUri != null) {
            audio = SdManager.getPathFromUri(getContext(), audioUri);
        }
    }


    private void onVideoSelected(Uri videoUri) {
        if (videoUri != null) {
            video = SdManager.getPathFromUri(getContext(), videoUri);
        }
    }


}
