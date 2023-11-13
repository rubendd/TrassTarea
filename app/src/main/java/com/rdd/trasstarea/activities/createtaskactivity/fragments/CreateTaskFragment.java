package com.rdd.trasstarea.activities.createtaskactivity.fragments;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.CheckTask;
import com.rdd.trasstarea.activities.createtaskactivity.ComunicateFragments;
import com.rdd.trasstarea.activities.createtaskactivity.dialogs.datepicker.DatePickerHandle;

public class CreateTaskFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private ComunicateFragments compartirViewModel;
    private CreateSecondTaskFrag createSecondTaskFrag;

    private Spinner spinner;
    private Button cancelar, siguiente;
    private EditText titulo,date1,date2;
    private CheckBox prioritaria;
    String select;

    public CreateTaskFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSecondTaskFrag = new CreateSecondTaskFrag();
        compartirViewModel = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmento1 = inflater.inflate(R.layout.create_task, container, false);

        //Configure
        initComponents(fragmento1);
        initSpinner();
        onClickDate();
        recuperarDatos();
        return fragmento1;
    }

    private void recuperarDatos(){
            if (compartirViewModel.getTitulo().isInitialized()) {
                compartirViewModel.getTitulo().observe(getViewLifecycleOwner(), nuevoTitulo -> {
                    // Actualiza la interfaz de usuario con el nuevo título
                    titulo.setText(nuevoTitulo);
                });
               compartirViewModel.getDate1().observe(getViewLifecycleOwner(), da -> {
                   date1.setText(da);
               });
               compartirViewModel.getDate2().observe(getViewLifecycleOwner(), da -> {
                   date2.setText(da);
               });
               compartirViewModel.getPrioritario().observe(getViewLifecycleOwner(), da -> {
                   prioritaria.setActivated(da);
               });
        }
    }


    private void initComponents(View fragmento1){
        titulo = fragmento1.findViewById(R.id.tituloAdd);
        date1 = fragmento1.findViewById(R.id.date1);
        date2 = fragmento1.findViewById(R.id.date2);
        spinner = fragmento1.findViewById(R.id.spinner);
        cancelar = fragmento1.findViewById(R.id.cancelar);
        siguiente = fragmento1.findViewById(R.id.siguiente);
        prioritaria = fragmento1.findViewById(R.id.prioritaria);

        siguiente.setOnClickListener(this::nextActivity);
        cancelar.setOnClickListener(view -> getActivity().finish());
        spinner.setOnItemSelectedListener(this);

    }


    private void nextActivity(View view){
        if (nextPageCheck()) {
            sendData();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, createSecondTaskFrag, "CreateSecondTaskFrag").commit();
        }
    }

    private void sendData(){
        compartirViewModel.setTitulo(titulo.getText().toString());
        compartirViewModel.setDate2(date2.getText().toString());
        compartirViewModel.setDate1(date1.getText().toString());
        compartirViewModel.setState(select);
        compartirViewModel.setPrioritario(prioritaria.isActivated());
    }

    private boolean nextPageCheck(){
        if (CheckTask.checkEditText(titulo)){
            Toast.makeText(getActivity(), "Debes insertar un titulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (CheckTask.checkEditText(date1)){
            Toast.makeText(getActivity(), "Debes insertar una fecha de creacion", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (CheckTask.checkEditText(date2)){
            Toast.makeText(getActivity(), "Debes insertar una fecha objetivo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void onClickDate(){
        date1.setOnClickListener(this::initDatePicker);
        date2.setOnClickListener(this::initDatePicker);
    }

    private void initDatePicker(View view){
        DatePickerHandle datePickerHandler1 = new DatePickerHandle((EditText) view, getParentFragmentManager());
        datePickerHandler1.showDatePicker(getParentFragmentManager());
    }

    private void initSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.progress, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         select = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
