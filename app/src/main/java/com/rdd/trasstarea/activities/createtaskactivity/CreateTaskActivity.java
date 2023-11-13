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

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.dialogs.datepicker.DatePickerHandle;
import com.rdd.trasstarea.activities.listactivity.ListActivity;

public class CreateTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Button cancelar, siguiente;
    private EditText titulo,date1,date2;
    private CheckBox prioritaria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);


        //Configure
        initComponents();
        initSpinner();
        onClickDate();
    }


    /**
     * Iniciar valores spinner.
     */
    private void initSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.progress, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    private void initComponents(){
        spinner = findViewById(R.id.spinner);
        siguiente = findViewById(R.id.siguiente);
        titulo = findViewById(R.id.tituloAdd);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        titulo = findViewById(R.id.tituloAdd);
        prioritaria = findViewById(R.id.prioritaria);
        cancelar = findViewById(R.id.cancelar);
        cancelar.setOnClickListener(v -> finish());
        siguiente.setOnClickListener(v -> nextPage());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void onClickDate(){
        date1.setOnClickListener(this::initDatePicker);
        date2.setOnClickListener(this::initDatePicker);
    }

    private void initDatePicker(View view){
        DatePickerHandle datePickerHandler1 = new DatePickerHandle((EditText) view, getSupportFragmentManager());
        datePickerHandler1.showDatePicker(getSupportFragmentManager());
    }

    private boolean nextPageCheck(){
        if (CheckTask.checkEditText(titulo)){
            Toast.makeText(this, "Debes insertar un titulo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (CheckTask.checkEditText(date1)){
            Toast.makeText(this, "Debes insertar una fecha de creacion", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (CheckTask.checkEditText(date2)){
            Toast.makeText(this, "Debes insertar una fecha objetivo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void nextPage(){
        if (nextPageCheck()){
            Intent intent = new Intent(this, SecondCreateTaskActv.class);
            startActivity(intent);
        }
    }


}
