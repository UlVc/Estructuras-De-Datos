package mx.unam.ciencias.edd.proyecto3.eddsvg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.eddsvg.SVG.*;

public class GraficadoraEDD extends ConstructorEDD {

    private static Lista<Integer> elementos = new Lista<Integer>();
    private static EstructurasDatosSVG edSVG = new EstructurasDatosSVG();

    /* Lee los elementos que se le pasaron después del tipo de estructura de datos y construye un código SVG.*/
    public static String construirEstructura(Diccionario<String, Integer> elementos, String estructura) throws Exception {
        if (estructura.equals("ArbolRojinegro"))
            return edSVG.arbolBinario(construirArbolRojinegroEntero(elementos), construirArbolRojinegroCadena(elementos), EstructurasDeDatos.ArbolRojinegro, elementos);
        else if (estructura.equals("ArbolAVL"))
            return edSVG.arbolBinario(construirArbolAVLEntero(elementos), construirArbolAVLCadena(elementos), EstructurasDeDatos.ArbolAVL, elementos);
        return "";
    }

    public static String construirEstructuraGrafica(Lista<Diccionario<String, Integer>> elementos, Lista<String> archivos, Lista<Integer> numeroDePalabras) throws Exception {
        return edSVG.grafica(construirGraficaEntero(numeroDePalabras), construirGraficaCadena(archivos, elementos));
    }

}