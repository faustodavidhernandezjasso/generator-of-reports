package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    private static int combina(byte a, byte b, byte c, byte d) {
        int entero = ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | (d & 0xFF);
        return entero;
    }

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        if (llave.length % 4 != 0) {
            int longitud = 4 - (llave.length % 4);
            byte[] arreglo = new byte[llave.length + longitud];
            for (int i = 0; i < arreglo.length; i++) {
                if (i < llave.length) {
                    arreglo[i] = llave[i];
                } else {
                    arreglo[i] = (byte) 0;
                }
            }
            int r = 0;
            for(int i = 0; i < arreglo.length; i += 4) {
                int n = combina(arreglo[i], arreglo[i+1], arreglo[i+2], arreglo[i+3]);
                r ^= n;
            }
            return r;
        } else {
            int r = 0;
            for (int i = 0; i < llave.length; i += 4) {
                int n = combina(llave[i], llave[i+1], llave[i+2], llave[i+3]);
                r ^= n;
            }
            return r;
        }
        // Aquí va su código.
    }

    private static int[] mezcla(int a, int b, int c) {
        int[] arreglo = new int[3];
        //1era parte de mezcla
        a -= b;
        a -= c;
        a ^= (c >>> 13);
        b -= c;
        b -= a;
        b ^= (a << 8);
        c -= a;
        c -= b;
        c ^= (b >>> 13);
        //2da parte de mezcla
        a -= b;
        a -= c;
        a ^= (c >>> 12);
        b -= c;
        b -= a;
        b ^= (a << 16);
        c -= a;
        c -= b;
        c ^= (b >>> 5);
        //3ra parte de mezcla
        a -= b;
        a -= c;
        a ^= (c >>> 3);
        b -= c;
        b -= a;
        b ^= (a << 10);
        c -= a;
        c -= b;
        c ^= (b >>> 15);
        arreglo[0] = a;
        arreglo[1] = b;
        arreglo[2] = c;
        return arreglo;
    }

    private static int combinaBJ(byte a, byte b, byte c, byte d) {
        int entero = ((a & 0xFF)) | ((b & 0xFF) << 8) | ((c & 0xFF) << 16) | ((d & 0xFF) << 24);
        return entero;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a = 0x9e3779b9;
        int b = 0x9e3779b9;
        int c = 0xffffffff;
        int residuo = llave.length % 12;
        int j = llave.length - residuo;
        for (int i = 0; i < j; i+=12) {
            int d = combinaBJ(llave[i], llave[i+1], llave[i+2], llave[i+3]);
            int e = combinaBJ(llave[i+4], llave[i+5], llave[i+6], llave[i+7]);
            int f = combinaBJ(llave[i+8], llave[i+9], llave[i+10], llave[i+11]);
            a += d;
            b += e;
            c += f;
            int[] enteros = mezcla(a, b, c);
            a = enteros[0];
            b = enteros[1];
            c = enteros[2];
        }
        c += llave.length;
        switch(residuo) {
            case 11:
                c += (llave[j+10] & 0xFF) << 24;
            case 10:
                c += (llave[j+9] & 0xFF) << 16;
            case 9:
                c += (llave[j+8] & 0xFF) << 8;
            case 8:
                b += (llave[j+7] & 0xFF) << 24;
            case 7:
                b += (llave[j+6] & 0xFF) << 16;
            case 6:
                b += (llave[j+5] & 0xFF) << 8;
            case 5:
                b += llave[j+4] & 0xFF;
            case 4:
                a += (llave[j+3] & 0xFF) << 24;
            case 3:
                a += (llave[j+2] & 0xFF) << 16;
            case 2:
                a += (llave[j+1] & 0xFF) << 8;
            case 1:
                a += llave[j] & 0xFF; 
        }
        int[] enteros = mezcla(a, b, c);
        return enteros[2];
        // Aquí va su código.
    } 

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        int h = 5381;
        for (int i = 0; i < llave.length; i++) {
            h *= 33;
            h += llave[i] & 0xFF;
        }
        return h;
        // Aquí va su código.
    }
}
