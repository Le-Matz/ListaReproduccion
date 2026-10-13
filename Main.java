import java.util.NoSuchElementException;
//para tirar errores como los pros
import java.util.Scanner;

// Menu de interaccion (No conoce los enlaces, solo usa la interfaz publica de la lista)
public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final ListaReproduccion LISTA = new ListaReproduccion();
    private static int numeroReproduccion;  // solo para numerar la salida de la simulacion

// Odio el unicode, pero espero que al menos el texto de terminal lo pueda escribir de forma decente sin que haga malabares con los acentos

    public static void main(String[] args) {
        try {
            int opcion;
            do {
                mostrarMenu();
                Integer o = Entrada.parsePositivo(leer("Elige una opción: "));
                opcion = (o == null) ? -1 : o;
                ejecutar(opcion);
            } while (opcion != 13);
        } catch (NoSuchElementException e) {
            System.out.println("\nSe terminó la entrada de datos. Fin del programa.");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== LISTA DE REPRODUCCIÓN =====");
        System.out.println(" 1. Agregar canción al inicio");
        System.out.println(" 2. Agregar canción al final");
        System.out.println(" 3. Buscar canción por identificador");
        System.out.println(" 4. Seleccionar canción por identificador");
        System.out.println(" 5. Eliminar canción por identificador");
        System.out.println(" 6. Eliminar la canción actual");
        System.out.println(" 7. Consultar la canción actual");
        System.out.println(" 8. Avanzar a la siguiente");
        System.out.println(" 9. Retroceder a la anterior");
        System.out.println("10. Mostrar la lista completa (ambos sentidos)");
        System.out.println("11. Consultar la cantidad de canciones");
        System.out.println("12. Simular la reproducción de k canciones");
        System.out.println("13. Salir");
    }

    private static void ejecutar(int opcion) {
        switch (opcion) {
            case 1: agregar(true); break;
            case 2: agregar(false); break;
            case 3: buscar(); break;
            case 4: seleccionar(); break;
            case 5: eliminarPorId(); break;
            case 6: eliminarActual(); break;
            case 7: consultarActual(); break;
            case 8: mover(true); break;
            case 9: mover(false); break;
            case 10: mostrarLista(); break;
            case 11: System.out.println("Cantidad de canciones: " + LISTA.cantidad()); break;
            case 12: simular(); break;
            case 13: System.out.println("Ayos"); break;
            default: System.out.println("Opción inválida. Elige un número del 1 al 13.");
        }
    }

    //lectura

    private static String leer(String mensaje) {
        System.out.print(mensaje);
        return SC.nextLine();
        //NoSuchElementException al llegar al fin de la entrada
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            String t = Entrada.parseTexto(leer(mensaje));
            if (t != null) return t;
            System.out.println("  El texto no puede estar vacío. Intenta de nuevo.");
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            Integer v = Entrada.parsePositivo(leer(mensaje));
            if (v != null) return v;
            System.out.println("  Debes escribir un número entero mayor que cero.");
        }
    }

    //operaciones 

    private static void agregar(boolean alInicio) {
        String titulo = leerTexto("Título: ");
        String artista = leerTexto("Artista: ");
        int duracion = leerEnteroPositivo("Duración en segundos (> 0): ");
        boolean eraVacia = LISTA.estaVacia();
        Cancion c = alInicio
                ? LISTA.agregarAlInicio(titulo, artista, duracion)
                : LISTA.agregarAlFinal(titulo, artista, duracion);
        System.out.println("Canción agregada " + (alInicio ? "al inicio" : "al final") + ": " + c);
        if (eraVacia) System.out.println("Es la primera canción, por lo que queda como actual.");
    }

    private static boolean hayCanciones() {
        if (LISTA.estaVacia()) {
            System.out.println("La lista está vacía.");
            return false;
        }
        return true;
    }

    private static void buscar() {
        if (!hayCanciones()) return;
        int id = leerEnteroPositivo("Identificador a buscar: ");
        Cancion c = LISTA.buscar(id);
        System.out.println(c != null ? "Encontrada: " + c : "No existe una canción con ID " + id + ".");
    }

    private static void seleccionar() {
        if (!hayCanciones()) return;
        int id = leerEnteroPositivo("Identificador a seleccionar: ");
        System.out.println(LISTA.seleccionar(id)
                ? "Canción actual: " + LISTA.cancionActual()
                : "No existe una canción con ID " + id + ". La canción actual no cambió.");
    }

    private static void eliminarPorId() {
        if (!hayCanciones()) return;
        int id = leerEnteroPositivo("Identificador a eliminar: ");
        Cancion previa = LISTA.buscar(id);
        if (previa == null || !LISTA.eliminarPorId(id)) {
            System.out.println("No existe una canción con ID " + id + ". La lista no cambió.");
            return;
        }
        System.out.println("Eliminada: " + previa);
        informarActual();
    }

    private static void eliminarActual() {
        if (!hayCanciones()) return;
        System.out.println("Eliminada: " + LISTA.eliminarActual());
        informarActual();
    }

    private static void informarActual() {
        Cancion a = LISTA.cancionActual();
        System.out.println(a == null ? "La lista quedó vacía." : "Canción actual: " + a);
    }

    private static void consultarActual() {
        if (!hayCanciones()) return;
        System.out.println("Canción actual: " + LISTA.cancionActual());
    }

    private static void mover(boolean adelante) {
        if (!hayCanciones()) return;
        Cancion c = adelante ? LISTA.avanzar() : LISTA.retroceder();
        System.out.println((adelante ? "Siguiente" : "Anterior") + " -> " + c);
    }

    private static void mostrarLista() {
        if (!hayCanciones()) return;
        Cancion a = LISTA.cancionActual();
        System.out.println("--- De inicio a fin ---");
        LISTA.recorrerAdelante(c -> System.out.println((c == a ? " ▶ " : "   ") + c));
        System.out.println("--- De fin a inicio ---");
        LISTA.recorrerAtras(c -> System.out.println((c == a ? " ▶ " : "   ") + c));
        System.out.println("(▶ marca la canción actual)");
    }

    private static void simular() {
        if (!hayCanciones()) return;
        int k = leerEnteroPositivo("¿Cuántas canciones reproducir? (k > 0): ");
        numeroReproduccion = 0;
        LISTA.reproducir(k, c -> System.out.println("  ♪ " + (++numeroReproduccion) + ". Reproduciendo " + c));
        System.out.println("Siguiente a reproducir: " + LISTA.cancionActual());
    }
}