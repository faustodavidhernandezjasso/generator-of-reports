package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.Color;


/**
 * Clase abstracta para definir las representaciones en SVG de Árboles Binarios.
 */
public abstract class ArbolBinarioSVG {

    /** Inicio del archivo SVG */
    private String inicioSVG = "<?xml version = \'1.0\' encoding = \'utf-8\' ?>\n";
    /** El arbol en SVG. */
    protected String arbol = "";
    /** La distancia entre los niveles del Árbol Binario. */
    protected int distanciaEntreVertices = 80;
    /** La coordenada en x de la raíz del árbol. */
    protected int coordenadaX = 55;
    /** La coordenada en y de la raíz del árbol. */
    protected int coordenadaY = 55;
    /** Los digitos del elemento del vértice del árbol. */
    protected int digitos;
    /** La distancia entre dos hermanos en el árbol binario. */
    protected int distanciaEntreHermanos = 0;
    /** El radio del vértice del árbol binario. */
    protected int radio = 16;
    /** La lista de los elementos del árbol binario. */
    protected Lista<Integer> elementos;


    /**
     * ancho. Define el ancho del Árbol Binario.
     * @param altura la altura del Árbol Binario.
     * @param radio el radio de los vértices del radio.
     * @return el ancho del Árbol Binario.
     */
    protected int ancho(int altura, int radio) {
        if (altura == 1) {
            return (2 * radio);
        } else {
            return (2 * ancho(altura - 1, radio)) + this.distanciaEntreHermanos;
        }
    }


    /**
    * cierre. Hace el cierre del archivo SVG.
    * @return La cadena que cierra el archivo SVG.
    */
    public String cierre() {
        return "\n </g> \n</svg>";
    }


    /**
     * setLongitud. Establece el ancho y la altura del archivo SVG.
     * @param ancho El ancho del archivo.
     * @param altura La altura del archivo.
     * @return El ancho y la altura del archivo SVG.
     */
    public String setLongitudArchivoSVG(int ancho, int altura) {
        String cadena = String.format("<svg width=\'%d\'" +
        " height=\'%d\' xmlns='http://www.w3.org/2000/svg'>", ancho, altura);
        return inicioSVG + cadena + "\n <g>";
    }


    /**
     * calculaDigitos. Calcula los digitos del elemento más grande de la lista de elementos.
     * @param numero el elemento de un vértice.
     * @return el número de digitos del elemento más grande de la lista de elementos.
     */
    protected int calculaDigitos(int numero) {
        int digitos = 0;
        while (numero > 0) {
            numero = numero / 10;
            digitos += 1;
        }
        return digitos;
    }


    /**
     * verticeArbol. Dibuja un vértice del Árbol Binario.
     * @param cx la coordenada x del centro del vértice.
     * @param cy la coordenada y del centro del vértice.
     * @param radio el radio del vértice.
     * @param stroke el color del contorno del vértice.
     * @param fill el color del vértice.
     * @return la representación en SVG de un vértice.
     */
    public String verticeArbol(int cx, int cy, int radio, String stroke, String fill) {
        String vertice = String.format("\n<circle cx=\'%d\' cy=\'%d\' r=\'%d\' " +
        "stroke=\'%s\' stroke-width=\'3\' fill=\'%s\' />", cx, cy, radio, stroke, fill);
        return vertice;
    }


    /**
     * elementoArbolBinario. El elemento del árbol binario.
     * @param vertice El vértice donde está el elemento del árbol binario.
     * @param x el entero desde donde vamos a empezar a dibujar el elemento.
     * @param y el entero desde donde vamos a empezar a dibujar el elemento.
     * @param radio el radio del vértice.
     * @return Representación del elemento del vértice en SVG.
     */
    public String elementoArbolBinario(VerticeArbolBinario<Integer> vertice,
                                       int x,
                                       int y,
                                       int radio) {
        int fuente = 16;
        if (this.digitos > 3) {
            fuente = 22;
        }
        int elementoDelVertice = Integer.valueOf(vertice.get());
        int digitosDelElemento = this.calculaDigitos(elementoDelVertice);
        int desplazamiento = 0;
        if (digitosDelElemento > 4) {
            desplazamiento = 10;
        }
        String elemento = String.format("\n<text x=\'%d\' y=\'%d\'" +
        " font-family=\'sans-serif\' font-size=\'%d\' fill=\'black\'>" +
        "%d</text>", x - (int) (radio / 2) - desplazamiento, y + 4, fuente, vertice.get());
        return elemento;
    }


    /**
     * aristaArbol. Dibuja las aristas del árbol binario.
     * @param x1 Coordenada en x donde inicia la arista del Árbol Binario.
     * @param y1  Coordenada en y donde inicia la arista del Árbol Binario.
     * @param x2 Coordenada en x donde termina la arista del Árbol Binario.
     * @param y2 Coordenada en y donde termina la arista del Árbol Binario.
     * @return La arista del árbol binario en SVG.
     */
    public String aristaArbol(int x1, int y1, int x2, int y2) {
        String arista = String.format("\n<line x1=\'%d\' y1=\'%d\' x2=\'%d\' " +
        "y2=\'%d\' stroke=\'black\' stroke-width=\'3\' />", x1 , y1 , x2 , y2);
        return arista;
    }


    /**
     * dibujaArbolBinario. Dibuja el Arbol Binario.
     * @param arbolBinario El Árbol Binario a dibujar.
     */
    protected void dibujaArbolBinario(ArbolBinario<Integer> arbolBinario) {
        this.digitos = this.calculaDigitos(this.elementos.get(0));
        if (digitos > 1) {
            radio = digitos * 9;
        }
        this.distanciaEntreHermanos = digitos * 10;
        int ancho = this.ancho(arbolBinario.altura() + 1, radio);
        this.arbol += this.setLongitudArchivoSVG(ancho,(int) (ancho / (digitos + 1)));
        this.dibujaVerticesArbolBinario(this.coordenadaX + (int) (ancho / 2),
                                        this.coordenadaY,
                                        radio, 
                                        ancho / 2, 
                                        arbolBinario.raiz());
    }


    /**
     * dibujaVerticesArbolBinario. Dibuja los vértices del Árbol Binario.
     * @param x la coordenada en x del vértice del Árbol Binario.
     * @param y la coordenada en y del vértice del Árbol Binario.
     * @param radio el radio del vértice.
     * @param ancho el ancho del árbol.
     * @param vertice el vértice del Árbol Binario a dibujar. 
     */
    protected void dibujaVerticesArbolBinario(int x,
                                              int y,
                                              int radio,
                                              int ancho,
                                              VerticeArbolBinario<Integer> vertice) {
        int actualizaAncho = ancho / 2;
        int y1 = y + this.distanciaEntreVertices + radio;

        if (vertice.hayIzquierdo()) {
            this.arbol += aristaArbol(x, y, x - actualizaAncho, y1);
            dibujaVerticesArbolBinario(x - actualizaAncho, y1, radio, actualizaAncho, vertice.izquierdo());
        }

        if (vertice.hayDerecho()) {
            this.arbol += aristaArbol(x, y, x + actualizaAncho, y1);
            dibujaVerticesArbolBinario(x + actualizaAncho, y1, radio, actualizaAncho, vertice.derecho());
        }
        this.dibujaVerticeArbolBinario(vertice, x, y, radio);
    }


    /**
     * dibujaVerticeArbolBinario. Dibuja el vértice del Árbol Binario junto con su elemento.
     * @param vertice  el vértice a dibujar.
     * @param x la coordenada en x del vértice del Árbol Binario.
     * @param y la coordenada en y del vértice del Árbol Binario.
     * @param radio el radio del vértice.
     */
    protected void dibujaVerticeArbolBinario(VerticeArbolBinario<Integer> vertice,
                                             int x,
                                             int y,
                                             int radio) {
        this.arbol += this.verticeArbol(x, y, radio, "black", "white");
        this.arbol += this.elementoArbolBinario(vertice, x, y, radio);
    }


    /**
     * Regresa la representación del Árbol Binario en SVG.
     * @return la representación del Árbol Binario en SVG.
     */
    public String arbolBinarioSVG() {
        return arbol;
    }

}