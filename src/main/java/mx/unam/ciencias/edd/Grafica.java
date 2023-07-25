package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
            // Aquí va su código.
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return this.iterador.hasNext();
            // Aquí va su código.
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return this.iterador.next().elemento;
            // Aquí va su código.
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* El diccionario de vecinos del vértice. */
        public Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.color = Color.NINGUNO;
            this.vecinos = new Diccionario<T, Vecino>();
            // Aquí va su código.
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return this.elemento;
            // Aquí va su código.
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return this.vecinos.getElementos();
            // Aquí va su código.
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return this.color;
            // Aquí va su código.
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecinos;
            // Aquí va su código.
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
            // Aquí va su código.
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return this.indice;
            // Aquí va su código.
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            return compareDouble(this.distancia, vertice.distancia);
            // Aquí va su código.
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
            // Aquí va su código.
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return this.vecino.elemento;
            // Aquí va su código.
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return this.vecino.vecinos.getElementos();
            // Aquí va su código.
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return this.vecino.color;
            // Aquí va su código.
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return this.vecino.vecinos;
            // Aquí va su código.
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        this.vertices = new Diccionario<T, Vertice>();
        // Aquí va su código.
    }

    private int compareDouble(double x, double y) {
        if (x != -1 && (y == -1 || y > x)) {
            return -1;
        } else if (y != -1 && (x == -1 || x > y)) {
            return 1;
        } else {
            return 0;
        }
    }
    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos();
        // Aquí va su código.
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
        // Aquí va su código.
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if (contiene(elemento) || elemento == null) {
            throw new IllegalArgumentException();
        }
        Vertice vertice = new Vertice(elemento);
        vertices.agrega(elemento, vertice);
        // Aquí va su código.
    }


    private Vertice buscaVertice(T elemento) {
        Iterator<T> iterador = this.vertices.iteradorLlaves();
        while (iterador.hasNext()) {
            T llave = iterador.next();
            if (llave.equals(elemento)) {
                return this.vertices.get(llave);
            }
        }
        return null;
    }


    private boolean buscaVecino(Vertice vertice, Diccionario<T, Vecino> vecinos) {
        Iterator<T> iterador = vecinos.iteradorLlaves();
        while (iterador.hasNext()) {
            Vecino vec = vecinos.get(iterador.next());
            if (vertice.elemento.equals(vec.vecino.elemento)) {
                return true;
            }
        }
        return false;
    }

    private Vecino getVecino(Vertice v, Vertice u) {
        Iterator<T> iterador = v.vecinos.iteradorLlaves();
        while (iterador.hasNext()) {
            Vecino vec = v.vecinos.get(iterador.next());
            if (vec.vecino.elemento.equals(u.elemento)) {
                return vec;
            }
        }
        return null;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a, b, 1.0);
        // Aquí va su código.
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        Vertice v = buscaVertice(a);
        Vertice u = buscaVertice(b);
        if (v == null || u == null) {
            throw new NoSuchElementException();
        } else if (sonVecinos(a, b)) {
            throw new IllegalArgumentException();
        } else if (a.equals(b)) {
            throw new IllegalArgumentException();
        } else if (peso <= 0) {
            throw new IllegalArgumentException();
        } else {
            Vecino vecinoDeU = new Vecino(v, peso);
            u.vecinos.agrega(a, vecinoDeU);
            Vecino vecinoDeV = new Vecino(u, peso);
            v.vecinos.agrega(b, vecinoDeV);
            aristas += 1;
        }
        // Aquí va su código.
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice v = buscaVertice(a);
        Vertice u = buscaVertice(b);
        if (v == null || u == null) {
            throw new NoSuchElementException();
        }
        if (!(sonVecinos(a,b))) {
            throw new IllegalArgumentException();
        }
        v.vecinos.elimina(getVecino(v, u).vecino.elemento);
        u.vecinos.elimina(getVecino(u, v).vecino.elemento);
        aristas -= 1;
	// Aquí va su código.
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        Iterator<T> iterador = this.vertices.iteradorLlaves();
        while (iterador.hasNext()) {
            T llave = iterador.next();
            if (llave.equals(elemento)) {
                return true;
            }
        }
        return false;
        // Aquí va su código.
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice vertice = buscaVertice(elemento);
        if (vertice == null) {
            throw new NoSuchElementException();
        }
        Iterator<T> iterador = vertice.vecinos.iteradorLlaves();
        while (iterador.hasNext()) {
            Vecino vec = vertice.vecinos.get(iterador.next());
            desconecta(vec.vecino.elemento, vertice.elemento);
        }
        this.vertices.elimina(vertice.elemento);
        // Aquí va su código.
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice u = buscaVertice(a);
        Vertice v = buscaVertice(b);
        if (u == null || v == null) {
            throw new NoSuchElementException();
        }
        boolean vecinoDeU = buscaVecino(v, u.vecinos);
        boolean vecinoDeV = buscaVecino(u, v.vecinos);
        return (vecinoDeU && vecinoDeV);
        // Aquí va su código.
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        Vertice v = buscaVertice(a);
        Vertice u = buscaVertice(b);
        if (v == null || u == null) {
            throw new NoSuchElementException();
        } else if (!(sonVecinos(a, b))) {
            throw new IllegalArgumentException();
        } else {
            Vecino vecino = getVecino(v , u);
            return vecino.peso;
        }
        // Aquí va su código.
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        Vertice v = buscaVertice(a);
        Vertice u = buscaVertice(b);
        if (v == null || u == null) {
            throw new NoSuchElementException();
        } else if (!(sonVecinos(a , b)) || peso <= 0) {
            throw new IllegalArgumentException();
        } else {
            Vecino vecino1 = getVecino(v, u);
            Vecino vecino2 = getVecino(u, v);
            vecino1.peso = peso;
            vecino2.peso = peso;
        }
        // Aquí va su código.
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Iterator<T> iterador = this.vertices.iteradorLlaves();
        while (iterador.hasNext()) {
            T llave = iterador.next();
            if (this.vertices.get(llave).elemento.equals(elemento)) {
                return (VerticeGrafica<T>) this.vertices.get(llave);
            }
        }
        throw new NoSuchElementException();
        // Aquí va su código.
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || (vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class)) {
            throw new IllegalArgumentException();
        }
        if (vertice.getClass() == Vertice.class) {
            Vertice v = (Vertice) vertice;
            v.color = color;
        }
        if (vertice.getClass() == Vecino.class) {
            Vecino v = (Vecino) vertice;
            v.vecino.color = color;
        }
        // Aquí va su código.
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if (this.getElementos() == 0) {
            return true;
        }
        Iterator<T> iterador = this.vertices.iteradorLlaves();
        Vertice vertice = this.vertices.get(iterador.next());
        for (Vertice v : vertices) {
            v.color = Color.ROJO;
        }
        vertice.color = Color.NEGRO;
        Pila<Vertice> pila = new Pila<Vertice>();
        pila.mete(vertice);
        while (!(pila.esVacia())) {
            Vertice auxiliar = pila.saca();
            for (Vecino v : auxiliar.vecinos) {
                if (v.vecino.color == Color.ROJO) {
                    v.vecino.color = Color.NEGRO;
                    pila.mete(v.vecino);
                }
            }
        }
        for (Vertice v : vertices) {
            if (v.color == Color.ROJO) {
                return false;
            }
        }
        for (Vertice v : vertices) {
            v.color = Color.NINGUNO;
        }
        return true;
        // Aquí va su código.
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        Iterator<T> iterador  = this.vertices.iteradorLlaves();
        while (iterador.hasNext()) {
            accion.actua(this.vertices.get(iterador.next()));
        }
        // Aquí va su código.
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = buscaVertice(elemento);
        if (v == null) {
            throw new NoSuchElementException();
        }
	    for (Vertice vertice : vertices) {
	        vertice.color = Color.ROJO;
	    }
	    v.color = Color.NEGRO;
	    Cola<Vertice> cola = new Cola<Vertice>();
	    cola.mete(v);
	    while (!(cola.esVacia())) {
	        Vertice auxiliar = cola.saca();
	        accion.actua(auxiliar);
	        if (!(auxiliar.vecinos.esVacia())) {
		        for (Vecino vertice : auxiliar.vecinos) {
		            if (vertice.vecino.color == Color.ROJO) {
			            vertice.vecino.color = Color.NEGRO;
			            cola.mete(vertice.vecino);
                    }
                }
            }
        }    
	    for (Vertice vertice : vertices) {
	        vertice.color = Color.NINGUNO;
        }
        // Aquí va su código.
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice v = buscaVertice(elemento);
	    if (v == null) {
	        throw new NoSuchElementException();
	    }
	    for (Vertice vertice : vertices) {
	        vertice.color = Color.ROJO;
	    }
	    Pila<Vertice> pila = new Pila<Vertice>();
	    v.color = Color.NEGRO;
	    pila.mete(v);
	    while (!(pila.esVacia())) {
	        Vertice auxiliar = pila.saca();
	        accion.actua(auxiliar);
	        if (!(auxiliar.vecinos.esVacia())) {
		        for (Vecino vertice : auxiliar.vecinos) {
		            if (vertice.vecino.color == Color.ROJO) {
			            vertice.vecino.color = Color.NEGRO;
			            pila.mete(vertice.vecino);
                    }
                }
            }
        }
	    for (Vertice vertice : vertices) {
	        vertice.color = Color.NINGUNO;
	    }
        // Aquí va su código.
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return this.vertices.esVacia();
        // Aquí va su código.
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        this.vertices.limpia();
        this.aristas = 0;
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String cadena = "{";
        for (Vertice vertice : vertices) {
            cadena += vertice.elemento.toString() + ", ";
        }
        cadena += "}, {";
        paraCadaVertice((v) -> setColor(v, Color.NINGUNO));
        for (Vertice vertice : vertices) {
            vertice.color = Color.NEGRO;
            for (Vecino v : vertice.vecinos) {
                if (v.vecino.color != Color.NEGRO) {
                    cadena += "(" + vertice.elemento.toString() + ", " + v.vecino.elemento.toString() + "), ";
                }
            }
        }
        cadena += "}";
        paraCadaVertice((vertice) -> setColor(vertice, Color.NINGUNO));
        return cadena;
        // Aquí va su código.
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        if (this.getElementos() != grafica.getElementos()) {
            return false;
        }
        if (this.getAristas() != grafica.getAristas()) {
            return false;
        }
        Iterator<T> iterador = this.vertices.iteradorLlaves();
        while (iterador.hasNext()) {
            Vertice vertice = this.vertices.get(iterador.next());
            Vertice v = vecinos(vertice, grafica.vertices);
            if (v == null) {
                return false;
            }
            Iterator<T> it = vertice.vecinos.iteradorLlaves();
            while (it.hasNext()) {
                Vecino u = vertice.vecinos.get(it.next());
                if (vecinos(u, v.vecinos) == null) {
                    return false;
                }
            }
        }
        return true;
        // Aquí va su código.
    }

    private Vertice vecinos(Vertice vertice, Diccionario<T, Vertice> vertices) {
        Iterator<T> iterador = vertices.iteradorLlaves();
        while (iterador.hasNext()) {
            Vertice v = vertices.get(iterador.next());
            if (vertice.elemento.equals(v.elemento)) {
                return v;
            }
        }
        return null;
    }

    private Vecino vecinos(Vecino v, Diccionario<T, Vecino> vecinos) {
        Iterator<T> iterador = vecinos.iteradorLlaves();
        while (iterador.hasNext()) {
            Vecino vec = vecinos.get(iterador.next());
            if (vec.vecino.elemento.equals(v.vecino.elemento)) {
                return vec;
            }
        }
        return null;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        Vertice s = buscaVertice(origen);
        Vertice t = buscaVertice(destino);
        if (s == null || t == null) {
            throw new NoSuchElementException();
        }
        Lista<VerticeGrafica<T>> trayectoria = new Lista<VerticeGrafica<T>>();
        if (origen.equals(destino)) {
            trayectoria.agregaFinal(s);
            return trayectoria;
        } else {
            for (Vertice vertice : vertices) {
                vertice.distancia = -1;
            }
            s.distancia = 0;
            Cola<Vertice> cola = new Cola<Vertice>();
            cola.mete(s);
            while (!(cola.esVacia())) {
                Vertice u = cola.saca();
                for (Vecino v : u.vecinos) {
                    if (v.vecino.distancia == -1) {
                        v.vecino.distancia = u.distancia + 1;
                        cola.mete(v.vecino);
                    }
                }
            }
            if (t.distancia == -1) {
                return trayectoria;
            } else {
                Vertice u = t;
                trayectoria.agregaFinal(t);
                while (!(u.elemento.equals(s.elemento))) {
                    for (Vecino v : u.vecinos) {
                        if (u.distancia == v.vecino.distancia + 1) {
                            trayectoria.agregaFinal(v.vecino);	
                            u = v.vecino;
                        }
                    }
                }
                return trayectoria.reversa();
            }
        }
        // Aquí va su código.
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        Vertice s = buscaVertice(origen);
        Vertice t = buscaVertice(destino);
        if (s == null || t == null) {
            throw new NoSuchElementException();
        }
        for (Vertice v : this.vertices) {
            v.distancia = -1;
        }
        s.distancia = 0;
        MonticuloMinimo<Vertice> monticulo = new MonticuloMinimo<Vertice>(this.vertices, this.vertices.getElementos());
        while(!(monticulo.esVacia())) {
            Vertice u = monticulo.elimina();
            for (Vecino v : u.vecinos) {
                if (compareDouble(v.vecino.distancia, suma(u.distancia, v.peso)) > 0) {
                    v.vecino.distancia = suma(u.distancia, v.peso);
                    monticulo.reordena(v.vecino);
                }
            }
        }
        Lista<VerticeGrafica<T>> trayectoria = new Lista<VerticeGrafica<T>>();
        if (t.distancia == -1) {
            return trayectoria;
        }
        Vertice u = t;
        trayectoria.agregaFinal(t);
        while (!(u.elemento.equals(s.elemento))) {
            for (Vecino v : u.vecinos) {
                if (u.distancia == suma(v.vecino.distancia, 1)) {
                    trayectoria.agregaFinal(v.vecino);
                    u = v.vecino;
                }
            }
        }
        return trayectoria.reversa();
        // Aquí va su código.
    }

    private double suma(double x, double y) {
        if (x == -1 || y == -1) {
            return -1;
        } else {
            return x + y;
        }
    }


}
