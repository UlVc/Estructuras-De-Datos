package mx.unam.ciencias.edd.proyecto3.excepcion;

public class Excepcion {

    public static void error(String mensaje) {
        System.out.println(mensaje);
        System.exit(-1);
    }
}
