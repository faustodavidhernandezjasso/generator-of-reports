package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            this.elemento = elemento;
            // Aquí va su código.
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            siguiente = cabeza;
            anterior = null;
            // Aquí va su código.
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
            // Aquí va su código.
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (siguiente == null) {
                throw new NoSuchElementException();
            }
            T next = siguiente.elemento;
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return next;
            // Aquí va su código.
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
            // Aquí va su código.
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (anterior == null) {
                throw new NoSuchElementException();
            }
            T previous = anterior.elemento;
            siguiente = anterior;
            anterior = anterior.anterior;
            return previous;
            // Aquí va su código.
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            siguiente = cabeza;
            anterior = null;
            // Aquí va su código.
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
            // Aquí va su código.
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
        // Aquí va su código.
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
        // Aquí va su código.
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return longitud == 0;
        // Aquí va su código.
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        Nodo nuevo = new Nodo(elemento);
	    if (elemento == null) {
	        throw new IllegalArgumentException();
        }
	    if (longitud == 0) {
            cabeza = rabo = nuevo;
        } else {
            rabo.siguiente = nuevo;
            nuevo.anterior = rabo;
            rabo = nuevo;
        }
        longitud += 1;
        // Aquí va su código.
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        Nodo nuevo = new Nodo(elemento);
	    if (elemento == null) {
	        throw new IllegalArgumentException();
        }
	    if (longitud == 0) {
            cabeza = rabo = nuevo;
        } else {
            rabo.siguiente = nuevo;
            nuevo.anterior = rabo;
            rabo = nuevo;
        }
        longitud += 1;
        // Aquí va su código.
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Nodo nuevo = new Nodo(elemento);
        if (longitud == 0) {
            cabeza = rabo = nuevo;
        } else {
            cabeza.anterior = nuevo;
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
        longitud += 1;
        // Aquí va su código.
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null) { 
            throw new IllegalArgumentException();
        }
        Nodo nuevo = new Nodo(elemento);
        if (longitud == 0) {
            cabeza = rabo = nuevo;
	        longitud = longitud + 1;
	        return;
        }
        if (i <= 0) {
            cabeza.anterior = nuevo;
	        nuevo.siguiente = cabeza;
	        cabeza = nuevo;
	        longitud = longitud + 1;
	        return;
        }
        if (i >= longitud) {
            rabo.siguiente = nuevo;
	        nuevo.anterior = rabo;
	        rabo = nuevo;
	        longitud = longitud + 1;
	        return;
        }
        Nodo aux = cabeza;
	    int z = 0;
	    while (z != i) {
            aux = aux.siguiente;
	        z = z + 1;
        }
        aux.anterior.siguiente = nuevo;
	    nuevo.anterior = aux.anterior;
	    aux.anterior = nuevo;
	    nuevo.siguiente = aux;
	    longitud = longitud + 1;
        // Aquí va su código.
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo auxiliar = cabeza;
	    while(auxiliar != null) {
            if (auxiliar.elemento.equals(elemento)) {
                if (auxiliar.anterior != null) {
                    auxiliar.anterior.siguiente = auxiliar.siguiente;
                } else {
                    cabeza = auxiliar.siguiente;
                }
                if (auxiliar.siguiente != null) {
                    auxiliar.siguiente.anterior = auxiliar.anterior;
                 } else {
                     rabo = auxiliar.anterior;
                 }
                 longitud = longitud - 1;
                 return;	
                }
                auxiliar = auxiliar.siguiente;
            }
        // Aquí va su código.
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (longitud == 0) {
            throw new NoSuchElementException();
        }
        T eliminado = cabeza.elemento;
        if (longitud == 1) {
            cabeza = rabo = null;
            longitud -= 1;
            return eliminado;
        }
        cabeza = cabeza.siguiente;
        cabeza.anterior = null;
        longitud -= 1;
        return eliminado;
        // Aquí va su código.
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (longitud == 0) {
            throw new NoSuchElementException();
        }
        T eliminado = rabo.elemento;
        if (longitud == 1) {
            cabeza = rabo = null;
            longitud -= 1;
            return eliminado;
        }
        rabo = rabo.anterior;
        rabo.siguiente = null;
        longitud -= 1;
        return eliminado;
        // Aquí va su código.
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        Nodo auxiliar = cabeza;
	    while (auxiliar != null) {
            if (auxiliar.elemento.equals(elemento)) {
                return true;
            }
            auxiliar = auxiliar.siguiente;
        }
        return false;
        // Aquí va su código.
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> lista = new Lista<T>();
        Nodo auxiliar = cabeza;
        while (auxiliar != null) {
            lista.agregaInicio(auxiliar.elemento);
            auxiliar = auxiliar.siguiente;
        }
        return lista;
        // Aquí va su código.
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> lista = new Lista<T>();
        Nodo auxiliar = cabeza;
        while (auxiliar != null) {
            lista.agregaFinal(auxiliar.elemento);
            auxiliar = auxiliar.siguiente;
        }
        return lista;
        // Aquí va su código.
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza = rabo = null;
        longitud = 0;
        // Aquí va su código.
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (longitud == 0) {
            throw new NoSuchElementException();
        }
        return cabeza.elemento;
        // Aquí va su código.
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (longitud == 0) {
            throw new NoSuchElementException();
        }
        return rabo.elemento;
        // Aquí va su código.
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i >= longitud) {
            throw new ExcepcionIndiceInvalido();
        }
        int z = 0;
        Nodo auxiliar = cabeza;
        while (z != i) {
            z += 1;
            auxiliar = auxiliar.siguiente;
        }
        return auxiliar.elemento;
        // Aquí va su código.
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        int z = 0;
        for (Nodo n = cabeza; n != null; n = n.siguiente) {
            if (n.elemento.equals(elemento)) {
                return z;
            }
            z = z + 1;
        }
        return -1;
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        String k = "[";
        for (Nodo m = cabeza; m != null; m = m.siguiente) {
            if (m.siguiente != null) {
                k += String.format("%s, ", m.elemento.toString());
            }
        }
        if (!esVacia()) {
            k += String.format("%s", this.getUltimo().toString());
        }
        k += "]";
	    return k;
        // Aquí va su código.
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if (lista.getLongitud() != getLongitud()) {
            return false;
        }
        Nodo nodo1 = lista.cabeza;
	    Nodo m = cabeza;
	    while (nodo1 != null && m != null) {
            if (!(nodo1.elemento.equals(m.elemento))) {
                return false;
            }
            nodo1 = nodo1.siguiente;
	        m = m.siguiente;
        }
        return true;
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        if (longitud < 2) {
            return copia();
        }
        Lista<T> izquierda = new Lista<T>();
        Lista<T> derecha = new Lista<T>();
        Iterador iterador = (Iterador) iteradorLista();
        int m = this.getLongitud() / 2;
        for (int j = 0; j < m; j++) {
            izquierda.agregaFinal(iterador.next());
        }
        for (int k = m; k < this.getLongitud(); k++) {
            derecha.agregaFinal(iterador.next());
        }
        return mezcla(izquierda.mergeSort(comparador), derecha.mergeSort(comparador), comparador);
        // Aquí va su código.
    }

    private Lista<T> mezcla(Lista<T> izquierda, Lista<T> derecha, Comparator<T> comparador) {
        Lista<T> mezclada = new Lista<T>();
        Nodo izq = izquierda.cabeza;
        Nodo der = derecha.cabeza;
        while (izq != null && der != null) {
            if (comparador.compare(izq.elemento, der.elemento) <= 0) {
                mezclada.agregaFinal(izq.elemento);
                izq = izq.siguiente;
            } else {
                mezclada.agregaFinal(der.elemento);
                der = der.siguiente;
            }
        }
        Nodo auxiliar;
        if (izq == null) {
            auxiliar = der;
        } else {
            auxiliar = izq;
        }
        while (auxiliar != null) {
            mezclada.agregaFinal(auxiliar.elemento);
            auxiliar = auxiliar.siguiente;
        }
        return mezclada;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo auxiliar = cabeza;
        while (auxiliar != null) {
            if (comparador.compare(auxiliar.elemento, elemento) == 0) {
                return true;
            }
            auxiliar = auxiliar.siguiente;
        }
        return false;
        // Aquí va su código.
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
