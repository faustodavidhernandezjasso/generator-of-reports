package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            this.color = Color.NINGUNO;
            // Aquí va su código.
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            if (esRojo(this)) {
                return "R{" + elemento.toString() + "}";
            } else {
                return "N{" + elemento.toString() + "}";
            } 
            // Aquí va su código.
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
                return (color == vertice.color && super.equals(objeto));
            // Aquí va su código.
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
        // Aquí va su código.
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        if (!(vertice instanceof ArbolRojinegro.VerticeRojinegro)) {
            throw new ClassCastException();
        } 
        VerticeRojinegro v = (VerticeRojinegro) vertice;
        return v.color;
        // Aquí va su código.
    }

    private boolean esRojo(VerticeRojinegro vertice) {
        return !(esNegro(vertice));
    }

    private boolean esNegro(VerticeRojinegro vertice) {
        return vertice.elemento == null || vertice.color == Color.NEGRO;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro ultimo = (VerticeRojinegro) getUltimoVerticeAgregado();
        ultimo.color = Color.ROJO;
        balanceaAgrega(ultimo);
        // Aquí va su código.
    }

    private void balanceaAgrega(VerticeRojinegro vertice) {
        // Caso 1
        if(!(vertice.hayPadre())) {
            vertice.color = Color.NEGRO; return;
        }

        VerticeRojinegro papa = (VerticeRojinegro) vertice.padre;
        VerticeRojinegro abuelo;
        VerticeRojinegro tio;

        // Caso 2
        if(esNegro(papa)) {
            return;
        }

        abuelo = (VerticeRojinegro) papa.padre;
        if (esHijoIzquierdo(papa)) {
            tio = (VerticeRojinegro) abuelo.derecho;
        } else {
            tio = (VerticeRojinegro) abuelo.izquierdo; 
        }

        // Caso 3
        if(tio != null) {
            if(esRojo(tio)) {
                tio.color = Color.NEGRO;
                papa.color = Color.NEGRO;
                abuelo.color = Color.ROJO;
                balanceaAgrega(abuelo);
                return;
            }
        }

        // Caso 4
        if((esHijoIzquierdo(papa) && !(esHijoIzquierdo(vertice))) || (!(esHijoIzquierdo(papa)) && esHijoIzquierdo(vertice))) {
            if(esHijoIzquierdo(papa)) {
                super.giraIzquierda(papa);
                papa = (VerticeRojinegro) abuelo.izquierdo;
                vertice = (VerticeRojinegro) abuelo.izquierdo;
            } else {
                super.giraDerecha(papa);
                papa = (VerticeRojinegro) abuelo.derecho;
                vertice = (VerticeRojinegro) abuelo.derecho;
            }
        }

        // Caso 5
        papa.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if(esHijoIzquierdo(vertice)) {
            super.giraDerecha(abuelo);
        } else {
            super.giraIzquierda(abuelo);
        }
    }

    

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro eliminado = (VerticeRojinegro) this.busca(elemento);
        if(eliminado == null) {
            return;
        }
        elementos -= 1;

        if(eliminado.hayIzquierdo() && eliminado.hayDerecho()) {
            eliminado = (VerticeRojinegro) intercambiaEliminable(eliminado);
        }

        VerticeRojinegro hijo;
        boolean hayFantasma = false;
        if(!(eliminado.hayIzquierdo()) && !(eliminado.hayDerecho())) {
            hijo = fantasma(eliminado);
            hayFantasma = true;
        } else {
            hijo = eliminado.hayIzquierdo() ? (VerticeRojinegro) eliminado.izquierdo : (VerticeRojinegro) eliminado.derecho;
        }

        eliminaVertice(eliminado);

        if(esRojo(hijo) && esNegro(eliminado)) { 
            hijo.color = Color.NEGRO;
            return;
        }
        
        if(esNegro(eliminado) && esNegro(hijo)) {
            balanceaElimina(hijo);
        }

        if(hayFantasma) {
            eliminaVertice(hijo);
        }
        // Aquí va su código.
    }

    private void balanceaElimina(VerticeRojinegro vertice) {
        if(vertice == null) {
            throw new IllegalArgumentException();
        }

        // Caso 1:
        if (!(vertice.hayPadre())) { 
            return;
        }
        VerticeRojinegro papa = (VerticeRojinegro) vertice.padre;
        VerticeRojinegro hijo = esHijoIzquierdo(vertice) ? (VerticeRojinegro) papa.derecho : (VerticeRojinegro) papa.izquierdo;

        // Caso 2
        if(esRojo(hijo) && esNegro(papa)) {
            papa.color = Color.ROJO;
            hijo.color = Color.NEGRO;
            
            if(esHijoIzquierdo(vertice)) {
                super.giraIzquierda(papa);
            } else {
                super.giraDerecha(papa);
            }

            hijo = esHijoIzquierdo(vertice) ? (VerticeRojinegro) papa.derecho : (VerticeRojinegro) papa.izquierdo;
        }

        VerticeRojinegro hi = (VerticeRojinegro) hijo.izquierdo;
        VerticeRojinegro hd = (VerticeRojinegro) hijo.derecho;

        boolean hiEsNegro = hi != null ? esNegro(hi) : true;
        boolean hdEsNegro = hd != null ? esNegro(hd) : true;

        // Caso 3
        if(esNegro(papa) && esNegro(hijo) && hiEsNegro && hdEsNegro) {
            hijo.color = Color.ROJO;
            balanceaElimina(papa);
            return;
        }

        // Caso 4
        if(esNegro(hijo) && hiEsNegro && hdEsNegro && esRojo(papa)) {
            hijo.color = Color.ROJO;
            papa.color = Color.NEGRO;
            return;
        }

        // Caso 5
        if((esHijoIzquierdo(vertice) && !hiEsNegro && hdEsNegro) || (!esHijoIzquierdo(vertice) && hiEsNegro && !hdEsNegro)) {
            hijo.color = Color.ROJO;
            if(!hiEsNegro && hi != null) {
                hi.color = Color.NEGRO;
            }
            if(!hdEsNegro && hd != null) {
                hd.color = Color.NEGRO;
            }
            
            if(esHijoIzquierdo(vertice)) {
                super.giraDerecha(hijo);
            } else {
                super.giraIzquierda(hijo);
            }

            hijo = esHijoIzquierdo(vertice) ? (VerticeRojinegro) papa.derecho : (VerticeRojinegro) papa.izquierdo;
            hi = (VerticeRojinegro) hijo.izquierdo;
            hd = (VerticeRojinegro) hijo.derecho;
            hiEsNegro = hi != null ? esNegro(hi) : true;
            hdEsNegro = hd != null ? esNegro(hd) : true;
        }

        // Caso 6
        if((esHijoIzquierdo(vertice)) && !hdEsNegro || (!esHijoIzquierdo(vertice) && !hiEsNegro)) {
            hijo.color = papa.color;
            papa.color = Color.NEGRO;
            if(esHijoIzquierdo(vertice)) {
                if(hd != null) {
                    hd.color = Color.NEGRO;
                }
                super.giraIzquierda(papa);
            } else {
                if(hi != null) {
                    hi.color = Color.NEGRO;
                }
                super.giraDerecha(papa);
            }
        }
    }

    private VerticeRojinegro fantasma(Vertice vertice) {
        VerticeRojinegro fantasma = (VerticeRojinegro) nuevoVertice(null);
        fantasma.color = Color.NEGRO;
        if (vertice != null) {
            fantasma.padre = vertice;
            vertice.izquierdo = fantasma;
        }
        return fantasma;
    }


    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
