package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.proyecto3.ArbolBinarioSVG;


/**
 * Clase para representar Árboles Rojinegros.
 */
public class ArbolRojinegroSVG extends ArbolBinarioSVG {


    /** El árbol Rojinegro que representaremos */
    private ArbolRojinegro<Integer> arbolRojinegro;


    /**
     * Define el estado inicial del Árbol Rojinegro.
     * @param elementos los elementos que tendrá el Árbol Rojinegro.
     */
    public ArbolRojinegroSVG(Lista<Integer> elementos) {
        arbolRojinegro = new ArbolRojinegro<Integer>();
        this.elementos = elementos;
    }


    /**
     * dibujaArbolRojinegro. Dibuja el Árbol Rojinegro.
     * @param arbolBinario El Árbol Rojinegro a dibujar.
     */
    public String dibujaArbolRojinegro() {
        for (Integer elemento : this.elementos) {
            arbolRojinegro.agrega(elemento);
        }
        dibujaArbolBinario(arbolRojinegro);
        return this.arbol + this.cierre(); 
    }


    /**
     * dibujaVerticeArbolBinario. Dibuja el vértice del Árbol Rojinegro junto con su elemento.
     * @param vertice  el vértice a dibujar.
     * @param x la coordenada en x del vértice del Árbol Rojinegro.
     * @param y la coordenada en y del vértice del Árbol Rojinegro.
     * @param radio el radio del vértice.
     */
    @Override
    protected void dibujaVerticeArbolBinario(VerticeArbolBinario<Integer> vertice,
                                             int x,
                                             int y,
                                             int radio) {
        if (this.arbolRojinegro.getColor(vertice) == Color.NEGRO) {
            this.arbol += this.verticeArbol(x, y, radio, "black", "black");
        } else {
            this.arbol += this.verticeArbol(x, y, radio, "red", "red");
        }
        this.arbol += this.elementoArbolBinario(vertice, x, y, radio);
    }


    /**
     * elementoArbolBinario. El elemento del árbol binario.
     * @param vertice El vértice donde está el elemento del árbol binario.
     * @param x el entero desde donde vamos a empezar a dibujar el elemento.
     * @param y el entero desde donde vamos a empezar a dibujar el elemento.
     * @param radio el radio del vértice.
     * @return Representación del elemento del vértice en SVG.
     */
    @Override
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
        " font-family=\'sans-serif\' font-size=\'%d\' fill=\'white\'>" +
        "%d</text>", x - (int) (radio / 2) - desplazamiento, y + 4, fuente, vertice.get());
        return elemento;
    }
    
}