package com.rdd.trasstarea.fragments;

import android.content.Context;
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

    private String date1;
    private Task task = new Task();
    private EditText descripcion;
    private LottieAnimationView btnCreateTask;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tarea", task);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Obtenemos una referencia del ViewModel
         comunicateFragments = new ViewModelProvider(requireActivity()).get(ComunicateFragments.class);
    }

    private void putDataTask(){
        if (comunicateFragments.getTaskLiveData().isInitialized()){
            comunicateFragments.getTaskLiveData().observe(getViewLifecycleOwner(), task -> descripcion.setText(task.getDescription()));
        }
    }

    private void recibirTask(){
        if (comunicateFragments.getTaskLiveData().isInitialized()){
            comunicateFragments.getTaskLiveData().observe(getViewLifecycleOwner(), task -> this.task = task);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmento2 = inflater.inflate(R.layout.create_task2, container, false);

        initComponents(fragmento2);
        if (savedInstanceState != null) {
            recibirTask();
        } else {
            putDataTask();
            recuperarDatos();
        }
        return fragmento2;
    }

    private void initComponents(View fragmento2){
        descripcion = fragmento2.findViewById(R.id.editTextDescription);
        Button btnSalir = fragmento2.findViewById(R.id.back);
        btnCreateTask = fragmento2.findViewById(R.id.create);
        btnSalir.setOnClickListener(this::backFragment);
        btnCreateTask.setOnClickListener(v -> {
            sendData();
            comunicador.mandarTask();
        });
    }

    private void backFragment(View view) {
        // Intenta encontrar la instancia existente del CreateTaskFragment por su tag
        CreateTaskFragment existingFragment = (CreateTaskFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("CreateTaskFragment");

        // Si no se encuentra, crea una nueva instancia
        if (existingFragment == null) {
            existingFragment = new CreateTaskFragment();
        }

        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, existingFragment, "CreateTaskFragment").commit();
    }


    private void recuperarDatos(){
        if (comunicateFragments.getTitulo().isInitialized()) {
            comunicateFragments.getTitulo().observe(getViewLifecycleOwner(), task::setTitulo);
            comunicateFragments.getDate1().observe(getViewLifecycleOwner(), da -> {
                date1 = da;
                System.out.println(date1);
            });
            comunicateFragments.getDate2().observe(getViewLifecycleOwner(), da -> task.setDateEnd(ListController.convertirFecha(da)));
            comunicateFragments.getPrioritario().observe(getViewLifecycleOwner(), task::setPrioritaria);
            comunicateFragments.getState().observe(getViewLifecycleOwner(), da -> task.setStatesNumber(Task.States.valueOf(String.valueOf(da))));
        }
    }

    private void sendData(){
        comunicateFragments.setDescription(descripcion.getText().toString());
    }

}
