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
    private static int[] porcentajes;

    public ConstruyeHTML(String body, String titulo, String directorio, Diccionario<String, Integer> diccionario, String arvolAVl, String arvolRN, int rebanadas, int[] porcentajes) {
        this.body = body;
        this.titulo = titulo;
        this.directorio = directorio;
        this.diccionario = diccionario;
        this.arvolAVl = arvolAVl;
        this.arvolRN = arvolRN;
        this.rebanadas = rebanadas;
        this.porcentajes = porcentajes;
    }

    public ConstruyeHTML(Lista<String> archivos, String directorio) {
        this.archivos = archivos;
        this.directorio = directorio;
        this.titulo = "index";
    }

    public static void generaHTML() {
        File newFolder = new File(directorio); // Falta agregar el caso cuando se pasa un nombre por la consola sin "/".

        newFolder.mkdirs();

        String xd = generaPieChart();

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
        Iterator<String> iteradorLLave = diccionario.iteradorLlaves();
        int i = 0;

        String svg = "    <svg class='chart' width='420' height='" + diccionario.getElementos() * 20 + "' xmlns='http://www.w3.org/2000/svg' aria-labelledby='title desc' role='img'> <title id='title'>A bar chart showing information</title>\n";

        while (iteradorLLave.hasNext()) {
            String llave = iteradorLLave.next();
            svg += barra(llave,i);
            i += 20;
        }

        return svg + "    </svg>\n";
    }

    private static String barra(String llave, int y) {
        return String.format("      <g class='bar'>\n        <rect width='%1$s' height='19' y='%2$s'></rect>\n        <text x='%5$s' y='%3$s' dy=''.35em'> %4$s </text>\n      </g> \n", diccionario.get(llave) * 10, y, y + 13, llave, (diccionario.get(llave) * 10) + 10);
    }

    private static String generaCodigoHTML() {
        return doctype + "<html>" + generaTitulo() + generaBody() + "</html>";
    }

    private static String generaPieChart() {
        pieChart pie = new pieChart(rebanadas);
        String s = "    <svg style='width: 230px; height: 230px;' xmlns='http://www.w3.org/2000/svg'>\n";
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

        for (int i = 0; i < rebanadas; i++) {
            String color = colores.get(i);
            s += pie.generaSVG(porcentajes[i], i + 1, color);
        }

        return s+= "    </svg>\n";
    }

    private static String generaBody() {
        return "<body>\n    " + body + "<br>\n" + generaBarraSVG() + generaPieChart() + arvolRN + "\n" + arvolAVl + "\n</body>\n";
    }

    private static String generaTitulo() {
        return "\n<head>\n    " + "<title> " + titulo + " </title>" + "\n</head>\n";
    }
}
