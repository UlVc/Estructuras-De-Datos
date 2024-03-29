package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.proyecto3.eddsvg.GraficadoraEDD;
import mx.unam.ciencias.edd.proyecto3.html.ConstruyeHTML;
import mx.unam.ciencias.edd.proyecto3.excepcion.Excepcion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.text.Normalizer;

public class Proyecto3 {

    private static GraficadoraEDD graficadora = new GraficadoraEDD();
    private static Lista<Integer> contadorElementos = new Lista<Integer>();
    private static Lista<Diccionario<String, Integer>> diccionarios = new Lista<Diccionario<String, Integer>>();
    private static String directorio = "";

    private static void html(String directorio, Lista<String> lista, String titulo) throws Exception {
        String cadena = "";
        String conteo = "";

        for (String s : lista) {
            String aux = Normalizer.normalize(s.trim().toLowerCase(), Normalizer.Form.NFD);
            aux = aux.replaceAll("[^\\p{ASCII}]", "");
            aux = aux.replaceAll("\\p{Punct}", "");
            aux = aux.replaceAll("[*\\p{L}\\p{Nd}]-", " ");
            aux = aux.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            cadena += " " + aux;
        }

        String[] arrayElementosAux = cadena.split(" ");
        Lista<String> listaElementosRepetidos = new Lista<String>();

        for (String s : arrayElementosAux)
            if (s.length() > 0)
                listaElementosRepetidos.agrega(s);

        Diccionario<String, Integer> diccionario = cuentaPalabras(listaElementosRepetidos);

        contadorElementos.agrega(diccionario.getElementos());

        Iterator<String> iteradorLLave = diccionario.iteradorLlaves();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();

            if (diccionario.get(llave) > 1)
                if (diccionario.get(llave) > 4)
                    conteo += "<strong><font color='red'>" + diccionario.get(llave) + "</font></strong> veces: <b>" + llave + "</b><br>\n";
                else
                    conteo += diccionario.get(llave) + " veces: <b>" + llave + "</b><br>\n";
            else
                conteo += diccionario.get(llave) + " vez: <b>" + llave + "</b><br>\n";
        }

        Diccionario<String, Integer> diccionarioAcotado = acotaDiccionario(diccionario);

        String avl = graficadora.construirEstructura(diccionarioAcotado, "ArbolAVL");
        String arn = graficadora.construirEstructura(diccionarioAcotado, "ArbolRojinegro");

        diccionario = cuentaPalabras(listaElementosRepetidos);

        Lista<Integer> listaRepeticiones = obtenListaRepeticiones(diccionario);
        Lista<String> listaElementos = obtenListaElementos(diccionario);

        diccionario = cuentaPalabras(listaElementosRepetidos);

        double[] porcentajes = calculaPorcentaje(listaRepeticiones);

        ConstruyeHTML html = new ConstruyeHTML(conteo, titulo, directorio, diccionario, avl, arn, porcentajes, listaElementos);
        html.generaHTML();

        diccionario = cuentaPalabras(listaElementosRepetidos);
        diccionarios.agrega(diccionario);
    }

    private static Lista<Integer> obtenListaRepeticiones(Diccionario<String, Integer> diccionario) {
        Lista<Integer> l = new Lista<Integer>();

        for (Integer k : diccionario)
            l.agrega(k);

        l = Lista.mergeSort(l).reversa();

        return l;
    }

    private static Lista<String> obtenListaElementos(Diccionario<String, Integer> diccionario) {
        Lista<Integer> l = obtenListaRepeticiones(diccionario);
        Lista<String> s = new Lista<String>();
        int longitud = diccionario.getElementos();

        for (int n = 0; n < longitud; n++) {
            Iterator<String> iteradorLLave = diccionario.iteradorLlaves();

            while (iteradorLLave.hasNext()) {
                String llave = iteradorLLave.next();

                if (diccionario.get(llave) == l.get(n)) {
                    s.agrega(llave);
                    diccionario.elimina(llave);
                    break;
                }
            }
        }

        if (longitud > 9)
            s.inserta(9, "resto de palabras");

        return s;
    }

    private static double[] calculaPorcentaje(Lista<Integer> repeticiones) {
        double n = 0;

        for (Integer i : repeticiones)
            n += i;

        int[] rebanadas = obtenElementosImportantes(repeticiones);
        double[] aux = new double[rebanadas.length];

        for (int i = 0; i < aux.length; i++)
            aux[i] = (rebanadas[i] * 100) / n;

        return aux;
    }

    private static int[] obtenElementosImportantes(Lista<Integer> lista) {
        int longitud = 0;

        if (lista.getElementos() < 10)
            longitud = lista.getElementos();
        else
            longitud = 10;

        int[] aux = new int[longitud];

        for (int x = 0; x < (longitud - 1); x++)
            aux[x] = lista.get(x);

        for (Integer i : aux)
            lista.elimina(i);

        for (Integer k : lista)
            aux[longitud - 1] += k;

        return aux;
    }

    private static Diccionario<String, Integer> acotaDiccionario(Diccionario<String, Integer> diccionario) {
        if (diccionario.getElementos() < 16)
            return diccionario;

        Lista<Integer> i = obtenListaRepeticiones(diccionario);
        Lista<String> s = obtenListaElementos(diccionario);
        Diccionario<String, Integer> aux = new Diccionario<String, Integer>();

        for (int j = 0; j < 15; j++)
            aux.agrega(s.get(j), i.get(j));

        return aux;
    }

    private static Diccionario<String, Integer> cuentaPalabras(Lista<String> lista) {
        Diccionario<String, Integer> diccionario = new Diccionario<String, Integer>();

        for (String s : lista)
            if (diccionario.contiene(s)) {
                int n = diccionario.get(s);
                diccionario.elimina(s);
                diccionario.agrega(s, n += 1);
            } else
                diccionario.agrega(s, 1);

        return diccionario;
    }

    private static Lista<String> obtenerArchivosYDirectorio(String[] args) {
        Lista<String> archivos = new Lista<String>();

        if (args.length < 1)
            Excepcion.error("Introduzca archivos y la bandera -o y después el nombre del directorio.");

        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-o"))
                try {
                    directorio = args[i + 1];
                } catch(ArrayIndexOutOfBoundsException aiobe){
                    Excepcion.error("Inserte un directorio después de la bandera -o.");
                }
            else
                archivos.agrega(args[i]);

        if (directorio.equals(""))
            Excepcion.error("Introduzca la bandera -o y después el nombre del directorio.");

        archivos.elimina(directorio);

        if (archivos.esVacia())
            Excepcion.error("Introduzca archivos.");

        return archivos;
    }

    public static void main(String[] args) throws Exception {
        Lista<String> titulosLista = new Lista<String>();
        Lista<String> lista = new Lista<String>();
        String cadena = "";

        Lista<String> archivos = obtenerArchivosYDirectorio(args);

        for (String s : archivos) {
            String[] directorioTitulo = s.split("/");
            String tituloConExtension = directorioTitulo[directorioTitulo.length - 1];
            String txt = tituloConExtension.substring(tituloConExtension.length() - 3, tituloConExtension.length());

            if (!txt.equals("txt"))
                Excepcion.error("Introduzca de manera correcta un archivo de texto.");

            String titulo = tituloConExtension.substring(0, tituloConExtension.length() - 4);
            titulosLista.agrega(titulo);

            try {
                BufferedReader br = new BufferedReader(new FileReader(s));

                while ((cadena = br.readLine()) != null)
                    if (cadena.trim().length() > 0)
                        lista.agrega(cadena.trim());

                br.close();

                if (lista.esVacia())
                    Excepcion.error("Introduzca texto en el archivo " + s);

            } catch (Exception e) {
                Excepcion.error("Introduzca de manera correcta un archivo de texto.");
            }


            html(directorio, lista, titulo);
            lista.limpia();
        }

        String graficaSVG = graficadora.construirEstructuraGrafica(diccionarios, titulosLista, contadorElementos);

        ConstruyeHTML html = new ConstruyeHTML(titulosLista, directorio);
        html.generadorIndex(contadorElementos, graficaSVG);
    }
}
