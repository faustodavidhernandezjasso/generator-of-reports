package mx.unam.ciencias.edd.proyecto3;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Diccionario;


/**
 * Clase que nos servirá para acceder a las plantillas de los archivos HTML.
 */
public class HTML {

    /** La plantila a la que accederemos en formato HTML. */
    private String archivoEnHTML;
    /** La plantilla en formato HTML que agregaremos a los archivos en formato HTML. */
    private String plantilla;


    /**
     * Define el estado inicial del reporte en formato HTML del archivo.
     * @param archivoEnHTML La plantilla a la que accederemos en formato HTML.
     */
    public HTML(String archivoEnHTML) {
        ClassLoader clase = getClass().getClassLoader();
        InputStream recurso = clase.getResourceAsStream(archivoEnHTML);
        this.plantilla = getPlantilla(recurso);
    }
    

    /**
     * getPlantilla. Accede al archivo en formato HTML y nos devuelve una cadena con toda las líneas del archivo en formato HTML
     * @param archivoEnHTML el archivo al cual debemos acceder en formato HTML
     * @return la cadena con todas las líneas del archivo en HTML.
     */
    public String getPlantilla(InputStream recurso) {
        Lista<String> lista = new Lista<String>();
        String cadena = null;
        try {
            InputStreamReader flujo = new InputStreamReader(recurso);
            BufferedReader leer = new BufferedReader(flujo);
            while ((cadena = leer.readLine()) != null) {
                lista.agregaFinal(cadena);
            }
            leer.close();
        } catch (IOException ioe) {
            System.err.println("Ha ocurrido un error al tratar de leer el archivo " + ioe.getMessage());
            System.exit(1);
        }
        String linea = "";
        for (String str : lista) {
            linea += str + "\n";
        }
        return linea;
    }


    /**
     * reemplaza. Reemplaza la información del archivo HTML por la información recibida que debe de llevar el reporte en HTML.
     * @param diccionario el diccionario en el cual está contenida toda la información que debe de contener el reporte en HTML.
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
    
}