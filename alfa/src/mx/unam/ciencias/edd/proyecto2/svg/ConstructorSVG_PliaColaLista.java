package mx.unam.ciencias.edd.proyecto2.svg;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto2.EstructurasDeDatos;

/**
 * Clase que recibe una lista, cola o pila y devuelve un codigo SVG.
 */
public class ConstructorSVG_PliaColaLista<T> {

    private Lista<T> elementos;
    private EstructurasDeDatos edd;

    ConstructorSVG_PliaColaLista(Lista<T> elementos, EstructurasDeDatos edd) {
        this.elementos = elementos;
        this.edd = edd;
    }

    public String drawSVG() {

        int i = 0;

        StringBuilder cadenaSVG = new StringBuilder();
        cadenaSVG.append(EtiquetasSVG.XML_PROLOG);
        if(edd != EstructurasDeDatos.PILA) {
            String conexion = edd == EstructurasDeDatos.LISTA ? "↔": "→";
            cadenaSVG.append(EtiquetasSVG.SvgAlturaAnchura(60, (10 + 80 * elementos.getElementos() - 10)));
            cadenaSVG.append(EtiquetasSVG.Defs.createDefs("conexion", EtiquetasSVG.dibujaFlecha(conexion)));
            cadenaSVG.append(EtiquetasSVG.ABRE_G_TAG);
            for(T x : elementos) {
                cadenaSVG.append(EtiquetasSVG.dibujaCuadrados(10 + (80 * i), 10, 40, 60, x.toString(), edd));
                if(i != elementos.getElementos() - 1)
                    cadenaSVG.append(EtiquetasSVG.Defs.createUseTag("conexion", 80 + i * 80, 35));
                i++;
            }
        }else {
            cadenaSVG.append(EtiquetasSVG.SvgAlturaAnchura(20 + elementos.getElementos() * 40, 80));
            cadenaSVG.append(EtiquetasSVG.ABRE_G_TAG);
            for(T x : elementos.reversa())
                cadenaSVG.append(EtiquetasSVG.dibujaCuadrados(10, 10 + 40 * i++, 40, 60, x.toString(), edd));
        }
        cadenaSVG.append(EtiquetasSVG.CIERRA_G_TAG);
        cadenaSVG.append(EtiquetasSVG.cierraSVG());
        return cadenaSVG.toString();
    }
}