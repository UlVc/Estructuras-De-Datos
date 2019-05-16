package mx.unam.ciencias.edd.proyecto3.eddsvg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.util.Iterator;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.eddsvg.Indexable;

/* Clase para construir las Estructuras de Datos e imprimpirlas en un formato SVG. */
public class ConstructorEDD {

	/* Obtiene elementos de un arreglo para introducirlos en un ArbolRojinegro. */
	public static ArbolRojinegro<Integer> construirArbolRojinegroEntero(Diccionario<String, Integer> elementos) throws NumberFormatException {
		ArbolRojinegro<Integer> abrn = new ArbolRojinegro<Integer>();

		for (Integer i : elementos)
			abrn.agrega(i);

		return abrn;
	}

	public static ArbolRojinegro<String> construirArbolRojinegroCadena(Diccionario<String, Integer> elementos) throws NumberFormatException {
		ArbolRojinegro<String> abrn = new ArbolRojinegro<String>();

		Iterator<String> iteradorLLave = elementos.iteradorLlaves();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            abrn.agrega(llave);
        }

		return abrn;
	}

	/* Obtiene elementos de un arreglo para introducirlos en un ArbolAVL. */
	public static ArbolAVL<Integer> construirArbolAVLEntero(Diccionario<String, Integer> elementos) throws NumberFormatException {
		ArbolAVL<Integer> ab = new ArbolAVL<Integer>();

		for (Integer i : elementos)
			ab.agrega(i);

		return ab;
	}

	/* Obtiene elementos de un arreglo para introducirlos en un ArbolAVL. */
	public static ArbolAVL<String> construirArbolAVLCadena(Diccionario<String, Integer> elementos) throws NumberFormatException {
		ArbolAVL<String> ab = new ArbolAVL<String>();

		Iterator<String> iteradorLLave = elementos.iteradorLlaves();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            ab.agrega(llave);
        }

		return ab;
	}

}