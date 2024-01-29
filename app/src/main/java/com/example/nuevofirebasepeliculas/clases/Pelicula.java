package com.example.nuevofirebasepeliculas.clases;

import java.io.Serializable;
import java.util.Objects;

public class Pelicula implements Serializable {
    //atributos----------------------
    private String titulo, genero;

    //constructores------------------
    public Pelicula(String titulo, String genero) {
        this.titulo = titulo;
        this.genero = genero;
    }

    public Pelicula() {
        this.titulo = "Sin titulo";
        this.genero = "Sin genero";
    }
    //getters---------------------
    public String getTitulo() {
        return titulo;
    }
    public String getGenero() {
        return genero;
    }

    //setters----------------------
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    //metodos-----------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pelicula)) return false;
        Pelicula pelicula = (Pelicula) o;
        return titulo.equals(pelicula.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
}
