package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto2.svg.SVG_Factory;

import mx.unam.ciencias.edd.proyecto2.excepciones.EddInvalidoExcepcion;
import mx.unam.ciencias.edd.proyecto2.excepciones.FormatoInvalidoExcepcion;
import mx.unam.ciencias.edd.proyecto2.excepciones.RelacionInvalidaExcepcion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Proyecto 2: Graficador de estructuras de datos.
 */
public class Proyecto2 {
    /** Lista a la cual la cual tendrá los elementos de cada estructura. */
    static Lista<Integer> elements = new Lista<>();
    /** Grafica a la que le serán agregadas elementos y relaciones. */
    static Grafica<Integer> grafo = new Grafica<>();
    /** Enumerador para identificar las diferentes estructuras de datos. */
    static EstructurasDeDatos edd = EstructurasDeDatos.VACIO;

    /**
     * Método que se encargará de saber que estructura es, para después poder trabajar con ellos.
     */
    private static void Estructuras(String[] args, boolean standardInput) throws IOException, IllegalArgumentException, FormatoInvalidoExcepcion, EddInvalidoExcepcion, RelacionInvalidaExcepcion {

        BufferedReader br;
        String input;
        int c = 0;

        if(standardInput)
            br = new BufferedReader(new InputStreamReader(System.in));
        else
            br = new BufferedReader(new FileReader(args[0]));

        while((input = br.readLine()) != null) {
            if(c == 0) {
                if (input.isEmpty() || input.charAt(0) != '#') throw new FormatoInvalidoExcepcion();
                edd = EstructurasDeDatos.getEnum(input.substring(2).trim());
                if (edd == EstructurasDeDatos.VACIO) throw new EddInvalidoExcepcion();
            }
            if(c == 1) {
                input = input + " ";
                StringBuilder a = new StringBuilder();
                for (int i = 0; i < input.length() - 1; i++) {
                    if(Character.isDigit(input.charAt(i))) {
                        a.append(input.charAt(i));
                        if(!Character.isDigit(input.charAt(i + 1))) {
                            if(edd == EstructurasDeDatos.GRAFICA)
                                grafo.agrega(Integer.parseInt(a.toString()));
                            else
                                elements.agrega(Integer.parseInt(a.toString()));
                            a = new StringBuilder();
                        }
                    }
                }
                if(edd != EstructurasDeDatos.GRAFICA) break;
            }
            if(c == 2 && edd == EstructurasDeDatos.GRAFICA) {
                Integer a = null, b = null;
                StringBuilder str = new StringBuilder();
                int relations = 0;
                for(int i = 0; i < input.length(); i++) {
                    if(Character.isDigit(input.charAt(i)))
                        str.append(input.charAt(i));
                    else if(input.charAt(i) == ',') {
                        if(a == null) {
                            a = Integer.parseInt(str.toString());
                            str = new StringBuilder();
                            relations++;
                        }else
                            throw new RelacionInvalidaExcepcion();
                    }
                    if(input.charAt(i) == ';' || i == input.length() - 1) {
                        relations++;
                        if(relations++ != 2) throw new RelacionInvalidaExcepcion();
                        b = Integer.parseInt(str.toString());
                        grafo.conecta(a, b);
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
     * Método que devulve una cadena el cual es es el código SVG y sera mostrado en la salida estandar.
     */
    private static String getSVG(EstructurasDeDatos edd, Lista<Integer> elementos, Grafica<Integer> grafo) {
        SVG_Factory<Integer> svgFactory = new SVG_Factory<>();
        switch(edd) {
            case LISTA:
            case COLA:
            case PILA:
                return svgFactory.getListStackQueue(elementos, edd).drawSVG();
            case AROJINEGRO:
            case ACOMPLETO:
            case AVL:
            case AORDENADO:
            case MONTICULOMINIMO:
                return svgFactory.getTree(elementos, edd).drawSVG();
            case GRAFICA:
                return svgFactory.getGraph(grafo).drawSVG();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        try {
            Estructuras(args, args.length == 0);
        }catch(IOException | IllegalArgumentException | FormatoInvalidoExcepcion | EddInvalidoExcepcion | RelacionInvalidaExcepcion e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        System.out.print(getSVG(edd, elements, grafo));
    }

}