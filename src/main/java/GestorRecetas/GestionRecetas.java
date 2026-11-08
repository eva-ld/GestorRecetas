
package GestorRecetas;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Imports necesarios para el JSON
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GestionRecetas {
    private static final String RUTA_FICHERO = "C:\\Users\\evalo\\Desktop\\Programación\\GestorRecetas\\src\\main\\java\\Ficheros\\";
    private static final String FICHERO_BINARIO = "recetas.dat";
    private static final String FICHERO_JSON = "configuracion.json";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        File ficheroRecetas = new File(RUTA_FICHERO + FICHERO_BINARIO);
        if (!ficheroRecetas.exists()) {
            System.out.println("El fichero no existe. \n GENERANDO AUTOMÁTICAMENTE UNO");
            crearCatalogoInicial();
        } else {
            System.out.println("Fichero receta encontrado.");
        }

        // Recopilamos toda la configuración guardada
        Configuracion configuracion = cargarConfiguracion();
        System.out.println(configuracion);

        int opcion = -1;
        do {
            System.out.println("\nGESTOR DE RECETAS EVA LOZANO");
            System.out.println("    1. Mostrar todas las recetas");
            System.out.println("    2. Buscar receta por identificador");
            System.out.println("    3. Modificar tiempo de preparación");
            System.out.println("    4. Añadir nueva receta");
            System.out.println("    5. Exportar catálogo a XML");
            System.out.println("    0. Salir");
            System.out.print("Seleccione una opción: \n");

            if (sc.hasNextInt()) {
                opcion = sc.nextInt();
                sc.nextLine(); // Limpiar buffer

                // Guardamos la última opción utilizada, a menos que esta sea salir, ya que no nos interesa
                if (opcion != 0) {
                    configuracion.setUltimaOpcion(opcion);
                }

                switch (opcion) {
                    case 1:
                        mostrarRecetas(); // Sin parámetros
                        break;
                    case 2:
                        buscarReceta(sc);
                        break;
                    case 3:
                        modificarTiempo(sc);
                        break;
                    case 4:
                        anadirReceta(sc);
                        break;
                    case 5:
                        exportarXML();
                        break;
                    case 0:
                        System.out.println("Saliendo de la aplicación");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } else {
                System.out.println("Por favor, introduzca un número válido.");
                sc.nextLine();
            }
        } while (opcion != 0);

        // JSON: Actualizamos el total de recetas y guardamos la configuración
        configuracion.setTotalRecetas(cargarRecetasDelFichero().size());
        guardarConfiguracion(configuracion);

        sc.close();
    }

    // Lee todas las recetas del fichero y las mete en una lista
    private static ArrayList<Receta> cargarRecetasDelFichero() {
        ArrayList<Receta> lista = new ArrayList<>();
        File fichero = new File(RUTA_FICHERO + FICHERO_BINARIO);

        if (!fichero.exists()) {
            return lista;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero))) {
            do {
                Receta r = (Receta) ois.readObject();
                lista.add(r);
            } while (true);
        } catch (EOFException e) {
            // Fin del fichero
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
        return lista;
    }

    // Guarda la lista completa de recetas en el fichero binario
    private static void guardarEnFichero(ArrayList<Receta> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_FICHERO + FICHERO_BINARIO))) {
            for (Receta r : lista) {
                oos.writeObject(r);
            }
        } catch (IOException e) {
            System.out.println("Error al guardar en el fichero: " + e.getMessage());
        }
    }

    // Crear el catálogo inicial utilizando la lista que creo en el arrayList
    private static void crearCatalogoInicial() {
        File directorio = new File(RUTA_FICHERO);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        ArrayList<Receta> recetasIniciales = new ArrayList<>();

        recetasIniciales.add(new Receta(50, "Postre", "Bizcocho de yogur", 1));
        recetasIniciales.add(new Receta(15, "Aperitivo", "Pan de gambas", 2));
        recetasIniciales.add(new Receta(25, "Principal", "Brochetas con pimiento", 3));

        guardarEnFichero(recetasIniciales);

        System.out.println("Catálogo inicial creado con éxito.");
    }

    // Mostrar todas las recetas
    protected static void mostrarRecetas() {
        ArrayList<Receta> lista = cargarRecetasDelFichero();
        if (lista.isEmpty()) {
            System.out.println("No hay recetas en el catálogo.");
            return;
        }

        System.out.println("\n LISTA DE RECETAS        ");
        for (Receta r : lista) System.out.println("    - " + r);
    }

    // Buscar receta por identificador
    private static void buscarReceta(Scanner sc) {

        System.out.print("Vamos a buscar una receta a través de su ID. \n Introduce el ID de la receta que desee: ");
        int id = sc.nextInt();

        ArrayList<Receta> lista = cargarRecetasDelFichero();

        boolean encontrada = false;

        for (Receta r : lista) {
            if (r.getId() == id) {
                System.out.println("La receta correspondiente es: ");
                System.out.print("      -" + r.getNombre() + " tiene un tiempo de preparación de " + r.getTiempoPreparacion() + " minutos y pertenece a la categoría " + r.getCategoria() + "\n");
                encontrada = true;
            }
        }
        if (!encontrada) {
            System.out.println("No se ha encontrado ninguna receta con ese ID.");
        }
    }

    // OPCIÓN 3: Modificar el tiempo de preparación
    private static void modificarTiempo(Scanner sc) {
        System.out.println("Vamos a modificar el tiempo de una receta, para ello debera de introducir su ID");
        int id = sc.nextInt();
        ArrayList<Receta> lista = cargarRecetasDelFichero();
        boolean encontrada = false;
        for (Receta r : lista) {
            if (r.getId() == id) {
                System.out.println("El tiempo establecido para esta receta (" + r.getNombre() + ") es: " + r.getTiempoPreparacion());
                encontrada = true;

                System.out.println("Introduce el nuevo tiempo deseado");
                int NuevotiempoPreparacion = sc.nextInt();
                r.setTiempoPreparacion(NuevotiempoPreparacion);
            }
        }
        if (encontrada) {
            guardarEnFichero(lista);
            System.out.println("Tiempo modificado correctamente.");
        } else {
            System.out.println("No se ha encontrado ninguna receta con ese ID.");
        }
    }

    // Añadir nueva receta
    // OPCIÓN 4: Añadir una receta nueva
    private static void anadirReceta(Scanner sc) {

        ArrayList<Receta> lista = cargarRecetasDelFichero();
        System.out.println("\nVamos a añadir una nueva receta.");
        int id;
        boolean existe;

        // Comprobamos que el ID no esté repetido
        do {
            existe = false;

            System.out.print("Introduce el ID de la receta: ");
            id = sc.nextInt();
            sc.nextLine();

            for (Receta r : lista) {
                if (r.getId() == id) {
                    existe = true;
                    System.out.println("Ya existe una receta con ese ID. Introduce otro.");
                    break;
                }
            }

        } while (existe);

        System.out.print("Introduce el nombre de la receta: ");
        String nombre = sc.nextLine();

        System.out.print("Introduce el tiempo de preparación en minutos: ");
        int tiempoPreparacion = sc.nextInt();
        sc.nextLine();

        System.out.print("Introduce la categoría: ");
        String categoria = sc.nextLine();

        Receta nuevaReceta = new Receta(tiempoPreparacion, categoria, nombre, id);// Creamos el nuevo objeto
        lista.add(nuevaReceta); // Añadimos la receta a la lista
        guardarEnFichero(lista); // Guardamos todas las recetas en el fichero
        System.out.println("Receta añadida correctamente.");
    }

    // Exportar catálogo a XML con DOM
    private static void exportarXML() {

        try {
            // Comprobamos si el directorio existe
            File directorio = new File(RUTA_FICHERO);
            if (!directorio.exists()) {
                directorio.mkdir();
            }

            // Cargar las recetas del fichero binario
            ArrayList<Receta> lista = cargarRecetasDelFichero();

            // Configurar y crear el documento XML vacío
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();

            // Crear el elemento raíz <recetas>
            Element raiz = documento.createElement("recetas");
            documento.appendChild(raiz);

            // Recorrer las recetas para crear cada <receta>
            for (Receta r : lista) {

                Element receta = documento.createElement("receta");
                raiz.appendChild(receta);

                // Crear la etiqueta <id>
                Element id = documento.createElement("id");
                id.setTextContent(String.valueOf(r.getId()));
                receta.appendChild(id);

                // Etiqueta <nombre>
                Element nombre = documento.createElement("nombre");
                nombre.setTextContent(r.getNombre());
                receta.appendChild(nombre);

                // Etiqueta  <tiempoPreparacion>
                Element tiempo = documento.createElement("tiempoPreparación");
                tiempo.setTextContent(String.valueOf(r.getTiempoPreparacion()));
                receta.appendChild(tiempo);

                // Etiqueta <categoria>
                Element categoria = documento.createElement("categoria");
                categoria.setTextContent(r.getCategoria());
                receta.appendChild(categoria);
            }

            // Guardar el documento XML en un fichero
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(documento),
                    new StreamResult(new File(RUTA_FICHERO + "recetas.xml")));

            System.out.println("Fichero recetas.xml creado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al crear el documento XML: " + e.getMessage());
        }
    }

    // Cargar la configuración desde el fichero JSON
    private static Configuracion cargarConfiguracion() {

        File fichero = new File(RUTA_FICHERO + FICHERO_JSON);

        if (!fichero.exists()) {
            return new Configuracion(0, 0);
        }

        try (FileReader reader = new FileReader(fichero)) {
            Gson gson = new Gson();
            Configuracion configuracion = gson.fromJson(reader, Configuracion.class);

            if (configuracion == null) {
                return new Configuracion(0, 0);
            }

            return configuracion;

        } catch (Exception e) {
            System.out.println("Error al leer la configuración: " + e.getMessage());
            return new Configuracion(0, 0);
        }
    }

    // Guardar la configuración en el fichero JSON
    private static void guardarConfiguracion(Configuracion configuracion) {

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            FileWriter writer = new FileWriter(RUTA_FICHERO + FICHERO_JSON);
            gson.toJson(configuracion, writer);
            writer.close();

            System.out.println("Configuración guardada correctamente.");

        } catch (Exception e) {
            System.out.println("Error al guardar la configuración: " + e.getMessage());
        }
    }
}
