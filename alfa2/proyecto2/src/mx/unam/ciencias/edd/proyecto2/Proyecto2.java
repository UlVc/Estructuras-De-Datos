package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

import mx.unam.ciencias.edd.*;

public class Proyecto2 {

	/* Obtiene elementos de un arreglo para meterlos en una lista. */
	public static Lista<Integer> obtenerElementosLista(Lista<Integer> l, Lista<Integer> elementos) throws NumberFormatException {
		for(int i=0;i<elementos.getLongitud();i++) { l.agrega(elementos.get(i)); }
		return l;
	}

	/* Obtiene elementos de un arreglo para volverlos Indexeables y meterlos en una Lista. */
	public static Lista<Indexable<Integer>> obtenerElementosListaIndexable(Lista<Indexable<Integer>> li, Lista<Integer> elementos) throws NumberFormatException {
		Lista<Integer> l = obtenerElementosLista(new Lista<Integer>(), elementos);
		for(int i:l) { li.agrega(new Indexable<Integer>(i, i)); }
		return li;
	}

	/* Obtiene elementos de un arreglo para meterlos en una cola. */
	public static Cola<Integer> obtenerElementosCola(Cola<Integer> c, Lista<Integer> elementos) throws NumberFormatException {
		for(int i=0;i<elementos.getLongitud();i++) { c.mete(elementos.get(i)); }
		return c;
	}

	/* Obtiene elementos de un arreglo para meterlos en una Pila. */
	public static Pila<Integer> obtenerElementosPila(Pila<Integer> p, Lista<Integer> elementos) throws NumberFormatException {
		for(int i=0;i<elementos.getLongitud();i++) { p.mete(elementos.get(i)); }
		return p;
	}

	/* Obtiene elementos de un arreglo para meterlos en un ArbolRojinegro. */
	public static ArbolRojinegro<Integer> obtenerElementosArbolRN(ArbolRojinegro<Integer> ab, Lista<Integer> elementos) throws NumberFormatException {
		for(int i=0;i<elementos.getLongitud();i++) { ab.agrega(elementos.get(i)); }
		return ab;
	}

	/* Obtiene elementos de un arreglo para meterlos en un ArbolAVL. */
	public static ArbolAVL<Integer> obtenerElementosArbolAVL(ArbolAVL<Integer> ab, Lista<Integer> elementos) throws NumberFormatException {
		for(int i=0;i<elementos.getLongitud();i++) { ab.agrega(elementos.get(i)); }
		return ab;
	}

	/* Obtiene elementos y relaciones de dos arreglos para meterlos en una Grafica. */
	public static Grafica<Integer> obtenerElementosGrafica(Grafica<Integer> g, Lista<Integer> elementos) throws NumberFormatException {

		Lista<String> relaciones = new Lista<String>();

		for(int i=0;i<elementos.getLongitud();i++) {
			if(elementos.get(i)<10)
				g.agrega(elementos.get(i));
			else {
				relaciones.agrega(Integer.toString(elementos.get(i)));
			}
		}

		for(int i=0;i<relaciones.getLongitud();i++)
			g.conecta(Integer.parseInt(relaciones.get(i).substring(0,1)), Integer.parseInt(relaciones.get(i).substring(1,2)));

		return g;
	}

	/* Lee los elementos que se le pasaron después del tipo de estructura de datos.*/
	public static void leerElementos(Lista<Integer> elementos, EstructurasDeDatos estructuraE) throws Exception {
		/*BufferedReader br = new BufferedReader();
		try {
	        elementos = br.readLine().replace(" ", "").split(",");
		 }catch(IOException e) {
		 	System.out.println("Error al introducir los elementos.");
		 	return;
		}*/

		Lista<Integer> lista = new Lista<Integer>();
		EstructurasDatosSVG edSVG = new EstructurasDatosSVG();

		//try {
			switch(estructuraE) {
				case Lista:
					lista = obtenerElementosLista(new Lista<Integer>(), elementos);
					System.out.println(edSVG.lista(lista));
					break;
				case Cola:
					Cola<Integer> cola = new Cola<Integer>();
					cola = obtenerElementosCola(cola, elementos);
					System.out.println(edSVG.meteSaca(cola));
					break;
				case Pila:
					Pila<Integer> pila = new Pila<Integer>();
					pila = obtenerElementosPila(pila, elementos);
					System.out.println(edSVG.meteSaca(pila));
					break;
				case ArbolBinario:
					lista = obtenerElementosLista(new Lista<Integer>(), elementos);
					ArbolBinarioOrdenado<Integer> arbolO = new ArbolBinarioOrdenado<Integer>(lista);
					System.out.println(edSVG.arbolBinario(arbolO, estructuraE));
					break;
				case ArbolBinarioCompleto:
					lista = obtenerElementosLista(new Lista<Integer>(), elementos);
					ArbolBinarioCompleto<Integer> arbolC = new ArbolBinarioCompleto<Integer>(lista);
					System.out.println(edSVG.arbolBinario(arbolC, estructuraE));
					break;
				case ArbolRojinegro:
					ArbolRojinegro<Integer> arbolRN = new ArbolRojinegro<Integer>();
					arbolRN = obtenerElementosArbolRN(arbolRN, elementos);
					System.out.println(edSVG.arbolBinario(arbolRN, estructuraE));
					break;
				case ArbolAVL:
					ArbolAVL<Integer> arbolAVL = new ArbolAVL<Integer>();
					arbolAVL = obtenerElementosArbolAVL(arbolAVL, elementos);
					System.out.println(edSVG.arbolBinario(arbolAVL, estructuraE));
					break;
				case Monticulo:
					Lista<Indexable<Integer>> li = obtenerElementosListaIndexable(new Lista<Indexable<Integer>>(), elementos);
					MonticuloMinimo<Indexable<Integer>> monticulo = new MonticuloMinimo<Indexable<Integer>>(li);
					System.out.println(edSVG.monticulo(monticulo));
					break;
				case Grafica:
					/*try {
				        relaciones = br.readLine().replace(" ", "").split(";");
					}catch(IOException e) {
						System.out.println("Introduzca de manera correcta las relaciones.");
						return;
					}*/
					Grafica<Integer> g = obtenerElementosGrafica(new Grafica<Integer>(), elementos);
					System.out.println(edSVG.grafica(g));
					break;
			}	
		/*}catch(NumberFormatException e) {
			System.out.println("Introducir únicamente números enteros.");
		}*/
	}

	public static void main(String[] args) throws Exception {

		EstructurasDeDatos estructuraE = null;
		String edd = args[0];
		int[] enteros = new int[args.length-1];
		Lista<Integer> elementosG = new Lista<Integer>();

		try {
			estructuraE = EstructurasDeDatos.valueOf(edd);
		}catch(IllegalArgumentException e) {
			System.out.println("Estructura inválida.");
		}

		if(args.length<2) {
			System.out.println("Introduce elementos.");
			System.exit(-1);
		}

		try {
		for(int i=1;i<args.length;i++)
			elementosG.agrega(Integer.parseInt(args[i].trim()));
		}catch(NumberFormatException nfe) {
			System.out.println("Introducir solamente numeros enteros.");
			System.exit(-1);
		}

		leerElementos(elementosG, estructuraE);

	}

}
	// /* Lee la estructura que se le haya pasado. */
	// public static String leerEstructura(String estructura, BufferedReader br) throws Exception {
	// 	try {
	//         estructura = br.readLine();
	// 	} catch(IOException e) {
	// 		System.out.println(e);
	// 	}
	// 	return estructura;
	// }

	// /* Lee los elementos que se le pasaron después del tipo de estructura de datos.*/
	// public static void leerElementos(String[] elementos, BufferedReader br, String[] relaciones, EstructurasDatosSVG edSVG, EstructurasDeDatos estructuraE, Lista<Integer> lista) throws Exception {
	// 	try {
	//         elementos = br.readLine().replace(" ", "").split(",");
	// 	}catch(IOException e) {
	// 		System.out.println("Error al introducir los elementos.");
	// 		return;
	// 	}
	// 	try {
	// 		switch(estructuraE) {
	// 			case Lista:
	// 				lista = obtenerElementosLista(new Lista<Integer>(), elementos);
	// 				System.out.println(edSVG.lista(lista));
	// 				break;
	// 			case Cola:
	// 				Cola<Integer> cola = new Cola<Integer>();
	// 				cola = obtenerElementosCola(cola, elementos);
	// 				System.out.println(edSVG.meteSaca(cola));
	// 				break;
	// 			case Pila:
	// 				Pila<Integer> pila = new Pila<Integer>();
	// 				pila = obtenerElementosPila(pila, elementos);
	// 				System.out.println(edSVG.meteSaca(pila));
	// 				break;
	// 			case ArbolBinario:
	// 				lista = obtenerElementosLista(new Lista<Integer>(), elementos);
	// 				ArbolBinarioOrdenado<Integer> arbolO = new ArbolBinarioOrdenado<Integer>(lista);
	// 				System.out.println(edSVG.arbolBinario(arbolO, estructuraE));
	// 				break;
	// 			case ArbolBinarioCompleto:
	// 				lista = obtenerElementosLista(new Lista<Integer>(), elementos);
	// 				ArbolBinarioCompleto<Integer> arbolC = new ArbolBinarioCompleto<Integer>(lista);
	// 				System.out.println(edSVG.arbolBinario(arbolC, estructuraE));
	// 				break;
	// 			case ArbolRojinegro:
	// 				ArbolRojinegro<Integer> arbolRN = new ArbolRojinegro<Integer>();
	// 				arbolRN = obtenerElementosArbolRN(arbolRN, elementos);
	// 				System.out.println(edSVG.arbolBinario(arbolRN, estructuraE));
	// 				break;
	// 			case ArbolAVL:
	// 				ArbolAVL<Integer> arbolAVL = new ArbolAVL<Integer>();
	// 				arbolAVL = obtenerElementosArbolAVL(arbolAVL, elementos);
	// 				System.out.println(edSVG.arbolBinario(arbolAVL, estructuraE));
	// 				break;
	// 			case Monticulo:
	// 				Lista<Indexable<Integer>> li = obtenerElementosListaIndexable(new Lista<Indexable<Integer>>(), elementos);
	// 				MonticuloMinimo<Indexable<Integer>> monticulo = new MonticuloMinimo<Indexable<Integer>>(li);
	// 				System.out.println(edSVG.monticulo(monticulo));
	// 				break;
	// 			case Grafica:
	// 				try {
	// 			        relaciones = br.readLine().replace(" ", "").split(";");
	// 				}catch(IOException e) {
	// 					System.out.println("Introduzca de manera correcta las relaciones.");
	// 					return;
	// 				}
	// 				Grafica<Integer> g = obtenerElementosGrafica(new Grafica<Integer>(), elementos, relaciones);
	// 				System.out.println(edSVG.grafica(g));
	// 				break;
	// 		}	
	// 	} catch(NumberFormatException e) {
	// 		System.out.println("Introducir únicamente números enteros.");
	// 	}
	// }

	// /* Revisa si se pasó un archivo y en dado caso lo lee. */
	// public static BufferedReader leerArchivo(String[] args, BufferedReader br) throws Exception {
	// 	if(args.length == 0)
	// 		br = new BufferedReader(new InputStreamReader(System.in));
	// 	else {
	// 		try {
	// 			br = new BufferedReader(new FileReader(args[0]));
	// 		}catch(IOException e) {
	// 			System.out.println("No se encontro el archivo");
	// 			return null;
	// 		}
	// 	}
	// 	return br;
	// }

	// public static void main(String[] args) throws Exception {

	// 	BufferedReader br = null;
	// 	String estructura = "";
	// 	String[] elementos = null, relaciones = null;;
	// 	EstructurasDeDatos estructuraE = null;
	// 	EstructurasDatosSVG edSVG = new EstructurasDatosSVG();
	// 	Lista<Integer> lista = null;

	// 	br = leerArchivo(args, br);
	// 	estructura = leerEstructura(estructura,br);

	// 	// Se comprueba si se mandó un '#'.
	// 	if(estructura.charAt(0) != '#') {
	// 		System.out.println("Error de sintaxis");
	// 		return;
	// 	}

	// 	estructura = estructura.replace("#", "").replace(" ", "");
	// 	try {
	// 		estructuraE = EstructurasDeDatos.valueOf(estructura);
	// 	}catch(IllegalArgumentException e) {
	// 		System.out.println("Estructura erronea");
	// 	}

	// 	leerElementos(elementos, br, relaciones, edSVG, estructuraE, lista);
	// }