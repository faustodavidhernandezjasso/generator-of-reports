package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice < elementos;
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (!(validaIndice(indice))) {
                throw new NoSuchElementException();
            }
            return arbol[indice++];
            // Aquí va su código.
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            this.indice = -1;
            // Aquí va su código.
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return this.indice;
            // Aquí va su código.
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
            // Aquí va su código.
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return this.elemento.compareTo(adaptador.elemento);
            // Aquí va su código.
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100);
        // Aquí va su código.
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
        // Aquí va su código.
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        arbol = nuevoArreglo(n);
        int i = 0;
        this.elementos = n;
        for (T elemento : iterable) {
            arbol[i] = elemento;
            elemento.setIndice(i);
            i += 1;
        }
        for (i = (n - 1) / 2; i >= 0; i--) {
            acomodandoHaciaAbajo(arbol[i]);
        }
        // Aquí va su código.
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if (this.elementos == this.arbol.length) {
            T[] arbolAgrega = nuevoArreglo(2 * (this.elementos));
            for (int i = 0; i < elementos; i++) {
                arbolAgrega[i] = this.arbol[i];
            }
            this.arbol = arbolAgrega;
        }
        this.arbol[elementos] = elemento;
        elemento.setIndice(elementos);
        this.elementos += 1;
        this.acomodandoHaciaArriba(elemento); 
        // Aquí va su código.
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if (this.esVacia()) {
            throw new IllegalStateException();
        }
        T raiz = arbol[0];
        intercambia(arbol[0], arbol[elementos-1]);
        elementos -= 1;
        arbol[elementos].setIndice(-1);
        arbol[elementos] = null;
        acomodandoHaciaAbajo(arbol[0]);
        return raiz;
        // Aquí va su código.
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int i = elemento.getIndice();
        if (!(validaIndice(i))) {
            return;
        }
        intercambia(arbol[i], arbol[elementos-1]);
        elementos -= 1;
        arbol[elementos] = null;
        elemento.setIndice(-1);
        reordena(arbol[i]);
        // Aquí va su código.
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        int i = elemento.getIndice();
        if (!(validaIndice(i))) {
            return false;
        } else {
            return (arbol[i].compareTo(elemento) == 0);
        }
        // Aquí va su código.
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return this.elementos == 0;
        // Aquí va su código.
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        for (int i = 0; i < elementos; i++) {
            arbol[i] = null;
        }
        elementos = 0;
        // Aquí va su código.
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if (elemento == null) {
            return;
        } 
        int i = elemento.getIndice() - 1;
        i = (i == -1) ? -1 : i / 2;
        if (!(validaIndice(i)) || (arbol[i].compareTo(elemento)) <= 0) {
            acomodandoHaciaAbajo(elemento);
        } else {
            acomodandoHaciaArriba(elemento);
        }
        // Aquí va su código.
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
        // Aquí va su código.
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if (!(validaIndice(i))) {
            throw new NoSuchElementException();
        }
        return arbol[i];
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String cadena = "";
        for (int i = 0; i < this.elementos; i++) {
            cadena += arbol[i].toString() + ", ";
        }
        return cadena;
        // Aquí va su código.
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if (this.getElementos() != monticulo.getElementos()) {
            return false;
        }
        for (int i = 0; i < this.getElementos(); i++) {
            if (!(this.get(i).equals(monticulo.get(i)))) {
                return false;
            }
        }
        return true;
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> lista1 = new Lista<Adaptador<T>>();
        for (T elemento : coleccion) {
            lista1.agrega(new Adaptador<T>(elemento));
        }
        Lista<T> ordenada = new Lista<T>();
        MonticuloMinimo<Adaptador<T>> monticulo = new MonticuloMinimo<Adaptador<T>>(lista1);
        while (!(monticulo.esVacia())) {
            Adaptador<T> auxiliar = monticulo.elimina();
            ordenada.agrega(auxiliar.elemento);
        }
        return ordenada;       
        // Aquí va su código.
    }

    private boolean validaIndice(int i) {
        return !(i < 0 || i >= this.elementos);
    }

    private void acomodandoHaciaAbajo(T vertice) {
        if (vertice == null) {
            return;
        }
        int i = vertice.getIndice()*2 + 1;
        int d = vertice.getIndice()*2 + 2;
        if (!(validaIndice(i)) && !(validaIndice(d))) {
            return;
        }
        int minimo = d;
        if (validaIndice(i)) {
            if (validaIndice(d)) {
                if (arbol[i].compareTo(arbol[d]) < 0) {
                    minimo = i;
                }
            } else {
                minimo = i;
            }
        }
        if (arbol[minimo].compareTo(vertice) < 0) {
            intercambia(vertice, arbol[minimo]);
            acomodandoHaciaAbajo(vertice);
        }
    }

    private void acomodandoHaciaArriba(T vertice) {
        int i = vertice.getIndice() - 1;
        i = (i == - 1) ? -1 : i / 2;
        if (!(validaIndice(i)) || arbol[i].compareTo(vertice) < 0) {
            return;
        }
        intercambia(arbol[i], vertice);
        acomodandoHaciaArriba(vertice);
    }

    private void intercambia(T vertice, T padre) {
       int i = padre.getIndice();
       arbol[vertice.getIndice()] = padre;
       arbol[padre.getIndice()] = vertice;
       padre.setIndice(vertice.getIndice());
       vertice.setIndice(i);
    }
}
