package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.Indexable;

/* Clase para construir las Estructuras de Datos e imprimpirlas en un formato SVG. */
public class ConstructorEDD {

	/* Obtiene elementos de un arreglo para introducirlos en un ArbolRojinegro. */
	public static ArbolRojinegro<Integer> construirArbolRojinegro(Lista<Integer> elementos) throws NumberFormatException {
		ArbolRojinegro<Integer> abrn = new ArbolRojinegro<Integer>();
		for(int i=0;i<elementos.getLongitud();i++) { abrn.agrega(elementos.get(i)); }
		return abrn;
	}

	/* Obtiene elementos de un arreglo para introducirlos en un ArbolAVL. */
	public static ArbolAVL<Integer> construirArbolAVL(Lista<Integer> elementos) throws NumberFormatException {
		ArbolAVL<Integer> ab = new ArbolAVL<Integer>();
		for(int i=0;i<elementos.getLongitud();i++) { ab.agrega(elementos.get(i)); }
		return ab;
	}

}