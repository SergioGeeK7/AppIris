package com.example.sergiogeek7.appiris.activities;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.sergiogeek7.appiris.R;

/**
 * Configuracion fragmento
 *
 * Pantalla de configuracion, mostrar tour
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prev_visualizer);
    }
}
