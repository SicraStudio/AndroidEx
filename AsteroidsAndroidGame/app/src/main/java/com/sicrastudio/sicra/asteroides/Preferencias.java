package com.sicrastudio.sicra.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by sicra on 18/01/15.
 */
public class Preferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
