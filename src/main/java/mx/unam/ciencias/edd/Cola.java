package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        String cadena = "";
        for (Nodo n = cabeza; n != null; n = n.siguiente) {
            cadena += String.valueOf(n.elemento) + ",";
        }
        return cadena;
        // Aquí va su código.
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Nodo mete = new Nodo(elemento);
        if (rabo == null) {
            cabeza = rabo = mete;
            return;
        }
        rabo.siguiente = mete;
        rabo = mete;
        // Aquí va su código.
    }
}
