package com.rdd.trasstarea.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.rdd.trasstarea.R;
import com.rdd.trasstarea.database.tarjetasd.SdManager;
import com.rdd.trasstarea.model.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CreateSecondTaskFrag extends Fragment {

    // Variables de instancia
    private static final int SELECCIONAR_DOCUMENTO_REQUEST = 2;

    private String date1;  // Variable para almacenar la fecha 1
    private TextView ruta_imagen;
    private TextView ruta_video;
    private TextView ruta_audio;
    private TextView ruta_documento;
    private String video;
    private String audio;
    private String imagen;
    private String documento;



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
            comunicateFragments.getTaskLiveData().observe(getViewLifecycleOwner(), task -> ruta_video.setText(task.getURL_vid()));
            comunicateFragments.getTaskLiveData().observe(getViewLifecycleOwner(), task -> ruta_audio.setText(task.getURL_aud()));
            comunicateFragments.getTaskLiveData().observe(getViewLifecycleOwner(), task -> ruta_documento.setText(task.getURL_doc()));
            comunicateFragments.getTaskLiveData().observe(getViewLifecycleOwner(), task -> ruta_imagen.setText(task.getURL_img()));
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
        ImageButton borrarDocumento = fragmento2.findViewById(R.id.borrardocumento);
        ImageButton borrarVideo = fragmento2.findViewById(R.id.borrarvideo);
        ImageButton borrarAudio = fragmento2.findViewById(R.id.borraraudio);
        ImageButton borrarImagen = fragmento2.findViewById(R.id.borrarimagen);
        btnCreateTask = fragmento2.findViewById(R.id.create);

        //Labels
        ruta_audio = fragmento2.findViewById(R.id.labelAudio);
        ruta_documento = fragmento2.findViewById(R.id.labelDocumento);
        ruta_imagen = fragmento2.findViewById(R.id.labelImagen);
        ruta_video = fragmento2.findViewById(R.id.labelVideo);

        //Listener
        btnSalir.setOnClickListener(this::backFragment);
        documento.setOnClickListener(v -> onSelectDocumentClick());
        audio.setOnClickListener(v -> onSelectAudioClick());
        imagen.setOnClickListener(v -> onSelectImgClick());
        video.setOnClickListener(v -> onSelectVideoClick());
        borrarDocumento.setOnClickListener(v -> borrarArchivo(ruta_documento.getText().toString(),ruta_documento));
        borrarImagen.setOnClickListener(v -> borrarArchivo(ruta_imagen.getText().toString(),ruta_imagen));
        borrarAudio.setOnClickListener(v -> borrarArchivo(ruta_audio.getText().toString(),ruta_audio));
        borrarVideo.setOnClickListener(v -> borrarArchivo(ruta_video.getText().toString(),ruta_video));

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
            comunicateFragments.getDate1().observe(getViewLifecycleOwner(), da -> date1 = da);
            comunicateFragments.getDate2().observe(getViewLifecycleOwner(), da -> task.setDateEnd(da));
            comunicateFragments.getPrioritario().observe(getViewLifecycleOwner(), task::setPrioritaria);
            comunicateFragments.getState().observe(getViewLifecycleOwner(), da -> task.setStatesNumber(Task.States.valueOf(String.valueOf(da))));

            comunicateFragments.getUrl_audio().observe(getViewLifecycleOwner(), s -> {
                audio = s;
                System.out.println(audio);
            });
            comunicateFragments.getUrl_doc().observe(getViewLifecycleOwner(), s -> {
                documento = s;
            });
            comunicateFragments.getUrl_video().observe(getViewLifecycleOwner(), s -> {
                video = s;
            });
            comunicateFragments.getUrl_img().observe(getViewLifecycleOwner(), s -> {
                imagen = s;
            });

        }
    }

    // Método para enviar datos al ViewModel compartido
    private void sendData() {
        comunicateFragments.setDescription(descripcion.getText().toString());
        comunicateFragments.setUrl_doc(ruta_documento.getText().toString());
        comunicateFragments.setUrl_audio(ruta_audio.getText().toString());
        comunicateFragments.setUrl_video(ruta_video.getText().toString());
        comunicateFragments.setUrl_img(ruta_imagen.getText().toString());
    }

    private void pedirPermisos(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Verificar si no tienes el permiso de escritura
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Solicitar permiso de escritura
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }

            // Verificar si no tienes el permiso de lectura
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Solicitar permiso de lectura
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);
            }
        }
    }


    private void borrarArchivo(String ruta, TextView textView){
        pedirPermisos();
        if(SdManager.borrarArchivo(ruta)){
            textView.setText("");
        } else {
            Log.e("error","No se pudo borrar el documento");
        }
    }

    private void onSelectDocumentClick() {
        pedirPermisos();
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");  // Todos los tipos de archivos

        seleccionarArchivo.launch(intent);
    }
    private void onSelectImgClick() {
        pedirPermisos();
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");  // Todos los tipos de archivos

        seleccionarArchivo.launch(intent);
    }
    private void onSelectVideoClick() {
        pedirPermisos();
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");  // Todos los tipos de archivos

        seleccionarArchivo.launch(intent);
    }
    private void onSelectAudioClick() {
        pedirPermisos();
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");  // Todos los tipos de archivos

        seleccionarArchivo.launch(intent);
    }

    ActivityResultLauncher<Intent> seleccionarArchivo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Manejar el resultado de la selección de archivos
                if (result.getResultCode() == Activity.RESULT_OK) {
                    handleFileSelectionResult(result.getData());
                }
            });

    /**
     * Método que coge el intent y guarda la uri del intent tambien comprueba la preferencia de la SdCard.
     * @param data
     */
    private void handleFileSelectionResult(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                guardarArchivoEnDirectorio(uri,SdManager.isSdChecked(requireContext()));
            }
        }
    }

    /**
     * Guardar en la sd card el archivo.
     * @param uri
     */
    public void guardarArchivoEnDirectorio(Uri uri, boolean guardarEnTarjetaSD) {
        try {

            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);

            // Obtener el nombre del archivo original desde la URI
            String nombreArchivoOriginal = SdManager.obtenerNombreArchivoDesdeUri(requireContext(), uri);

            // Define la ruta de destino en tu directorio específico
            File directorioApp;

            if (guardarEnTarjetaSD && SdManager.isTarjetaSDMontada()) {
                directorioApp = new File(Environment.getExternalStorageDirectory(), "archivos_adjuntos");
            } else {
                directorioApp = new File(requireActivity().getExternalFilesDir(null), "archivos_adjuntos");
            }

            if (!directorioApp.exists()) {
                if (directorioApp.mkdirs()) {
                    Log.d("Almacenamiento", "Directorio de archivos adjuntos creado: " + directorioApp.getAbsolutePath());
                } else {
                    Log.e("Almacenamiento", "Error al crear el directorio de archivos adjuntos");
                    return;
                }
            }

            // Usar el mismo nombre del archivo original
            File archivoDestino = new File(directorioApp, nombreArchivoOriginal);

            OutputStream outputStream = new FileOutputStream(archivoDestino);

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            Log.d("Almacenamiento", "Archivo guardado en: " + archivoDestino.getAbsolutePath());

            if (SdManager.esTipoDocumento(requireContext(), uri)) {
                documento = archivoDestino.getAbsolutePath();
                ruta_documento.setText(documento);
            } else if (SdManager.esTipoAudio(requireContext(), uri)) {
                audio = archivoDestino.getAbsolutePath();
                ruta_audio.setText(audio);
            } else if (SdManager.esTipoImagen(requireContext(), uri)) {
                imagen = archivoDestino.getAbsolutePath();
                ruta_imagen.setText(imagen);
            } else if (SdManager.esTipoVideo(requireContext(), uri)) {
                video = archivoDestino.getAbsolutePath();
                ruta_video.setText(video);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Almacenamiento", "Error al guardar el archivo: " + e.getMessage());
        }
    }
}
