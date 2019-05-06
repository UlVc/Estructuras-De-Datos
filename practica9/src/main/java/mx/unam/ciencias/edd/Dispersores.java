package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {

        int r = 0;

        if(llave.length % 4 != 0) {

            int x = 4 - llave.length % 4;

            for(int i = 0; i < llave.length + x - 4; i += 4)
                r ^= combinaBigEndian(llave[i], llave[i+1], llave[i+2], llave[i+3]);

            switch(x) {
                case 1:
                    r ^= combinaBigEndian(llave[llave.length-3], llave[llave.length-2], llave[llave.length-1], (byte) 0);
                    break;
                case 2:
                    r ^= combinaBigEndian(llave[llave.length-2], llave[llave.length-1], (byte) 0, (byte) 0);
                    break;
                case 3:
                    r ^= combinaBigEndian(llave[llave.length-1], (byte) 0, (byte) 0, (byte) 0);
                    break;
            }

            return r;
            
        }

        for(int i = 0; i < llave.length; i += 4)
            r ^= combinaBigEndian(llave[i],llave[i+1],llave[i+2],llave[i+3]);

        return r;

    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {

        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;
        int x = llave.length;
        int recorrido = 0;

        while(x > 11) {

            a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
            b += combinaLittleEndian(llave[recorrido+4], llave[recorrido+5], llave[recorrido+6], llave[recorrido+7]);
            c += combinaLittleEndian(llave[recorrido+8], llave[recorrido+9], llave[recorrido+10], llave[recorrido+11]);

            a -= b;
            a -= c;
            a ^= (c >> 13);
            b -= c;
            b -= a;
            b ^= (a << 8);
            c -= a;
            c -= b;
            c ^= (b >> 13);

            a -= b;
            a -= c;
            a ^= (c >> 12);
            b -= c;
            b -= a;
            b ^= (a << 16);
            c -= a;
            c -= b;
            c ^= (b >> 5);

            a -= b;
            a -= c;
            a ^= (c >> 3);
            b -= c;
            b -= a;
            b ^= (a << 10);
            c -= a;
            c -= b;
            c ^= (b >> 15);

            x -= 12;
            recorrido += 12;
        }

        c += llave.length;

        switch(x) {
            case 1:
                a += combinaLittleEndian(llave[recorrido], (byte) 0, (byte) 0, (byte) 0);
                break;
            case 2:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], (byte) 0, (byte) 0);
                break;
            case 3:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], (byte) 0);
                break;
            case 4:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
                break;
            case 5:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
                b += combinaLittleEndian(llave[recorrido+4], (byte) 0, (byte) 0, (byte) 0);
                break;
            case 6:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
                b += combinaLittleEndian(llave[recorrido+4], llave[recorrido+5], (byte) 0, (byte) 0);
                break;
            case 7:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
                b += combinaLittleEndian(llave[recorrido+4], llave[recorrido+5], llave[recorrido+6], (byte) 0);
                break;
            case 8:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
                b += combinaLittleEndian(llave[recorrido+4], llave[recorrido+5], llave[recorrido+6], llave[recorrido+7]);
                break;
            case 9:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
                b += combinaLittleEndian(llave[recorrido+4], llave[recorrido+5], llave[recorrido+6], llave[recorrido+7]);
                c += combinaLittleEndian((byte) 0, (byte) 0, (byte) 0, llave[recorrido+8]);
                break;
            case 10:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
                b += combinaLittleEndian(llave[recorrido+4], llave[recorrido+5], llave[recorrido+6], llave[recorrido+7]);
                c += combinaLittleEndian((byte) 0, (byte) 0, llave[recorrido+8], llave[recorrido+9]);
                break;
            case 11:
                a += combinaLittleEndian(llave[recorrido], llave[recorrido+1], llave[recorrido+2], llave[recorrido+3]);
                b += combinaLittleEndian(llave[recorrido+4], llave[recorrido+5], llave[recorrido+6], llave[recorrido+7]);
                c += combinaLittleEndian((byte) 0, llave[recorrido+8], llave[recorrido+9], llave[recorrido+10]);
                break;
        }

        a -= b;
        a -= c;
        a ^= (c >> 13);
        b -= c;
        b -= a;
        b ^= (a << 8);
        c -= a;
        c -= b;
        c ^= (b >> 13);

        a -= b;
        a -= c;
        a ^= (c >> 12);
        b -= c;
        b -= a;
        b ^= (a << 16);
        c -= a;
        c -= b;
        c ^= (b >> 5);

        a -= b;
        a -= c;
        a ^= (c >> 3);
        b -= c;
        b -= a;
        b ^= (a << 10);
        c -= a;
        c -= b;
        c ^= (b >> 15);

        return c;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        return 0;
    }

    /*private static void mezcla(int a, int b, int c) {

        a -= b;
        a -= c;
        a ^= (c >> 13);
        b -= c;
        b -= a;
        b ^= (a << 8);
        c -= a;
        c -= b;
        c ^= (b >> 13);

        a -= b;
        a -= c;
        a ^= (c >> 12);
        b -= c;
        b -= a;
        b ^= (a << 16);
        c -= a;
        c -= b;
        c ^= (b >> 5);

        a -= b;
        a -= c;
        a ^= (c >> 3);
        b -= c;
        b -= a;
        b ^= (a << 10);
        c -= a;
        c -= b;
        c ^= (b >> 15);

    }*/

    private static int combinaBigEndian(byte a, byte b, byte c, byte d) {
        return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF));
    }

    private static int combinaLittleEndian(byte a, byte b, byte c, byte d) {
        return ((d & 0xFF) << 24) | ((c & 0xFF) << 16) | ((b & 0xFF) << 8) | ((a & 0xFF));
    }

}
