package com.rdd.trasstarea.activities.createtaskactivity.fragments;

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

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.ComunicateFragments;
import com.rdd.trasstarea.activities.listactivity.ListActivity;
import com.rdd.trasstarea.listcontroller.ListController;
import com.rdd.trasstarea.model.Task;

public class CreateSecondTaskFrag extends Fragment {

    private String titulo, date1, date2, state = "";
    private Task task = new Task();
    private EditText descripcion;
    private boolean prioritaria;
    private Button btnSalir, btnCreateTask;
    private CreateTaskFragment createTaskFragment;

    private ComunicateFragments comunicateFragments;


    public interface mandarTarea{
        void mandarTask();
    }

    private mandarTarea comunicador;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof mandarTarea){
            comunicador = (mandarTarea) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createTaskFragment = new CreateTaskFragment();


        //Obtenemos una referencia del ViewModel
         comunicateFragments = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmento2 = inflater.inflate(R.layout.create_task2, container, false);



        initComponents(fragmento2);
        recuperarDatos();
        return fragmento2;
    }

    private void initComponents(View fragmento2){
        descripcion = fragmento2.findViewById(R.id.editTextDescription);
        btnSalir = fragmento2.findViewById(R.id.back);
        btnCreateTask = fragmento2.findViewById(R.id.create);
        btnSalir.setOnClickListener(this::backFragment);
        btnCreateTask.setOnClickListener(v -> {
            comunicador.mandarTask();
        });
    }

    private void backFragment(View view) {
        // Intenta encontrar la instancia existente del CreateTaskFragment por su tag
        CreateTaskFragment existingFragment = (CreateTaskFragment) getActivity().getSupportFragmentManager().findFragmentByTag("CreateTaskFragment");

        // Si no se encuentra, crea una nueva instancia
        if (existingFragment == null) {
            existingFragment = new CreateTaskFragment();
        }

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, existingFragment, "CreateTaskFragment").commit();
    }


    private void recuperarDatos(){
        if (comunicateFragments.getTitulo().isInitialized()) {
            comunicateFragments.getTitulo().observe(getViewLifecycleOwner(), nuevoTitulo -> task.setTitulo(nuevoTitulo));
            comunicateFragments.getDate1().observe(getViewLifecycleOwner(), da -> {
                date1 = da;
                System.out.println(date1);
            });
            comunicateFragments.getDate2().observe(getViewLifecycleOwner(), da -> task.setDateEnd(ListController.convertirFecha(da)));
            comunicateFragments.getPrioritario().observe(getViewLifecycleOwner(), da -> task.setPrioritaria(da));
            comunicateFragments.getState().observe(getViewLifecycleOwner(), da -> task.setStatesNumber(Task.States.valueOf(String.valueOf(da))));
        }
    }

    private void createTask(){
        task.setDescription(descripcion.getText().toString());
    }




}
