package com.rdd.trasstarea.activities.createtaskactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rdd.trasstarea.R;
import com.rdd.trasstarea.activities.createtaskactivity.dialogs.DatePicker;

public class CreateTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Button cancelar, siguiente;
    private EditText titulo,date1,date2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        spinner = findViewById(R.id.spinner);
        cancelar = findViewById(R.id.cancelar);
        siguiente = findViewById(R.id.siguiente);
        titulo = findViewById(R.id.tituloAdd);


        cancelar.setOnClickListener(v -> finish());


        //Configure
        initSpinner();
        attachDatePicker();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Debes elegir una opcion", Toast.LENGTH_SHORT).show();
    }

    private void attachDatePicker(){
        findViewById(R.id.date2).setOnClickListener(v -> {
            DatePicker date = new DatePicker();
            date.show(getSupportFragmentManager(), "datePicker2");
        });

        findViewById(R.id.date1).setOnClickListener(v -> {
            DatePicker date = new DatePicker();
            date.show(getSupportFragmentManager(), "datePicker1");
        });
    }
}
