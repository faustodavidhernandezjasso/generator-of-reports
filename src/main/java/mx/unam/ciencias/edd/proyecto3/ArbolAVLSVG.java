package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.ArbolBinario;


/**
 * Clase para representar Árboles AVL.
 */
public class ArbolAVLSVG extends ArbolBinarioSVG {


    /** El Árbol AVL que representaremos */
    private ArbolAVL<Integer>  arbolAVL;


     /**
     * Define el estado inicial del Árbol AVL en SVG.
     * @param elementos La lista de elementos del Árbol AVL.
     */
    public ArbolAVLSVG(Lista<Integer> elementos) {
        arbolAVL = new ArbolAVL<Integer>();
        this.elementos = elementos;
    }


    /**
     * dibujaArbolBinario. Dibuja el Arbol Binario.
     * @param arbolBinario El Árbol Binario a dibujar.
     */
    @Override
    protected void dibujaArbolBinario(ArbolBinario<Integer> arbolBinario) {
        this.digitos = this.calculaDigitos(this.elementos.get(0));
        if (digitos > 1) {
            radio = digitos * 9;
        }
        this.distanciaEntreHermanos = digitos * 20;
        int ancho = this.ancho(arbolBinario.altura() + 1, radio);
        this.arbol += this.setLongitudArchivoSVG(ancho + 50, (int) (ancho / digitos) + 100);
        this.dibujaVerticesArbolBinario(this.coordenadaX + (int) (ancho / 2),
                                        this.coordenadaY,
                                        radio, 
                                        ancho / 2, 
                                        arbolBinario.raiz());
    }


    /**
     * balanceVerticeAVL. Dibuja el balance del vértice del Árbol AVL.
     * @param vertice el vértice del Árbol AVL.
     * @param x la coordena en x del balance.
     * @param y la coordena en y del balance.
     * @param radio el radio del vértice.
     * @return el balance del vértice del Árbol AVL.
     */
    public String balanceVerticeAVL(VerticeArbolBinario<Integer> vertice,
                                    int x,
                                    int y,
                                    int radio) {
        int indice = this.calculaDigitos(vertice.get());
        if (!(vertice.hayPadre())) {
            if (indice >= 4) {
                x += 25;
                y += 25;    
            } else {
                x += 15;
                y += 10;
            }
        } else {
            if (vertice.padre().hayIzquierdo()) {
                if (vertice.padre().izquierdo() == vertice) {
                    x -= 20;
                } else {
                    x += 5;
                }
            } else {
                x += 5;
            }
        }
        if (indice >= 4) {
            return balanceDelVertice(x, y - (int) ((1.4)*radio), 18,
                                     vertice.toString().substring(indice + 1, vertice.toString().length()));   
        } else {
            return balanceDelVertice(x, y - (int) ((1.4)*radio), 8,
                                     vertice.toString().substring(indice + 1, vertice.toString().length()));
        }
    }


    /**
     * dibujaVerticeArbolBinario. Dibuja el vértice del Árbol AVL junto con su elemento.
     * @param vertice  el vértice a dibujar.
     * @param x la coordenada en x del vértice del Árbol AVL.
     * @param y la coordenada en y del vértice del Árbol AVL.
     * @param radio el radio del vértice.
     */
    @Override
    protected void dibujaVerticeArbolBinario(VerticeArbolBinario<Integer> vertice,
                                             int x,
                                             int y,
                                             int radio) {
        this.arbol += this.verticeArbol(x, y, radio, "black", "white");
        this.arbol += this.elementoArbolBinario(vertice, x, y, radio);
        this.arbol += this.balanceVerticeAVL(vertice, x, y, radio);
    }


    /**
     * balanceDelVertice. Dibuja el balance del vértice.
     * @param x la coordena en x del balance.
     * @param y la coordena en y del balance.
     * @param font el tamaño de la letra.
     * @param vertice el vértice el cual queremos dibujar su balance.
     * @return el balance del vértice.
     */
    private String balanceDelVertice(int x, int y, int font, String vertice) {
        String balanceVertice = String.format("\n<text x=\"%d\" y=\"%d\" font-family=\"sans-serif\""
                                + " font-size=\"%d\" fill=\"black\"" + ">{%s}</text>", x, y,font, vertice);
        return balanceVertice;
    }


    /**
     * dibujaArbolAVL. Dibuja el Árbol AVL.
     * @param arbolBinario El Árbol AVL a dibujar.
     */
    public String dibujaArbolAVL() {
        for (Integer elemento : this.elementos) {
            this.arbolAVL.agrega(elemento);
        }
        this.distanciaEntreVertices = 50;
        this.dibujaArbolBinario(arbolAVL);
        return this.arbol + cierre();
    }

}