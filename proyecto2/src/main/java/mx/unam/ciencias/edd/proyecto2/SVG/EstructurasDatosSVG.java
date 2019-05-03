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

	/* Construye el código SVG de una Lista. */
	public String lista(Lista<Integer> l) {

		for(int e: l) {
			cadena += utils.rectanguloConNumero(e, i, 40, padding, border);
			i += longitudNumero(e)*10+padding*2;
			if(e != l.getUltimo())
				cadena += utils.dobleFlecha(i+2, 58);
			i += border;
		}
		largoSVG += i;

		return String.format("%1$s<svg width='%2$s' height='100'>%3$s</svg>",xmlProlog,largoSVG,cadena);
	}

	/* Construye el código SVG de un meteSaca. */
	public String meteSaca(MeteSaca<Integer> ms) {

		while(!ms.esVacia()) {
			e = ms.saca();
			cadena += utils.rectanguloConNumero(e, i, 40, padding, border);
			i += longitudNumero(e)*10+padding*2;
			if(!ms.esVacia())
				cadena += utils.flechaDerecha(i+2, 58);
			i += border;
		}
		largoSVG += i;

		return String.format("%1$s<svg width='%2$s' height='100'>%3$s</svg>",xmlProlog,largoSVG,cadena);
	}


	/* Construye el código SVG de un Monticulo. */
	public String monticulo(MonticuloMinimo<Indexable<Integer>> mm) {

		ArbolBinarioCompleto<Integer> abc = new ArbolBinarioCompleto<Integer>();

		for(Indexable<Integer> i:mm)
			abc.agrega(i.getElemento());

		return arbolBinario(abc, EstructurasDeDatos.ArbolBinarioCompleto);
	}

	/* Construye el código SVG de una Gráfica. */
	public String grafica(Grafica<Integer> g) {

		max = GraficaAuxSVG.obtenerMaximo(g);
		radio = (longitudNumero(max)*10+padding*2)/2;
		perimetro = g.getElementos()*radio*3;
		radioG = perimetro/3.1416;
		largoSVGD = altoSVGD = radioG*2 + radio*2.0*2.0;
		cadena = GraficaAuxSVG.obtenerVertices(g, radioG, radio, largoSVGD/2, altoSVGD/2, utils);

		return String.format("%1$s<svg width='%2$s' height='%3$s'>%4$s</svg>",xmlProlog,largoSVGD,altoSVGD,cadena);
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