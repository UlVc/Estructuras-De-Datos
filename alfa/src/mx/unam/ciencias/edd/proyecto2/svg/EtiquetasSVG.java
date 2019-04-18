package mx.unam.ciencias.edd.proyecto2.svg;

import mx.unam.ciencias.edd.proyecto2.EstructurasDeDatos;

/**
 * Clase para generar los archivos SVG.
 */
public class EtiquetasSVG {

    /* Etiquetas */
    static final String XML_PROLOG = "<?xml version='1.0' encoding='UTF-8' ?>\n";
    static final String ABRE_G_TAG = "<g>\n";
    static final String CIERRA_G_TAG = "</g>\n";
    //static final String ABRE_DEFS_TAG = "<defs>\n";
    //static final String CIERRA_DEFS_TAG = "</defs>\n";

    /* Modificadores de texto. */
    static final String FUENTE_TEXTO = "sans-serif";
    static final String ANCHO_FUENTE = "middle";
    static final int TAMAÑO_FUENTE_TEXTO = 16;
    static final int TAMAÑO_CONEXION = 21;
    static final double TAMAÑO_FUENTE_VERTICE   = 11.5;
    static final int TAMAÑO_FUENTE_BALANCE_AVL = 12;
    static final int ANCHO_LINEA = 2;
    //static final String LISTA_DOBLEMENTE_LIGADA = "↔";
    //static final String PILA_COLA = "→";
    //static final int RADIO_VERTICE = 25;

    /* Colores. */ 
    static final String COLOR_BLANCO = "white";
    static final String COLOR_NEGRO = "black";
    static final String COLOR_ROJO = "red";
    static final String COLOR_AZUL = "blue";

    static int getVerticeRadio(Iterable<?> iterable) {
        int max = 0;
        int verticeRadio = 15;
        for(Object e : iterable)
            if (e.toString().length() > max)
                max = e.toString().length();
        if(max >= 4)
            verticeRadio = ((max - 3) * 4) + verticeRadio;
        return verticeRadio;
    }

    static String SvgAlturaAnchura(double height, double width) {
        return "<svg height='" + height + "' width='" + width + "'>\n";
        //return String.format("<svg height='%f' width='%f'>\n", height, width);
    }

    static String cierraSVG() {
        return "</svg>\n";
    }

    static String openG_TagWithId(String id) {
        //return String.format("<g id='%s'>\n", id);
        return "<g id='" + id + "'>\n";
    }

    static String dibujaFlecha(String arrow) {
        return String.format("<text x='%d' y='%d' fill='%s' font-family='%s' font-size='%d' " + "text-anchor='%s'>%s</text>\n", 0, 0, COLOR_NEGRO, FUENTE_TEXTO, TAMAÑO_CONEXION, ANCHO_FUENTE, arrow);
    }

    static String dibujaLinea(double x1, double y1, double x2, double y2) {
        return String.format("<line stroke='%s' stroke-width='%d' " + "x1='%f' y1='%f' x2='%f' y2='%f'/>\n", COLOR_NEGRO, ANCHO_LINEA, x1, y1, x2, y2);
    }

    static String dibujaCuadrados(int x, int y, int altura, int ancho, String texto, EstructurasDeDatos edd) {
        final String RECT_TAG = String.format("<rect " + "x='%d' y='%d' height='%d' width='%d' fill='%s' stroke='%s' stroke-width='%d'/>\n", x, y, altura, ancho, COLOR_BLANCO, COLOR_NEGRO, ANCHO_LINEA);
        final String TEXT_TAG = String.format("<text " + "x='%d' y='%d' fill='%s' font-family='%s' font-size='%d' text-anchor='%s'>%s</text>\n", edd != EstructurasDeDatos.PILA ? x + 30 : 40, edd != EstructurasDeDatos.PILA ? 35 : y + 25, COLOR_NEGRO, FUENTE_TEXTO, TAMAÑO_FUENTE_TEXTO, ANCHO_FUENTE, texto);
        return RECT_TAG + TEXT_TAG;
    }

    static String dibujaVerticeGrafo(double x, double y, double radio, String texto) {
        final String CIRCLE_TAG = String.format("<circle " + "cx='%f' cy='%f' r='%f' fill='%s' stroke='%s' stroke-width='%d'/>\n", x, y, radio, COLOR_BLANCO, COLOR_NEGRO, ANCHO_LINEA);
        final String TEXT_TAG = String.format("<text " + "x='%f' y='%f' fill='%s' font-family='%s' font-size='%f' text-anchor='%s'>%s</text>\n", x, y + 4, COLOR_NEGRO, FUENTE_TEXTO, TAMAÑO_FUENTE_VERTICE, ANCHO_FUENTE, texto);
        return CIRCLE_TAG + TEXT_TAG;
    }

    static String dibujaVerticeArbol(double x, double y, double radio,String texto, String vertexColor, boolean avlTree, String balanceHeight) {
        final String CIRCLE_TAG = String.format("<circle " + "cx='%f' cy='%f' r='%f' fill='%s' stroke='%s' stroke-width='%d'/>\n", x, y, radio, vertexColor, COLOR_NEGRO, ANCHO_LINEA);
        final String TEXT_TAG = String.format("<text " + "x='%f' y='%f' fill='%s' font-family='%s' font-size='%f' text-anchor='%s'>%s</text>\n", x, y + 4, vertexColor.equals(COLOR_BLANCO) ? COLOR_NEGRO : COLOR_BLANCO, FUENTE_TEXTO, TAMAÑO_FUENTE_VERTICE, ANCHO_FUENTE, texto);
        final String BALANCE_AND_DEPTH_TEXT_TAG = String.format("<text " + "x='%f' y='%f' fill='%s' font-family='%s' font-size='%d' text-anchor='%s'>%s</text>\n", x + 35, y - 15, COLOR_AZUL, FUENTE_TEXTO, TAMAÑO_FUENTE_BALANCE_AVL, ANCHO_FUENTE, balanceHeight);
        return CIRCLE_TAG + TEXT_TAG + (avlTree ? BALANCE_AND_DEPTH_TEXT_TAG : "");
    }

    /* Clase auxiliar para la etiqueta defs */
    static class Defs {

        static String createDefs(String id, String inside) {
            return "<defs>\n" + openG_TagWithId(id) + inside + CIERRA_G_TAG + "</defs>\n";
            //return ABRE_DEFS_TAG + openG_TagWithId(id) + inside + CIERRA_G_TAG + CIERRA_DEFS_TAG;
        }

        static String createUseTag(String id, int x, int y) {
            return String.format("<use xlink:href='#%s' x='%d' y='%d' />\n", id, x, y);
        }

    }

}