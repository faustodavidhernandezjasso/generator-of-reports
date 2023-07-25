package mx.unam.ciencias.edd.proyecto3;


/**
 * Clase que representa el Proyecto 3.
 */
public class Proyecto3 {
    

    /** Nos sirve para leer los archivos recibidos desde la terminal
     * y escribir sus reportes en formato HTML con su respectivo 
     * archivo index.
     */
    private static Argumentos argumento;

    /**
     * Proyecto 3 : Generador de Reportes en HTML.
     */
    public static void main(String[] args) {
        argumento = new Argumentos();
        argumento.revisaArgumentos(args);
    }
    
}