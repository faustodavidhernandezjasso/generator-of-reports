package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;


/**
 * Clase que nos va a servir para representar la Gráfica de Barras en el reporte HTML.
 */
public class GraficaDeBarras {


    /** La Gráfica de Barras */
    private String graficaDeBarras = "";
    /** El inicio del archivo SVG */
    private String inicio = "<?xml version='1.0' encoding='UTF-8' ?>\n";
    /** El fin del archivo SVG */
    private String fin = "</g>\n" + "</svg>";
    /** Las medidas del archivo SVG */
    private String grafica = "<svg width='550' height='500' xmlns='http://www.w3.org/2000/svg'>\n" + "<g>\n";
    /** Los porcentajes 25%, 50%, 75% y 100% que aparecen en la gráfica de barras. */
    private String[] porcentajes = {
                                    "<text x='5' y='350' class='heavy'>25%</text>",
                                    "<text x='5' y='250' class='heavy'>50%</text>",
                                    "<text x='5' y='150' class='heavy'>75%</text>",
                                    "<text x='5' y='48' class='heavy'>100%</text>",
                                    "<line x1='50' y1='350' x2='42' y2='350' stroke='black' stroke-width='3' />",
                                    "<line x1='50' y1='250' x2='42' y2='250' stroke='black' stroke-width='3' />",
                                    "<line x1='50' y1='150' x2='42' y2='150' stroke='black' stroke-width='3' />",
                                    "<line x1='50' y1='50' x2='42' y2='50' stroke='black' stroke-width='3' />"
                                   };
    /** La línea horizontal de la gráfica de barras. */
    private String lineaHorizontal = "<line x1='48' y1='450' x2='500' y2='450' stroke='black' stroke-width='3' />\n";
    /** La línea vertical de la gráfica de barras. */
    private String lineaVertical = "<line x1='48' y1='452' x2='48' y2='50' stroke='black' stroke-width='3' />\n";
    /** Las frecuencias relativas de cada palabra en la gráfica de barras. */
    private Lista<Double> frecuenciasRelativas;
    /** La posición en x de la primera barra de la Gráfica de Barras. */
    private int x = 49;
    /** La posición en y de la primera barra de la Gráfica de Barras. */
    private double y = 450;


    /**
     * Define el estado inicial de la Gráfica de Barras. 
     * @param frecuenciasRelativas la lista con la frecuencias relativas de cada palabra para poder graficar cada barra.
     */
    public GraficaDeBarras(Lista<Double> frecuenciasRelativas) {
        this.frecuenciasRelativas = frecuenciasRelativas;
        this.graficaDeBarras += this.inicio + this.grafica;
        for (String porcentaje : porcentajes) {
            this.graficaDeBarras += porcentaje + "\n";
        }
        this.graficaDeBarras += this.lineaHorizontal + this.lineaVertical;
    }


    /**
     * dibujaGrafica. Dibuja la gráfica de barras.
     * @return la gráfica de barras en SVG.
     */
    public String dibujaGrafica() {
        double restante = 1;
        for (Double d : this.frecuenciasRelativas) {
            restante -= d;
        }
        int i = 0;
        this.frecuenciasRelativas.agregaInicio(Double.valueOf(restante));
        for (Double d : this.frecuenciasRelativas) {
            double y1 = this.y - (400)*(d);
            double altura = (400) * (d);
            this.graficaDeBarras += this.dibujaBarra(y1, altura, i);
            i += 1;
        }
        return this.graficaDeBarras + this.fin;
    }


    /**
     * dibujaBarra. Dibuja la barra
     * @param y1 la posición de la barra.
     * @param altura la altura de la barra.
     * @param i el color de la barra.
     * @return la barra en SVG.
     */
    public String dibujaBarra(double y1, double altura, int i) {
        String rectangulo = "<rect x='" + this.x + "' y='" + y1 + "' width='50' height='" + altura + 
                            "' fill='" + GraficaDePastel.colores.get(i) + "'/>\n";
        this.x += 50;
        return rectangulo;
    }
    
}