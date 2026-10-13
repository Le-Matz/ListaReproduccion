//Representa una cancion y es inmutable (una vez creada no cambia), lo que evita que el contenido de un nodo se altere por accidente (o malicia)
public class Cancion {
    private final int id;
    private final String titulo;
    private final String artista;
    private final int duracionSegundos;


    // se avienta el poderosisimo IllegalArgumentException si el titulo o el artista son nulos o vacíos o si la duración no es mayor que cero
    
    public Cancion(int id, String titulo, String artista, int duracionSegundos) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (artista == null || artista.trim().isEmpty()) {
            throw new IllegalArgumentException("El artista no puede estar vacío.");
        }
        if (duracionSegundos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.artista = artista.trim();
        this.duracionSegundos = duracionSegundos;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public int getDuracionSegundos() { return duracionSegundos; }

    @Override
    public String toString() {
        return String.format("[ID %d] \"%s\" - %s (%d s, %d:%02d)",
                id, titulo, artista, duracionSegundos,
                duracionSegundos / 60, duracionSegundos % 60);
    }
}
