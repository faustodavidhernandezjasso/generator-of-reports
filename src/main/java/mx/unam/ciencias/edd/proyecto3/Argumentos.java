package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.LeerYEscribir;


/**
 * Clase que nos servirá para trabajar con los argumentos recibidos desde la terminal.
 */
public class Argumentos {


    /** Nos va a servir para leer lo que hay en los archivos recibidos en el terminal y escribir sus respectivos reportes */
    private LeerYEscribir lye;
    /** Bandera. */
    private static String bandera = "-o";
    /** El directorio que nos pasan desde la terminal. */
    public static String directorio = null;
    /** Nos indica sí en la terminal nos pasaron la bandera -o. */
    private boolean hayBandera = false;
    /** La lista con los archivos recibidos desde la terminal.  */
    private Lista<String> archivos = new Lista<String>();


    /**
     * revisaArgumentos. Revisa los argumentos recibidos desde la terminal.
     * @param args los argumentos recibidos desde la terminal.
     */
    public void revisaArgumentos(String[] args) {
        String auxiliar = "";
        for (String cadena : args) {
	        if (auxiliar.equals(bandera)) {
                auxiliar = cadena;
                directorio = cadena;
                hayBandera = true;
	            continue;
            }
            auxiliar = cadena;
            if (cadena.equals(bandera)) {
                hayBandera = true;
                continue;
            }
            archivos.agregaFinal(cadena);
        }
        if (hayBandera == false || directorio == null) {
            System.err.println("Por favor introduzca un directorio seguido de la bandera -o");
            System.exit(1);
        }
        lye = new LeerYEscribir();
        lye.leerArchivo(archivos);
    }

}