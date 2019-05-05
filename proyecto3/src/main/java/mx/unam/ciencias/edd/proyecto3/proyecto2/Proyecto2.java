package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.SVG.*;

public class Proyecto2 extends ConstructorEDD {

	private static EstructurasDeDatos estructura = null;
	private static Lista<Integer> elementos = new Lista<Integer>();

	/* Lee los elementos que se le pasaron después del tipo de estructura de datos y construye un código SVG.*/
	public static void construirEstructura(Lista<Integer> elementos, EstructurasDeDatos estructura) throws Exception {

		EstructurasDatosSVG edSVG = new EstructurasDatosSVG();

		switch(estructura) {
			case ArbolRojinegro:
				System.out.println(edSVG.arbolBinario(construirArbolRojinegro(elementos), estructura));
				break;
			case ArbolAVL:
				System.out.println(edSVG.arbolBinario(construirArbolAVL(elementos), estructura));
				break;
		}

	}

}