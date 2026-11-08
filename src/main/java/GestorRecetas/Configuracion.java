package GestorRecetas;

public class Configuracion {
    private int  UltimaOpcion;
    private int TotalRecetas;

    public Configuracion(int totalRecetas, int ultimaOpcion) {
        // Constructor vacío necesario para Gson
    }

    public int getUltimaOpcion() {
        return UltimaOpcion;
    }

    public void setUltimaOpcion(int ultimaOpcion) {
        UltimaOpcion = ultimaOpcion;
    }

    public int getTotalRecetas() {
        return TotalRecetas;
    }

    public void setTotalRecetas(int totalRecetas) {
        TotalRecetas = totalRecetas;
    }

    @Override
    public String toString() {
        return " La última opción es =" + UltimaOpcion +
                ", y el total de recetas corresponde a =" + TotalRecetas;
    }
}

