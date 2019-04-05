package com.sicrastudio.examen2ev;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sicra on 25/02/15.
 */
public class Universitario implements Serializable {

    private int id;
    private String nombre;
    private String fechaNac;
    private String pais;
    private String sexo;
    private String ingles;

    public Universitario() {
    }

    public Universitario(String nombre, String pais, String sexo, String fechaNac, String ingles) {
        this.nombre = nombre;
        this.pais = pais;
        this.sexo = sexo;
        this.fechaNac = fechaNac;
        this.ingles = ingles;
    }

    public Universitario(int id, String nombre, String fechaNac, String pais, String sexo, String ingles) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNac = fechaNac;
        this.pais = pais;
        this.sexo = sexo;
        this.ingles = ingles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getIngles() {
        return ingles;
    }

    public void setIngles(String ingles) {
        this.ingles = ingles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
