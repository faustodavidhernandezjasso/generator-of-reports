package mx.unam.ciencias.edd.proyecto3;

import java.io.FileInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.Reporte;
import mx.unam.ciencias.edd.proyecto3.Palabra;
import mx.unam.ciencias.edd.proyecto3.IndexHTML;

/**
 * Clase que nos permite leer los archivos introducidos desde la terminal y escribir sus reportes en HTML
 * junto con su respectivo archivo index también formato HTML.
 */
public class LeerYEscribir {


    /** La lista que reportes a la que agregaremos los archivos recibidos desde la terminal.*/
    private Lista<Reporte> reportes = new Lista<Reporte>();


    /**
     * leerArchivo. Lee el contenido de cada uno de los archivos recibidos desde la terminal y por cada archivo,
     * cree una instancia de la clase Reporte, con la cual trabajaremos para escribir el reporte del archivo. 
     * @param archivos la lista de nombre de los archivos, recibidos desde la terminal.
     */
    public void leerArchivo(Lista<String> archivos) {
        for (String arch : archivos) {
            try {
                Lista<String> renglones = new Lista<String>();
                File archivo = new File(arch);
                FileInputStream archivoLeer = new FileInputStream(archivo);
                InputStreamReader flujo = new InputStreamReader(archivoLeer);
                BufferedReader leer = new BufferedReader(flujo);
                String linea = null;
                while ((linea = leer.readLine()) != null) {
                    renglones.agregaFinal(linea);
                }
                Reporte reporte = new Reporte(renglones, archivo.getName(), Argumentos.directorio);
                reporte.palabrasEnArchivo();
                reportes.agregaFinal(reporte);
                leer.close();
            } catch (IOException ioe) {
                System.err.println("Hubo un error al tratar de leer el archivo");
            }
        }
        crearDirectorio();
        for (Reporte rep : this.reportes) {
            this.escribeReporteHTML(rep);
        }
        this.escribeIndex();
    }
    

    /**
     * crearDirectorio. Crea el directorio en el cual estarán todos los reportes generados y
     * el archivo index, junto con sus respectivos gráficos en SVG de cada archivo.
     * @return la ruta donde estará el directorio.
     */
    public void crearDirectorio() {
        File directorio = new File(Argumentos.directorio);
        if (!(directorio.isDirectory()) && !(directorio.mkdirs())) {
            System.err.println("Hubo un error al crear el directorio");
            System.exit(1);
        }
    }


    /**
     * escribeReporteHTML. Escribe el reporte en HTML del archivo recibido desde la terminal.
     * @param reporte el reporte del archivo el cual se escribirá en HTML.
     */
    public void escribeReporteHTML(Reporte reporte) {
        File archivo = new File(Argumentos.directorio, reporte.getArchivo() + ".html");
        reporte.setReporte(archivo);
        try {
            FileOutputStream fos = new FileOutputStream(archivo);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter escribir = new BufferedWriter(osw);
            escribir.write(reporte.reporteHTML());
            escribir.close();
        } catch (IOException ioe) {
            System.err.println("Ha ocurrido un error al tratar de escribir el archivo HTML " + ioe.getMessage());
        }
    }


    /**
     * escribeIndex. Escribe el archivo index en HTML.
     */
    public void escribeIndex() {
        File archivoIndex = new File(Argumentos.directorio, "index.html");
        IndexHTML index = new IndexHTML(reportes);
        try {
            FileOutputStream archivo = new FileOutputStream(archivoIndex);
            OutputStreamWriter flujo = new OutputStreamWriter(archivo);
            BufferedWriter escribir = new BufferedWriter(flujo);
            escribir.write(index.index());
            escribir.close();
        } catch (IOException ioe) {
            System.err.println("Ha ocurrido un error al tratar de escribir el archivo index.html");
            System.exit(1);
        }
    }

}