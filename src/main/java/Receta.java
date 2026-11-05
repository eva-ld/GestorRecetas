import java.io.Serializable;

public class Receta implements Serializable {

    private final int id;
    private final String nombre;
    private int tiempoPreparacion;
    private  String Categoria;

    public Receta(int tiempoPreparacion, String categoria, String nombre, int id) {
        this.tiempoPreparacion = tiempoPreparacion;
        Categoria = categoria;
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
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }


    @Override
    public String toString() {
        return "La receta con nombre" + nombre + '\'' +
                "y cuyo id es  " + id +
                ",tiene un tiempo de preparacion=" + tiempoPreparacion +
                ", y pertemece a la categoria= '" + Categoria + "'";
    }

}