package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto2.svg.SVG_Factory;

import mx.unam.ciencias.edd.proyecto2.exception.EddInvalidoExcepcion;
import mx.unam.ciencias.edd.proyecto2.exception.FormatoInvalidoExcepcion;
import mx.unam.ciencias.edd.proyecto2.exception.RelacionInvalidaExcepcion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * <p><b>Proyecto 2: Graficador de estructuras de datos.</b></p>
 * <p>Graficador de diferentes estructuras de datos a SVG</p>
 * <p>El programa consiste en dada la entrada estandar o un archivo a leer, se agregaran los elementos a una estructura
 * de datos, en caso de ser gráfica también se harán relaciones, para después mostrar el resultado en la salida estándar.</p>
 */
public class Proyecto2 {
    /** Lista a la cual la cual tendrá los elementos de cada estructura. */
    static Lista<Integer> elements = new Lista<>();
    /** Grafica a la que le serán agregadas elementos y relaciones. */
    static Grafica<Integer> graph = new Grafica<>();
    /** Enumerador para identificar las diferentes estructuras de datos. */
    static EstructurasDeDatos dataStructure = EstructurasDeDatos.VACIO;

    /**
     * Método que se encargará de saber que estructura es, meter los elementos a una lista o gráfica, para después
     * poder trabajar con ellos.
     */
    private static void readStructureAndAddElementsAndRelations(String[] args, boolean standardInput)
            throws IOException, FormatoInvalidoExcepcion, EddInvalidoExcepcion,
                    RelacionInvalidaExcepcion, IllegalArgumentException {
        BufferedReader br;
        String input;
        int c = 0;

        if (standardInput)
            br = new BufferedReader(new InputStreamReader(System.in));
        else
            br = new BufferedReader(new FileReader(args[0]));

        while ((input = br.readLine()) != null) {
            if (c == 0) {
                if (input.isEmpty() || input.charAt(0) != '#')
                    throw new FormatoInvalidoExcepcion();
                dataStructure = EstructurasDeDatos.getEnum(input.substring(2).trim());
                if (dataStructure == EstructurasDeDatos.VACIO)
                    throw new EddInvalidoExcepcion();
            }
            if (c == 1) {
                input = input + " ";
                StringBuilder a = new StringBuilder();
                for (int i = 0; i < input.length() - 1; i++) {
                    if (Character.isDigit(input.charAt(i))) {
                        a.append(input.charAt(i));
                        if (!Character.isDigit(input.charAt(i + 1))) {
                            if (dataStructure == EstructurasDeDatos.GRAFICA)
                                graph.agrega(Integer.parseInt(a.toString()));
                            else
                                elements.agrega(Integer.parseInt(a.toString()));
                            a = new StringBuilder();
                        }
                    }
                }
                if (dataStructure != EstructurasDeDatos.GRAFICA)
                    break;
            }
            if (c == 2 && dataStructure == EstructurasDeDatos.GRAFICA) {
                Integer a = null, b = null;
                StringBuilder str = new StringBuilder();
                int relations = 0;
                for (int i = 0; i < input.length(); i++) {
                    if (Character.isDigit(input.charAt(i))) {
                        str.append(input.charAt(i));
                    } else if (input.charAt(i) == ',') {
                        if (a == null) {
                            a = Integer.parseInt(str.toString());
                            str = new StringBuilder();
                            relations++;
                        } else {
                            throw new RelacionInvalidaExcepcion();
                        }
                    }
                    if (input.charAt(i) == ';' || i == input.length() - 1) {
                        relations++;
                        if (relations++ != 2)
                            throw new RelacionInvalidaExcepcion();
                        b = Integer.parseInt(str.toString());
                        graph.conecta(a, b);
                        a = b = null;
                        str = new StringBuilder();
                        relations = 0;
                    }
                }
                break;
            }
            c++;
        }
    }

    /**
     * Método que dada un enumerador, lista de elementos o una gráfica devulve una cadena el cual es es el código SVG,
     * el cual será mostrado en la salida estándar
     */
    private static String getSVG(EstructurasDeDatos dataStructure, Lista<Integer> elements, Grafica<Integer> graph) {
        SVG_Factory<Integer> svgFactory = new SVG_Factory<>();
        switch (dataStructure) {
            case LISTA:
            case COLA:
            case PILA:
                return svgFactory.getListStackQueue(elements, dataStructure).drawSVG();
            case AROJINEGRO:
            case ACOMPLETO:
            case AVL:
            case AORDENADO:
            case MONTICULOMINIMO:
                return svgFactory.getTree(elements, dataStructure).drawSVG();
            case GRAFICA:
                return svgFactory.getGraph(graph).drawSVG();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        try {
            readStructureAndAddElementsAndRelations(args, args.length == 0);
        } catch (IOException | FormatoInvalidoExcepcion | EddInvalidoExcepcion | RelacionInvalidaExcepcion | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        System.out.print(getSVG(dataStructure, elements, graph));
    }

}