package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.Indexable;

/* Clase para construir las Estructuras de Datos e imprimpirlas en un formato SVG. */
public class ConstructorEDD {

	/* Obtiene elementos de un arreglo para volverlos Indexeables y introducirlos en una Lista. */
	public static Lista<Indexable<Integer>> obtenerElementosListaIndexable(Lista<Indexable<Integer>> li, Lista<Integer> elementos) throws NumberFormatException {
		for(int i: elementos) { li.agrega(new Indexable<Integer>(i,i)); }
		return li;
	}

	/* Obtiene elementos de un arreglo para introducirlos en una cola. */
	public static Cola<Integer> construirCola(Lista<Integer> elementos) throws NumberFormatException {
		Cola<Integer> c = new Cola<Integer>();
		for(int i=0;i<elementos.getLongitud();i++) { c.mete(elementos.get(i)); }
		return c;
	}

	/* Obtiene elementos de un arreglo para introducirlos en una Pila. */
	public static Pila<Integer> construirPila(Lista<Integer> elementos) throws NumberFormatException {
		Pila<Integer> p = new Pila<Integer>();
		for(int i=0;i<elementos.getLongitud();i++) { p.mete(elementos.get(i)); }
		return p;
	}

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

	/* Obtiene elementos y relaciones de dos arreglos para introducirlos en una Grafica. */
	public static Grafica<Integer> construirGrafica(Lista<Integer> elementos) throws NumberFormatException {

		Lista<String> relaciones = new Lista<String>();
		Grafica<Integer> g = new Grafica<Integer>();
		try {
			for(int i=0;i<elementos.getLongitud();i++)
				if(elementos.get(i)<10)
					g.agrega(elementos.get(i));
				else
					relaciones.agrega(Integer.toString(elementos.get(i)));

			for(int i=0;i<relaciones.getLongitud();i++)
				g.conecta(Integer.parseInt(relaciones.get(i).substring(0,1)), Integer.parseInt(relaciones.get(i).substring(1,2)));
		}catch(Exception e) {
			System.out.println("Introduzca relaciones validas.");
			System.exit(-1);
		}
		return g;
	}

}