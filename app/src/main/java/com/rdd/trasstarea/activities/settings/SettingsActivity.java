package com.rdd.trasstarea.activities.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.rdd.trasstarea.R;

public class SettingsActivity extends PreferenceFragmentCompat {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String key = "session";


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Si se selecciona el botón atrás de la barra de menú,
        // cerramos la actividad Preferencias
        if (item.getItemId() == android.R.id.home) {
           // finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean reviewSession(){
        return this.preferences.getBoolean(key, false);
    }

    private void saveSession(boolean checked){
        editor.putBoolean(key, checked);
        editor.apply();
    }
/*
    private void initElement(){
        preferences = this.getPreferences(Context.MODE_PRIVATE);
        editor = preferences.edit();
    }*/
}
