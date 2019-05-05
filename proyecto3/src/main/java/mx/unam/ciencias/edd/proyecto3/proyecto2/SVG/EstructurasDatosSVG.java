package mx.unam.ciencias.edd.proyecto2.SVG;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto2.*;

/* Clase para dibujar Estructuras de Datos en código SVG.*/
public class EstructurasDatosSVG {

	private SVGUtils utils;
	private final String xmlProlog;
	private String cadena = "";
	private int padding = 15, border = 25;
	private int i = border, largoSVG = border, e, altoSVG, radio, perimetro, max, x, y;
	private double radioG, largoSVGD, altoSVGD;

	/* Constructor único que inicializa los valores. */
	public EstructurasDatosSVG() {
		utils = new SVGUtils();
		xmlProlog = "<?xml version='1.0' encoding='utf-8'?>\n";
	}

	/* Construye el código SVG de un ArbolBinario. */
	public String arbolBinario(ArbolBinario<Integer> ab, EstructurasDeDatos arbol_a) {

		int padding = 15, largoSVG, altoSVG, radio;
		int x, y;
		String cadena;
		VerticeArbolBinario<Integer> max;

		if(ab.esVacia()) { return xmlProlog; }

		max = ArbolBinarioAuxSVG.obtenerMaximo(ab.raiz());
		radio = (longitudNumero(max.get())*10+padding*2)/2;
		altoSVG = ArbolBinarioAuxSVG.obtenerAlturaSVGArbol(ab,radio);
		largoSVG = ArbolBinarioAuxSVG.obtenerLongitudSVGArbol(ab,radio);
		x = largoSVG/2;
		y = radio*3;
		cadena = ArbolBinarioAuxSVG.obtenerVertices(ab.raiz(), radio, largoSVG/2, x, y, arbol_a, utils);

		return String.format("%1$s<svg width='%2$s' height='%3$s'>%4$s</svg>",xmlProlog,largoSVG,altoSVG,cadena);
	}

	/* Obtiene la longitud de un número */
	private int longitudNumero(int n) {
		int i = 1;
		while (n >= 10) {
			n /= 10;
			i++;
		}
		return i;
	}
}