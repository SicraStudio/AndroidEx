package com.sicrastudio.examen2ev;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by sicra on 1/03/15.
 */
public class Preferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.layout_preferencias);
    }
}
