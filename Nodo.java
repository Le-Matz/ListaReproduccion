/*
  Pos es el nodo de la cancion, que mas?
  Bueno, si, lo de siempre, solo se puede acceder a los enlaces desde dentro del paquete,
  para que la lista pueda manipularlos pero no el resto del mundo. ah, y el nodo es inmutable,
  no se puede cambiar la cancion que contiene, naturalmente por lo mismo

  Solo a traves de ListaReproduccion y el entorno de pruebas se puede acceder (espero recordar incluirlo en el repositorio)
 */
class Nodo {
    private final Cancion dato;
    Nodo anterior;
    Nodo siguiente;

    Nodo(Cancion dato) {
        this.dato = dato;
    }

    Cancion getDato() {
        return dato;
    }
}
