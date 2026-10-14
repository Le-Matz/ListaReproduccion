Gestor de listas de reproducción con lista doblemente enlazada circular (Diseñado para funcionar a partir de JAVA 8)

    1. Estructura del proyecto

`Cancion.java`            Dato inmutable: id, título, artista, duración (> 0). Valida en el constructor
`Nodo.java`               Nodo con `dato`, `anterior` y `siguiente`
`ListaReproduccion.java`  Lista circular propia: `inicio`, `fin`, `actual`, `tamanio` y generador de ids. No imprime nada
`Entrada.java`            Validación de entradas (enteros positivos, texto no vacío)
`Main.java`               Menú de consola (las 13 opciones). Solo usa la interfaz pública de la lista
`PruebasLista.java`       37 pruebas reproducibles con estado inicial, operación, esperado, obtenido, cantidad e integridad

    No se usa `LinkedList`, `ArrayList`, arreglos ni otras colecciones para almacenar canciones
    Solo se emplean `Scanner` (lectura) y `Consumer` (acción aplicada a cada canción en recorridos y reproducción)

    2. Para compilar y ejecutar:
Una vez tengas una copia de los archivos en tu directorio de destino preferido,:
  Compila con:
    javac -d out Main.java
    javac -d out PruebasLista.java
    
  Para poder ver bien los acentos, y el símbolo "▶"
    java -Dstdout.encoding=UTF-8 -cp out Main            "El menú del programa"
    java -Dstdout.encoding=UTF-8 -cp out PruebasLista    "El entorno de pruebas" (código de salida 1 si alguna falla)

    3. Creado por:

Bailón Solís Luis Andrés                 (Documentación e investigación)
Calderón Curiel Erick Sebastián          (Control de calidad)
Hernández Terrazas Samuel Kaleb Farhid   (Pruebas y Videodocumentacion)
Romero Rodríguez Leonardo Maximiliano    (Programador principal)
