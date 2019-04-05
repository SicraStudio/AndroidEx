package com.sicrastudio.examen2ev;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;


public class Altas extends Activity {

    private EditText et_nombre;
    private EditText et_fecha;
    private Spinner sp_paises;
    private RadioButton rb_man;
    private RadioButton rb_woman;
    private RadioGroup rg;
    private CheckBox cb_idioma;

    private Button btnRegistrar;
    private Button btnVerRegs;
    private Button btnVolver;

    private Boolean guardado;
    private Universitario universitario;
    private UniversitarioDBAdapter uniAdapter;
    // Temporal hasta que hagas las PREF
    //private String[] paises = {"España", "Francia", "Italia", "Portugal", "Reino Unido"};
    private String pais;
    private String sexo;
    private String ingles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guardado = false;
        uniAdapter = new UniversitarioDBAdapter(this);
        pais = "Indefinido";
        ingles = "No";

        // Inicializamos
        et_nombre = (EditText) findViewById(R.id.txtName);
        et_fecha = (EditText) findViewById(R.id.editTextFecha);
        sp_paises = (Spinner) findViewById(R.id.sPaises);
        rb_man = (RadioButton) findViewById(R.id.rMan);
        rb_woman = (RadioButton) findViewById(R.id.rWoman);
        cb_idioma = (CheckBox) findViewById(R.id.chIdioma);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnVerRegs = (Button) findViewById(R.id.btnRegistros);
        btnVolver = (Button) findViewById(R.id.btnCancelar);

        String continente = null;
        // Recuperamos el valor de las preferencias
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.i("INFO PREF", "Preferencia: " + preferences.getString("opcCont", "Europa"));
        continente = preferences.getString("opcCont", "Europa"); // dejamos europa como valor por defecto

        ArrayAdapter<CharSequence> adapter = null;

        switch (continente) {
            case "3":
                adapter = ArrayAdapter.createFromResource(this,R.array.eu, android.R.layout.simple_spinner_item);
                break;
            case "2":
                adapter = ArrayAdapter.createFromResource(this,R.array.am, android.R.layout.simple_spinner_item);
                break;
            case "1":
                adapter = ArrayAdapter.createFromResource(this,R.array.as, android.R.layout.simple_spinner_item);
                break;
        }

        if (adapter != null) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_paises.setAdapter(adapter);
        }

        sp_paises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pais = sp_paises.getSelectedItem().toString();

                if (pais.equalsIgnoreCase("eeuu") || pais.equalsIgnoreCase("uk") || pais.equalsIgnoreCase("india") || pais.equalsIgnoreCase("canadá")) {
                    cb_idioma.setChecked(true);
                } else {
                    cb_idioma.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cb_idioma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ingles = "Sí";
                    cb_idioma.setText(ingles);
                } else {
                    ingles = "No";
                    cb_idioma.setText(ingles);
                }
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guardado)
                    finish();
                else if (!guardado || !TextUtils.isEmpty(et_nombre.getText().toString()))
                    Toast.makeText(getBaseContext(),"No ha guardado.. guarde antes", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uniAdapter.abrir();

                    universitario = new Universitario();
                    String nombre = et_nombre.getText().toString();
                    String fecha = et_fecha.getText().toString();
                    //comprobamos que  como minimo le meta el nombre
                    if (TextUtils.isEmpty(et_nombre.getText().toString())) {
                        Toast.makeText(Altas.this, "Debes escribir el Nombre....", Toast.LENGTH_SHORT).show();
                        guardado = false;
                    } else {
                        if (rb_man.isChecked()) {
                            sexo = "Hombre";
                        } else {
                            sexo = "Mujer";
                        }
                        universitario.setNombre(nombre);
                        universitario.setPais(pais);
                        universitario.setSexo(sexo);
                        universitario.setFechaNac(fecha);
                        universitario.setIngles(ingles);

                        if (universitario == null) {
                            Log.e("ERROR", "Uni NULL");
                        } else {
                            universitario.setId((int) uniAdapter.creaUniv(universitario));
                            if (universitario.getId() != -1) {
                                guardado = true;
                                Toast.makeText(Altas.this, "Guardado!!!", Toast.LENGTH_SHORT).show();
                                et_nombre.setText("");
                                et_fecha.setText("");
                                sp_paises.setSelection(0);
                                rb_man.setChecked(true);
                                cb_idioma.setChecked(false);

                                // Pasamos el universitario a la actividad Registro
                                Intent intent = new Intent(Altas.this, Registro.class);
                                intent.putExtra("universitario", universitario);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getBaseContext(), "ERROR No guardado!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    uniAdapter.cerrar();
                } catch (SQLiteException ex) {
                    Log.e("ERROR SQLite", "Error acceso a la BBDD: " + ex.getMessage());
                }
            }
        });

        btnVerRegs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Altas.this, Registros.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
