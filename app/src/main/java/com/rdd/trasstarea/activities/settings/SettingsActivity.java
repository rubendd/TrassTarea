package com.rdd.trasstarea.activities.settings;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.rdd.trasstarea.R;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Esta actividad no utiliza setContentView()!!
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Preferencias de usuario");
        setSettings();


        //Cargamos el fragmento de preferencias
        getSupportFragmentManager().beginTransaction()
                //El recurso 'android.R.id.content' es la ventana activa de la aplicación
                .replace(android.R.id.content, new SettingFragments())
                .commit();
    }



    private void setSettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Tema oscuro.
        boolean temaClaro = preferences.getBoolean("claro", true);
        AppCompatDelegate.setDefaultNightMode(temaClaro ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);

        //Tamaño fuente
        Configuration configuration = getResources().getConfiguration();

        //Tamaño fuente
        String fuente = preferences.getString("fuente","2");
        if (fuente.equals("1")) configuration.fontScale = 0.8f;
        if (fuente.equals("2")) configuration.fontScale = 1.0f;
        if (fuente.equals("3")) configuration.fontScale = 1.5f;

        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //El recurso 'android.R.id.home' es el botón 'home' (flecha atrás) en la barra de acción
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    public static class SettingFragments extends PreferenceFragmentCompat{


        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);

            Preference claro = findPreference("claro");
            Preference tamanioFuente = findPreference("fuente");
            Preference altoContraste = findPreference("contraste");


            assert claro != null;
            claro.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean claroBool = (boolean) newValue;
                AppCompatDelegate.setDefaultNightMode(claroBool ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("claro",claroBool);
                editor.apply();
                return true;
            });

            assert tamanioFuente != null;
            tamanioFuente.setOnPreferenceChangeListener((preference, newValue) -> {
                System.out.println("Hola");
                String fuente = (String) newValue;
                Configuration configuration = getResources().getConfiguration();

                //Tamaño fuente
                if (fuente.equals("1")) configuration.fontScale = 0.8f;
                if (fuente.equals("2")) configuration.fontScale = 1.0f;
                if (fuente.equals("3")) configuration.fontScale = 1.3f;

                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fuente", fuente);
                editor.apply();
                getActivity().recreate();
                return true;
            });

        }


    }
}



