import java.util.function.Consumer;

//he aqui, el entorno de pruebas, ya simplificado y limpio, aparte de main, para poder hacer y deshacer sin lastimar nada. Ya debe de estar todo listo para este entonces, junto al ultimo commit

public class PruebasLista {
    private static int total = 0;
    private static int fallos = 0;
    private static int visitas = 0;   
    // contador auxiliar para los recorridos

    //utilidades para las pruebas

    static String ids(ListaReproduccion l, boolean adelante) {
        StringBuilder sb = new StringBuilder();
        Consumer<Cancion> c = x -> {
            if (sb.length() > 0) sb.append(',');
            sb.append(x.getId());
        };
        if (adelante) l.recorrerAdelante(c); else l.recorrerAtras(c);
        return "[" + sb + "]";
    }

    static String idDe(Cancion c) {
        return c == null ? "null" : String.valueOf(c.getId());
    }

    static String estado(ListaReproduccion l) {
        return ids(l, true) + " actual=" + idDe(l.cancionActual()) + " n=" + l.cantidad();
    }

    // Lista base: ids [4,3,1,2,5], actual = 1
    static ListaReproduccion listaBase() {
        ListaReproduccion l = new ListaReproduccion();
        l.agregarAlFinal("T1", "A1", 100);   // (1)
        l.agregarAlFinal("T2", "A2", 110);   // (1,2)
        l.agregarAlInicio("T3", "A3", 120);  // (3,1,2)
        l.agregarAlInicio("T4", "A4", 130);  // (4,3,1,2)
        l.agregarAlFinal("T5", "A5", 140);   // (4,3,1,2,5)
        return l;
    }

    // Lista con n canciones agregadas al final: ids [1..n], actual = 1
    static ListaReproduccion listaDe(int n) {
        ListaReproduccion l = new ListaReproduccion();
        for (int i = 1; i <= n; i++) l.agregarAlFinal("C" + i, "Art" + i, 60 + i);
        return l;
    }

    static void prueba(String nombre, String inicial, String operacion,
                       String esperado, String obtenido, ListaReproduccion lista) {
        total++;
        boolean ok = esperado.equals(obtenido);
        String integridad = (lista == null) ? "n/a" : lista.verificarIntegridad();
        boolean integra = (lista == null) || integridad == null;
        System.out.println("Prueba " + total + ": " + nombre);
        System.out.println("  Estado inicial : " + inicial);
        System.out.println("  Operación      : " + operacion);
        System.out.println("  Esperado       : " + esperado);
        System.out.println("  Obtenido       : " + obtenido);
        if (lista != null) {
            System.out.println("  Cantidad       : " + lista.cantidad()
                    + " | Recorrido atrás: " + ids(lista, false));
            System.out.println("  Integridad     : " + (integra ? "OK" : "FALLA -> " + integridad));
        }
        boolean paso = ok && integra;
        System.out.println("  Resultado      : " + (paso ? "PASA" : "FALLA") + "\n");
        if (!paso) fallos++;
    }

    //Pruebas

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS PARA LA LISTA DOBLEMENTE ENLAZADA CIRCULAR ===\n");

        listaVacia();
        primeraCancion();
        navegacionUnNodo();
        insercionesInicioFinal();
        recorridos();
        avanceYRetrocesoCircular();
        busquedaYSeleccion();
        eliminaciones();
        eliminacionActual();
        eliminacionUnica();
        idInexistente();
        reproduccion();
        entradasInvalidas();
        identificadores();
        volumen();

        System.out.println("=== RESUMEN: " + (total - fallos) + " de " + total + " pruebas pasaron ===");
        if (fallos > 0) System.exit(1);
    }

    static void listaVacia() {
        ListaReproduccion l = new ListaReproduccion();
        String ini = estado(l);
        String obt = "actual=" + idDe(l.cancionActual()) + " avanzar=" + idDe(l.avanzar()) + " retroceder=" + idDe(l.retroceder()) + " eliminarActual=" + idDe(l.eliminarActual());
        prueba("Lista vacia: consultar y navegar", ini,"cancionActual(), avanzar(), retroceder(), eliminarActual()","actual=null avanzar=null retroceder=null eliminarActual=null", obt, l);

        obt = "buscar=" + idDe(l.buscar(1)) + " seleccionar=" + l.seleccionar(1) + " eliminarPorId=" + l.eliminarPorId(1);
        prueba("Lista vacia: buscar, seleccionar y eliminar por id", ini,"buscar(1), seleccionar(1), eliminarPorId(1)","buscar=null seleccionar=false eliminarPorId=false", obt, l);

        StringBuilder sb = new StringBuilder();
        int r = l.reproducir(3, c -> sb.append(c.getId()));
        obt = "reproducidas=" + r + " salida='" + sb + "' | " + estado(l);
        prueba("Lista vacia: recorrer, contar y reproducir", ini,"recorrerAdelante(), recorrerAtras(), cantidad(), reproducir(3)","reproducidas=0 salida='' | [] actual=null n=0", obt, l);
    }

    static void primeraCancion() {
        ListaReproduccion l = new ListaReproduccion();
        l.agregarAlFinal("T1", "A1", 100);
        Nodo n = l.nodoInicio();
        String obt = estado(l) + " autoenlaces=" + (n == l.nodoFin() && n.siguiente == n && n.anterior == n && l.nodoActual() == n);
        prueba("Insertar la primera cancion (al final)", "[] actual=null n=0","agregarAlFinal(\"T1\")","[1] actual=1 n=1 autoenlaces=true", obt, l);

        ListaReproduccion l2 = new ListaReproduccion();
        l2.agregarAlInicio("T1", "A1", 100);
        Nodo m = l2.nodoInicio();
        obt = estado(l2) + " autoenlaces=" + (m == l2.nodoFin() && m.siguiente == m && m.anterior == m && l2.nodoActual() == m);
        prueba("Insertar la primera cancion (al inicio)", "[] actual=null n=0","agregarAlInicio(\"T1\")","[1] actual=1 n=1 autoenlaces=true", obt, l2);
    }

    static void navegacionUnNodo() {
        ListaReproduccion l = listaDe(1);
        String ini = estado(l);
        String obt = "avanzar=" + idDe(l.avanzar()) + " retroceder=" + idDe(l.retroceder()) + " avanzar=" + idDe(l.avanzar()) + " | " + estado(l);
        prueba("Navegacion en ambos sentidos con un solo nodo", ini,"avanzar(), retroceder(), avanzar()","avanzar=1 retroceder=1 avanzar=1 | [1] actual=1 n=1", obt, l);
    }

    static void insercionesInicioFinal() {
        ListaReproduccion l = new ListaReproduccion();
        l.agregarAlFinal("T1", "A1", 100);
        String ini = estado(l);
        l.agregarAlFinal("T2", "A2", 110);
        l.agregarAlInicio("T3", "A3", 120);
        l.agregarAlInicio("T4", "A4", 130);
        l.agregarAlFinal("T5", "A5", 140);
        prueba("Inserciones al inicio y al final (la actual no cambia)", ini,"agregarAlFinal(T2), agregarAlInicio(T3), agregarAlInicio(T4), agregarAlFinal(T5)","[4,3,1,2,5] actual=1 n=5", estado(l), l);
    }

    static void recorridos() {
        ListaReproduccion l = listaBase();
        prueba("Recorrido hacia adelante con 5 canciones", estado(l),"recorrerAdelante()", "[4,3,1,2,5]", ids(l, true), l);
        prueba("Recorrido hacia atras con 5 canciones", estado(l),"recorrerAtras()", "[5,2,1,3,4]", ids(l, false), l);
    }

    static void avanceYRetrocesoCircular() {
        ListaReproduccion l = listaBase();
        l.seleccionar(5);
        // ultima
        String ini = estado(l);
        String obt = "ret=" + idDe(l.avanzar()) + " | " + estado(l);
        prueba("Avanza desde la ultima hacia la primera", ini, "avanzar()","ret=4 | [4,3,1,2,5] actual=4 n=5", obt, l);

        ListaReproduccion l2 = listaBase();
        l2.seleccionar(4);
        //primera
        ini = estado(l2);
        obt = "ret=" + idDe(l2.retroceder()) + " | " + estado(l2);
        prueba("Retroceder desde la primera hacia la ultima", ini, "retroceder()","ret=5 | [4,3,1,2,5] actual=5 n=5", obt, l2);

        ListaReproduccion l3 = listaBase();
        ini = estado(l3);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(l3.avanzar().getId()).append(' ');
        prueba("Cinco avances completan una vuelta", ini, "avanzar() x5","2 5 4 3 1 | [4,3,1,2,5] actual=1 n=5", sb.toString().trim() + " | " + estado(l3), l3);
    }

    static void busquedaYSeleccion() {
        ListaReproduccion l = listaBase();
        String ini = estado(l);
        Cancion c = l.buscar(2);
        String obt = (c == null ? "null" : c.getTitulo() + "/" + c.getArtista() + "/" + c.getDuracionSegundos()) + " | " + estado(l);
        prueba("Buscar por id no modifica la actual", ini, "buscar(2)","T2/A2/110 | [4,3,1,2,5] actual=1 n=5", obt, l);

        obt = "seleccionar=" + l.seleccionar(3) + " | " + estado(l);
        prueba("Seleccionar por id cambia la actual", ini, "seleccionar(3)","seleccionar=true | [4,3,1,2,5] actual=3 n=5", obt, l);
    }

    static void eliminaciones() {
        ListaReproduccion l = listaBase();
        l.seleccionar(1);
        String ini = estado(l);
        String obt = "ret=" + l.eliminarPorId(4) + " | " + estado(l);
        prueba("Eliminar el primer nodo (no actual)", ini, "eliminarPorId(4)","ret=true | [3,1,2,5] actual=1 n=4", obt, l);

        l = listaBase();
        ini = estado(l);
        obt = "ret=" + l.eliminarPorId(5) + " | " + estado(l);
        prueba("Eliminar el ultimo nodo (no actual)", ini, "eliminarPorId(5)","ret=true | [4,3,1,2] actual=1 n=4", obt, l);

        l = listaBase();
        ini = estado(l);
        obt = "ret=" + l.eliminarPorId(2) + " | " + estado(l);
        prueba("Eliminar un nodo intermedio (no actual)", ini, "eliminarPorId(2)","ret=true | [4,3,1,5] actual=1 n=4", obt, l);

        l = listaBase();
        l.seleccionar(2);
        ini = estado(l);
        obt = "ret=" + l.eliminarPorId(2) + " | " + estado(l);
        prueba("Eliminar por id la cancion actual: pasa a la siguiente", ini, "eliminarPorId(2)","ret=true | [4,3,1,5] actual=5 n=4", obt, l);
    }

    static void eliminacionActual() {
        ListaReproduccion l = listaBase();
        String ini = estado(l);
        String obt = "ret=" + idDe(l.eliminarActual()) + " | " + estado(l);
        prueba("Eliminar la actual (intermedia)", ini, "eliminarActual()","ret=1 | [4,3,2,5] actual=2 n=4", obt, l);

        l = listaBase();
        l.seleccionar(5);
        ini = estado(l);
        obt = "ret=" + idDe(l.eliminarActual()) + " | " + estado(l);
        prueba("Eliminar la actual cuando es la ultima: pasa a la primera", ini, "eliminarActual()","ret=5 | [4,3,1,2] actual=4 n=4", obt, l);

        l = listaBase();
        l.seleccionar(4);
        ini = estado(l);
        obt = "ret=" + idDe(l.eliminarActual()) + " | " + estado(l);
        prueba("Eliminar la actual cuando es la primera", ini, "eliminarActual()","ret=4 | [3,1,2,5] actual=3 n=4", obt, l);
    }

    static void eliminacionUnica() {
        ListaReproduccion l = listaDe(1);
        String ini = estado(l);
        String obt = "ret=" + idDe(l.eliminarActual()) + " | " + estado(l) + " refs=" + (l.nodoInicio() == null && l.nodoFin() == null && l.nodoActual() == null);
        prueba("Eliminar la unica cancion con eliminarActual", ini, "eliminarActual()","ret=1 | [] actual=null n=0 refs=true", obt, l);

        l = listaDe(1);
        ini = estado(l);
        obt = "ret=" + l.eliminarPorId(1) + " | " + estado(l)
                + " refs=" + (l.nodoInicio() == null && l.nodoFin() == null && l.nodoActual() == null);
        prueba("Eliminar la unica canción con eliminarPorId", ini, "eliminarPorId(1)","ret=true | [] actual=null n=0 refs=true", obt, l);

        l.agregarAlInicio("Nueva", "Artista", 90);
        prueba("La lista vaciada vuelve a funcionar (id 2, queda como actual)", "[] actual=null n=0","agregarAlInicio(\"Nueva\")", "[2] actual=2 n=1", estado(l), l);
    }

    static void idInexistente() {
        ListaReproduccion l = listaDe(3);
        String ini = estado(l);
        String obt = "buscar(99)=" + idDe(l.buscar(99)) + " buscar(0)=" + idDe(l.buscar(0)) + " buscar(-1)=" + idDe(l.buscar(-1)) + " seleccionar(99)=" + l.seleccionar(99) + " eliminarPorId(99)=" + l.eliminarPorId(99) + " | " + estado(l);
        prueba("Busqueda, seleccion y eliminacion de ids inexistentes", ini,"buscar(99), buscar(0), buscar(-1), seleccionar(99), eliminarPorId(99)","buscar(99)=null buscar(0)=null buscar(-1)=null seleccionar(99)=false eliminarPorId(99)=false | [1,2,3] actual=1 n=3",obt, l);
    }

    static void reproduccion() {
        ListaReproduccion l = listaDe(3);
        String ini = estado(l);
        StringBuilder sb = new StringBuilder();
        int r = l.reproducir(7, c -> sb.append(c.getId()).append(' '));
        prueba("Reproduccion simulada con k > n (k=7, n=3)", ini, "reproducir(7)","reproducidas=7 orden=1 2 3 1 2 3 1 | [1,2,3] actual=2 n=3","reproducidas=" + r + " orden=" + sb.toString().trim() + " | " + estado(l), l);

        l = listaDe(3);
        ini = estado(l);
        StringBuilder sb2 = new StringBuilder();
        r = l.reproducir(3, c -> sb2.append(c.getId()).append(' '));
        prueba("Reproduccion simulada con k = n (da una vuelta exacta)", ini, "reproducir(3)","reproducidas=3 orden=1 2 3 | [1,2,3] actual=1 n=3","reproducidas=" + r + " orden=" + sb2.toString().trim() + " | " + estado(l), l);

        l = listaDe(3);
        l.seleccionar(3);
        ini = estado(l);
        StringBuilder sb3 = new StringBuilder();
        r = l.reproducir(1, c -> sb3.append(c.getId()));
        prueba("Reproduccion simulada con k = 1 desde la ultima (pasa a la primera)", ini, "reproducir(1)","reproducidas=1 orden=3 | [1,2,3] actual=1 n=3","reproducidas=" + r + " orden=" + sb3 + " | " + estado(l), l);

        l = listaDe(1);
        ini = estado(l);
        StringBuilder sb4 = new StringBuilder();
        r = l.reproducir(4, c -> sb4.append(c.getId()).append(' '));
        prueba("Reproduccion simulada con un solo nodo (k=4)", ini, "reproducir(4)","reproducidas=4 orden=1 1 1 1 | [1] actual=1 n=1","reproducidas=" + r + " orden=" + sb4.toString().trim() + " | " + estado(l), l);
    }

    static void entradasInvalidas() {
        ListaReproduccion l = listaDe(3);
        String ini = estado(l);
        String obt = "";
        try { l.reproducir(0, c -> { }); obt += "k=0:sin-error "; }
        catch (IllegalArgumentException e) { obt += "k=0:rechazado "; }
        try { l.reproducir(-2, c -> { }); obt += "k=-2:sin-error "; }
        catch (IllegalArgumentException e) { obt += "k=-2:rechazado "; }
        prueba("Reproduccion con k no positivo", ini, "reproducir(0), reproducir(-2)","k=0:rechazado k=-2:rechazado | [1,2,3] actual=1 n=3",obt + "| " + estado(l), l);

        obt = "";
        try { l.agregarAlFinal("X", "Y", 0); obt += "dur=0:sin-error "; }
        catch (IllegalArgumentException e) { obt += "dur=0:rechazado "; }
        try { l.agregarAlInicio("X", "Y", -5); obt += "dur=-5:sin-error "; }
        catch (IllegalArgumentException e) { obt += "dur=-5:rechazado "; }
        try { l.agregarAlFinal("   ", "Y", 10); obt += "titulo-blanco:sin-error "; }
        catch (IllegalArgumentException e) { obt += "titulo-blanco:rechazado "; }
        try { l.agregarAlFinal("X", null, 10); obt += "artista-null:sin-error "; }
        catch (IllegalArgumentException e) { obt += "artista-null:rechazado "; }
        prueba("Canciones invalidas no modifican la lista", ini,"agregar con duracion 0, duracion -5, titulo en blanco, artista null","dur=0:rechazado dur=-5:rechazado titulo-blanco:rechazado artista-null:rechazado | [1,2,3] actual=1 n=3",obt + "| " + estado(l), l);

        l.agregarAlFinal("Valida", "Y", 10);
        prueba("Un intento fallido no consume identificadores", estado(listaDe(3)),"tras los intentos fallidos, agregarAlFinal(\"Valida\")","[1,2,3,4] actual=1 n=4", estado(l), l);

        String obt2 = "";
        String[] entradas = { "abc", "", "  ", "0", "-3", "2.5", "99999999999", " 7 ", "+4", "12" };
        for (String e : entradas) {
            Integer v = Entrada.parsePositivo(e);
            obt2 += (v == null ? "x" : v.toString()) + " ";
        }
        prueba("Validacion de enteros positivos (abc, vacio, blancos, 0, -3, 2.5, desbordado, ' 7 ', +4, 12)","n/a", "Entrada.parsePositivo(...)","x x x x x x x 7 4 12", obt2.trim(), null);

        prueba("Validacion de texto (null, blancos, ' hola ')", "n/a", "Entrada.parseTexto(...)","x x hola",(Entrada.parseTexto(null) == null ? "x" : "?") + " " + (Entrada.parseTexto("   ") == null ? "x" : "?") + " " + Entrada.parseTexto(" hola "), null);
    }

    static void identificadores() {
        ListaReproduccion l = listaDe(3);
        String ini = estado(l);
        l.eliminarPorId(3);
        Cancion nueva = l.agregarAlFinal("N", "A", 50);
        prueba("Los identificadores no se reutilizan despues de eliminar", ini,"eliminarPorId(3), agregarAlFinal(\"N\")","id=4 | [1,2,4] actual=1 n=3", "id=" + nueva.getId() + " | " + estado(l), l);
    }

    static void volumen() {
        ListaReproduccion l = new ListaReproduccion();
        int n = 10000;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) l.agregarAlInicio("T" + i, "A", 100);
            else l.agregarAlFinal("T" + i, "A", 100);
        }
        visitas = 0;
        // solo un contador, no guarda canciones
        l.recorrerAdelante(c -> visitas++);
        int adelante = visitas;
        visitas = 0;
        l.recorrerAtras(c -> visitas++);
        int atras = visitas;
        prueba("Volumen (10000) inserciones alternadas con recorrido completo", "[] n=0","10 000 inserciones; recorrerAdelante(); recorrerAtras()","n=10000 adelante=10000 atras=10000","n=" + l.cantidad() + " adelante=" + adelante + " atras=" + atras, l);

        for (int id = 2; id <= n; id += 2) l.eliminarPorId(id);   // elimina los ids pares
        visitas = 0;
        l.recorrerAdelante(c -> visitas++);
        prueba("Volumen (5000) eliminacion de ids pares", "n=10000", "eliminarPorId(id par) x5000","n=5000 visitas=5000", "n=" + l.cantidad() + " visitas=" + visitas, l);
    }
}