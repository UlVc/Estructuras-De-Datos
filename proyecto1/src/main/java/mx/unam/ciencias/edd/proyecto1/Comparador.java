package mx.unam.ciencias.edd.proyecto1;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * <p> Clase Comparador que recibe un String.</p>
 *
 * <p> Clase para comparar las cadenas de forma de distinta.
 * Se ingoran las minúsculas, carácteres especiales y mayúsculas.
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
		String a = Normalizer.normalize(cad.toString(),Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","").replaceAll("[^a-zA-Z0-9]","").toLowerCase();
		String b = Normalizer.normalize(this.cadena,Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+","").replaceAll("[^a-zA-Z0-9]","").toLowerCase();
		return b.compareTo(a);
	}

}