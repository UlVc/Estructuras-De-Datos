package mx.unam.ciencias.edd.proyecto2.svg;

/**
 * Interfaz que será implementada a todas las clases que puedan ser graficables a SVG.
 */
public interface SVG_Graficable {
    /**
     * Metodo que regresa un String el cual es un svg.
     */
    String drawSVG();
}