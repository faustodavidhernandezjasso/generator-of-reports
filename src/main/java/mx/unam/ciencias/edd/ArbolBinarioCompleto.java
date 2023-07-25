package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            cola = new Cola<Vertice>();
            if(raiz != null) {
                cola.mete(raiz);
            }
            // Aquí va su código.
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !(cola.esVacia());
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            if (cola.mira().derecho != null) {
                cola.mete(cola.mira().izquierdo);
                cola.mete(cola.mira().derecho);
            } else if (cola.mira().izquierdo != null) {
                cola.mete(cola.mira().izquierdo);
            }
            return cola.saca().elemento;
            // Aquí va su código.
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice vertice = nuevoVertice(elemento);
        elementos++;
        if (raiz == null) {
            raiz = vertice;
        } else {
            Cola<Vertice> cola = new Cola<Vertice>();
            cola.mete(raiz);
            while(!(cola.esVacia())) {
                Vertice aux = cola.saca();
                if (aux.hayIzquierdo()) {
                    cola.mete(aux.izquierdo);
                } else if (!(aux.hayIzquierdo())) {
                    aux.izquierdo = vertice;
                    vertice.padre = aux;
                    return;
                }
                if (aux.hayDerecho()) {
                    cola.mete(aux.derecho);
                } else if (!(aux.hayDerecho())) {
                    aux.derecho = vertice;
                    vertice.padre = aux;
                    return;
                }
            }
        }
        // Aquí va su código.
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice vertice = (ArbolBinario<T>.Vertice) busca(elemento);
        if (vertice == null) {
            return;
        }
        elementos -= 1;
        if (elementos == 0) {
            raiz = null;
            return;
        }
        Cola<Vertice> cola = new Cola<Vertice>();
        Vertice ultimo = raiz;
        cola.mete(ultimo);
        while (!(cola.esVacia())) {
            Vertice auxiliar = cola.saca();
            if (!(auxiliar.hayDerecho()) && !(auxiliar.hayIzquierdo()) && cola.esVacia()) {
                ultimo = auxiliar;
            }
            if (auxiliar.hayIzquierdo()) {
                cola.mete(auxiliar.izquierdo);
            } 
            if (auxiliar.hayDerecho()) {
                cola.mete(auxiliar.derecho);
            }
        }
        T eliminar = ultimo.elemento;
        ultimo.elemento = vertice.elemento;
        vertice.elemento = eliminar;
        if (ultimo.padre.izquierdo.elemento.equals(elemento)) {
            ultimo.padre.izquierdo = null;
        } else {
            ultimo.padre.derecho = null;
        }
        ultimo.padre = null;
        // Aquí va su código.
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if (esVacia()) {
            return -1;
        }
        int h = (int) (Math.log(elementos) / Math.log(2));
        return h;
        // Aquí va su código.
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if (raiz == null) {
            return;
        }
        Cola<VerticeArbolBinario<T>> cola = new Cola<VerticeArbolBinario<T>>();
        cola.mete(raiz());
        while (!(cola.esVacia())) {
            VerticeArbolBinario<T> vertice = cola.saca();
            accion.actua(vertice);
            if (vertice.hayIzquierdo()) {
                cola.mete(vertice.izquierdo());
            }
            if (vertice.hayDerecho()) {
                cola.mete(vertice.derecho());
            }
        }
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
