package mx.unam.ciencias.edd.proyecto3.eddsvg.SVG;

import java.util.NoSuchElementException;
import java.util.Iterator;

import mx.unam.ciencias.edd.proyecto3.eddsvg.EstructurasDeDatos;
import mx.unam.ciencias.edd.proyecto3.eddsvg.SVG.SVGUtils;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.VerticeArbolBinario;

import mx.unam.ciencias.edd.Diccionario;

public abstract class ArbolBinarioAuxSVG extends ArbolBinario {

    private static VerticeArbolBinario<Integer> der = null, izq = null, max;

    /* Obtiene el número más grande del subárbol. */
    public static VerticeArbolBinario<Integer> obtenerMaximo(VerticeArbolBinario<Integer> vertice) {

        VerticeArbolBinario<Integer> der = null, izq = null, max;

        if (vertice == null) { return null; }

        if (!vertice.hayIzquierdo() && !vertice.hayDerecho()) { return vertice; }

        else
            if (vertice.hayIzquierdo())
                izq = obtenerMaximo(vertice.izquierdo());   
            if (vertice.hayDerecho())
                der = obtenerMaximo(vertice.derecho());

        if (izq != null && der != null) 
            max = ((izq.get().compareTo(der.get()) >= 0) ? izq : der);
        else
            if (izq == null)
                max = der;
            else 
                max = izq; 

        return ((vertice.get().compareTo(max.get()) >= 0) ? vertice : max);
    }

        /* Longitud para el código SVG. */
    public static int obtenerLongitudSVGArbol(ArbolBinario<Integer> ab, int radio) {
        int numeroHojas = (int) Math.pow(2, ArbolBinarioAuxSVG.profundidad(ab));
        return (numeroHojas+(numeroHojas / 2) + 2) * (radio * 2);
    }

    /* Altura para el código SVG. */
    public static int obtenerAlturaSVGArbol(ArbolBinario<Integer> ab, int radio) {
        return (ArbolBinarioAuxSVG.profundidad(ab) + 3) * (radio * 2);
    }

    /* Obtiene el código SVG de los vértices del árbol. */
    public static String obtenerVertices(VerticeArbolBinario<String> vertice, int radio, int i, int x, int y, EstructurasDeDatos edd, SVGUtils utils) {

        String arbol = "", color = "white", colorLetra = "black";

        i /= 2;

        if (vertice.hayIzquierdo()) {
            arbol += utils.linea(x, y, x-i, y+radio*2);
            arbol += obtenerVertices(vertice.izquierdo(), radio, i, x-i, y+radio*2, edd, utils);
        }
        if (vertice.hayDerecho()) {
            arbol += utils.linea(x, y, x+i, y+radio*2);
            arbol += obtenerVertices(vertice.derecho(), radio, i, x+i, y+radio*2, edd, utils);
        }

        if (vertice.toString().charAt(0) == ('R')) {
            colorLetra = "white";
            color = "red";
        }
        if (vertice.toString().charAt(0) == ('N')) {
            colorLetra = "white";
            color = "black";
        }

        arbol += utils.circuloConTexto(vertice.get(), x, y, radio, color, colorLetra);

        if (edd == EstructurasDeDatos.ArbolAVL)
            arbol += utils.texto(vertice.toString().split(" ")[1], x + radio - 10, y - (radio / 2) - 10, "text-anchor='end'");

        return arbol;
    }

    /* Regresa la profundidad del árbol. Será de gran ayuda para imprimir de manera correcta el SVG. */
    public static int profundidad(ArbolBinario<Integer> ab) {
        return profundidad((Vertice) ab.raiz());
    }

    /* Auxiliar para el metodo profundidad. */
    private static int profundidad(Vertice v) {
        return v == null ? - 1 : 1 + Math.max(profundidad(v.izquierdo), profundidad(v.derecho));
    }

}