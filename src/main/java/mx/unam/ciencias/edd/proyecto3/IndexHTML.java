package mx.unam.ciencias.edd.proyecto3;

import java.util.Iterator;
import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.proyecto3.Reporte;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.proyecto3.GraficaSVG;
import mx.unam.ciencias.edd.proyecto3.CreaArchivo;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * Clase que nos sirve para representar el index de los reporter generados en HTML.
 */
public class IndexHTML {
 

    /** La plantilla que utilizaremos para generar el archivo index en formato HTML. */
    private String index = "HTML/index.html";
    /** La plantilla que utilizaremos para hacer referencia a los demás archivos generados, en el archivo index */
    private String plantillaArchivo = "HTML/ArchivoIndex.html";
    /** Los nombres de los archivos. */
    private String nombres = "HTML/Nombres.html";
    /** La plantilla en formato HTML que agregaremos al archivo index en formato HTML. */
    private String plantilla;
    /** El número de archivos de los que tenemos que generar el reporte en HTML. */
    private int archivos = 0;
    /** La lista archivos de los que tenemos que generar el reporte en HTML. */
    private Lista<Reporte> reportes;
    /** Lista que contiene las lineas de los archivos en formato HTML. */
    private Lista<String> lineas;
    /** Diccionario que contiene la información que contendra el archivo index. */
    private Diccionario<String, String> indexHTML;
    /** Gráfica que nos sirve para representar las relaciones entre los archivos.
     * Recordamos que dos archivos están conectados en la gráfica, sí y solamente sí, tienen una palabra
     * en común de más de 7 carácteres. */
    private Grafica<Integer> grafica;
   

    /**
     * Define el estado inicial del archivo index.
     * @param reportes la lista de reportes que estarán como referencias en el archivo index. 
     */
    public IndexHTML(Lista<Reporte> reportes) {
        this.reportes = reportes;
        this.plantilla = "";
        this.grafica = new Grafica<Integer>();
        this.indexHTML = new Diccionario<String, String>();
    }


    /**
     * leerPlantilla. Lee el archivo en formato HTML que utilizaremos para cada parte del index.
     * @param archivo el archivo al que accederemos en formato HTML.
     */
    public void leerPlantilla(String archivo) {
        try {
            this.lineas = new Lista<String>();
            ClassLoader clase = getClass().getClassLoader();
            InputStream recurso = clase.getResourceAsStream(archivo);
            InputStreamReader flujo = new InputStreamReader(recurso);
            BufferedReader leer = new BufferedReader(flujo);
            String auxiliar = null;
            while ((auxiliar = leer.readLine()) != null) {
                lineas.agregaFinal(auxiliar);
            }
            leer.close();
        } catch (IOException ioe) {
            System.err.println("Ha habido un error al tratar de leer el archivo");
        }
        for (String cadena : lineas) {
            this.plantilla += cadena + "\n";
        }
    }


    /**
     * reemplaza. Reemplaza la información del archivo HTML por la información recibida que debe de llevar el index en HTML.
     * @param diccionario el diccionario en el cual está contenida toda la información que debe de contener el index en HTML.
     * @return la plantilla con la información debe de llevar el reporte pero en formato HTML.
     */
    public String reemplaza(Diccionario<String, String> diccionario) {
        Iterator<String> iterador = diccionario.iteradorLlaves();
        while (iterador.hasNext()) {
            String llave = iterador.next();
            String cadena = "\\$ " + llave + " \\$";
            String valor = diccionario.get(llave);
            this.plantilla = this.plantilla.replaceAll(cadena, valor);
        }
        return this.plantilla;
    }


    /**
     * index. Determina todo lo que debe de contener el archivo index y devuelve toda la información en formato HTML.  
     * @return la información que debe de contener el archivo index, la devuelve en formato HTML.
     */
    public String index() {
        for (Reporte reporte : this.reportes) {
            this.archivos += 1;
            String archivoIndex = "";
            Diccionario<String, String> diccionario = new Diccionario<String, String>();
            diccionario.agrega("ruta_del_arhivo", reporte.getReporte().getAbsolutePath());
            diccionario.agrega("nombre_del_archivo", reporte.getArchivo());
            diccionario.agrega("numero_de_palabras_en_el_archivo", Integer.toString(reporte.getPalabrasEnElArchivo()));
            this.leerPlantilla(this.plantillaArchivo);
            archivoIndex += this.reemplaza(diccionario);
            indexHTML.agrega("Archivos", archivoIndex);
        }
        this.plantilla = "";
        for (Reporte reporte : this.reportes) {
            String nombre = "";
            Diccionario<String, String> dicc = new Diccionario<String, String>();
            dicc.agrega("Nombre", reporte.getArchivo());
            this.leerPlantilla(this.nombres);
            nombre += this.reemplaza(dicc);
            this.indexHTML.agrega("Nombres",nombre);
        }
        this.dibujaGraficaenIndex();
        this.plantilla = "";
        this.leerPlantilla(this.index);
        return this.reemplaza(this.indexHTML);
    }


    /**
     * dibujaGraficaenIndex. Dibuja la gráfica que debe de estar en el index y crea el archivo donde estará el SVG de la gráfica del index.
     */
    public void dibujaGraficaenIndex() {
        for (int i = 1; i <= archivos; i++) {
            this.grafica.agrega(Integer.valueOf(i));
        }
        int contador = 1;
        while (contador < this.reportes.getLongitud() + 1) {
            for (int i = 1; i < this.reportes.getLongitud() + 1; i ++) {
                if (contador != i) {
                    Conjunto<String> s = this.reportes.get(contador-1).getConjunto().interseccion(this.reportes.get(i-1).getConjunto());
                    if (!(s.esVacia())) {
                        if (!(this.grafica.sonVecinos(Integer.valueOf(contador), Integer.valueOf(i)))) {
                            this.grafica.conecta(Integer.valueOf(contador), Integer.valueOf(i));
                        }
                    }
                }
            }
            contador += 1;
        }
        GraficaSVG graficaEnSVG = new GraficaSVG(this.grafica);
        Diccionario<String, String> contenido = new Diccionario<String, String>();
        contenido.agrega("Gráfica", graficaEnSVG.dibujaGrafica());
        CreaArchivo crea = new CreaArchivo("");
        crea.creaArchivo(contenido);
        this.indexHTML.agrega("Gráfica", crea.getRutas().get(0));
    }
}