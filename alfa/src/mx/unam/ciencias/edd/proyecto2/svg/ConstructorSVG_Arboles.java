package mx.unam.ciencias.edd.proyecto2.svg;

import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.Indexable;
import mx.unam.ciencias.edd.MonticuloMinimo;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto2.EstructurasDeDatos;

/**
 * Clase que recibe un tipo de arbol y devuelve un SVG.
 */
public class ConstructorSVG_Arboles<T extends Comparable<T>> {

    private ArbolBinario<T> arbol;
    private EstructurasDeDatos edd;
    private int ancho, altura, profundidad;

    ConstructorSVG_Arboles(Lista<T> elementos, EstructurasDeDatos edd) {
        this.edd = edd;
        switch(edd) {
            case AORDENADO:
                arbol = new ArbolBinarioOrdenado<>();
                break;
            case AROJINEGRO:
                arbol = new ArbolRojinegro<>();
                break;
            case AVL:
                arbol = new ArbolAVL<>();
                break;
            case ACOMPLETO:
            case MONTICULOMINIMO:
                arbol = new ArbolBinarioCompleto<>();
                break;
        }
        if(edd == EstructurasDeDatos.MONTICULOMINIMO) {
            Lista<Indexable<T>> le = new Lista<>();
            elementos.forEach(e -> le.agrega(new Indexable<T>(e, Integer.parseInt(e.toString()))));
            MonticuloMinimo<Indexable<T>> minHeap = new MonticuloMinimo<>(le);
            minHeap.forEach(e -> arbol.agrega(e.getElemento()));
        }else {
            elementos.forEach(arbol::agrega);
        }
    }

    public String drawSVG() {
        int vertexRadius = EtiquetasSVG.getVerticeRadio(arbol);
        profundidad = arbol.profundidad() == 0 ? 1 : arbol.profundidad();
        altura = 100 + profundidad * 100;
        ancho = (int) (Math.pow(2, profundidad) * vertexRadius*2);

        StringBuilder builder = new StringBuilder();
        builder.append(EtiquetasSVG.XML_PROLOG);
        builder.append(EtiquetasSVG.SvgAlturaAnchura(altura, ancho));
        builder.append(EtiquetasSVG.ABRE_G_TAG);
        if(!arbol.esVacio()) {
            builder.append(dibujaAristas(arbol.raiz(), (ancho / 2) / 2, ancho / 2, 50));
            builder.append(dibujaVertices(arbol.raiz(), (ancho / 2) / 2, ancho / 2, 50, vertexRadius));
        }
        builder.append(EtiquetasSVG.CIERRA_G_TAG);
        builder.append(EtiquetasSVG.cierraSVG());
        return builder.toString();
    }

    /**
     * Método que devulve un codigo SVG con los vértices del árbol.
     */
    private String dibujaVertices(VerticeArbolBinario vertice, double decremento, double coordenadasVerticePadre, double y,double radius) {
        String s = "";
        if(vertice != null) {
            String balance = getAlturaYBalance(vertice.toString());
            s += EtiquetasSVG.dibujaVerticeArbol(coordenadasVerticePadre, y, radius,vertice.get().toString(), getVerticeColor(vertice), balance != null, balance);
            if(vertice.hayIzquierdo() && vertice.hayDerecho())
                s += dibujaVertices(vertice.getIzquierdo(), decremento / 2, coordenadasVerticePadre - decremento, y + 100,radius) +
                        dibujaVertices(vertice.getDerecho(), decremento / 2, coordenadasVerticePadre + decremento, y + 100,radius);
            else if(vertice.hayIzquierdo())
                s += dibujaVertices(vertice.getIzquierdo(), decremento / 2, coordenadasVerticePadre - decremento, y + 100,radius);
            else if(vertice.hayDerecho())
                s += dibujaVertices(vertice.getDerecho(), decremento / 2, coordenadasVerticePadre + decremento, y + 100,radius);
        }
        return s;
    }

    /**
     * Método que devulve un codigo SVG con las aristas del árbol.
     */
    private String dibujaAristas(VerticeArbolBinario vertice, double decremento, double coordenadasVerticePadre, double y) {
        String s = "";
        if(vertice.hayIzquierdo() && vertice.hayDerecho())
            s += EtiquetasSVG.dibujaLinea(coordenadasVerticePadre, y, coordenadasVerticePadre - decremento, y + 100) +
                    EtiquetasSVG.dibujaLinea(coordenadasVerticePadre, y, coordenadasVerticePadre + decremento, y + 100) +
                    dibujaAristas(vertice.getIzquierdo(), decremento / 2, coordenadasVerticePadre - decremento, y + 100) +
                    dibujaAristas(vertice.getDerecho(), decremento / 2, coordenadasVerticePadre + decremento, y + 100);
        else if(vertice.hayDerecho())
            s += EtiquetasSVG.dibujaLinea(coordenadasVerticePadre, y, coordenadasVerticePadre + decremento, y + 100) +
                    dibujaAristas(vertice.getDerecho(), decremento / 2, coordenadasVerticePadre + decremento, y + 100);
        else if(vertice.hayIzquierdo())
            s += EtiquetasSVG.dibujaLinea(coordenadasVerticePadre, y, coordenadasVerticePadre - decremento, y + 100) +
                    dibujaAristas(vertice.getIzquierdo(), decremento / 2, coordenadasVerticePadre - decremento, y + 100);
        return s;
    }

    /**
     * Método que devuelve el color de un vértice.
     */
    private String getVerticeColor(VerticeArbolBinario vertice) {
        String verticeColor;
        if(edd == EstructurasDeDatos.AROJINEGRO)
            if(vertice.toString().substring(0, 1).equals("N"))
                verticeColor = EtiquetasSVG.COLOR_NEGRO;
            else
                verticeColor = EtiquetasSVG.COLOR_ROJO;
        else
            verticeColor = EtiquetasSVG.COLOR_BLANCO;
        return verticeColor;
    }

    /**
     * Método que devuelve el balance y altura. Se usara cuando es un arbol AVL.
     */
    private String getAlturaYBalance(String s) {
        String balance = null;
        if(edd == EstructurasDeDatos.AVL) {
            StringBuilder h = new StringBuilder();
            for(int i = s.length() - 1; i >= 0; i--)
                if(s.charAt(i) == ' ') break;
                else
                    h.append(s.charAt(i));
            balance = h.reverse().toString();
        }
        return balance;
    }

}