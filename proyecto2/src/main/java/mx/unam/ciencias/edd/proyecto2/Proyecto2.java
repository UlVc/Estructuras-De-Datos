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

		Lista<Integer> lista = new Lista<Integer>();
		EstructurasDatosSVG edSVG = new EstructurasDatosSVG();

			switch(estructura) {
				case Lista:
					System.out.println(edSVG.lista(elementos));
					break;
				case Cola:
					System.out.println(edSVG.meteSaca(construirCola(elementos)));
					break;
				case Pila:
					System.out.println(edSVG.meteSaca(construirPila(elementos)));
					break;
				case ArbolBinario:
					System.out.println(edSVG.arbolBinario(new ArbolBinarioOrdenado<Integer>(elementos), estructura));
					break;
				case ArbolBinarioOrdenado:
					System.out.println(edSVG.arbolBinario(new ArbolBinarioOrdenado<Integer>(elementos), estructura));
					break;
				case ArbolBinarioCompleto:
					System.out.println(edSVG.arbolBinario(new ArbolBinarioCompleto<Integer>(elementos), estructura));
					break;
				case ArbolRojinegro:
					System.out.println(edSVG.arbolBinario(construirArbolRojinegro(elementos), estructura));
					break;
				case ArbolAVL:
					System.out.println(edSVG.arbolBinario(construirArbolAVL(elementos), estructura));
					break;
				case Monticulo:
					MonticuloMinimo<Indexable<Integer>> monticulo = new MonticuloMinimo<Indexable<Integer>>(obtenerElementosListaIndexable(new Lista<Indexable<Integer>>(), elementos));
					System.out.println(edSVG.monticulo(monticulo));
					break;
				case Grafica:
					try {
						System.out.println(edSVG.grafica(construirGrafica(elementos)));
						break;
					}catch(IllegalArgumentException iae) {
						System.out.println("Introduzca de manera correcta los elementos y las relaciones.");
						System.exit(-1);
					}
			}	
	}

	/* Lee un archivo de texto. */
	private static void leeArchivo(String path) throws Exception {

		Lista<String> st = new Lista<String>();
		String cadena = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			while((br.readLine()) != null)
   				st.agrega(br.readLine().trim());
   		}catch(Exception e) {
   			System.out.println("Introduzca de manera correcta un archivo de texto.");
   			System.exit(-1);
   		}

   		try {
			estructura = EstructurasDeDatos.valueOf(st.get(0));
		}catch(IllegalArgumentException e) {
			System.out.println("Estructura inválida.");
			System.exit(-1);
		}

		for(int i=1;i<st.getLongitud();i++)
			cadena += ";" + st.get(i).replace(" ",";");

		String[] arrayElementos = cadena.split(";");

		try {
			for(int i=1;i<arrayElementos.length;i++)
				elementos.agrega(Integer.parseInt(arrayElementos[i]));
		}catch(NumberFormatException nfe) {
			System.out.println("Introducir solamente numeros enteros.");
			System.exit(-1);
		}

		construirEstructura(elementos,estructura);
		System.exit(-1);
	}

	public static void main(String[] args) throws Exception {

		if(args.length == 1)
			leeArchivo(args[0].trim());

		if(args.length == 0) {
			System.out.println("Introduzca una estructura de datos y sus elementos o un archivo de texto.");	
			System.exit(-1);
		}

		try {
			estructura = EstructurasDeDatos.valueOf(args[0]);
		}catch(IllegalArgumentException e) {
			System.out.println("Estructura inválida.");
			System.exit(-1);
		}

		try {
			for(int i=1;i<args.length;i++)
				elementos.agrega(Integer.parseInt(args[i].trim()));
		}catch(NumberFormatException nfe) {
			System.out.println("Introducir únicamente números enteros.");
			System.exit(-1);
		}

		construirEstructura(elementos, estructura);

	}

}