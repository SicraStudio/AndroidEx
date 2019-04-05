package com.sicrastudio.examen2ev;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Registros extends Activity {

    private ListView               lista;
    private ArrayAdapter<String>   adapter;
    private UniversitarioDBAdapter uniAdapter;
    private List<String>           datos;
    private List<Universitario>    listaUNIV;
    private static int CODIGO_ACTIVIDAD = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registros);

        lista = (ListView) findViewById(R.id.lstRegistros);
        datos = new ArrayList<>();
        listaUNIV = new ArrayList<>();

        try {
            uniAdapter = new UniversitarioDBAdapter(this);
        } catch (SQLiteException ex) {
            Log.e("ERROR SQLite", "Error acceso a la BBDD: " + ex.getMessage());
        }

        this.llenaLista();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Registros.this, Registro.class);
                intent.putExtra("universitario", listaUNIV.get(position));
                startActivityForResult(intent, CODIGO_ACTIVIDAD);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // volvemos a llenar la lista si se ha eliminado el registro mostrado
        if (requestCode == CODIGO_ACTIVIDAD) {
            if (resultCode == Activity.RESULT_OK) {
                lista.invalidateViews();
                datos = new ArrayList<>();
                listaUNIV = new ArrayList<>();
                this.llenaLista();
            }
        }
    }

    private void llenaLista() {
        try {
            uniAdapter.abrir();
            Cursor c = uniAdapter.obtenerUniversitarios();

            if (c.moveToFirst()) {
                String fila;
                do {
                    fila = "ID: [" + c.getLong(0) + "]\nNombre: " + c.getString(1) + "\n" +
                            "F. Nacimiento: " + c.getString(2) + "\nPaís: " + c.getString(3) + "\n" +
                            "Sexo: " + c.getString(4) + "\nHabla inglés: " + c.getString(5);
                    datos.add(fila);
                    listaUNIV.add(
                            new Universitario(
                                    (int)c.getLong(0),
                                    c.getString(1),
                                    c.getString(2),
                                    c.getString(3),
                                    c.getString(4),
                                    c.getString(5)
                            )
                    );
                } while (c.moveToNext());
                c.close();
                adapter = new ArrayAdapter<String>(
                        Registros.this,
                        android.R.layout.simple_list_item_1,
                        datos
                );

                lista.setAdapter(adapter);
            }
            uniAdapter.cerrar();
        } catch (SQLiteException ex) {
            Log.e("ERROR SQLite", "Error acceso a la BBDD: " + ex.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registros, menu);
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
