package mx.unam.ciencias.edd.proyecto3.eddsvg;

import java.io.IOException;

import java.util.Iterator;

import mx.unam.ciencias.edd.*;

/* Clase para construir las Estructuras de Datos e imprimpirlas en un formato SVG. */
public class ConstructorEDD {

    // Obtiene elementos de un arreglo para introducirlos en un ArbolRojinegro.
    public static ArbolRojinegro<Integer> construirArbolRojinegroEntero(Diccionario<String, Integer> elementos) throws NumberFormatException {
        ArbolRojinegro<Integer> abrn = new ArbolRojinegro<Integer>();

        for (Integer i : elementos)
            abrn.agrega(i);

        return abrn;
    }

    public static ArbolRojinegro<String> construirArbolRojinegroCadena(Diccionario<String, Integer> elementos) throws NumberFormatException {
        ArbolRojinegro<String> abrn = new ArbolRojinegro<String>();
        Iterator<String> iteradorLLave = elementos.iteradorLlaves();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            abrn.agrega(llave);
        }

        return abrn;
    }

    // Obtiene elementos de un arreglo para introducirlos en un ArbolAVL.
    public static ArbolAVL<Integer> construirArbolAVLEntero(Diccionario<String, Integer> elementos) throws NumberFormatException {
        ArbolAVL<Integer> ab = new ArbolAVL<Integer>();

        for (Integer i : elementos)
            ab.agrega(i);

        return ab;
    }

    // Obtiene elementos de un arreglo para introducirlos en un ArbolAVL.
    public static ArbolAVL<String> construirArbolAVLCadena(Diccionario<String, Integer> elementos) throws NumberFormatException {
        ArbolAVL<String> ab = new ArbolAVL<String>();
        Iterator<String> iteradorLLave = elementos.iteradorLlaves();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            ab.agrega(llave);
        }

        return ab;
    }

    // Obtiene elementos y relaciones de dos arreglos para introducirlos en una Grafica.
    public static Grafica<Integer> construirGraficaEntero(Lista<Integer> elementos) throws NumberFormatException {
        Grafica<Integer> g = new Grafica<Integer>();

        for (int i = 1; i <= elementos.getElementos(); i++)
            g.agrega(i);

        return g;
    }

    // Obtiene elementos y relaciones de dos arreglos para introducirlos en una Grafica.
    public static Grafica<String> construirGraficaCadena(Lista<String> archivos, Lista<Diccionario<String, Integer>> elementos) throws NumberFormatException {
        Grafica<String> g = new Grafica<String>();
        Lista<Lista<String>> relaciones = new Lista<Lista<String>>();
        
        for (String s : archivos)
            g.agrega(s);

        relaciones = palabrasEnCadaArchivo(archivos, elementos);
        relaciones = relaciones(relaciones);

        for (Lista<String> l : relaciones)
            g.conecta(l.getPrimero(), l.getUltimo());

        return g;
    }

    private static Lista<Lista<String>> palabrasEnCadaArchivo(Lista<String> archivos, Lista<Diccionario<String, Integer>> elementos) {
        Lista<Lista<String>> lista = new Lista<Lista<String>>();

        for (String s : archivos) {
            Lista<String> aux = new Lista<String>();
            aux.agrega(s);

            for (Diccionario<String, Integer> d : elementos) {
                Iterator<String> iteradorLLave = d.iteradorLlaves();

                while (iteradorLLave.hasNext()) {
                    String llave = iteradorLLave.next();
                    aux.agrega(llave);
                }

                elementos.elimina(d);
                break;
            }
            lista.agrega(aux);
        }

        return lista;
    }

    // Mejorar algoritmo
    private static Lista<Lista<String>> relaciones(Lista<Lista<String>> lista) {
        Lista<Lista<String>> relaciones = new Lista<Lista<String>>();

        for (Lista<String> l : lista) {
            Lista<Lista<String>> lista2 = new Lista<Lista<String>>();
            lista2 = lista.copia();
            lista2.elimina(l);

            for(String s : l) {
                for (Lista<String> k : lista2) {
                    if (s.length() > 6)
                        if (k.contiene(s)) {
                            Lista<String> aux = new Lista<String>();
                            aux.agrega(l.getPrimero());
                            aux.agrega(k.getPrimero());
                            if (!relaciones.contiene(aux))
                                relaciones.agrega(aux);
                        }
                }   
            }

        lista.elimina(l);
        }

        return relaciones;
    }
}