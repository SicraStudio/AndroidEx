package com.sicrastudio.examen2ev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UniversitarioDBAdapter {

    //Campos de la BD
    public static final String CAMPO_ID          = "id";
    public static final String CAMPO_NOMBRE      = "Nombre";
    public static final String CAMPO_FECHANAC    = "FechaNac";
    public static final String CAMPO_PAIS        = "Pais";
    public static final String CAMPO_SEXO        = "Sexo";
    public static final String CAMPO_INGLES      = "Ingles";

    private static final String TABLA_BD = "Universitario";
    private Context mContexto;
    private SQLiteDatabase db;
    private SQLiteHelper helper;

    //constructor
    public UniversitarioDBAdapter(Context context) { mContexto = context; }

    //Abrir la base de datos
    public UniversitarioDBAdapter abrir() throws SQLException {
        helper = new SQLiteHelper(mContexto);
        db = helper.getWritableDatabase();
        return this;
    }

    //cerrar
    public void cerrar() {
        helper.close();
    }

    //insertar un universitario
    public long creaUniv(Universitario u) {
        ContentValues initialValues = crearCV(u);
        return db.insert(TABLA_BD,null,initialValues);
    }

    private ContentValues crearCV (Universitario u) {
        ContentValues values = new ContentValues();
        values.put(CAMPO_NOMBRE, u.getNombre());
        values.put(CAMPO_FECHANAC, u.getFechaNac());
        values.put(CAMPO_PAIS, u.getPais());
        values.put(CAMPO_SEXO, u.getSexo());
        values.put(CAMPO_INGLES, u.getIngles());

        return values;
    }

    // consulta a todos los UNIV
    public Cursor obtenerUniversitarios() {
        return db.query(TABLA_BD,new String[] {CAMPO_ID,CAMPO_NOMBRE,CAMPO_FECHANAC,CAMPO_PAIS,CAMPO_SEXO,CAMPO_INGLES},null,null,null,null,null);
    }

    // borrar un UNIV segun su id
    public int borraUniv(Universitario u) {
        return db.delete(TABLA_BD, "id="+u.getId(), null);
    }

}
