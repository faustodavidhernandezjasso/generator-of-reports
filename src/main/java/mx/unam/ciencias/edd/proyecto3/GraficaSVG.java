package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.VerticeGrafica;

/**
 * Clase que nos sirve para representar la Gráfica en el archivo index.
 */
public class GraficaSVG {
    

    /** La gráfica en SVG */
    private String graficaSVG = "";
    /** El inicio del archivo SVG. */
    private String inicioSVG = "<?xml version = \'1.0\' encoding = \'utf-8\' ?>\n";
    /** El fin del archivo SVG. */
    private String fin = "</g>\n</svg>";
    /** El ángulo de la circunferencia. */
    private double angulo = 360;
    /** La coordenada x de cada vértice de la gráfica. */
    private double x;
    /** La coordenada y de cada vértice de la gráfica. */
    private double y;
    /** La gráfica que dibujaremos. */
    private Grafica<Integer> grafica;


    /**
     * Define el estado inicial de la gráfica.
     * @param grafica la gráfica a dibujar.
     */
    public GraficaSVG(Grafica<Integer> grafica) {
        this.grafica = grafica;
    }


    /**
     * setLongitud. Define el ancho y la altura del archivo SVG,
     * @param ancho el ancho del archivo SVG.
     * @param altura la altura del archivo SVG.
     * @return el inicio del archivo SVG, con medidas.
     */
    public String setLongitudArchivoSVG(double ancho, double altura) {
        String cadena = String.format("<svg width=\'%f\'" +
        " height=\'%f\' xmlns='http://www.w3.org/2000/svg'>", ancho, altura);
        return inicioSVG + cadena + "\n<g>\n";
    }


    /**
     * calculaX. Calculamos la coordena en x del vértice, usamos las coordenadas polares. x = rcos(θ)
     * @param radio el radio del círculo que usaremos como referencia para dibujar la gráfica.
     * @param angulo el ángulo donde estará el vértice.
     * @return la coordena en x del vértice.
     */
    public double calculaX(double radio, double angulo) {
        double x = Math.cos(Math.toRadians(angulo)) * radio;
        return x;
    }


    /**
     * calculaX. Calculamos la coordena en x del vértice, usamos las coordenadas polares. x = rcos(θ)
     * @param radio el radio del círculo que usaremos como referencia para dibujar la gráfica.
     * @param angulo el ángulo donde estará el vértice.
     * @return la coordena en x del vértice.
     */
    public double calculaY(double radio, double angulo) {
        double y = Math.sin(Math.toRadians(angulo)) * radio;
        return y;
    }


    /**
     * aristaGrafica. Dibuja la arista entre dos vértices en la gráfica.
     * @param x1 la coordenada en x donde inicia la arista.
     * @param y1 la coordenada en y donde inicia la arista.
     * @param x2 la coordenada en x donde termina la arista.
     * @param y2 la coordenada en y donde termina la arista.
     * @return la arista entre dos vértices en la gráfica.
     */
    public String aristaGrafica(double x1, double y1, double x2, double y2) {
        String arista = String.format("<line x1='%f' y1='%f' x2='%f' y2='%f' stroke='black' stroke-width='2' />\n ", x1,
                                                                                                                   y1,
                                                                                                                   x2,
                                                                                                                   y2);
        return arista;
    }
    

    /**
     * verticeGrafica. Dibuja el vértice de la gráfica.
     * @param cx la coordena x del centro del vértice de la gráfica.
     * @param cy la coordena y del centro del vértice de la gráfica.
     * @return el vértice de la gráfica.
     */
    public String verticeGrafica(double cx, double cy) {
        String vertice = String.format("<circle cx='%f' cy='%f' r='22' stroke='black' stroke-width='1' fill='white' />\n", cx, cy);
        return vertice;
    }


    /**
     * elementoVertice. Dibuja el elemento de un vértice de la gráfica.
     * @param vertice el vértice en el cual le dibujaremos su elemento.
     * @param x la coordena en x del elemento del vértice.
     * @param y la coordena en y del elemento del vértice.
     * @return el elemento de un vértice de la gráfica.
     */
    public String elementoVertice(VerticeGrafica<Integer> vertice, double x, double y) {
        String elemento = String.format("<text font-family='sans-serif' font-size='12' x='%f' y='%f' text-anchor='middle' fill='black'>%s</text"+">\n", x, y, vertice.get().toString());
        return elemento;
    }


    /**
     * conectaVertices. Conecta dos vértices por medio de una arista en la gráfica.
     * @param x1 la coordenada en x donde inicia la arista.
     * @param y1 la coordenada en y donde inicia la arista.
     * @param x2 la coordenada en x donde termina la arista.
     * @param y2 la coordenada en y donde termina la arista.
     */
    public void conectaVertices(double x1, double y1, double x2, double y2) {
        this.graficaSVG += aristaGrafica(x1, y1, x2, y2);
    }


    /**
     * dibujaVertice. Dibuja un vértice en la gráfica.
     * @param vertice el vértice que queremos dibujar.
     * @param x la coordena en x del vértice.
     * @param y la coordena en y del vértice.
     */
    public void dibujaVertice(VerticeGrafica<Integer> vertice, double x, double y) {
        this.graficaSVG += verticeGrafica(x, y);
        this.graficaSVG += elementoVertice(vertice, x, y + 5);
    }


    /**
     * dibujaGrafica. Dibuja la gráfica.
     * @return la gráfica en SVG.
     */
    public String dibujaGrafica() {
        final double ang = this.angulo / this.grafica.getElementos();
        double radio = (20) * this.grafica.getElementos();
        double centro = radio + 35;
        this.graficaSVG += setLongitudArchivoSVG((int) (2.5*radio) + 70, (int) (2.5*radio) + 50);
        double[] arreglo = {ang};
        Lista<Integer> elementos = new Lista<Integer>();
        this.grafica.paraCadaVertice((vertice) -> elementos.agregaFinal(vertice.get()));
        this.grafica.paraCadaVertice((vertice) -> {
            this.x = calculaX(radio, arreglo[0]);
            this.y = calculaY(radio, arreglo[0]);
            for (VerticeGrafica<Integer> vecino : vertice.vecinos()) {
                if (vecino.getColor() != Color.ROJO) {
                    int i = elementos.indiceDe(vecino.get());
                    double x1 = calculaX(radio, (i+1)*ang);
                    double y1 = calculaY(radio, (i+1)*ang);
                    this.conectaVertices(centro + this.x, centro - this.y, centro + x1, centro - y1);
                }
                this.grafica.setColor(vertice, Color.ROJO);
            }
            this.dibujaVertice(vertice, centro + this.x, centro - this.y);
            double auxiliar = arreglo[0];
            auxiliar += ang;
            arreglo[0] = auxiliar;
        });
        this.graficaSVG += this.fin;
        return this.graficaSVG;
    }


    /**
     * getGraficaSVG. Regresamos la gráfica en SVG.
     * @return la gráfica en SVG.
     */
    public String getGraficaSVG() {
        return this.graficaSVG;
    }

}