package com.rdd.trasstarea.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
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
import com.rdd.trasstarea.viewmodel.ComunicateFragments;

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
    private AudioFragment audioFragment;
    private VideoFragment videoFragment;
    private FotoFragment fotoFragment;
    private DocumentoFragment documentoFragment;



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
        audioFragment = new AudioFragment();
        videoFragment = new VideoFragment();
        fotoFragment = new FotoFragment();
        documentoFragment = new DocumentoFragment();
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

        ruta_audio.setOnClickListener(v -> iniciarFragmentoAudio());
        ruta_imagen.setOnClickListener(v -> iniciarFragmentoFoto());
        ruta_video.setOnClickListener(v -> iniciarFragmentoVideo());
        ruta_documento.setOnClickListener(v -> iniciarFragmentoDocumento());


        //Listener
        btnSalir.setOnClickListener(this::backFragment);
        documento.setOnClickListener(v -> onSelectDocumentClick());
        audio.setOnClickListener(v -> openAudioChooser());
        imagen.setOnClickListener(v -> lanzarSelectorFoto());
        video.setOnClickListener(v -> openVideoChooser());
        borrarDocumento.setOnClickListener(v -> borrarArchivo(ruta_documento.getText().toString(),ruta_documento));
        borrarImagen.setOnClickListener(v -> borrarArchivo(ruta_imagen.getText().toString(),ruta_imagen));
        borrarAudio.setOnClickListener(v -> borrarArchivo(ruta_audio.getText().toString(),ruta_audio));
        borrarVideo.setOnClickListener(v -> borrarArchivo(ruta_video.getText().toString(),ruta_video));

        btnCreateTask.setOnClickListener(v -> {
            sendData();
            comunicador.mandarTask();
        });
    }

    private void iniciarFragmentoFoto() {
        if (!ruta_imagen.getText().toString().isEmpty()) {
            comunicateFragments.setFoto(Uri.parse(ruta_imagen.getText().toString()));
            // Reemplaza el fragmento actual con el segundo fragmento y lo agrega a la pila de retroceso

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_task_create, fotoFragment, "FotoFragmento")
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void iniciarFragmentoAudio(){
        if (!ruta_audio.getText().toString().isEmpty()) {
            comunicateFragments.setAudio(Uri.parse(ruta_audio.getText().toString()));
            // Reemplaza el fragmento actual con el segundo fragmento y lo agrega a la pila de retroceso

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_task_create, audioFragment, "AudioFragmento")
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void iniciarFragmentoVideo(){
        if (!ruta_video.getText().toString().isEmpty()) {
            comunicateFragments.setVideo(Uri.parse(ruta_video.getText().toString()));
            // Reemplaza el fragmento actual con el segundo fragmento y lo agrega a la pila de retroceso

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_task_create, videoFragment, "VideoFragmento")
                    .addToBackStack(null)
                    .commit();
        }
    }
    private void iniciarFragmentoDocumento(){
        if (!ruta_documento.getText().toString().isEmpty()) {
            comunicateFragments.setDocumento(Uri.parse(ruta_documento.getText().toString()));
            // Reemplaza el fragmento actual con el segundo fragmento y lo agrega a la pila de retroceso

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_task_create, documentoFragment, "DocumentoFragmento")
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void openAudioChooser() {
        Intent recordSoundIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        Intent getContentIntent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("audio/*");
        Intent chooserIntent = Intent.createChooser(getContentIntent, "Grabaciones");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{recordSoundIntent});

        lanzadorGrabadora.launch(chooserIntent);
    }

    ActivityResultLauncher<Intent> lanzadorGrabadora = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null || data.getData() == null) {
                        // La URI es nula, no hacer nada
                        return;
                    }
                    Uri audioUri = data.getData();
                    if (isContentUri(audioUri)) {
                        // Es una URI de contenido, copia y guarda el archivo en la carpeta de tu aplicación
                        try {
                            copyAndSaveArchivo(audioUri,"audio");
                        } catch (IOException e) {
                            e.printStackTrace();
                            // Manejar errores, por ejemplo, mostrar un mensaje de error al usuario
                            Toast.makeText(requireContext(), "Error al guardar el archivo de audio", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ruta_audio.setText(audioUri.toString());
                    }
                }
            }
    );

    private void openVideoChooser() {
        Intent aVideos = new Intent();
        aVideos.setType("video/*");
        aVideos.setAction(Intent.ACTION_GET_CONTENT);

        Intent aCamaraVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_TITLE, "Vídeos");
        chooser.putExtra(Intent.EXTRA_INTENT, aVideos);
        Intent[] intentarray = {aCamaraVideo};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentarray);

        lanzadorCamaraVideo.launch(chooser);
    }


    ActivityResultLauncher<Intent> lanzadorCamaraVideo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri video = result.getData() != null ? result.getData().getData() : null;
                    if (video != null) {
                        if (isContentUri(video)) {
                            // Es una URI de contenido, copia y guarda el archivo en la carpeta de tu aplicación
                            try {
                                copyAndSaveArchivo(video,"video");
                            } catch (IOException e) {
                                e.printStackTrace();
                                // Manejar errores, por ejemplo, mostrar un mensaje de error al usuario
                                Toast.makeText(requireContext(), "Error al guardar el archivo de audio", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ruta_audio.setText(video.toString());
                        }
                    }
                }
            }
    );

    private boolean isContentUri(Uri uri) {
        // Verifica si la URI es un proveedor de contenido
        return ContentResolver.SCHEME_CONTENT.equals(uri.getScheme());
    }



    private void copyAndSaveArchivo(Uri sourceUri, String tipo) throws IOException {
        // Abre un InputStream desde la URI de origen
        InputStream inputStream = requireActivity().getContentResolver().openInputStream(sourceUri);

        String sourceFileName = getFileNameFromUri(sourceUri);

        // Crea una ruta de destino en la carpeta de tu aplicación (puedes personalizar la ruta según tus necesidades)
        File destinationFile = new File(requireContext().getFilesDir(), sourceFileName);

        // Abre un OutputStream para la ruta de destino
        OutputStream outputStream = new FileOutputStream(destinationFile);

        // Copia los datos desde el InputStream al OutputStream
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        // Cierra los flujos
        inputStream.close();
        outputStream.close();
        if (tipo.equals("audio")){
            ruta_audio.setText(destinationFile.getAbsolutePath());
        }
        if (tipo.equals("video")){
            ruta_video.setText(destinationFile.getAbsolutePath());
        }
        if (tipo.equals("foto")){
            ruta_imagen.setText(destinationFile.getAbsolutePath());
        }
        if (tipo.equals("documento")) {
            ruta_documento.setText(destinationFile.getAbsolutePath());
        }



    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = "nombre_desconocido"; // Nombre predeterminado en caso de que no se pueda obtener desde la Uri

        if (uri != null) {
            Cursor cursor = null;
            try {
                // Consulta el nombre del archivo usando la Uri
                cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        // Obtiene el nombre del archivo desde el cursor
                        fileName = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return fileName;
    }

    private void lanzarSelectorFoto() {
        ruta_imagen.setText(null);

        Intent aFotos = new Intent();
        aFotos.setType("image/*");
        aFotos.setAction(Intent.ACTION_GET_CONTENT);

        Intent aCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_TITLE, "Fotos");
        chooser.putExtra(Intent.EXTRA_INTENT, aFotos);
        Intent[] intentArray = {aCamara};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        lanzadorCamara.launch(chooser);
    }

    ActivityResultLauncher<Intent> lanzadorCamara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::procesarResultadoCamara
    );

    private void procesarResultadoCamara(ActivityResult result) {
        Intent intentDevuelto = result.getData();
        Uri fotoSeleccionada = obtenerUriFotoSeleccionada(intentDevuelto);

        if (fotoSeleccionada != null) {
            if (isContentUri(fotoSeleccionada)) {
                guardarImagenDesdeUri(fotoSeleccionada);
            } else {
                mostrarUriImagen(fotoSeleccionada);
            }
        } else {
            guardarImagenTemporal(intentDevuelto);
        }
    }

    private Uri obtenerUriFotoSeleccionada(Intent intentDevuelto) {
        if (intentDevuelto != null) {
            return intentDevuelto.getData();
        }
        return null;
    }

    private void guardarImagenDesdeUri(Uri fotoSeleccionada) {
        try {
            copyAndSaveArchivo(fotoSeleccionada, "foto");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensajeError("Error al guardar la imagen");
        }
    }

    private void mostrarUriImagen(Uri fotoSeleccionada) {
        ruta_imagen.setText(fotoSeleccionada.toString());
    }

    private void guardarImagenTemporal(Intent intentDevuelto) {
        FileOutputStream fos = null;
        File file = new File(getActivity().getCacheDir(), "imagen_tem.jpg");

        try {
            Bitmap imagenCapturada = (Bitmap) intentDevuelto.getExtras().get("data");

            fos = new FileOutputStream(file);
            imagenCapturada.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Uri fotoUri = Uri.fromFile(file);
        ruta_imagen.setText(fotoUri.toString());
    }

    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
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
        comunicateFragments.setDocumento(Uri.parse(ruta_documento.getText().toString()));
        comunicateFragments.setAudio(Uri.parse(ruta_audio.getText().toString()));
        comunicateFragments.setVideo(Uri.parse(ruta_video.getText().toString()));
        comunicateFragments.setFoto(Uri.parse(ruta_imagen.getText().toString()));
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



    ActivityResultLauncher<Intent> seleccionarArchivo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Manejar el resultado de la selección de archivos
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        handleFileSelectionResult(result.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

    /**
     * Método que coge el intent y guarda la uri del intent tambien comprueba la preferencia de la SdCard.
     * @param data
     */
    private void handleFileSelectionResult(Intent data) throws IOException {
        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                copyAndSaveArchivo(uri,"documento");
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

            String nombreArchivoOriginal = SdManager.obtenerNombreArchivoDesdeUri(requireContext(), uri);

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
