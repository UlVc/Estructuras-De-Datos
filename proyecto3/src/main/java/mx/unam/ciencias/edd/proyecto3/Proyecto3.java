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

    public static void main(String[] args) throws Exception {

        pieChart pie = new pieChart(49.6);
        pie.generaSVG();

        Lista<String> archivos = new Lista<String>();
        Lista<String> titulosLista = new Lista<String>();
        Lista<String> lista = new Lista<String>();

        String directorio = "";
        String cadena = "";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o"))
                directorio = args[i + 1];
            else
                archivos.agrega(args[i]);
        }

        archivos.elimina(directorio);

        for (String s : archivos) {
            String[] directorioTitulo = s.split("/");
            String tituloConExtension = directorioTitulo[directorioTitulo.length - 1];
            String titulo = tituloConExtension.substring(0, tituloConExtension.length() - 4);
            titulosLista.agrega(titulo);

            try {
                BufferedReader br = new BufferedReader(new FileReader(s));
                while ((cadena = br.readLine()) != null)
                    lista.agrega(cadena.trim());
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

    private static void html(String directorio, Lista<String> lista, String titulo) throws Exception {
        String cadena = "";
        String conteo = "";

        for (String s : lista)
            cadena += ";" + s.toLowerCase().replace(".", "").trim().replace(" ", ";").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace(",", "");

        // Se tiene problemas cuando el archivo tiene varias lineas vacias.

        String[] arrayElementos = cadena.split(";");

        Diccionario<String, Integer> diccionario = cuentaPalabras(arrayElementos);

        contadorElementos.agrega(diccionario.getElementos());
        diccionarios.agrega(diccionario);

        Iterator<String> iteradorLLave = diccionario.iteradorLlaves();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();

            if (diccionario.get(llave) > 1)
                conteo += diccionario.get(llave) + " veces: " + llave + "<br>";
            else
                conteo += diccionario.get(llave) + " vez: " + llave + "<br>";
        }

        // Falta hacer que sólo se impriman los 15 más importantes.

        Diccionario<String, Integer> diccionarioAcotado = acotaDiccionario(diccionario);

        String avl = graficadora.construirEstructura(diccionarioAcotado, "ArbolAVL");
        String arn = graficadora.construirEstructura(diccionarioAcotado, "ArbolRojinegro");

        ConstruyeHTML html = new ConstruyeHTML(conteo, titulo, directorio, diccionario, avl, arn);
        html.generaHTML();
    }

    private static Diccionario<String, Integer> acotaDiccionario(Diccionario<String, Integer> diccionario) {
        if (diccionario.getElementos() < 14)
            return diccionario;

        Diccionario<String, Integer> aux = new Diccionario<String, Integer>();
        Iterator<String> iteradorLLave = diccionario.iteradorLlaves();
        int contador = 1;

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            aux.agrega(llave, diccionario.get(llave));

            if (contador == 15)
                break;

            contador++;
        }

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

    private static Diccionario<String, Integer> ordena(Diccionario<String, Integer> diccionario) {
        Lista<Integer> l = new Lista<Integer>();
        Diccionario<String, Integer> aux = new Diccionario<String, Integer>();
        Iterator<String> iteradorLLave = diccionario.iteradorLlaves();
        int i = 0;

        for (Integer i : diccionario)
            l.agrega(i);

        l = Lista.mergeSort(l).reversa();

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            aux.agrega(llave, l.get(i));
            i++;
        }

        return aux;
    }
}
