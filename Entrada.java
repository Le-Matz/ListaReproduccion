//Para validar entradas del usuario
public final class Entrada {
    private Entrada() { }

    //Devuelve el entero positivo representado (texto) o null si es invalido
    public static Integer parsePositivo(String texto) {
        if (texto == null) return null;
        try {
            int valor = Integer.parseInt(texto.trim());
            return valor > 0 ? valor : null;
        } catch (NumberFormatException e) {
            return null;
            // no numerico, decimal, vacío o desbordado
        }
    }

    //Devuelve el texto sin espacios laterales o null si es nulo o está en blanco
    public static String parseTexto(String texto) {
        if (texto == null) return null;
        String t = texto.trim();
        return t.isEmpty() ? null : t;
    }
}
