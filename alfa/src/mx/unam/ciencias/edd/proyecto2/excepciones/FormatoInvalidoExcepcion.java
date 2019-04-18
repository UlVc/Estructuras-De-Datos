package mx.unam.ciencias.edd.proyecto2.excepciones;

/**
 * Clase para excepciones con formato invalido.
 */
public class FormatoInvalidoExcepcion extends RuntimeException {

    public FormatoInvalidoExcepcion() {
        super("Formato inválido.");
    }

}