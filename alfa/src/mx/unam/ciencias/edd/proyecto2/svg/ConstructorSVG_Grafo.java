package mx.unam.ciencias.edd.proyecto2.svg;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;

/**
 * Clase que recibe una grafica y la convierte a codigo SVG.
 */
public class ConstructorSVG_Grafo<T> {

    private Grafica<T> grafo;
    private Lista<Point> pointsList = new Lista<>();

    ConstructorSVG_Grafo(Grafica<T> grafo) {
        this.grafo = grafo;
    }

    public String drawSVG() {

        StringBuilder cadenaSVG = new StringBuilder();
        int vertexRadius = EtiquetasSVG.getVerticeRadio(grafo);
        double radiusCircle = ((2*grafo.getElementos())*(2*vertexRadius))/Math.PI;
        double height = (2*vertexRadius)+(2* radiusCircle)+vertexRadius;

        cadenaSVG.append(EtiquetasSVG.XML_PROLOG);

        if(grafo.getElementos() == 0)
            cadenaSVG.append(EtiquetasSVG.SvgAlturaAnchura(0, 0));
        else
            cadenaSVG.append(EtiquetasSVG.SvgAlturaAnchura(height, height));

        cadenaSVG.append(EtiquetasSVG.ABRE_G_TAG);
        createVertices(radiusCircle, grafo.getElementos(), height/2);
        if(grafo.getElementos() > 1)
            cadenaSVG.append(dibujaAristas());
        cadenaSVG.append(dibujaVertices(vertexRadius));
        cadenaSVG.append(EtiquetasSVG.CIERRA_G_TAG);

        cadenaSVG.append(EtiquetasSVG.cierraSVG());
        return cadenaSVG.toString();
    }

    /**
     * Método que dados los vértices de la gráfica, les va asignando una coordenada como si estuviesen en la
     * circunferencia de un círculo, para después llenar una lista con los puntos.
     *  @param radio      El radio del círculo,
     * @param nParts El número de veces que será dividida en partes proporcionales la circunferencia.
     * @param half
     */
    private void createVertices(double radio, int nParts, double half) {
        double angulo;
        double x, y;
        int i = 0;
        for(T e : grafo) {
            if(grafo.getElementos() == 1) {
                pointsList.agrega(new Point(half, half, e));
            }else {
                angulo = i++ * (360 / nParts);
                x = radio * Math.cos(Math.toRadians(angulo));
                y = radio * Math.sin(Math.toRadians(angulo));
                pointsList.agrega(new Point(x + half, y + half, e));
            }
        }
    }

    /* Método que dibuja vértices. */
    private String dibujaVertices(int radio) {
        StringBuilder builder = new StringBuilder();
        for(Point p : pointsList)
            builder.append(EtiquetasSVG.dibujaVerticeGrafo(p.x, p.y, radio, p.data.toString()));
        return builder.toString();
    }

    /* Método que dibuja aristas. */
    private String dibujaAristas() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < pointsList.getElementos(); i++) {
            Point a = pointsList.get(i);
            for(int j = i; j < grafo.getElementos(); j++) {
                Point b = pointsList.get(j);
                if(grafo.sonVecinos(a.data, b.data))
                    builder.append(EtiquetasSVG.dibujaLinea(a.x, a.y, b.x, b.y));
            }
        }
        return builder.toString();
    }

    /**
     * Clase auxiliar la cual será útil para tener un punto con las coordenadas.
     */
    private class Point {
        double x;
        double y;
        T data;
        Point(double x, double y, T data) {
            this.x = x;
            this.y = y;
            this.data = data;
        }
    }
}
