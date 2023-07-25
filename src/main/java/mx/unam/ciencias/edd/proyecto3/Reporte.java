package mx.unam.ciencias.edd.proyecto3;

import java.text.Normalizer;
import java.util.Iterator;
import java.io.File;
import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.proyecto3.Palabra;
import mx.unam.ciencias.edd.proyecto3.HTML;
import mx.unam.ciencias.edd.proyecto3.ArbolAVLSVG;
import mx.unam.ciencias.edd.proyecto3.ArbolBinarioSVG;
import mx.unam.ciencias.edd.proyecto3.ArbolRojinegroSVG;
import mx.unam.ciencias.edd.proyecto3.GraficaDePastel;
import mx.unam.ciencias.edd.proyecto3.GraficaSVG;
import mx.unam.ciencias.edd.proyecto3.GraficaDeBarras;
import mx.unam.ciencias.edd.proyecto3.CreaArchivo;


/**
 * Clase que nos sirve para representar el reporte en HTML del archivo que recibimos desde la terminal.
 */
public class Reporte {


    /** El archivo que contendrá la información del reporte en HTML. */
    private File reporte;
    /** Nos sirve para mapear palabras a enteros, contiene las palabras del archivo y el número que aparecen en él. */
    private Diccionario<String, Integer> diccionario;
    /** Nos sirve para almacenar cada parte que debe de contener el reporte en HTML */
    private Diccionario<String, String> contenido;
    /** La líneas del archivo que recibimos desde la terminal. */
    private Lista<String> lineasEnElArchivo;
    /** Las palabras que contiene el archivo. */
    private Lista<Palabra> palabras;
    /** Las palabra más comunes que aparecen en el archivo. */
    private Lista<Palabra> palabrasMasComunes;
    /** La lista de palabras del archivo ordenada de mayor a menor, de acuerdo con el número de veces que aparecen en el archivo. */
    private Lista<Palabra> ordenada;
    /** Las frecuencias relativas de las 8 palabras más comunes del archivo. La frecuencia relativa se calcula como, número de veces que aparece la palabra en el archivo entre el número total de palabras en el archivo. */
    private Lista<Double> frecuenciasRelativas;
    /** El conjunto de palabras del archivo de al menos 7 caracteres.  */
    private Conjunto<String> conjunto;
    /** El nombre del archivo. */
    private String archivo;
    /** El directorio en donde estará el reporte del archivo recibido desde la terminal. */
    private String directorio;
    /** La plantilla para el archivo HTML */
    private String reporteHTML = "HTML/PlantillaParaReporte.html";
    /** La plantilla para las 15 palabras más utilizadas en HTML. */
    private String palabrasMasUtilizadas = "HTML/15PalabrasMasUtilizadas.html";
    /** Las palabras que contiene el archivo. */
    private String palabrasTotales = "HTML/Palabras.html";
    /** El formato que se agregará a las palabras que aparezcan en la gráficas de pastel y de barras del reporte. */
    private String palabraGrafica = "HTML/PalabrasGrafica.html";
    /** El número total de palabras en el archivo. */
    private int palabrasEnElArchivo = 0;


    /**
     * Define el estado inicial del reporte del archivo.
     * @param lineasEnElArchivo las líneas del archivo.
     * @param archivo el nombre del archivo recibido desde la terminal.
     * @param directorio el directorio donde escribiremos el reporte del archivo en HTML.
     */
    public Reporte(Lista<String> lineasEnElArchivo, String archivo, String directorio) {
        this.lineasEnElArchivo = lineasEnElArchivo;
        this.archivo = archivo;
        this.directorio = directorio;
        this.diccionario = new Diccionario<String, Integer>();
        this.contenido = new Diccionario<String, String>();
        this.conjunto = new Conjunto<String>();
    }


    /**
     * Define el archivo del reporte.
     * @param reporte el archivo del reporte.
     */
    public void setReporte(File reporte) {
        this.reporte = reporte;
    }


    /**
     * Regresa el archivo del reporte.
     * @return el archivo del reporte.
     */
    public File getReporte() {
        return this.reporte;
    }


    /**
     * Regresa el nombre del archivo del reporte.
     * @return el nombre del archivo del reporte.
     */
    public String getArchivo() {
        return this.archivo;
    }


    /**
     * Regresa el directorio en donde estará escrito el reporte en formato HTML.
     * @return el directorio en donde estará escrito el reporte en formato HTML.
     */
    public String getDirectorio() {
        return this.directorio;
    }


    /**
     * Regresa el número de palabras en el archivo.
     * @return el número de palabras en el archivo.
     */
    public int getPalabrasEnElArchivo() {
        return this.palabrasEnElArchivo;
    }


    /**
     * Regresa el conjunto del archivo que contiene palabras del archivo de al menos 7 caracteres.
     * @return el conjunto del archivo que contiene palabras del archivo de al menos 7 caracteres.
     */
    public Conjunto<String> getConjunto() {
        return this.conjunto;
    }


    /**
     * palabrasEnArchivo. Calcula el número de veces que aparece una palabra en el archivo recibido desde la terminal.
     */
    public void palabrasEnArchivo() {
        for (String cadena : lineasEnElArchivo) {
            String[] lineas = cadenaNormalizada(cadena);
            for (String palabra : lineas) {
                palabrasEnElArchivo += 1;
                if (this.diccionario.contiene(palabra)) {
                    int contador = this.diccionario.get(palabra);
                    this.diccionario.agrega(palabra, contador + 1);
                } else {
                    this.diccionario.agrega(palabra, 1);
                }
            }
        }
        this.palabrasNumeroEnElArchivo();
        this.llenaConjunto();
    }


    /**
     * cadenaNormaliza. Recibe una línea del archivo, la normaliza, mantiene en ella solamente las palabras y los espacios en blanco.
     * @param cadena la cadena a normalizar.
     * @return un arreglo con las palabras de la línea del archivo.
     */
    public String[] cadenaNormalizada(String cadena) {
        cadena = Normalizer.normalize(cadena, Normalizer.Form.NFKD);
        cadena = cadena.replaceAll("[^\\p{IsAlphabetic}\\p{IsWhite_Space}]", "");
        cadena = cadena.trim();
        cadena = cadena.toLowerCase();
        String[] arreglo;
        if (cadena.equals("")) {
            arreglo = new String[0];
            return arreglo;
        } else {
            arreglo = cadena.split(" ");
            return arreglo;    
        }
    }


    /**
     * calculaFrecuenciasRelativas. Calcula las frecuencias relativas de cada palabra que estará en la Gráfica de Pastel y en la Gráfica de Barras.
     * @param palabrasGrafica la lista de palabras que aparecerán en la Gráfica de Pastel y en la Gráfica de Barras.
     * @return la lista con las frecuencias relativas de las palabras que aparecerán en la Gráfica de Pastel y en la Gráfica de Barras.
     */
    public Lista<Double> calculaFrecuenciasRelativas(Lista<Palabra> palabrasGrafica) {
        Lista<Double> frecuenciasRelativas = new Lista<Double>();
        for (Palabra palabra : palabrasGrafica) {
            frecuenciasRelativas.agregaFinal(Double.valueOf(palabra.getNumero()) / Double.valueOf(this.palabrasEnElArchivo));
        }
        return frecuenciasRelativas;
    }


    /**
     * palabrasNumeroEnElArchivo. Mapea palabras del archivo y con el número de veces que aparecen en el archivo, 
     * llena una lista con todas las palabras, y las ordena para poder utilizar las palabras más comunes en el archivo. 
     */
    public void palabrasNumeroEnElArchivo() {
        Iterator<String> iterador = this.diccionario.iteradorLlaves();
        palabras = new Lista<Palabra>();
        while (iterador.hasNext()) {
            String linea = iterador.next();
            int numero = this.diccionario.get(linea);
            palabras.agregaFinal(new Palabra(linea, numero));
        }
        ordenada = Lista.mergeSort(palabras).reversa();
        palabrasMasComunes = new Lista<Palabra>();
        for (int i = 0; i < 15; i++) {
            palabrasMasComunes.agregaFinal(ordenada.get(i));
        }
    }


    /**
     * agregaPalabrasHTML. Agrega las palabras del archivo al reporte en formato HTML del archivo.
     * @return las palabras del archivo en formato HTML para el reporte del archivo.
     */
    public String agregaPalabrasHTML() {
        String palabras = "";
        for (Palabra palabra : this.ordenada) {
            HTML palabrasHTML = new HTML(this.palabrasTotales);
            Diccionario<String, String> diccionarioPalabras = new Diccionario<String, String>();
            diccionarioPalabras.agrega("palabra", palabra.getPalabra());
            diccionarioPalabras.agrega("numero", Integer.toString(palabra.getNumero()));
            palabras += palabrasHTML.reemplaza(diccionarioPalabras);
        }
        return palabras;
    }


    /**
     * agrega15PalabrasMasUtilizadas. Agrega las 15 palabras más utilizadas del archivo al reporte en formato HTML del archivo.
     * @return las 15 palabras más utilizadas en formato HTML para el reporte del archivo.
     */
    public String agrega15PalabrasMasUtilizadas() {
        String palabrasComunes = "";
        for (Palabra palabra : this.palabrasMasComunes) {
            HTML comunes = new HTML(this.palabrasMasUtilizadas);
            Diccionario<String, String> palabras_Mas_Comunes = new Diccionario<String, String>();
            palabras_Mas_Comunes.agrega("palabra", palabra.getPalabra());
            palabras_Mas_Comunes.agrega("numero", Integer.toString(palabra.getNumero()));
            palabrasComunes += comunes.reemplaza(palabras_Mas_Comunes);
        }
        return palabrasComunes;
    }


    /**
     * agregaArboles. Agrega el Árbol Rojinegro y el Árbol AVL con las 15 palabras más utilizadas en el archivo al reporte.
     * @param elementos El número de veces que cada una de las 15 palabras aparece en el archivo. 
     */
    public void agregaArboles(Lista<Integer> elementos) {
        ArbolRojinegroSVG arbolRojinegro = new ArbolRojinegroSVG(elementos);
        this.contenido.agrega("ÁrbolRojinegro", arbolRojinegro.dibujaArbolRojinegro());
        ArbolAVLSVG arbolAVL = new ArbolAVLSVG(elementos);
        this.contenido.agrega("ÁrbolAVL", arbolAVL.dibujaArbolAVL());
    }


    /**
     * agregaGraficas. Agrega la Gráfica de Pastel y la Gráfica de Barras en el reporte del archivo.
     * @param palabraGrafica las palabra que contendrán ambas gráficas.
     * @return el diccionario que contiene la Gráfica de Pastel y la Gráfica de Barras en SVG.
     */
    public Diccionario<String, String> agregaGraficas(Lista<Palabra> palabraGrafica) {
        this.frecuenciasRelativas = calculaFrecuenciasRelativas(palabraGrafica);
        GraficaDePastel graficaDePastel = new GraficaDePastel(this.frecuenciasRelativas);
        this.contenido.agrega("GráficaDePastel", graficaDePastel.dibujaGrafica());
        GraficaDeBarras graficaDeBarras = new GraficaDeBarras(this.frecuenciasRelativas);
        this.contenido.agrega("GráficaDeBarras", graficaDeBarras.dibujaGrafica());
        return this.contenido;
    }


    /**
     * reporteHTML. Mapea en un diccionario todo lo que debe de tener el reporte del archivo con su respectiva información.
     * @return el reporte en HTML del archivo.
     */
    public String reporteHTML() {
        Diccionario<String, String> reporteHTML = new Diccionario<String, String>();
        HTML reporte = new HTML(this.reporteHTML);
        reporteHTML.agrega("Nombre_del_Archivo", this.archivo);
        reporteHTML.agrega("Número_de_palabras_en _el_archivo", Integer.toString(palabrasEnElArchivo));
        reporteHTML.agrega("Palabras_en_el_archivo", this.agregaPalabrasHTML());
        reporteHTML.agrega("15_palabras_más utilizadas_en_el_archivo", this.agrega15PalabrasMasUtilizadas());
        Lista<Integer> elementos = new Lista<Integer>();
        for (Palabra palabra : palabrasMasComunes) {
            elementos.agregaFinal(palabra.getNumero());
        }
        this.agregaArboles(elementos);
        Lista<Palabra> palabraGrafica = new Lista<Palabra>();
        for (int i = 0; i < 8; i++) {
            palabraGrafica.agregaFinal(palabrasMasComunes.get(i));
        }
        this.agregaGraficas(palabraGrafica);
        CreaArchivo archivo = new CreaArchivo(this.archivo);
        archivo.creaArchivo(this.agregaGraficas(palabraGrafica));
        Lista<String> rutas = archivo.getRutas();
        String[] arreglo = {"ArbolAVL", "GraficaDeBarras", "GraficaDePastel", "ArbolRojinegro"};
        for (int i = 0; i < rutas.getLongitud(); i++) {
            reporteHTML.agrega(arreglo[i], rutas.get(i));
        }
        Lista<Double> porcentajes = calculaPorcentaje(this.frecuenciasRelativas);
        String cadena = agregaPorcentajes(palabraGrafica, porcentajes);
        reporteHTML.agrega("Palabras_Grafica", cadena);
        return reporte.reemplaza(reporteHTML);
    }


    /**
     * calculaPorcentaje. Calcula los porcentajes que debe tener cada palabra en la Gráfica de Pastel y en la Gráfica de Barras.
     * @param frecuenciasRelativas las frecuencias relativas de las palabras que estarán en la Gráfica de Pastel y en la Gráfica de Barras.
     * @return la lista de porcentajes de las palabras que estarán en la Gráfica de Pastel y en la Gráfica de Barras.
     */
    public Lista<Double> calculaPorcentaje(Lista<Double> frecuenciasRelativas) {
        Lista<Double> porcentajes = new Lista<Double>();
        for (Double d : frecuenciasRelativas) {
            double porcentaje = Double.valueOf(d) *  100;
            porcentajes.agregaFinal(porcentaje);
        }
        return porcentajes;
    } 


    /**
     * agregaPorcentajes. Agrega los porcentajes de las palabras que estarán en la Gráfica de Pastel y en la Gráfica de Barras.
     * @param palabraGrafica la lista de palabras que estarán en la Gráfica de Pastel y en la Gráfica de Barras.
     * @param porcentajes la lista de porcentajes de las palabras que estarán en la Gráfica de Pastel y en la Gráfica de Barras.
     * @return los porcentajes de las palabras que estarán en la Gráfica de Pastel y en la Gráfica de Barras.
     */
    public String agregaPorcentajes(Lista<Palabra> palabraGrafica, Lista<Double> porcentajes) {
        String palabra = "";
        HTML h = new HTML(this.palabraGrafica);
        Diccionario<String, String> dic = new Diccionario<String, String>();
        dic.agrega("color", GraficaDePastel.colores.get(0));
        dic.agrega("palabra", String.format("restantes : %2.2f" , porcentajes.get(0)));
        palabra += h.reemplaza(dic);
        for (int i = 0; i < palabraGrafica.getLongitud(); i++) {
            int j = i + 1;
            HTML html = new HTML(this.palabraGrafica);
            Diccionario<String, String> diccionario = new Diccionario<String, String>();
            diccionario.agrega("color", GraficaDePastel.colores.get(j));
            diccionario.agrega("palabra", String.format("%s : %2.2f", palabraGrafica.get(i).getPalabra(),porcentajes.get(j)));
            palabra += html.reemplaza(diccionario);
        }
        return palabra;
    }


    /**
     * llenaConjunto. Llena un conjunto con las palabras de al menos 7 caracteres que están en el archivo.
     */
    private void llenaConjunto() {
        Iterator<String> iterador = this.diccionario.iteradorLlaves();
        while (iterador.hasNext()) {
            String palabra = iterador.next();
            if (palabra.length() >= 7) {
                this.conjunto.agrega(palabra);
            }
        }
    }
    
}
