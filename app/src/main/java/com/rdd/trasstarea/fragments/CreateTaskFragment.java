package com.rdd.trasstarea.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.CheckTask;
import com.rdd.trasstarea.activities.createtaskactivity.dialogs.datepicker.DatePickerHandle;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

import java.util.Calendar;
public class CreateTaskFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private ComunicateFragments compartirViewModel;  // ViewModel compartido entre fragmentos
    private CreateSecondTaskFrag createSecondTaskFrag;  // Segundo fragmento para la creación de tareas

    private Spinner spinner;
    private EditText titulo, date1, date2;
    private CheckBox prioritaria;
    private TextView text;
    private String select;  // Variable para almacenar la selección del Spinner

    // Constructor por defecto del fragmento
    public CreateTaskFragment() {
    }

    // Método llamado para guardar el estado del fragmento antes de ser destruido y recreado
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Verifica si el LiveData de la tarea está inicializado
        if (compartirViewModel.getTaskLiveData().isInitialized()) {
            text.setText("Editar tarea");

            // Observa cambios en la tarea y actualiza la interfaz de usuario en consecuencia
            compartirViewModel.getTaskLiveData().observe(getViewLifecycleOwner(), task -> {
                if (task != null) {
                    titulo.setText(task.getTitulo());
                    date1.setText(ListController.calendarToText(Calendar.getInstance()));
                    date2.setText(ListController.calendarToText(task.getDateEnd()));
                    prioritaria.setChecked(task.isPrioritaria());
                    obtenerState(task);
                    outState.putSerializable("task", task);  // Guarda la tarea en el Bundle
                }
            });
        }
    }

    // Método llamado al crear el fragmento
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSecondTaskFrag = new CreateSecondTaskFrag();
        compartirViewModel = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);

    }

    // Método llamado para crear la vista del fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmento1 = inflater.inflate(R.layout.create_task, container, false);

        // Configuración de componentes y manejo de datos
        initComponents(fragmento1);
        initSpinner();
        onClickDate();
        if (savedInstanceState != null) {
            addDatosDelBundle(savedInstanceState);
        } else {
            putDataTask();
            recuperarDatos();
        }
        return fragmento1;
    }

    // Método para agregar datos desde el Bundle al restaurar el estado del fragmento
    private void addDatosDelBundle(Bundle savedInstance) {
        Task task = (Task) savedInstance.getSerializable("task");

        // Actualiza la interfaz de usuario con los datos de la tarea
        if (task != null) {
            titulo.setText(task.getTitulo());
            date1.setText(ListController.calendarToText(task.getFechaInicio()));
            date2.setText(ListController.calendarToText(task.getDateEnd()));
            prioritaria.setChecked(task.isPrioritaria());
            obtenerState(task);
        }
    }

    // Método para obtener datos de la tarea y actualizar la interfaz de usuario
    private void putDataTask() {
        if (compartirViewModel.getTaskLiveData().isInitialized()) {
            text.setText("Editar tarea");
            compartirViewModel.getTaskLiveData().observe(getViewLifecycleOwner(), task -> {
                if (task != null) {
                    titulo.setText(task.getTitulo());
                    date1.setText(ListController.calendarToText(Calendar.getInstance()));
                    date2.setText(ListController.calendarToText(task.getDateEnd()));
                    prioritaria.setChecked(task.isPrioritaria());
                    obtenerState(task);
                }
            });
        }
    }

    // Método para obtener el estado de la tarea y actualizar el Spinner
    private void obtenerState(Task task) {
        spinner.setSelection(Task.setSpinnerEnum(task.getProgresState()));
    }

    // Método para recuperar datos del ViewModel compartido
    private void recuperarDatos() {
        if (compartirViewModel.getTitulo().isInitialized()) {
            compartirViewModel.getTitulo().observe(getViewLifecycleOwner(), nuevoTitulo -> {
                // Actualiza la interfaz de usuario con el nuevo título
                titulo.setText(nuevoTitulo);
            });
            compartirViewModel.getDate1().observe(getViewLifecycleOwner(), da -> date1.setText(da));
            compartirViewModel.getDate2().observe(getViewLifecycleOwner(), da -> date2.setText(da));
            compartirViewModel.getPrioritario().observe(getViewLifecycleOwner(), da -> prioritaria.setChecked(da));
        }
    }

    // Método para inicializar los componentes de la interfaz de usuario
    private void initComponents(View fragmento1) {
        // Inicialización de vistas y configuración de listeners
        titulo = fragmento1.findViewById(R.id.tituloAdd);
        date1 = fragmento1.findViewById(R.id.date1);
        date2 = fragmento1.findViewById(R.id.date2);
        spinner = fragmento1.findViewById(R.id.spinner);
        Button cancelar = fragmento1.findViewById(R.id.cancelar);
        Button siguiente = fragmento1.findViewById(R.id.siguiente);
        prioritaria = fragmento1.findViewById(R.id.prioritaria);
        text = fragmento1.findViewById(R.id.texto);

        // Configuración de listeners
        siguiente.setOnClickListener(this::nextActivity);
        cancelar.setOnClickListener(view -> requireActivity().finish());
        spinner.setOnItemSelectedListener(this);
    }

    // Método llamado al hacer clic en el botón siguiente
    private void nextActivity(View view) {
        if (nextPageCheck()) {
            sendData();

            // Reemplaza el fragmento actual con el segundo fragmento y lo agrega a la pila de retroceso
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_task_create, createSecondTaskFrag, "CreateSecond")
                        .addToBackStack(null)
                        .commit();

        }
    }

    // Método para enviar datos al ViewModel compartido
    private void sendData() {

        //Crear
        compartirViewModel.setTitulo(titulo.getText().toString());
        compartirViewModel.setDate2(date2.getText().toString());
        compartirViewModel.setDate1(date1.getText().toString());
        compartirViewModel.setState(select);
        compartirViewModel.setPrioritario(prioritaria.isChecked());

        //Editar
        compartirViewModel.getTaskLiveData().observe(getViewLifecycleOwner(), task1 -> {
            task1.setTitulo(titulo.getText().toString());
            task1.setFechaInicio(ListController.convertirFecha(date1.getText().toString()));
            task1.setDateEnd(ListController.convertirFecha(date2.getText().toString()));
            task1.setProgresState(task1.setStatesNumber(Task.States.valueOf(select)));
            task1.setPrioritaria(prioritaria.isChecked());
        });

    }

    // Método para verificar si los campos obligatorios están completos antes de pasar a la siguiente página
    private boolean nextPageCheck() {
        if (CheckTask.checkEditText(titulo)) {
            Toast.makeText(getActivity(), "Debes insertar un título", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (CheckTask.checkEditText(date1)) {
            Toast.makeText(getActivity(), "Debes insertar una fecha de creación", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (CheckTask.checkEditText(date2)) {
            Toast.makeText(getActivity(), "Debes insertar una fecha objetivo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método llamado al hacer clic en una fecha para mostrar el selector de fecha
    private void onClickDate() {
        date1.setOnClickListener(this::initDatePicker);
        date2.setOnClickListener(this::initDatePicker);
    }

    // Método para inicializar el selector de fecha
    private void initDatePicker(View view) {
        DatePickerHandle datePickerHandler1 = new DatePickerHandle((EditText) view, getParentFragmentManager());
        datePickerHandler1.showDatePicker(getParentFragmentManager());
    }

    // Método para inicializar el Spinner con las opciones de estado de la tarea
    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.progress, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Método llamado cuando se selecciona un elemento en el Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        select = parent.getItemAtPosition(position).toString();
    }

    // Método llamado cuando no se selecciona ningún elemento en el Spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No se realiza ninguna acción en este caso
    }
}

