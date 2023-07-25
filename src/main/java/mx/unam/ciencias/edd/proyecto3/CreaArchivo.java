package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Diccionario;
import java.util.Iterator;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.File;

/**
 * Clase para escribir archivos.
 */
public class CreaArchivo {


    /** El nombre del archivo */
    private String nombreDelArchivo;
    /** Lista que contiene las rutas de los archivos que escribimos */
    private Lista<String> rutas;


    /**
     * CreaArchivo. Crea un archivo.
     * @param nombreDelArchivo el nombre del archivo.
     */
    public CreaArchivo(String nombreDelArchivo) {
        this.nombreDelArchivo = nombreDelArchivo;
        this.rutas = new Lista<String>();
    }


    /**
     * creaArchivo. Crea el archivo y escribe en él la información que necesitamos.
     * @param contenido el contenido que queremos que tenga el archivo.
     */
    public void creaArchivo(Diccionario<String, String> contenido) {
        Iterator<String> iterador = contenido.iteradorLlaves();
        while (iterador.hasNext()) {
            String cadena = iterador.next();
            File archivo = new File(Argumentos.directorio, this.nombreDelArchivo + cadena + ".svg");
            this.rutas.agregaFinal(archivo.getAbsolutePath());
            try {
                FileOutputStream archivoEscribe = new FileOutputStream(archivo);
                OutputStreamWriter flujo = new OutputStreamWriter(archivoEscribe);
                BufferedWriter escribe = new BufferedWriter(flujo);
                escribe.write(contenido.get(cadena));
                escribe.close();
            } catch (IOException ioe) {
                System.err.println("Ha ocurrido un error al tratar de escribir en el archivo");
            }
        }
    }

    
    /**
     * getRutas. Regresa la lista de rutas absolutas de los archivos que escribimos.
     * @return la lista de rutas de los archivos.
     */
    public Lista<String> getRutas() {
        return this.rutas;
    }
    
}