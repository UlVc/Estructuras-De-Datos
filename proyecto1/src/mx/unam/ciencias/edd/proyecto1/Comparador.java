package mx.unam.ciencias.edd.proyecto1;

import java.text.Collator;

/**
 * <p> Clase Comparador que recibe un String.</p>
 *
 * <p> Clase para comparar las cadenas de forma de distinta.
 * Se ingoran las minúsculas, carácteres especiales y mayúsculas.
 * Se implementó {@link Comparable} para sobreescribir el método
 * {@link Comparable#compareTo(Object)}. </p>
 *
 */
public class Comparador implements Comparable<Comparador> {

	String cadena;

	/* Constructor */
	public Comparador(String cadena) {
		this.cadena = cadena;
	}

	/* Método que nos regresa una copia de la cadena. */
	@Override public String toString() {
		return cadena;
	}

	/* Método que realiza comparaciones para que acomoda el texto. */
	@Override public int compareTo(Comparador cad) {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.PRIMARY);
		return collator.compare(cadena.replaceAll("\\P{Lower}+", ""), cad.toString().replaceAll("\\P{Lower}+", "")); //P{Lower}+ := letras minúsculas de [a,z]
	}

}