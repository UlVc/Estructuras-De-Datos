package mx.unam.ciencias.edd.proyecto2.svg;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto2.EstructurasDeDatos;

/**
 * <p>Clase que implementa {@link SVG_Graficable} la cual dada una lista hace las operaciones necesarias para
 * que el en método {@link SVG_Graficable#drawSVG} devuelva un código SVG de una lista, pila o cola.</p>
 */
class SVG_List_Stack_Queue<T> implements SVG_Graficable {
    private Lista<T> elements;
    private EstructurasDeDatos edd;

    SVG_List_Stack_Queue(Lista<T> elements, EstructurasDeDatos edd) {
        this.elements = elements;
        this.edd = edd;
    }

    @Override public String drawSVG() {
        int i = 0;

        StringBuilder builder = new StringBuilder();
        builder.append(SVG_Util.XML_PROLOG);
        if (edd != EstructurasDeDatos.PILA) {
            String arrow = edd == EstructurasDeDatos.LISTA ? SVG_Util.TEXT_LEFT_RIGHT_ARROW : SVG_Util.TEXT_RIGHTWARD_ARROW;
            builder.append(SVG_Util.startSVGAndPutHeightWidth(60, (10 + 80 * elements.getElementos() - 10)));
            builder.append(SVG_Util.Defs.createDefs(SVG_Util.ID_DEF_ARROW, SVG_Util.drawArrow(arrow)));
            builder.append(SVG_Util.OPEN_G_TAG);
            for (T x : elements) {
                builder.append(SVG_Util.drawSquare(10 + (80 * i), 10, 40, 60, x.toString(), edd));
                if (i != elements.getElementos() - 1)
                    builder.append(SVG_Util.Defs.createUseTag(SVG_Util.ID_DEF_ARROW, 80 + i * 80, 35));
                i++;
            }
        } else {
            builder.append(SVG_Util.startSVGAndPutHeightWidth(20 + elements.getElementos() * 40, 80));
            builder.append(SVG_Util.OPEN_G_TAG);
            for (T x : elements.reversa())
                builder.append(SVG_Util.drawSquare(10, 10 + 40 * i++, 40, 60, x.toString(), edd));
        }
        builder.append(SVG_Util.CLOSE_G_TAG);
        builder.append(SVG_Util.closeSVG());
        return builder.toString();
    }
}