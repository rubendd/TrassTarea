package com.rdd.trasstarea.activities.settings;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.rdd.trasstarea.R;

import java.util.Objects;


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

    private void restablecerPreferencias(){


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
            Preference criterio = findPreference("criterio");
            Preference asc = findPreference("asc");
            Preference tarjetasd = findPreference("sd");
            Preference restablecer = findPreference("rb");
            SeekBarPreference seekBarPreference = findPreference("borrado");


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

            // Asegúrate de que tarjetasd no sea nulo antes de usarlo
            if (tarjetasd != null) {
                tarjetasd.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean sd_bool = (boolean) newValue;


                    Configuration configuration = getResources().getConfiguration();
                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                    // Guardar la preferencia en SharedPreferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("sd", sd_bool);
                    editor.apply();


                    return true;
                });
            }



            if (seekBarPreference != null) {
                // Obtén el valor actual de las SharedPreferences y establece el resumen
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                int valorActual = preferences.getInt("borrado", 0);

                if (valorActual == 0) {
                    seekBarPreference.setSummary("Duración de archivos guardados: (indefinido)");
                } else {
                    seekBarPreference.setSummary("Duración de archivos guardados: " + valorActual + "dias");
                }

                // Establece un oyente para detectar cambios en la SeekBarPreference
                seekBarPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    int nuevoValor = (int) newValue;

                    if (nuevoValor == 0) {
                        preference.setSummary("Duración de archivos guardados: (indefinido)");
                    } else {
                        preference.setSummary("Duración de archivos guardados: " + nuevoValor + " días");
                    }
                    // Actualiza el resumen con el nuevo valor


                    // Guarda el nuevo valor en SharedPreferences
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("borrado", nuevoValor);
                    editor.apply();

                    return true;
                });
            }

            if (restablecer != null) {
                restablecer.setOnPreferenceClickListener(preference -> {
                    SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    requireActivity().recreate();
                    return true;
                });
            }

        }


    }
}



