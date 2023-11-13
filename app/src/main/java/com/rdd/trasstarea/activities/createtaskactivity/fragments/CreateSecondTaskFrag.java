package com.rdd.trasstarea.activities.createtaskactivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.ComunicateFragments;

public class CreateSecondTaskFrag extends Fragment {

    private String titulo, date1, date2, state;
    private EditText descripcion;
    private boolean prioritaria;
    private Button btnSalir, btnCreateTask;
    private CreateTaskFragment createTaskFragment;



    public CreateSecondTaskFrag() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createTaskFragment = new CreateTaskFragment();

        //Obtenemos una referencia del ViewModel
        ComunicateFragments compartirViewModel = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);

        //Creamos un observador (de String) para implementar el método onChanged()
        Observer<String> tituloObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        };


        //Asignamos un observador al MutableLiveData
        compartirViewModel.getTitulo().observe(this, tituloObserver);

        //O bien, creamos el observador dentro de la asignación de forma anónima y con lambda
        //compartirViewModel.getNombre().observe(this, s -> textoRecibido.setText(s));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmento2 = inflater.inflate(R.layout.create_task2, container, false);
        initComponents(fragmento2);
        return fragmento2;
    }

    private void initComponents(View fragmento2){
        btnSalir = fragmento2.findViewById(R.id.back);
        btnCreateTask = fragmento2.findViewById(R.id.create);
        btnSalir.setOnClickListener(this::backFragment);
    }

    private void backFragment(View view) {
        requireActivity().getSupportFragmentManager().popBackStack();

        // Intenta encontrar la instancia existente del CreateTaskFragment por su tag
        CreateTaskFragment existingFragment = (CreateTaskFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CreateTaskFragment");

        // Si no se encuentra, crea una nueva instancia
        if (existingFragment == null) {
            existingFragment = new CreateTaskFragment();
        }

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, existingFragment, "CreateTaskFragment").commit();
    }





}
