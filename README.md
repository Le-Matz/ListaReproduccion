Gestor de listas de reproducción con lista doblemente enlazada circular (Diseñado para funcionar a partir de JAVA 8)

## 1. Estructura del proyecto

| Archivo | Que hace? |
|---|---|
| `Cancion.java` | Dato inmutable: id, título, artista, duración (> 0). Valida en el constructor. |
| `Nodo.java` | Nodo con `dato`, `anterior` y `siguiente`. |
| `ListaReproduccion.java` | Lista circular propia: `inicio`, `fin`, `actual`, `tamanio` y generador de ids. No imprime nada. |
| `Entrada.java` | Validación de entradas (enteros positivos, texto no vacío). |
| `Main.java` | Menú de consola (con 13 opciones). Solo usa la interfaz pública de la lista. |
| `PruebasLista.java` | 37 pruebas reproducibles con estado inicial, operación, esperado, obtenido, cantidad e integridad. |

No se usa `LinkedList`, `ArrayList`, arreglos ni otras colecciones para almacenar canciones

Solo se emplean `Scanner` (lectura) y `Consumer` (acción aplicada a cada canción en recorridos y reproducción)

## 2. Para compilar y ejecutar:
Una vez tengas una copia de los archivos en tu directorio de destino preferido.
  Compila con:
  
        javac -d out Main.java
        javac -d out PruebasLista.java
    
  Para poder ver bien los acentos, y el símbolo "▶"
  
        java -Dstdout.encoding=UTF-8 -cp out Main
        java -Dstdout.encoding=UTF-8 -cp out PruebasLista

## 3. Análisis de complejidad (n = canciones; costo de procesar una canción = O(1))

| Operación | Complejidad | En el código: |
|---|---|---|
| Agregar al inicio / al final | **O(1)** | Se crean un nodo y se reasignan cuatro referencias (`nuevo.anterior/siguiente`, `fin.siguiente`, `inicio.anterior`) más `inicio` o `fin`. No hay ciclos: se llega al final por la referencia `fin`. |
| Consultar la canción actual | **O(1)** | Devuelve `actual.getDato()`. |
| Avanzar / retroceder | **O(1)** | Una asignación: `actual = actual.siguiente` o `.anterior`. |
| Eliminar la canción actual | **O(1)** | Parte de la referencia `actual`, ya disponible: `desenlazar` hace un número fijo de asignaciones y comparaciones. |
| Cantidad de canciones | **O(1)** | Devuelve el contador `tamanio`, mantenido en cada inserción y eliminación. |
| Buscar / seleccionar por id | **O(n)** | `buscarNodo` recorre como máximo `tamanio` nodos. Peor caso: el id es el último o no existe (n comparaciones). Seleccionar solo agrega una asignación O(1). |
| Eliminar por id | **O(n)** | Es *localizar* + *modificar enlaces*: `buscarNodo` es O(n) y `desenlazar` es O(1); O(n) + O(1) = O(n). |
| Mostrar todas (ambos sentidos) | **O(n)** | Dos recorridos de exactamente `tamanio` pasos cada uno: 2n = O(n). |
| Simular k reproducciones | **O(k)** | El ciclo se ejecuta exactamente k veces con trabajo O(1) por iteración. No depende de n; si k > n se da más de una vuelta. |

Localizar un nodo cuesta O(n) porque la lista no tiene acceso directo por id; modificar enlaces cuesta O(1) cuando ya se tiene la referencia.
Por eso eliminar por id es O(n) pero eliminar la actual es O(1). Estas cotas se derivan del código y no de tiempos medidos.

## 5. Pruebas

Se ejecutan 37 pruebas (resultado esperado: **37 de 37 pasan**)

| Requisito de prueba | Pruebas |
|---|---|
| Lista vacía | 1–3 |
| Inserción de la primera canción | 4–5 |
| Navegación con un solo nodo | 6 |
| Inserciones al inicio y final | 7 |
| Recorridos con 5 canciones | 8–9 |
| Avance desde la última / retroceso desde la primera | 10–12 |
| Búsqueda y selección | 13–14 |
| Eliminar primero, último, intermedio, y actual por id | 15–18 |
| Eliminar la actual (intermedia, última, primera) | 19–21 |
| Eliminar la única canción y reutilizar la lista | 22–24 |
| Id inexistente (búsqueda, selección, eliminación) | 25 |
| Reproducción simulada (k > n, k = n, k = 1, un solo nodo) | 26–29 |
| Entradas inválidas (k ≤ 0, duración ≤ 0, textos vacíos, enteros mal escritos) | 30–34 |
| Ids sin huecos ni reutilización | 32 y 35 |
| Volumen (10 000 inserciones y 5 000 eliminaciones) | 36–37 |

Cada prueba imprime estado inicial, operación, esperado y obtenido; además verifica cantidad y consistencia de enlaces.

 ## 6. Creado por:

Bailón Solís Luis Andrés                 (Documentación e investigación)

Calderón Curiel Erick Sebastián          (Control de calidad)

Hernández Terrazas Samuel Kaleb Farhid   (Pruebas y Videodocumentacion)

Romero Rodríguez Leonardo Maximiliano    (Programador principal)
