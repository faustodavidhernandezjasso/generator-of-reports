package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
            this.elemento = elemento;
            // Aquí va su código.
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <code>true</code> si el vértice tiene padre,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayPadre() {
            return this.padre != null;
            // Aquí va su código.
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <code>true</code> si el vértice tiene izquierdo,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return this.izquierdo != null;
            // Aquí va su código.
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <code>true</code> si el vértice tiene derecho,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return this.derecho != null;
            // Aquí va su código.
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            if (padre == null) {
                throw new NoSuchElementException();
            } else {
                return padre;
            }
            // Aquí va su código.
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            if (izquierdo == null) {
                throw new NoSuchElementException();
            }
            return izquierdo;
            // Aquí va su código.
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            if (derecho == null) {
                throw new NoSuchElementException();
            }
            return derecho;
            // Aquí va su código.
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            if (hayIzquierdo() && hayDerecho()) {
                return 1 + Math.max(this.izquierdo.altura(), this.derecho.altura());
            } else if (hayIzquierdo()) {
                return 1 + this.izquierdo.altura();
            } else if (hayDerecho()) {
                return 1 + this.derecho.altura();
            } else {
                return 0;
            }
            // Aquí va su código.
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            return this.hayPadre() ? 1 + this.padre.profundidad() : 0;
            // Aquí va su código.
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return this.elemento;
            // Aquí va su código.
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            return equals(this, vertice);
            // Aquí va su código.
        }

        private boolean equals(Vertice ver1, Vertice ver2) {
            if (ver1 == null && ver2 == null) {
                return true;
            }
            if (ver1 == null && ver2 != null) {
                return false;
            }
            if (ver1 != null && ver2 == null) {
                return false;
            }
            if (ver1.elemento == null && ver2.elemento == null) {
                return true;
            } 
            if (!(ver1.elemento.equals(ver2.elemento))) {
                return false;
            }
            return equals(ver1.izquierdo, ver2.izquierdo) && equals(ver1.derecho, ver2.derecho);
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
            return this.elemento.toString();
            // Aquí va su código.
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        for (T elemento : coleccion) {
            this.agrega(elemento);
        }
        // Aquí va su código.
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        Vertice vertice = new Vertice(elemento);
        return vertice;
        // Aquí va su código.
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        if (this.esVacia()) {
            return -1;
        }
        return raiz.altura();
        // Aquí va su código.
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return elementos;
        // Aquí va su código.
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if (elemento == null) {
            return false;
        }
        return contiene(elemento, raiz);
        // Aquí va su código.
    }

    private boolean contiene(T elemento, Vertice vertice) {
        if (vertice == null) {
            return false;
        }
        if (elemento.equals(vertice.elemento)) {
            return true;
        }
        boolean resultado = contiene(elemento, vertice.izquierdo);
        if (resultado != false) {
            return resultado;
        } else {
            return contiene(elemento, vertice.derecho);
        }
    }
    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <code>null</code>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <code>null</code> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        return busca(elemento, raiz);
        // Aquí va su código.
    }

    private VerticeArbolBinario<T> busca(T elemento, Vertice vertice) {
        if (vertice == null) {
            return null;
        }
        if (elemento.equals(vertice.elemento)) {
            return vertice;
        } 
        VerticeArbolBinario<T> buscado = busca(elemento, vertice.izquierdo);
        if (buscado != null) {
            return buscado;
        } else {
            return busca(elemento, vertice.derecho);
        }
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        if (raiz == null) {
            throw new NoSuchElementException();
        } else {
            return raiz;
        }
        // Aquí va su código.
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return raiz == null;
        // Aquí va su código.
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        raiz = null;
        elementos = 0;
        // Aquí va su código.
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
            return  (this.esVacia() && arbol.esVacia()) || (this.raiz.equals(arbol.raiz));
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        if (this.raiz == null) { 
            return ""; 
        }
        int altura = altura() + 1;
        boolean[] arreglo = new boolean[altura];
        for(int i = 0; i < altura; i++) {
            arreglo[i] = false;
        }
        return toString(this.raiz, 0, arreglo);
        // Aquí va su código.
    }

    private String toString(Vertice vertice, int nivel, boolean[] arreglo) {
        String s = vertice + "\n";
        arreglo[nivel] = true;
        if(vertice.hayIzquierdo() && vertice.hayDerecho()) {
            s += dibujaEspacios(nivel, arreglo);
            s += "├─›";
            s += toString(vertice.izquierdo, nivel+1, arreglo);
            s += dibujaEspacios(nivel, arreglo);
            s += "└─»";
            arreglo[nivel] = false;
            s += toString(vertice.derecho, nivel+1, arreglo);
        } else if(vertice.hayIzquierdo()) {
            s += dibujaEspacios(nivel, arreglo);
            s += "└─›";
            arreglo[nivel] = false;
            s += toString(vertice.izquierdo, nivel+1, arreglo);
        } else if(vertice.hayDerecho()) {
            s += dibujaEspacios(nivel, arreglo);
            s += "└─»";
            arreglo[nivel] = false;
            s += toString(vertice.derecho, nivel+1, arreglo);
        }
        return s;
    }

    private String dibujaEspacios(int nivel, boolean[] arreglo) {
        String s = "";
        for(int i = 0; i < nivel; i++) {
            s += arreglo[i] ? "│  " : "   ";
        }
        return s;
    }

    protected boolean esHijoIzquierdo(Vertice vertice) {
        if(vertice.padre == null) { return false; }
        if(vertice.padre.hayIzquierdo()) {
            return vertice.padre.izquierdo == vertice;
        }
        return false;
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
