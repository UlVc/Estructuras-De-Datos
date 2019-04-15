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
 * <p>Clase que implementa {@link SVG_Graficable} la cual dada una lista de elementos hace las operaciones necesarias para
 * que el en método {@link SVG_Graficable#drawSVG} devuelva un código SVG de los diferentes tipos de árboles.</p>
 */
class SVG_Tree<T extends Comparable<T>> implements SVG_Graficable {

    private ArbolBinario<T> arbol;
    private EstructurasDeDatos edd;

    private int width;
    private int height;
    private int depth;

    SVG_Tree(Lista<T> elementos, EstructurasDeDatos edd) {
        this.edd = edd;
        switch (edd) {
            case ACOMPLETO:
            case MONTICULOMINIMO:
                arbol = new ArbolBinarioCompleto<>();
                break;
            case AORDENADO:
                arbol = new ArbolBinarioOrdenado<>();
                break;
            case AROJINEGRO:
                arbol = new ArbolRojinegro<>();
                break;
            case AVL:
                arbol = new ArbolAVL<>();
                break;
        }
        if (edd == EstructurasDeDatos.MONTICULOMINIMO) {
            Lista<Indexable<T>> le = new Lista<>();
            elementos.forEach(e -> le.agrega(new Indexable<T>(e, Integer.parseInt(e.toString()))));
            MonticuloMinimo<Indexable<T>> minHeap = new MonticuloMinimo<>(le);
            minHeap.forEach(e -> arbol.agrega(e.getElemento()));
        } else {
            elementos.forEach(arbol::agrega);
        }
    }

    @Override public String drawSVG() {
        int vertexRadius = SVG_Util.getVertexRadius(arbol);
        depth = arbol.profundidad() == 0 ? 1 : arbol.profundidad();
        height = 100 + depth * 100;
        width = (int) (Math.pow(2, depth) * vertexRadius*2);

        StringBuilder builder = new StringBuilder();
        builder.append(SVG_Util.XML_PROLOG);
        builder.append(SVG_Util.startSVGAndPutHeightWidth(height, width));
        builder.append(SVG_Util.OPEN_G_TAG);
        if (!arbol.esVacio()) {
            builder.append(drawLines(arbol.raiz(), (width / 2) / 2, width / 2, 50));
            builder.append(drawVertices(arbol.raiz(), (width / 2) / 2, width / 2, 50, vertexRadius));
        }
        builder.append(SVG_Util.CLOSE_G_TAG);
        builder.append(SVG_Util.closeSVG());
        return builder.toString();
    }

    /**
     * Método que devulve un codigo SVG con los vértices del árbol.
     *
     * @param vertex           Vertice de un árbol.
     * @param decrement        Variable que se irá diviendo en cada llamada recursiva para tener el valor de "la nueva mitad".
     * @param fatherCoordinate Variable que indica la coordenada <b><i>x</i></b> del padre.
     * @param y                La coordenada <b><i>y</i></b>, en cada llamada recusiva aumentará un tamaño constante.
     * @return Un código SVG con las vértices de arbol.
     */
    private String drawVertices(VerticeArbolBinario vertex, double decrement, double fatherCoordinate, double y,double radius) {
        String s = "";
        if (vertex != null) {
            String balance = getHeightAndBalance(vertex.toString());
            s += SVG_Util.drawTreeVertex(fatherCoordinate, y, radius,vertex.get().toString(), getVertexColor(vertex), balance != null, balance);
            if (vertex.hayIzquierdo() && vertex.hayDerecho())
                s += drawVertices(vertex.getIzquierdo(), decrement / 2, fatherCoordinate - decrement, y + 100,radius) +
                        drawVertices(vertex.getDerecho(), decrement / 2, fatherCoordinate + decrement, y + 100,radius);
            else if (vertex.hayIzquierdo())
                s += drawVertices(vertex.getIzquierdo(), decrement / 2, fatherCoordinate - decrement, y + 100,radius);
            else if (vertex.hayDerecho())
                s += drawVertices(vertex.getDerecho(), decrement / 2, fatherCoordinate + decrement, y + 100,radius);
        }
        return s;
    }

    /**
     * Método que devulve un codigo SVG con las aristas del árbol.
     *
     * @param vertex           Vertice de un árbol.
     * @param decrement        Variable que se irá diviendo en cada llamada recursiva para tener el valor de "la nueva mitad".
     * @param fatherCoordinate Variable que indica la coordenada <b><i>x</i></b> del padre.
     * @param y                La coordenada <b><i>y</i></b>, en cada llamada recusiva aumentará un tamaño constante.
     * @return Un código SVG con las aristas de arbol.
     */
    private String drawLines(VerticeArbolBinario vertex, double decrement, double fatherCoordinate, double y) {
        String s = "";
        if (vertex.hayIzquierdo() && vertex.hayDerecho())
            s += SVG_Util.drawLine(fatherCoordinate, y, fatherCoordinate - decrement, y + 100) +
                    SVG_Util.drawLine(fatherCoordinate, y, fatherCoordinate + decrement, y + 100) +
                    drawLines(vertex.getIzquierdo(), decrement / 2, fatherCoordinate - decrement, y + 100) +
                    drawLines(vertex.getDerecho(), decrement / 2, fatherCoordinate + decrement, y + 100);
        else if (vertex.hayIzquierdo())
            s += SVG_Util.drawLine(fatherCoordinate, y, fatherCoordinate - decrement, y + 100) +
                    drawLines(vertex.getIzquierdo(), decrement / 2, fatherCoordinate - decrement, y + 100);
        else if (vertex.hayDerecho())
            s += SVG_Util.drawLine(fatherCoordinate, y, fatherCoordinate + decrement, y + 100) +
                    drawLines(vertex.getDerecho(), decrement / 2, fatherCoordinate + decrement, y + 100);
        return s;
    }

    /**
     * Método que devuelve el color de un vértice.
     *
     * @param vertex El método {@link VerticeArbolBinario#toString()} que devuelve el toString() del vértice.
     * @return Color {@link SVG_Util#COLOR_RED} o {@link SVG_Util#COLOR_BLACK} en caso de ser un vertice de un
     * <b>Arbol rojinegro</b>, {@link SVG_Util#COLOR_WHITE} en otro caso.
     */
    private String getVertexColor(VerticeArbolBinario vertex) {
        String vertexColor;
        if (edd == EstructurasDeDatos.AROJINEGRO)
            if (vertex.toString().substring(0, 1).equals("N"))
                vertexColor = SVG_Util.COLOR_BLACK;
            else
                vertexColor = SVG_Util.COLOR_RED;
        else
            vertexColor = SVG_Util.COLOR_WHITE;
        return vertexColor;
    }

    /**
     * Método que devuelve el balance y altura en caso de ser un <b>Arbol AVL</b>.
     *
     * @param s El método {@link VerticeArbolBinario#toString()} que devuelve el toString() del vértice.
     * @return El balance y altura.
     */
    private String getHeightAndBalance(String s) {
        String balance = null;
        if (edd == EstructurasDeDatos.AVL) {
            StringBuilder h = new StringBuilder();
            for (int i = s.length() - 1; i >= 0; i--)
                if (s.charAt(i) == ' ')
                    break;
                else
                    h.append(s.charAt(i));
            balance = h.reverse().toString();
        }
        return balance;
    }

}