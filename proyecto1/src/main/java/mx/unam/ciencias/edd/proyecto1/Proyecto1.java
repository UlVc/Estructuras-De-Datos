package mx.unam.ciencias.edd.proyecto1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

import mx.unam.ciencias.edd.Lista;

/**
 * <p>Proyecto 1: Ordenador lexicográfico.</p>
 *
 * <p> Ordenador lexicográfico (una versión más sencilla del
 * programa sort en Unix) que funciona con uno o más archivos de texto o la
 * entrada estándar y que imprima su salida en la salida estándar.</p>
 *
 */
public class Proyecto1 {

    /* Variables de tipo String que nos ayudarán para representar banderas y errores. */
    static final String ERROR_ENTRADA_ESTANDAR = "Hubo un error con la entrada.";
    static final String ERROR_ENTRADA_OUTPUT = "Hubo un error en la dirección.";
    static final String B_REVERSA = "-r";
    static final String B_OUTPUT = "-o";
    static String path, entrada;
    static Banderas b = new Banderas();

    /* Lineas del archivo recibido. */
    static Lista<Comparador> parrafoLista = new Lista<>();

    /* Lista donde se guardarán los archivos que se pasan como argumento. */
    static Lista<String> archivosLista = new Lista<>();
    static Lista<String> aux2 = new Lista<>();

    /* Variables booleanas que nos indicara si la bandera se activaron. */
    static boolean esReversa, esOutput;

    /**
     * Metodo que analiza los argumentos recibidos y nos indicará si tiene alguna bandera;
     * en caso de que en los argumenton tuvieran archivos, los guardará en una lista.
     * @param args Argumentos recibidos de la consola.
     */
    private static void checaArgumentos(String[] args) {

        esOutput = b.output(args);
        esReversa = b.esReversa(args);

        if(esOutput)
            path = b.path(args);

        for(int i=0;i<args.length;i++)
            if(!args[i].equals(B_REVERSA) && !args[i].equals(B_OUTPUT)) 
                archivosLista.agrega(args[i]);
    }

    /* Método que se utiliza cuando se pasó un texto por la entrada estándar. */
    private static void eE() {
        for(String archivos : archivosLista)
            try(BufferedReader bfr = new BufferedReader(new FileReader(archivos))) {
                if(esOutput) {
                    while((entrada = bfr.readLine()) != null)
                        parrafoLista.agrega(new Comparador(entrada));
                    b.banderaOutput(path,entrada,bfr,listaOrdenada());
                }else
                    while((entrada = bfr.readLine()) != null)
                        parrafoLista.agrega(new Comparador(entrada));
            }catch(IOException io) {
                System.out.println(ERROR_ENTRADA_OUTPUT);
                System.exit(-1);
            }
    }

    /* Método que se utiliza cuando se pasó un archivo por medio de cat. */
    private static void cat() {
        try(BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in))) {
            if(esOutput) {
                while((entrada = bfr.readLine()) != null)
                    parrafoLista.agrega(new Comparador(entrada));
                b.banderaOutput(path,entrada,bfr,listaOrdenada());
            }else
                while((entrada = bfr.readLine()) != null)
                    parrafoLista.agrega(new Comparador(entrada));
            }catch(IOException io) {
                System.out.println(ERROR_ENTRADA_ESTANDAR);
                System.exit(-1);
            }
    }

    /** 
     * Método que imprime lo que se recibió por medio de la entrada estándar,
     * y analiza si se usó la bandera '-r' o no.
     */
    private static Lista<String> imprimeLista() {
        Lista<String> aux = new Lista<>();
        int x = 0;
        for(Comparador cad : esReversa ? Lista.mergeSort(parrafoLista).reversa() : Lista.mergeSort(parrafoLista)) {
            aux.agrega(cad.toString());
            System.out.println(cad);
            x++;
        }
        return aux;
    }

    /** 
     * Método que agrega lo que se recibió por medio de la entrada estándar a una Lista,
     * y analiza si se usó la bandera '-r' o no.
     */
    private static Lista<String> listaOrdenada() {
        Lista<String> aux = new Lista<>();
        int x = 0;
        for(Comparador cad : esReversa ? Lista.mergeSort(parrafoLista).reversa() : Lista.mergeSort(parrafoLista)) {
            aux.agrega(cad.toString());
            x++;
        }
        return aux;
    }

    /**
     * Metodo que nos dice como se comportara nuestro programa, ya sea leyendo un
     * archivo o leyendo el texto con la entrada estandar.
     */
    private static boolean esCat(String[] args, boolean esReversa, boolean esOutput) {
        return (args.length == 0 || (args.length == 1 && esReversa) || (args.length >= 1 && esOutput)) ? true : false;
    }

    public static void main(String[] args) {

        checaArgumentos(args);
        boolean esCat = esCat(args, esReversa, esOutput);

        if(esCat) cat();
        else eE();

        imprimeLista();

    }

}