package com.sicrastudio.sicra.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Asteroides extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asteroides);

        TextView txtTitulo = (TextView) findViewById(R.id.textTitulo);
        Button bt_jugar = (Button) findViewById(R.id.botonJugar);
        Button bt_config = (Button) findViewById(R.id.botonConfig);
        Button bt_acercade = (Button) findViewById(R.id.botonAcercaDe);
        Button bt_salir = (Button) findViewById(R.id.botonSalir);

        Typeface fontTitulo = Typeface.createFromAsset(getAssets(), "space age.ttf");
        Typeface fontBotones = Typeface.createFromAsset(getAssets(), "NEUROPOL.ttf");

        txtTitulo.setTypeface(fontTitulo);
        bt_jugar.setTypeface(fontBotones);
        bt_config.setTypeface(fontBotones);
        bt_acercade.setTypeface(fontBotones);
        bt_salir.setTypeface(fontBotones);

        bt_acercade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Asteroides.this, AcercaDe.class);
                startActivity(intent);
            }
        });

        bt_salir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mostrarPreferencias();
                finish();
            }
        });

        bt_jugar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Asteroides.this, Juego.class);
                startActivity(intent);
            }
        });

        bt_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Asteroides.this, Preferencias.class);
                startActivity(intent);
            }
        });
    }

    public void mostrarPreferencias() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "Música: " + pref.getBoolean("musica", true) + ". Gráficos: " + pref.getString("preguntas", "?");
        Toast.makeText(
                this,
                s,
                Toast.LENGTH_SHORT
        ).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asteroides, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_acercade:
                intent = new Intent(this, AcercaDe.class);
                startActivity(intent);
                break;
            case R.id.action_config:
                intent = new Intent(this, Preferencias.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
