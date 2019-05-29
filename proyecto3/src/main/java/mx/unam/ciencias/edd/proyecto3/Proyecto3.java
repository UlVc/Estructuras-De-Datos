package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.proyecto3.eddsvg.GraficadoraEDD;
import mx.unam.ciencias.edd.proyecto3.html.ConstruyeHTML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;

public class Proyecto3 {

    private static GraficadoraEDD graficadora = new GraficadoraEDD();
    private static Lista<Integer> contadorElementos = new Lista<Integer>();
    private static Lista<Diccionario<String, Integer>> diccionarios = new Lista<Diccionario<String, Integer>>();
    private static String directorio = "";
    private static Lista<String> archivos = new Lista<String>();

    private static void html(String directorio, Lista<String> lista, String titulo) throws Exception {
        String cadena = "";
        String conteo = "";

        for (String s : lista)
            cadena += ";" + s.trim().toLowerCase().replace(".", "").replace(" ", ";").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace(",", "").replace("?", "").replace("¿", "");

        String[] arrayElementos = cadena.split(";");

        Diccionario<String, Integer> diccionario = cuentaPalabras(arrayElementos);

        contadorElementos.agrega(diccionario.getElementos());
        diccionarios.agrega(diccionario);

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

        diccionario = cuentaPalabras(arrayElementos);

        Lista<Integer> listaRepeticiones = obtenListaRepeticiones(diccionario);
        Lista<String> listaElementos = obtenListaElementos(diccionario);

        diccionario = cuentaPalabras(arrayElementos);

        double[] porcentajes = calculaPorcentaje(listaRepeticiones);

        ConstruyeHTML html = new ConstruyeHTML(conteo, titulo, directorio, diccionario, avl, arn, porcentajes, listaElementos);
        html.generaHTML();
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

    private static Diccionario<String, Integer> cuentaPalabras(String[] array) {
        Diccionario<String, Integer> diccionario = new Diccionario<String, Integer>();

        for (int i = 1; i < array.length; i++)
            if (diccionario.contiene(array[i])) {
                int n = diccionario.get(array[i]);
                diccionario.elimina(array[i]);
                diccionario.agrega(array[i], n += 1);
            } else
                diccionario.agrega(array[i], 1);

        return diccionario;
    }

    private static void obtenerArchivosYDirectorio(String[] args) {
        for (int i = 0; i < args.length; i++)
            if (args[i].equals("-o"))
                directorio = args[i + 1];
            else
                archivos.agrega(args[i]);

        archivos.elimina(directorio);
    }

    public static void main(String[] args) throws Exception {
        Lista<String> titulosLista = new Lista<String>();
        Lista<String> lista = new Lista<String>();
        String cadena = "";

        obtenerArchivosYDirectorio(args);

        for (String s : archivos) {
            String[] directorioTitulo = s.split("/");
            String tituloConExtension = directorioTitulo[directorioTitulo.length - 1];
            String titulo = tituloConExtension.substring(0, tituloConExtension.length() - 4);
            titulosLista.agrega(titulo);

            try {
                BufferedReader br = new BufferedReader(new FileReader(s));
                
                while ((cadena = br.readLine()) != null)
                    if (cadena.trim().length() > 0)
                        lista.agrega(cadena.trim());

                br.close();
            } catch (Exception e) {
                System.out.println("Introduzca de manera correcta un archivo de texto.");
                System.exit(-1);
            }

            html(directorio, lista, titulo);
            lista.limpia();
        }

        String graficaSVG = graficadora.construirEstructuraGrafica(diccionarios, titulosLista, contadorElementos);

        ConstruyeHTML html = new ConstruyeHTML(titulosLista, directorio);
        html.generadorIndex(contadorElementos, graficaSVG);
    }
}
