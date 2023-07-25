package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {
            pila = new Pila<Vertice>();
            if (esVacia()) {
                return;
            }
            for (Vertice vertice = raiz; vertice != null; vertice = vertice.izquierdo) {
                pila.mete(vertice);
            }
            // Aquí va su código.
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(pila.esVacia());
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice vertice = pila.saca();
            Vertice auxiliar;
            if (vertice.hayDerecho()) {
                auxiliar = vertice.derecho;
                while (auxiliar != null) {
                    pila.mete(auxiliar);
                    auxiliar = auxiliar.izquierdo;
                }
            }
            return vertice.elemento;
            // Aquí va su código.
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice vertice = nuevoVertice(elemento);
        elementos++;
        ultimoAgregado = vertice;
        if (raiz == null) {
            raiz = vertice;
            return;
        }
        auxiliarAgrega(raiz, vertice);
        // Aquí va su código.
    }

    private void auxiliarAgrega(Vertice actual, Vertice nuevo) {
        if (nuevo.elemento.compareTo(actual.elemento) <= 0) {
            if (!(actual.hayIzquierdo())) {
                actual.izquierdo = nuevo;
                nuevo.padre = actual; 
            } else {
                auxiliarAgrega(actual.izquierdo, nuevo);
            }
        } else {
            if (!(actual.hayDerecho())) {
                actual.derecho = nuevo;
                nuevo.padre = actual;
            } else {
                auxiliarAgrega(actual.derecho, nuevo);
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice eliminar = (ArbolBinario<T>.Vertice) busca(elemento);
        if (eliminar == null) {
            return;
        }
        elementos -= 1;
        if (!(eliminar.hayDerecho()) && !(eliminar.hayIzquierdo())) {
            eliminaVertice(eliminar);
        }
        else if (eliminar.hayDerecho() && !(eliminar.hayIzquierdo())) {
            eliminaVertice(eliminar);
        } else if (!(eliminar.hayDerecho()) && eliminar.hayIzquierdo()) {
            eliminaVertice(eliminar);
        } else {
            Vertice vertice = intercambiaEliminable(eliminar);
            eliminaVertice(vertice); 
        }
        // Aquí va su código.
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice auxiliar = maximoEnSubarbol(vertice.izquierdo);
        Vertice intercambia = nuevoVertice(vertice.elemento);
        vertice.elemento = auxiliar.elemento;
        auxiliar.elemento = intercambia.elemento;
        return auxiliar;
        // Aquí va su código.
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        if (vertice.hayIzquierdo() && vertice.hayDerecho()) {
            return;
        }
        Vertice papa = vertice.padre;
        Vertice auxiliar;
        if (vertice.hayIzquierdo()) {
            auxiliar = vertice.izquierdo;
        } else {
            auxiliar = vertice.derecho;
        }
        if(auxiliar != null) { 
            auxiliar.padre = papa; 
        }
        if(papa == null) {
            this.raiz = auxiliar;
            return;
        }
        boolean resultado = papa.hayIzquierdo() ? papa.izquierdo == vertice : false;
        if(resultado) {
            papa.izquierdo = auxiliar;
        } else {
            papa.derecho = auxiliar;
        }
        // Aquí va su código.
    }

    private Vertice maximoEnSubarbol(Vertice vertice) {
        if (vertice.derecho == null) {
            return vertice;
        }
        return maximoEnSubarbol(vertice.derecho);
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz, elemento);
        // Aquí va su código.
    }

    private VerticeArbolBinario<T> busca(Vertice vertice, T elemento) {
        if (vertice == null) {
            return null;
        }
        if (vertice.get().equals(elemento)) {
            return vertice;
        }
        if (vertice.get().compareTo(elemento) > 0) {
            return busca(vertice.izquierdo, elemento);
        } else {
            return busca(vertice.derecho, elemento);
        }
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
        // Aquí va su código.
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        Vertice q = (ArbolBinario<T>.Vertice) vertice;
        if (!q.hayIzquierdo()) {
            throw new IllegalArgumentException();
        }
        Vertice p = q.izquierdo;
        if(p.hayDerecho()) {
            q.izquierdo = p.derecho;
            p.derecho.padre = q;
        } else {
            q.izquierdo = null;
        }
        p.derecho = q;
        if(q.hayPadre()) {
            boolean izquierdo = q.padre.hayIzquierdo() ? q.padre.izquierdo == q : false;
            if(izquierdo) {
                q.padre.izquierdo = p;
            } else {
                q.padre.derecho = p;
            }
            p.padre = q.padre;
        } else {
            p.padre = null;
            this.raiz = p;
        }
        q.padre = p;
        // Aquí va su código.
    }


    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        Vertice q = (ArbolBinario<T>.Vertice) vertice;
        if (!q.hayDerecho()) {
            throw new IllegalArgumentException();
        }

        Vertice p = q.derecho;
        if(p.hayIzquierdo()) {
            q.derecho = p.izquierdo;
            p.izquierdo.padre = q;
        } else {
            q.derecho = null;
        }
        p.izquierdo = q;
        if(q.hayPadre()) {
            boolean izquierdo = q.padre.hayIzquierdo() ? q.padre.izquierdo == q : false;
            if(izquierdo) {
                q.padre.izquierdo = p;
            } else {
                q.padre.derecho = p;
            }
            p.padre = q.padre;
        } else {
            p.padre = null;
            this.raiz = p;
        }
        q.padre = p;
        // Aquí va su código.
    }



    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPreOrder(accion, raiz);
        // Aquí va su código.
    }

    private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice) {
        if (vertice == null) {
            return;
        }
        accion.actua(vertice);
        dfsPreOrder(accion, vertice.izquierdo);
        dfsPreOrder(accion, vertice.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsInOrder(accion , raiz);
        // Aquí va su código.
    }

    private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice) {
        if (vertice == null) {
            return;
        }
        dfsInOrder(accion, vertice.izquierdo);
        accion.actua(vertice);
        dfsInOrder(accion, vertice.derecho);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(accion, raiz);
        // Aquí va su código.
    }

    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice vertice) {
        if (vertice == null) {
            return;
        }
        dfsPostOrder(accion, vertice.izquierdo);
        dfsPostOrder(accion, vertice.derecho);
        accion.actua(vertice);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
