package mx.unam.ciencias.edd.proyecto2.svg;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;

/**
 * Clase que implementa {@link SVG_Graficable} la cual dada un gráfica hace las operaciones necesarias para
 * que el en método {@link SVG_Graficable#drawSVG} devuelva un código SVG.
 */
class SVG_Graph<T> implements SVG_Graficable {

    private Grafica<T> grafo;
    private Lista<Point> pointsList = new Lista<>();

    SVG_Graph(Grafica<T> grafo) {
        this.grafo = grafo;
    }

    @Override public String drawSVG() {
        StringBuilder builder = new StringBuilder();
        int vertexRadius = SVG_Util.getVertexRadius(grafo);
        double radiusCircle = ((2*grafo.getElementos())*(2*vertexRadius))/Math.PI;
        double height = (2*vertexRadius)+(2* radiusCircle)+vertexRadius;

        builder.append(SVG_Util.XML_PROLOG);
        if (grafo.getElementos() == 0)
            builder.append(SVG_Util.startSVGAndPutHeightWidth(0, 0));
        else
            builder.append(SVG_Util.startSVGAndPutHeightWidth(height, height));

        builder.append(SVG_Util.OPEN_G_TAG);
        createVertices(radiusCircle, grafo.getElementos(), height/2);
        if (grafo.getElementos() > 1)
            builder.append(dibujaAristas());
        builder.append(dibujaVertices(vertexRadius));
        builder.append(SVG_Util.CLOSE_G_TAG);

        builder.append(SVG_Util.closeSVG());
        return builder.toString();
    }

    /**
     * Método que dados los vértices de la gráfica, les va asignando una coordenada como si estuviesen en la
     * circunferencia de un círculo, para después llenar una lista con los puntos.
     *  @param radio      El radio del círculo,
     * @param nParts El número de veces que será dividida en partes proporcionales la circunferencia.
     * @param half
     */
    private void createVertices(double radio, int nParts, double half) {
        double angle;
        double x, y;
        int i = 0;
        for (T e : grafo) {
            if (grafo.getElementos() == 1) {
                pointsList.agrega(new Point(half, half, e));
            } else {
                angle = i++ * (360 / nParts);
                x = radio * Math.cos(Math.toRadians(angle));
                y = radio * Math.sin(Math.toRadians(angle));
                pointsList.agrega(new Point(x + half, y + half, e));
            }
        }
    }

    /**
     * Método que dibuja vértices.
     */
    private String dibujaVertices(int radio) {
        StringBuilder builder = new StringBuilder();
        for (Point p : pointsList)
            builder.append(SVG_Util.drawGraphVertex(p.x, p.y, radio, p.data.toString()));
        return builder.toString();
    }

    /**
     * Método que dibuja aristas.
     */
    private String dibujaAristas() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < pointsList.getElementos(); i++) {
            Point a = pointsList.get(i);
            for (int j = i; j < grafo.getElementos(); j++) {
                Point b = pointsList.get(j);
                if (grafo.sonVecinos(a.data, b.data)) {
                    builder.append(SVG_Util.drawLine(a.x, a.y, b.x, b.y));
                }
            }
        }
        return builder.toString();
    }

    /**
     * Clase auxiliar la cual será útil para tener un punto con las coordenadas x & y.
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
