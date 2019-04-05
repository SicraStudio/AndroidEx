package com.sicrastudio.examen2ev;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    //nombre de la base de datos
    private static final String __DATABASE = "dbUniversidad";
    //versión de la base de datos
    private static final int __VERSION = 3;
    //nombre tabla y campos de tabla
    public final String __tabla__ = "Universitario";
    public final String __campo_id = "id";
    public final String __campo_nombre = "Nombre";
    public final String __campo_fechanac = "FechaNac";
    public final String __campo_pais = "Pais";
    public final String __campo_sexo = "Sexo";
    public final String __campo_ingles = "Ingles";
    //Instrucción SQL para crear las tablas
	/*
	 * CREATE TABLE "Universitario" (
	                "id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ,
	                "Nombre" TEXT, "FechaNac" DATETIME, "Pais" TEXT, "Sexo" TEXT, "Ingles" TEXT )
	 * */
    private final String sql = "CREATE TABLE " + __tabla__ + " ( " +
            __campo_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            __campo_nombre + " TEXT NULL, " + __campo_fechanac + " TEXT, " + __campo_pais + " TEXT NULL, " +
            __campo_sexo + " TEXT NULL, " + __campo_ingles + " TEXT NULL " +
            " )";

    /**
     * Constructor de clase
     * */
    public SQLiteHelper(Context context) {
        super( context, __DATABASE, null, __VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( sql );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db,  int oldVersion, int newVersion ) {
        if ( newVersion > oldVersion )
        {
            //elimina tabla
            db.execSQL( "DROP TABLE IF EXISTS " + __tabla__ );
            //y luego creamos la nueva tabla
            db.execSQL( sql );
        }
    }

}
