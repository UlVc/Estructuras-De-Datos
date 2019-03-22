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
 * <p>Proyecto 1: Ordenador lexicografico.</p>
 
 * <p> Ordenador lexicográfico (una versión más sencilla del
 * programa sort en Unix) que funciona con uno o más archivos de texto o la
 * entrada estándar y que imprima su salida en la salida estándar.</p>
 *
 */
public class Proyecto1 {

    /* Variables de tipo String que nos ayudarán para representar banderas y errores. */
    static final String ERROR_ENTRADA_ESTANDAR = "Hubo un error con la entrada.";
    static final String ERROR_LECTURA_ARCHIVOS = "Hubo un error en el leer archivo(s).";
    static final String B_REVERSA = "-r";
    static final String B_ARCHIVO = "-o";
    static String path;

    /* Lineas del archivo recibido. */
    static Lista<Comparador> parrafoLista = new Lista<>();

    /* Lista donde se guardarán los archivos que se pasan como argumento. */
    static Lista<String> archivosLista = new Lista<>();

    /* Variables booleanas que nos indicara si la bandera se activaron. */
    static boolean esReversa = false;
    static boolean esArchivo = false;

    public static void main(String[] args) {

        checaArgumentos(args);
        boolean esEntradaEstandar = entradaEstandar(args, esReversa, esArchivo);
        String entrada;
        System.out.println(esArchivo);
        if(!esEntradaEstandar) {

            //---------------Se pasó un archivo o más.---------------

            for(String archivos : archivosLista)
                try(BufferedReader bfr = new BufferedReader(new FileReader(archivos))) {
                    while((entrada = bfr.readLine()) != null)
                        parrafoLista.agrega(new Comparador(entrada));  //Vamos agregando línea a línea las lineas del archivo que se pasó en los argumentos.
                }catch(IOException io) {
                    System.out.println(ERROR_LECTURA_ARCHIVOS);
                    System.exit(-1);
                }
        }else

            //---------------Se pasó un texto por la entrada estándar.---------------

            try(BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in))) {

                //---------Si se pasó la bandera '-o'---------
                if(esArchivo) {
                	System.out.println("SE ACTIVÓ LA BANDERA -O");
                    try{
                        File file = new File(path);
                        FileWriter filew = new FileWriter(file);

                        if(!file.exists())
                            file.createNewFile();

                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

                        while ((entrada = bfr.readLine()) != null) {
                            bw.write(entrada);
                        	bw.newLine();
                        }

                        bw.close();

                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }else
                    while((entrada = bfr.readLine()) != null)
                        parrafoLista.agrega(new Comparador(entrada)); //Vamos agregando línea a línea las lineas de la entrada estándar.

            }catch(IOException io) {
                System.out.println(ERROR_ENTRADA_ESTANDAR);
                System.exit(-1);
            }

        //Imprimimos el archivo; si se mandó la bandera '-r', se ejecutará reversa() en la lista.
        for(Comparador cad : esReversa ? Lista.mergeSort(parrafoLista).reversa() : Lista.mergeSort(parrafoLista))
            System.out.println(cad);
    }

    /**
     * Metodo que analiza los argumentos recibidos y nos indicará si tiene alguna bandera;
     * en caso de que en los argumenton tuvieran archivos, los guardará en una lista.
     * @param args Argumentos recibidos de la consola.
     */
    private static void checaArgumentos(String[] args) {

        for(int i=0;i<args.length;i++) {
        	if(args[i].equals(B_REVERSA)) esReversa = true;
            if(args[i].equals(B_ARCHIVO)) {// '-o'
            	System.out.println("SE ACTIVÓ LA BANDERA '-O' ");
            	esArchivo = true;
            	path = args[i+1];
            }
            if(!args[i].equals(B_REVERSA) && !args[i].equals(B_ARCHIVO)) archivosLista.agrega(args[i]);
        }
    }

    /**
     * Metodo que nos dice como se comportara nuestro programa, ya sea leyendo un
     * archivo o leyendo el texto con la entrada estandar.
     * @param args Argumentos recibidos de la consola.
     * @param esReversa Booleano que nos indica si se introdujo la bandera -r.
     * @param esArchivo Booleano que nos indica si se introdujo la bandera -o.
     * @return <tt>true</tt> Si es entrada estandar,
     *         <tt>false</tt> si leera un archivo(s).
     */
    private static boolean entradaEstandar(String[] args, boolean esReversa, boolean esArchivo) {
        return (args.length == 0 || (args.length == 1 && esReversa) || (args.length >= 1 && esArchivo)) ? true : false;
    }

}