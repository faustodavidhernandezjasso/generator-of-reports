package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import java.util.Iterator;


/**
 * Clase que nos va a servir para representar la Gráfica de Pastel en el reporte HTML.
 */
public class GraficaDePastel {


    /** La Gráfica de Pastel en SVG. */
    private String graficaDePastel = "";
    /** El radio de la Gráfica de Pastel. */
    private double radio = 200;
    /** Coordena del centro de la Gráfica de Pastel en x. */
    private double cx = 200;
    /** Coordena del centro de la Gráfica de Pastel en y. */
    private double cy = 200;
    /** El inicio del archivo en SVG. */
    private String inicio = "<?xml version='1.0' encoding='UTF-8' ?>\n<svg width='400' heigth='400' xmlns='http://www.w3.org/2000/svg'>\n<g>\n";
    /** La Gráfica de Pastel sin rebanadas. */
    private String grafica = "<circle cx='200' cy ='200' r='200' fill='lightgoldenrodyellow' />\n";
    /** El fin del archivo SVG. */
    private String fin = "</g>\n</svg>";
    /** Las frecuencias relativas de las palabras que estarán en la Gráfica de Pastel. */
    private Lista<Double> frecuenciasRelativas;
    /** Los colores que le corresponden a cada palabra. */
    public static Lista<String> colores = new Lista<String>();
    

    /**
     * Define el estado inicial de la Gráfica de Pastel.
     * @param frecuenciasRelativas la lista con la frecuencias relativas de cada palabra para poder graficar cada barra.
     */
    public GraficaDePastel(Lista<Double> frecuenciasRelativas) {
        llenaLista();
        this.frecuenciasRelativas = frecuenciasRelativas;
        this.graficaDePastel += inicio + grafica;
    }


    /**
     * llenaLista. Llena la lista con los colores que le corresponderan a cada palabra.
     */
    private static void llenaLista() {
        colores.agregaFinal("lightgoldenrodyellow");
        colores.agregaFinal("lightseagreen");
        colores.agregaFinal("lightgrey");
        colores.agregaFinal("lightsalmon");
        colores.agregaFinal("moccasin");
        colores.agregaFinal("lightpink");
        colores.agregaFinal("dodgerblue");
        colores.agregaFinal("springgreen");
        colores.agregaFinal("yellow");
    }


    /**
     * dibujaGrafica. Dibuja la Gráfica de Pastel.
     * @return la Gráfica de Pastel en SVG.
     */
    public String dibujaGrafica() {
        double anguloInicial = 0;
        double anguloFinal = 0;
        int i = 1;
        for (Double d : this.frecuenciasRelativas) {
            double grado = d*360;
            anguloInicial = anguloFinal;
            anguloFinal += grado;
            this.graficaDePastel += dibujaRebanada(anguloInicial, anguloFinal, i++);
        }
        return this.graficaDePastel + this.fin;
    }


    /**
     * dibujaRebanada. Usaremos la parametrización de R². 
     * La circunferencia con centro en C = (a,b) y  radio r se puede parametrizar
     * usando funciones trigonométricas de un solo parámetro θ para obtener una función paramétrica
     * f(θ)=(x, y). Y de aquí podemos calcular a x y a y como sigue:
     * x = a + rcos(θ).
     * y = b + rsen(θ).
     * con  0<=θ<2pi 
     * @param anguloInicial el ángulo desde donde debe iniciar la rebanada.
     * @param anguloFinal el ángulo donde debe acabar la rebanada.
     * @param i el color de la rebanada.
     * @return la rebanada.
     */
    public String dibujaRebanada(double anguloInicial, double anguloFinal, int i) {
        double xi = this.cx + (this.cx) * Math.cos(Math.toRadians(anguloInicial) - Math.toRadians(90));
        double yi = this.cy + (this.cy) * Math.sin(Math.toRadians(anguloInicial) - Math.toRadians(90));
        double xf = this.cx + (this.cx) * Math.cos(Math.toRadians(anguloFinal) - Math.toRadians(90));
        double yf = this.cy + (this.cy) * Math.sin(Math.toRadians(anguloFinal) - Math.toRadians(90));
        String path = "<path d='" + "M " + this.cx + ", " + this.cy + " L " + xi + " " + yi +
                      " A " + this.cx + " " + this.cy + "   0 0 1 " + xf + " " + yf + " z'" +
                      " fill=" + "'" + colores.get(i) + "'" + "/>\n";
        return path;
    }

}