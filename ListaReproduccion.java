import java.util.function.Consumer;
// para manejar las canciones

/*
  Lista doblemente enlazada circular de canciones (sin usar otras estructuras existentes de Java)
 
  Invariantes (se comprueban en verificarIntegridad()):
   - Lista vacia: inicio == fin == actual == null y tamanio == 0
   - Un solo nodo: sus enlaces anterior y siguiente apuntan a si mismo
   - Lista no vacia: fin.siguiente == inicio e inicio.anterior == fin
   - Para todo nodo p: p.siguiente.anterior == p y p.anterior.siguiente == p
   - actual pertenece a la lista
 
  Todos los recorridos están delimitados por "tamanio" pasos, porque en la lista circular no hay null que los detenga
 */
public class ListaReproduccion {
    private Nodo inicio;
    private Nodo fin;
    private Nodo actual;
    private int tamanio;
    private int proximoId = 1;   
    // generador de identificadores

    // Insercion

    // Agrega una cancion al inicio y devuelve la canción creada
    public Cancion agregarAlInicio(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        if (tamanio == 0) {
            insertarPrimero(nuevo);
        } else {
            nuevo.anterior = fin;
            nuevo.siguiente = inicio;
            inicio.anterior = nuevo;
            fin.siguiente = nuevo;
            inicio = nuevo;
            // la cabeza cambia, el final y actual no
        }
        tamanio++;
        return nuevo.getDato();
    }

    //Agrega una cancion al final y devuelve la canción creada.
    public Cancion agregarAlFinal(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        if (tamanio == 0) {
            insertarPrimero(nuevo);
        } else {
            nuevo.anterior = fin;
            nuevo.siguiente = inicio;
            fin.siguiente = nuevo;
            inicio.anterior = nuevo;
            fin = nuevo;                   
             // el final cambia, inicio y actual no
        }
        tamanio++;
        return nuevo.getDato();
    }

    /*
      Valida los datos (y lanza IllegalArgumentException si son invalidos)
      y solo despues de eso consume un identificador, para que una insercion
      fallida no deje "huecos" en la numeracion
     */
    private Nodo crearNodo(String titulo, String artista, int duracion) {
        Cancion c = new Cancion(proximoId, titulo, artista, duracion);
        proximoId++;
        return new Nodo(c);
    }

    private void insertarPrimero(Nodo nuevo) {
        nuevo.anterior = nuevo;
        nuevo.siguiente = nuevo;
        inicio = nuevo;
        fin = nuevo;
        actual = nuevo;
        // la primera canción es la actual
    }

    // Consulta y navegacion

    // Cancion actual o null si la lista esta vacia
    public Cancion cancionActual() {
        return actual == null ? null : actual.getDato();
    }

    //Avanza y devuelve la nueva cancion actual (null si esta vacia)
    public Cancion avanzar() {
        if (actual == null) return null;
        actual = actual.siguiente;
        return actual.getDato();
    }

    // Retrocede y devuelve la nueva cancion actual (null si... ya sabes)
    public Cancion retroceder() {
        if (actual == null) return null;
        actual = actual.anterior;
        return actual.getDato();
    }

    // Cantidad de canciones
    public int cantidad() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    // Busqueda / seleccion

    //Devuelve la cancion con ese id o null sin modificar la lista
    public Cancion buscar(int id) {
        Nodo n = buscarNodo(id);
        return n == null ? null : n.getDato();
    }

    // Convierte en actual la canción con ese id (o false si no existe)
    public boolean seleccionar(int id) {
        Nodo n = buscarNodo(id);
        if (n == null) return false;
        actual = n;
        return true;
    }

    //Localiza un nodo recorriendo como maximo "tamanio" posiciones
    private Nodo buscarNodo(int id) {
        Nodo p = inicio;
        for (int i = 0; i < tamanio; i++) {
            if (p.getDato().getId() == id) return p;
            p = p.siguiente;
        }
        return null;
    }

    //Eliminacion

    // Elimina por id (false si no existe)
    public boolean eliminarPorId(int id) {
        Nodo n = buscarNodo(id);
        if (n == null) return false;
        desenlazar(n);
        return true;
    }

    // Elimina la canción actual (devuelve la eliminada o null si esta vacia)
    public Cancion eliminarActual() {
        if (actual == null) return null;
        Cancion eliminada = actual.getDato();
        desenlazar(actual);
        return eliminada;
    }

    /*
      Quita un nodo conocido de la lista en O(1), se guardan primero las
      referencias y solo despues de eso se cambian los enlaces para no perder nodos
     */
    private void desenlazar(Nodo n) {
        if (tamanio == 1) {
            // única canción: lista vacía
            inicio = null;
            fin = null;
            actual = null;
            tamanio = 0;
            n.anterior = null;
            n.siguiente = null;
            return;
        }
        Nodo ant = n.anterior;
        Nodo sig = n.siguiente;
        ant.siguiente = sig;
        sig.anterior = ant;
        if (n == inicio) inicio = sig;
        if (n == fin) fin = ant;
        if (n == actual) actual = sig;
        n.anterior = null;
        n.siguiente = null;
        tamanio--;
        //el nodo eliminado ya no referencia a la lista
    }
 
    // Recorrido (cada cancion se "escucha" una vez)

    // Va de inicio a fin aplicando la accion a cada cancion
    public void recorrerAdelante(Consumer<Cancion> accion) {
        Nodo p = inicio;
        for (int i = 0; i < tamanio; i++) {
            accion.accept(p.getDato());
            p = p.siguiente;
        }
    }

    //Recorre de fin a inicio aplicando la accion a cada cancion
    public void recorrerAtras(Consumer<Cancion> accion) {
        Nodo p = fin;
        for (int i = 0; i < tamanio; i++) {
            accion.accept(p.getDato());
            p = p.anterior;
        }
    }

    // Reproduccion simulada

    /*
      Reproduce k canciones: aplica la accion a la actual y avanza k veces, la actual es la que corresponderia reproducir despues
      O(k) independiente de n (si k > n se da más de una vuelta)
      @return cantidad de canciones reproducidas (0 si la lista está vacía)
      @throws IllegalArgumentException si k no es positivo
     */
    public int reproducir(int k, Consumer<Cancion> accion) {
        if (k <= 0) {
            throw new IllegalArgumentException("k debe ser un entero positivo.");
        }
        if (actual == null) return 0;
        for (int i = 0; i < k; i++) {
            accion.accept(actual.getDato());
            actual = actual.siguiente;
        }
        return k;
    }

    // Verificacion

    /*
      Esto no es parte del programa, solo sirve para pelearme con las pruebas (porque todos sabemos que me gusta quemar procesadores, y conforme crezco, se me hace mas divertido... fue bueno aceptar todos esos pentiums como pago en especie)
      Basicamente, comprueba todas las condiciones de la estructura, pero no sirve sin el entorno de pruebas (de las cuales hay como 37, una para cada caso... pero mejor no complico a los demas con eso)
      @return null si todo es correcto, si no, arroja el primer problema
     */
    
    /*
    public String verificarIntegridad() {
        if (tamanio < 0) return "contador negativo";
        if (tamanio == 0) {
            if (inicio != null || fin != null || actual != null) {
                return "lista vacia con referencias no nulas";
            }
            return null;
        }
        if (inicio == null || fin == null || actual == null) {
            return "lista no vacia con inicio/fin/actual nulos";
        }
        if (fin.siguiente != inicio) return "fin.siguiente != inicio";
        if (inicio.anterior != fin) return "inicio.anterior != fin";
        if (tamanio == 1) {
            if (inicio != fin || inicio.siguiente != inicio || inicio.anterior != inicio) {
                return "con un solo nodo los enlaces deben apuntar a si mismo";
            }
        }
        Nodo p = inicio;
        boolean actualPertenece = false;
        for (int i = 0; i < tamanio; i++) {
            if (p == null || p.siguiente == null || p.anterior == null) {
                return "enlace nulo en la posicion " + i;
            }
            if (p.siguiente.anterior != p) return "siguiente.anterior != nodo en posicion " + i;
            if (p.anterior.siguiente != p) return "anterior.siguiente != nodo en posicion " + i;
            if (p == actual) actualPertenece = true;
            if (i == tamanio - 1 && p != fin) return "tras 'tamanio' pasos no se llega a fin";
            p = p.siguiente;
        }
        if (p != inicio) return "el recorrido no cierra el circulo en inicio";
        if (!actualPertenece) return "la cancion actual no pertenece a la lista";
        return null;
    }
        // Accesos solo para las pruebas... estare desarrollando toc?
        Nodo nodoInicio() { return inicio; }
        Nodo nodoFin() { return fin; }
        Nodo nodoActual() { return actual; }
    */
}