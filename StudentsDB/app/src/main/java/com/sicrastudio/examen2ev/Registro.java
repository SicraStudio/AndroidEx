package com.sicrastudio.examen2ev;

import android.app.Activity;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Registro extends Activity {

    private TextView tv_resultado;
    private Button btn_volver, btn_eliminar;

    private Universitario univ;
    private UniversitarioDBAdapter uniAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        tv_resultado = (TextView) findViewById(R.id.txtResultado);
        btn_volver = (Button) findViewById(R.id.btnVolver);
        btn_eliminar = (Button) findViewById(R.id.btnDelete);

        uniAdapter = new UniversitarioDBAdapter(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            univ = (Universitario)bundle.getSerializable("universitario");
            tv_resultado.setText("ID: [" + (int)univ.getId() + "]\nNombre: " + univ.getNombre() + "\n" +
                    "F. Nacimiento: " + univ.getFechaNac() + "\nPaís: " + univ.getPais() + "\n" +
                    "Sexo: " + univ.getSexo() + "\nHabla inglés: " + univ.getIngles());
        } else {
            Toast.makeText(this, "Error: no se han recibido datos válidos", Toast.LENGTH_SHORT).show();
        }

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (univ != null) {
                    try {
                        uniAdapter.abrir();
                        int res = uniAdapter.borraUniv(univ);

                        if (res > 0) {
                            tv_resultado.setText("...");
                            Toast.makeText(Registro.this, "Registro Eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Registro.this, "Error: no se ha borrado nada", Toast.LENGTH_SHORT).show();
                        }
                        uniAdapter.cerrar();
                        setResult(RESULT_OK);
                        finish();
                    } catch (SQLiteException ex) {
                        Log.e("ERROR SQLite", "Error acceso a la BBDD: " + ex.getMessage());
                    }
                } else {
                    Toast.makeText(Registro.this, "Error: Univ nulo", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registro, menu);
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
