package mx.unam.ciencias.edd.proyecto3;

/**
 * Clase que nos sirve para representar las palabras del archivo.
 */
public class Palabra implements Comparable<Palabra> {
    

    /** La palabra del archivo */
    private String palabra;
    /** El número de veces que aparece la palabra en el archivo. */
    private int numero;
    

    /**
     * Define el estado inicical de la palabra.
     * @param palabra la palabra del archivo.
     * @param numero el número de veces que aparece la palabra en el archivo.
     */
    public Palabra(String palabra, int numero) {
        this.palabra = palabra;
        this.numero = numero;
    }

    /**
     * getPalabra. Regresa la palabra.
     * @return la palabra.
     */
    public String getPalabra() {
        return this.palabra;
    }


    /**
     * getNumero. Regresa el número de veces que aparece la palabra en el archivo.
     * @return el número de veces que aparece la palabra.
     */
    public int getNumero() {
        return this.numero;
    }


    /**
     * Compara dos palabras por el número de veces que aparecen en el archivo.
     */
    @Override
    public int compareTo(Palabra palabra) {
        if (this.numero == palabra.numero) {
            return 0;
        } else if (this.numero > palabra.numero) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Regresa una representación de la palabra.
     */
    @Override
    public String toString() {
        String representacion = this.palabra + " : " + this.numero;
        return representacion;
    }
}