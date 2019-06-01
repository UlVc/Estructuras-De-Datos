package mx.unam.ciencias.edd.proyecto3.html;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.eddsvg.svg.PieChart;

import java.io.FileWriter;
import java.io.File;
import java.util.Iterator;

public class ConstruyeHTML {

    private static Diccionario<String, Integer> diccionario = new Diccionario<String, Integer>();
    private static Lista<String> archivos = new Lista<String>();
    private static final String doctype = "<!DOCTYPE html>\n";
    private static String titulo = "";
    private static String body = "";
    private static String directorio = "";
    private static String arvolAVl = "";
    private static String arvolRN = "";
    private static int rebanadas;
    private static double[] porcentajes;
    private static Lista<String> listaElementos;
    private static int numeroDeBarras = 0;

    public ConstruyeHTML(String body, String titulo, String directorio, Diccionario<String, Integer> diccionario, String arvolAVl, String arvolRN, double[] porcentajes, Lista<String> listaElementos) {
        this.body = body;
        this.titulo = titulo;
        this.directorio = checaDirectorio(directorio);
        this.diccionario = diccionario;
        this.arvolAVl = arvolAVl;
        this.arvolRN = arvolRN;
        this.rebanadas = porcentajes.length;
        this.porcentajes = porcentajes;
        this.listaElementos = listaElementos;
    }

    public ConstruyeHTML(Lista<String> archivos, String directorio) {
        this.archivos = archivos;
        this.directorio = checaDirectorio(directorio);
        this.titulo = "index";
    }

    private String checaDirectorio(String directorio) {
        if (!directorio.substring(directorio.length() - 1, directorio.length()).equals("/"))
            return directorio += "/";

        return directorio;
    }

    public static void generaHTML() throws Exception {
        File carpeta = new File(directorio);

        carpeta.mkdirs();

        try {
            FileWriter fw = new FileWriter(directorio + titulo + ".html");
            fw.write(generaCodigoHTML());
            fw.close();
        } catch (Exception e) {
            String s = generaCodigoHTML();
            System.out.println("No se encontró ruta especificada.");
        }
    }

    public static void generadorIndex(Lista<Integer> contadorElementos, String grafica) {
        String index = doctype + "<html>" + generaTitulo();

        index += "<header><h1 align='center'>Proyecto 3: Contador de palabras</h1></header>";

        for (String s : archivos) {
            int i = contadorElementos.getPrimero();

            if (i > 59)
                index += "    <a href='" + s + ".html'>" + s + "</a> - N&uacutemero de palabras en el archivo: <strong><font color='red'>" + i + " </font></strong><br> \n";
            else
                index += "    <a href='" + s + ".html'>" + s + "</a> - N&uacutemero de palabras en el archivo: <strong>" + i + " </strong><br> \n";

            contadorElementos.elimina(i);
        }

        index += "<body>\n";
        index += grafica;
        index += "\n</body>";

        index += "\n</html>";

        try {
            FileWriter fw = new FileWriter(directorio + "index.html");
            fw.write(index);
            fw.close();
        } catch (Exception e) {
            System.out.println("No se encontro ruta especificada.");
        }
    }

    private static String generaBarraSVG() {
        int y = 0;
        numeroDeBarras = diccionario.getElementos() - 1;
        Lista<String> colores = colores();
        Lista<Double> aux = new Lista<Double>();

        for (Double d : porcentajes)
            aux.agrega(d);

        aux = Lista.mergeSort(aux).reversa();

        String svg = "    <svg class='chart' width='" + aux.get(0) * 15 + "' height='" + ((rebanadas * 20) + 20) + "' xmlns='http://www.w3.org/2000/svg' aria-labelledby='title desc' role='img'> <title id='title'>Porcentaje sobre palabras repetidas</title>\n";

        for (int i = 0; i < rebanadas; i++) {
            String color = colores.get(i);
            svg += barra(porcentajes[i], color, listaElementos.get(i), y);
            y += 20;
        }

        return svg + "    </svg>\n";
    }

    private static String barra(double porcentaje, String color, String elemento, int y) {
        double longitudBarra = porcentaje;
        String s = "      <g class='bar'>\n        <rect width='" + (longitudBarra * 10) + "' height='19' y='" + y + "' fill='" + color + "'></rect>\n";

        numeroDeBarras--;

        return s;
    }

    private static String generaCodigoHTML() {
        return doctype + "<html>" + generaTitulo() + " <header><h1 align='center'>" + titulo + "</h1></header>\n" + generaReferenciaIndex() + "<br>" + generaBody() + "</html>";
    }

    private static String generaPieChart() {
        PieChart pie = new PieChart(rebanadas);
        String s = "    <svg style='width: 300px; height: 300px;' xmlns='http://www.w3.org/2000/svg'>\n";
        Lista<String> colores = colores();

        for (int i = 0; i < rebanadas; i++) {
            String color = colores.get(i);

            s += pie.generaSVG(porcentajes[i], i + 1, color, listaElementos.get(i));
        }

        s += "    </svg>\n";

        return s;
    }

    private static String generaInformacion() {
        PieChart pie = new PieChart(rebanadas);
        Lista<String> colores = colores();
        int max = 0;

        for (String d : listaElementos)
            if (d.length() > max)
                max = d.length();

        String t = "    <svg style='width:" + (max * 10) + "px; height: 330px;' xmlns='http:www.w3.org/2000/svg'>\n";

        for (int i = 0; i < rebanadas; i++) {
            String color = colores.get(i);

            t += pie.generaTexto(listaElementos.get(i), color, i + 1, porcentajes[i]);
        }

        t += "    </svg>\n";

        return t;
    }

    private static String generaBody() {
        return "<body>\n    " + body + "<br>\n" + generaInformacion() + generaPieChart() + generaBarraSVG() + arvolRN + "\n" + arvolAVl + "\n</body>\n";
    }

    private static String generaTitulo() {
        return "\n<head>\n    " + "<title> " + titulo + " </title>" + "\n</head>\n";
    }

    private static String generaReferenciaIndex() {
        return "<a href='index.html'>Regresar al index</a><br>\n";
    }

    private static Lista<String> colores() {
        Lista<String> colores = new Lista<String>();

        colores.agrega("#EF9A9A");
        colores.agrega("#F06292");
        colores.agrega("#AB47BC");
        colores.agrega("#7E57C2");
        colores.agrega("#7986CB");
        colores.agrega("#0097A7");
        colores.agrega("#26A69A");
        colores.agrega("#81C784");
        colores.agrega("#4CAF50");
        colores.agrega("#DCE775");

        return colores;
    }
}
