package mx.unam.ciencias.edd.proyecto2.exception;

/**
 * Clase para excepciones de estructuras de datos no validas.
 */
public class EddInvalidoExcepcion extends RuntimeException {

    public EddInvalidoExcepcion() {
        super("La estructura de datos es inválida.");
    }

}