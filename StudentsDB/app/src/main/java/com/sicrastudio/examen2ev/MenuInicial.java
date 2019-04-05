package com.sicrastudio.examen2ev;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MenuInicial extends Activity {

    private Button btnAltas;
    private Button btnPref;
    private Button btnAcerca;
    private Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        btnAltas = (Button) findViewById(R.id.buttonAltas);
        btnAcerca = (Button) findViewById(R.id.buttonAcerca);
        btnPref = (Button) findViewById(R.id.buttonPref);
        btnSalir = (Button) findViewById(R.id.buttonSalir);

    }

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAltas:
                //se lanza la activity altas
                lanzaAltas();
                break;
            case R.id.buttonAcerca:
                Toast.makeText(MenuInicial.this, "David Arcis Moreno\n\nCURSO 2014 - 2015", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonPref:
                lanzaPreferencias();
                break;
            case R.id.buttonSalir:
                finish();
                break;
        }
    }

    private void lanzaAltas() {
        Intent i = new Intent(this, Altas.class);
        startActivity(i);
    }

    private void lanzaPreferencias() {
        Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
