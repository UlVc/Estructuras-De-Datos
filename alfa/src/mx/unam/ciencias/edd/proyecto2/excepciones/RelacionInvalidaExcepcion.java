package mx.unam.ciencias.edd.proyecto2.excepciones;

/**
 * Clase para excepciones de relaciones inválidas en gráficas.
 */
public class RelacionInvalidaExcepcion extends RuntimeException {

	public RelacionInvalidaExcepcion() {
		super("La relación de la grafica no es válida.");
	}

}