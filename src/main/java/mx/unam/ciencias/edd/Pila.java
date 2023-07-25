package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        String cadena = "";
        for (Nodo n = cabeza; n != null; n = n.siguiente) {
            cadena += n.elemento + "\n";
        }
        return cadena;
        // Aquí va su código.
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Nodo mete = new Nodo(elemento);
        if (cabeza == null) {
            cabeza = rabo = mete;
            return;
        }
        mete.siguiente = cabeza;
        cabeza = mete;
        // Aquí va su código.
    }
}
