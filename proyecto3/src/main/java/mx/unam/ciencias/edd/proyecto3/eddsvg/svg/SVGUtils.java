package mx.unam.ciencias.edd.proyecto3.eddsvg.svg;

/* Clase para crear código SVG. */
public class SVGUtils {

    public String texto(String texto, double x, double y, String extra) {
        return String.format("      <text x='%1$s' y='%2$s' font-size='12' %3$s>%4$s</text>\n", x, y, extra, texto);
    }
    public String numero(int numero, double x, double y, String extra) {
        return String.format("      <text x='%1$s' y='%2$s' font-size='12' %3$s>%4$s</text>\n", x, y, extra, numero);
    }

    public String circulo(double radio, double x, double y, String color) {
        String color_s = "";

        if (!color.equals(""))
            color_s = "fill='" + color + "'";

        return String.format("      <circle cx='%1$s' cy='%2$s' r='%3$s' %4$s stroke='black' stroke-width='1'/>\n", x, y, radio, color_s);
    }

    public String circuloConTexto(String texto, double x, double y, int radio, String color, String colorLetra) {
        return circulo(radio, x, y, color) + texto(texto, x, y + 5, "text-anchor='middle' fill='"+ colorLetra +"'");
    }
    public String circuloConNumero(int n, double x, double y, int radio, String color, String colorLetra) {
        return circulo(radio, x, y, color) + numero(n, x, y + 5, "text-anchor='middle' fill='" + colorLetra + "'");
    }

    public String linea(double x1, double y1, double x2, double y2) {
        return String.format("      <line x1='%1$s' y1='%2$s' x2='%3$s' y2='%4$s' stroke='black' stroke-width='2'/>\n", x1, y1, x2, y2);
    }

    private int longitudNumero(int n) {
        int i = 1;

        while (n >= 10) {
            n /= 10;
            i++;
        }

        return i;
    }
    
}