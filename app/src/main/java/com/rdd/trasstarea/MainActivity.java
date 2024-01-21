package com.rdd.trasstarea;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.rdd.trasstarea.activities.listactivity.ListActivity;
import com.rdd.trasstarea.database.tarjetasd.SdManager;

public class MainActivity extends AppCompatActivity {


    public static final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificarPermsisos();
        SdManager.borrarArchivosPorTiempo(getApplicationContext());
        LottieAnimationView btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> start());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    /**
     * Emepezar actividad.
     */
    private void start() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }


    /**
     * Pedimos permisos.
     */
    private void verificarPermsisos(){
        int permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permiso2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permiso != PackageManager.PERMISSION_GRANTED && permiso2 != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }
    }










    
}