package com.rdd.trasstarea.activities.settings;

import android.content.SharedPreferences;
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


        //Cargamos el fragmento de preferencias
        getSupportFragmentManager().beginTransaction()
                //El recurso 'android.R.id.content' es la ventana activa de la aplicación
                .replace(android.R.id.content, new SettingFragments())
                .commit();
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
        }


    }
}



