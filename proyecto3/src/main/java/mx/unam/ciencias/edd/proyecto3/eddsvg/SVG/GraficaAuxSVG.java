package mx.unam.ciencias.edd.proyecto3.eddsvg.SVG;

import java.util.NoSuchElementException;

import mx.unam.ciencias.edd.proyecto3.eddsvg.EstructurasDeDatos;
import mx.unam.ciencias.edd.proyecto3.eddsvg.SVG.SVGUtils;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.VerticeGrafica;
import mx.unam.ciencias.edd.Arreglos;

public class GraficaAuxSVG extends Grafica {

    private static String vertices = "", aristas = "", color = "white", colorLetra = "black";
    private static double anguloi = 0, xi, yi;
    private static Arreglos arr = null;
    private static VerticeGrafica<String> vi = null;
    private static VerticeCoordenada coordenadai;

    /**
     * Clase auxiliar para dibujar las graficas.
     */
    private static class VerticeCoordenada implements Comparable<VerticeCoordenada> {
        
        VerticeGrafica<String> vertice;
        double x;
        double y;

        /* Crea el vertice coordenada a partir de un vertice y sus coordenadas. */
        public VerticeCoordenada(VerticeGrafica<String> vertice, double x, double y) {
            this.vertice = vertice;
            this.x = x;
            this.y = y;
        }

        /* Compara entre dos VerticeCoordenada. */
        @Override public int compareTo(VerticeCoordenada vc) {
            return vertice.get().compareTo(vc.vertice.get());
        }

        /* Compara dos VerticeCoordeanda si son iguales */
        public boolean equals(VerticeCoordenada vc) {
            return vc.vertice.get().equals(vertice.get());
        }
    }

    /* Obtiene el codigo SVG de los vertices de la grafica. */
    public static String obtenerVertices(Grafica<String> g, double radioG, int radio, double x, double y, SVGUtils utils)  {

        VerticeCoordenada[] coordenadas = new VerticeCoordenada[g.getElementos()];
        double angulo = Math.toRadians(360 / g.getElementos());
        int i = 0;

        for (String v : g) {
            xi = radioG * Math.cos(anguloi);
            yi = radioG * Math.sin(anguloi);
            vertices += utils.circuloConTexto(v, x + xi, y + yi, radio, color, colorLetra);
            vi = g.vertice(v);
            coordenadai = new VerticeCoordenada(vi, x + xi, y + yi);
            coordenadas[i] = coordenadai;
            anguloi += angulo;
            i++;
        }

        arr.quickSort(coordenadas);

        for (VerticeCoordenada v : coordenadas) {
            for (VerticeGrafica<String> vecino : v.vertice.vecinos()) {
                coordenadai = new VerticeCoordenada(vecino, 0, 0);
                coordenadai = coordenadas[arr.busquedaBinaria(coordenadas, coordenadai)];
                aristas += utils.linea(v.x, v.y, coordenadai.x, coordenadai.y);
            }
        }

        return aristas + vertices;
    }

    public static int obtenerMaximo(Grafica<Integer> g) {
        int max = 0;
        for (int i : g) {
            max = i;
            break;
        }
        for (int i : g)
            if (max < i)
                max = i;
        return max;
    }

}