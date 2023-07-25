package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
            this.llave = llave;
            this.valor = valor;
            // Aquí va su código.
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
            for (int i = 0; i < entradas.length; i++) {
                if (entradas[i] != null && entradas[i].getLongitud() > 0) {
                    this.iterador = entradas[i].iteradorLista();
                    this.indice = i;
                    break;
                } 
            }
            // Aquí va su código.
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            if (iterador == null) {
                return false;
            } else {
                return iterador.hasNext();
            }
            // Aquí va su código.
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
            if (iterador == null) {
                throw new NoSuchElementException();
            } else {
                Entrada entrada = iterador.next();
                if (!(iterador.hasNext())) {
                    this.iterador = null;
                    for (int i = indice + 1; i < entradas.length; i++) {
                        if (entradas[i] != null) {
                            this.iterador = entradas[i].iteradorLista();
                            this.indice = i;
                            break;
                        }
                    }
                }
                return entrada;
            }
            // Aquí va su código.
        }

        /* Mueve el iterador a la siguiente entrada válida. */
        private void mueveIterador() {
            for (int i = 0; i < entradas.length; i++) {
                if (entradas[i] != null && entradas[i].getLongitud() > 0) {
                    iterador = entradas[i].iteradorLista();
                    indice = i;
                    break;
                }
            }
            // Aquí va su código.
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            return siguiente().llave;
            // Aquí va su código.
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            return siguiente().valor;
            // Aquí va su código.
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        this.dispersor = dispersor;
        if (capacidad < MINIMA_CAPACIDAD) {
            capacidad = MINIMA_CAPACIDAD;
        }
        int potencia = 1;
        int capacidadDoble = capacidad * 2;
        while (potencia < capacidadDoble) {
            potencia = potencia * 2;
        }
        entradas = nuevoArreglo(potencia);
        elementos = 0;
        // Aquí va su código.
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        if (llave == null || valor == null) {
            throw new IllegalArgumentException();
        }
        int mascara = entradas.length - 1;
        int i = dispersor.dispersa(llave) & mascara;
        Entrada e = new Entrada(llave, valor);
        if (entradas[i] == null) {
            Lista<Entrada> entrada = new Lista<Entrada>();
            entradas[i] = entrada;
        } else {
            for (int j = 0; j < entradas[i].getLongitud(); j++) {
                if (entradas[i].get(j).llave.equals(llave)) {
                    entradas[i].get(j).valor = valor;
                    return;
                }
            }
        }
        entradas[i].agregaFinal(e);
        elementos += 1;
        if (this.carga() >= MAXIMA_CARGA) {
            Lista<Entrada>[] arregloNuevo = nuevoArreglo(this.entradas.length * 2);
            for (int j = 0; j < this.entradas.length; j++) {
                if (entradas[j] != null) {
                    int nuevaMascara = (this.entradas.length * 2) - 1;
                    for (int k = 0; k < entradas[j].getLongitud(); k++) {
                        int nuevoIndice = dispersor.dispersa(entradas[j].get(k).llave) & nuevaMascara;
                        if (arregloNuevo[nuevoIndice] == null) {
                            arregloNuevo[nuevoIndice] = new Lista<Entrada>();
                            arregloNuevo[nuevoIndice].agregaFinal(this.entradas[j].get(k));
                        } else {
                            arregloNuevo[nuevoIndice].agregaFinal(this.entradas[j].get(k));
                        }
                    }
                }
            }
            this.entradas = arregloNuevo;
        }
        // Aquí va su código.
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        if (llave == null) {
            throw new IllegalArgumentException();
        }
        int mascara = this.entradas.length - 1;
        int indice = dispersor.dispersa(llave) & mascara;
        if (entradas[indice] == null) {
            throw new NoSuchElementException();
        }
        for (int i = 0; i < this.entradas[indice].getLongitud(); i++) {
            if (entradas[indice].get(i).llave.equals(llave)) {
                return entradas[indice].get(i).valor;
            }
        }
        throw new NoSuchElementException();
        // Aquí va su código.
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        if (llave == null) {
            return false;
        }
        int mascara = this.entradas.length - 1;
        int indice = dispersor.dispersa(llave) & mascara;
        if (entradas[indice] == null) {
            return false;
        }
        for (int i = 0; i < entradas[indice].getLongitud(); i++) {
            if (entradas[indice].get(i).llave.equals(llave)) {
                return true;
            }
        }
        return false;
        // Aquí va su código.
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        if (llave == null) {
            throw new IllegalArgumentException();
        }
        int mascara = this.entradas.length - 1;
        int indice = dispersor.dispersa(llave) & mascara;
        if (entradas[indice] == null) {
            throw new NoSuchElementException();
        }
        boolean encontrada = false;
        for (int i = 0; i < entradas[indice].getLongitud(); i++) {
            if (entradas[indice].get(i).llave.equals(llave)) {
                entradas[indice].elimina(entradas[indice].get(i));
                elementos -= 1;
                encontrada = true;
            }
        }
        if (!(encontrada)) {
            throw new NoSuchElementException();
        }
        // Aquí va su código.
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        int suma = 0;
        for (int i = 0; i < this.entradas.length; i++) {
            if (entradas[i] != null) {
                suma += entradas[i].getLongitud() - 1;
            }
        }
        return suma;
        // Aquí va su código.
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        int maxima = 0;
        for (int i = 0; i < entradas.length; i++) {
            if (entradas[i] != null) {
                if (entradas[i].getLongitud() > maxima) {
                    maxima = entradas[i].getLongitud();
                }
            }
        }
        return maxima - 1; 
        // Aquí va su código.
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        return (double) elementos / entradas.length;
        // Aquí va su código.
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        return elementos;
        // Aquí va su código.
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        return this.elementos == 0;
        // Aquí va su código.
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        this.entradas = nuevoArreglo(this.entradas.length);
        this.elementos = 0;
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        if (this.elementos == 0) {
            return "{}";
        }
        String diccionarioCadena = "{ ";
        Iterador iterador = new Iterador();
        while (iterador.hasNext()) {
            Entrada entrada = iterador.siguiente();
            diccionarioCadena += "'" + entrada.llave.toString() + "'" + ": ";
            diccionarioCadena += "'" + entrada.valor.toString() + "', ";   
        }
        diccionarioCadena += "}";
        return diccionarioCadena;
        // Aquí va su código.
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        if (this.esVacia() && d.esVacia()) {
            return true;
        }
        if (this.getElementos() != d.getElementos()) {
            return false;
        }
        Iterator<K> iterador = iteradorLlaves();
        while (iterador.hasNext()) {
            K llave = iterador.next();
            if (!(d.contiene(llave))) {
                return false;
            }
            if (!(d.get(llave).equals(this.get(llave)))) {
                return false;
            }
        }
        return true; 
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
