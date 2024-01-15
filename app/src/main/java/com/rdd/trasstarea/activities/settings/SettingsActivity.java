package com.rdd.trasstarea.activities.settings;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.rdd.trasstarea.R;


public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Esta actividad no utiliza setContentView()!!
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Preferencias de usuario");

       // setSettings();


        //Cargamos el fragmento de preferencias
        getSupportFragmentManager().beginTransaction()
                //El recurso 'android.R.id.content' es la ventana activa de la aplicación
                .replace(android.R.id.content, new SettingFragments())
                .commit();
    }


    /*
    private void setSettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Tema oscuro.
        boolean temaClaro = preferences.getBoolean("claro", true);
        AppCompatDelegate.setDefaultNightMode(temaClaro ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
    }
    */
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
            Preference criterio = findPreference("criterio");
            Preference asc = findPreference("asc");


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
                String fuente = (String) newValue;
                Configuration configuration = getResources().getConfiguration();

                // Tamaño fuente
                if (fuente.equals("a")) configuration.fontScale = 0.8f;
                if (fuente.equals("b")) configuration.fontScale = 1.0f;
                if (fuente.equals("c")) configuration.fontScale = 1.3f;

                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                // Guardar la preferencia en SharedPreferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fuente", fuente);
                editor.apply();

                // Forzar el reinicio de la actividad
                getActivity().recreate();

                return true;
            });

            assert criterio != null;
            criterio.setOnPreferenceChangeListener((preference, newValue) -> {
                String criterios = (String) newValue;
                Configuration configuration = getResources().getConfiguration();

                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                // Guardar la preferencia en SharedPreferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("criterio", criterios);
                editor.apply();

                return true;
            });

            assert asc != null;
            asc.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean asc_s = (boolean) newValue;
                Configuration configuration = getResources().getConfiguration();

                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                // Guardar la preferencia en SharedPreferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("asc", asc_s);
                editor.apply();

                return true;
            });
        }
    }
}



