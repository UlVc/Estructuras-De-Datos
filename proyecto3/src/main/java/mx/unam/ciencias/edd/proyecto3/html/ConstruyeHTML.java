package mx.unam.ciencias.edd.proyecto3.html;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.pieChart;

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
        this.directorio = directorio;
        this.diccionario = diccionario;
        this.arvolAVl = arvolAVl;
        this.arvolRN = arvolRN;
        this.rebanadas = porcentajes.length;
        this.porcentajes = porcentajes;
        this.listaElementos = listaElementos;
    }

    public ConstruyeHTML(Lista<String> archivos, String directorio) {
        this.archivos = archivos;
        this.directorio = directorio;
        this.titulo = "index";
    }

    public static void generaHTML() {
        File newFolder = new File(directorio); // Falta agregar el caso cuando se pasa un nombre por la consola sin "/".

        newFolder.mkdirs();

        try {
            FileWriter fw = new FileWriter(directorio + titulo + ".html");
            fw.write(generaCodigoHTML());
            fw.close();
        } catch (Exception e) {
            System.out.println("No se encontró ruta especificada.");
        }
    }

    public static void generadorIndex(Lista<Integer> contadorElementos, String grafica) {
        String index = doctype + "<html>" + generaTitulo();

        for (String s : archivos) {
            int i = contadorElementos.getPrimero();
            index += "    <a href='" + s + ".html'>" + s + "</a> Número de palabras en el archivo: " + i + " <br> \n";
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

        String svg = "    <svg class='chart' width='420' height='" + numeroDeBarras * 20 + "' xmlns='http://www.w3.org/2000/svg' aria-labelledby='title desc' role='img'> <title id='title'>A bar chart showing information</title>\n";

        for (int i = 0; i < rebanadas; i++) {
            String color = colores.get(i);
            svg += barra(porcentajes[i], color, listaElementos.get(i), y);
            y += 20;
        }

        return svg + "    </svg>\n";
    }

    private static String barra(double porcentaje, String color, String elemento, int y) {
        double longitudBarra = porcentaje;
        String s = "      <g class='bar'>\n        <rect width='" + longitudBarra + "' height='19' y='" + y + "' fill='" + color + "'></rect>\n        <text x='" + (longitudBarra + 10) + "' y='" + (y + 13) + "' dy=''.35em'>" + elemento + "</text>\n      </g> \n";
        
        numeroDeBarras--;

        return s;
    }

    private static String generaCodigoHTML() {
        return doctype + "<html>" + generaTitulo() + generaBody() + "</html>";
    }

    private static String generaPieChart() {
        pieChart pie = new pieChart(rebanadas);
        String s = "    <svg style='width: 400px; height: 300px;' xmlns='http://www.w3.org/2000/svg'>\n";
        Lista<String> colores = colores();

        for (int i = 0; i < rebanadas; i++) {
            String color = colores.get(i);
            s += pie.generaSVG(porcentajes[i], i + 1, color, listaElementos.get(i));
        }

        return s+= "    </svg>\n";
    }

    private static String generaBody() {
        return "<body>\n    " + body + "<br>\n" + generaBarraSVG() + generaPieChart() + arvolRN + "\n" + arvolAVl + "\n</body>\n";
    }

    private static String generaTitulo() {
        return "\n<head>\n    " + "<title> " + titulo + " </title>" + "\n</head>\n";
    }

    private static Lista<String> colores() {
        Lista<String> colores = new Lista<String>();

        colores.agrega("#BB3D49");
        colores.agrega("#61C0BF");
        colores.agrega("#F04B27");
        colores.agrega("#EEC014");
        colores.agrega("#C014EE");
        colores.agrega("#69D78B");
        colores.agrega("#68F6C6");
        colores.agrega("#745D97");
        colores.agrega("#F07F47");
        colores.agrega("#F56767");

        return colores;
    }
}
