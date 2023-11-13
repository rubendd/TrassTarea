package com.rdd.trasstarea.activities.createtaskactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.dialogs.datepicker.DatePickerHandle;
import com.rdd.trasstarea.activities.createtaskactivity.fragments.CreateSecondTaskFrag;
import com.rdd.trasstarea.activities.createtaskactivity.fragments.CreateTaskFragment;
import com.rdd.trasstarea.activities.listactivity.ListActivity;

public class CreateTaskActivity extends AppCompatActivity {


    public static final String TITULO = "titulo";
    public static final String DATE1 = "fecha1";
    public static final String DATE2 = "fecha2";
    public static final String STATE = "estado";
    public static final String PRIORITAIO = "prioritario";


    private CreateTaskFragment createTaskFragment;
    private CreateSecondTaskFrag createSecondTaskFrag;
    private ComunicateFragments comunicateFragments;
    private Spinner spinner;
    private Button cancelar, siguiente;
    private EditText titulo,date1,date2;
    private CheckBox prioritaria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_create_task);

        comunicateFragments = new ViewModelProvider(this).get(ComunicateFragments.class);

        createTaskFragment = new CreateTaskFragment();
        createSecondTaskFrag = new CreateSecondTaskFrag();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_task_create, createTaskFragment).addToBackStack(null).commit();

    }



    public void sendData(View view) {
        Intent intent = new Intent(this, SecondCreateTaskActv.class);
        //Seteamos strings y datos
        String s_titulo = titulo.getText().toString();
        String s_date = date1.getText().toString();
        String s_date2 = date2.getText().toString();
        int estado = spinner.getBaseline();
        boolean b_prioritaria = prioritaria.isActivated();

        //Lo metemos en el intent.
        intent.putExtra(TITULO, s_titulo);
        intent.putExtra(DATE1, s_date);
        intent.putExtra(DATE2, s_date2);
        intent.putExtra(STATE, estado);
        intent.putExtra(PRIORITAIO, b_prioritaria);
        startActivity(intent);
    }


}
