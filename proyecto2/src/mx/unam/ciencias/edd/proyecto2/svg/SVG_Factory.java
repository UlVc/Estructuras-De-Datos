package mx.unam.ciencias.edd.proyecto2.svg;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto2.EstructurasDeDatos;

/**
 * <p>Se utilizó el patrón de Diseño <b>Factory Pattern</b> (el cual es un patrón de creación) para evitar
 * que se estén creando objetos directamente de las clases, de esta forma la creación de objetos
 * es mas sencilla y solamente se pueden mandar a llamar métodos de la interfaz {@link SVG_Graficable}.</p>
 * <a href="https://en.wikipedia.org/wiki/Factory_method_pattern">Consulte aquí</a>
 *
 */
public class SVG_Factory<T extends Comparable<T>> {

    public SVG_Factory() {}

    public SVG_Graficable getListStackQueue(Lista<T> elements, EstructurasDeDatos edd) {
        return new SVG_List_Stack_Queue<>(elements, edd);
    }

    public SVG_Graficable getTree(Lista<T> elements, EstructurasDeDatos edd) {
        return new SVG_Tree<>(elements, edd);
    }

    public SVG_Graficable getGraph(Grafica<T> graph) {
        return new SVG_Graph<>(graph);
    }

}