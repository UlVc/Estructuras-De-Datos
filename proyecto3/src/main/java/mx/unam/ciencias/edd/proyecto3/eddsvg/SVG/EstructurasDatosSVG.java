package mx.unam.ciencias.edd.proyecto3.eddsvg.SVG;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.eddsvg.*;

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
    public String arbolBinario(ArbolBinario<Integer> ab, ArbolBinario<String> abC, EstructurasDeDatos arbol_a, Diccionario<String, Integer> elementos) {
        int padding = 15, largoSVG, altoSVG, radio;
        int x, y;
        String cadena;
        VerticeArbolBinario<Integer> max;

        if (ab.esVacia())
            return xmlProlog;

        max = ArbolBinarioAuxSVG.obtenerMaximo(ab.raiz());
        radio = (longitudNumero(max.get()) * 10 + padding * 2) / 2;
        altoSVG = ArbolBinarioAuxSVG.obtenerAlturaSVGArbol(ab, radio);
        largoSVG = ArbolBinarioAuxSVG.obtenerLongitudSVGArbol(ab, radio);
        x = largoSVG / 2;
        y = radio * 3;
        cadena = ArbolBinarioAuxSVG.obtenerVertices(abC.raiz(), radio, largoSVG/2, x, y, arbol_a, utils);

        return String.format("%1$s<svg width='%2$s' height='%3$s' xmlns='http://www.w3.org/2000/svg'>%4$s</svg>", xmlProlog, largoSVG, altoSVG, cadena);
    }

    /* Construye el código SVG de una Gráfica. */
    public String grafica(Grafica<Integer> g, Grafica<String> gC) {
        max = GraficaAuxSVG.obtenerMaximo(g);
        radio = (longitudNumero(max) * 10 + padding * 2) / 2;
        perimetro = g.getElementos() * radio * 3;
        radioG = perimetro / 3.1416;
        largoSVGD = altoSVGD = radioG * 2 + radio * 2.0 * 2.0;
        cadena = GraficaAuxSVG.obtenerVertices(gC, radioG, radio, largoSVGD / 2, altoSVGD / 2, utils);

        return String.format("%1$s<svg width='%2$s' height='%3$s' xmlns='http://www.w3.org/2000/svg'>%4$s</svg>", xmlProlog, largoSVGD, altoSVGD, cadena);
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