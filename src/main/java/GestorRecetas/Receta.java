package GestorRecetas;

import java.io.Serializable;

public class Receta implements Serializable {

    private final int id;
    private final String nombre;
    private int tiempoPreparacion;
    private  String categoria;

    public Receta(int tiempoPreparacion, String categoria, String nombre, int id) {
        this.tiempoPreparacion = tiempoPreparacion;
        this.categoria = categoria;
        this.nombre = nombre;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        categoria = categoria;
    }


    @Override
    public String toString() {
        return "La receta con nombre " + nombre +
                " y cuyo id es " + id +
                ", tiene un tiempo de preparación de " + tiempoPreparacion +
                " minutos, y su categoría correspondiente es: " + categoria ;
    }

}