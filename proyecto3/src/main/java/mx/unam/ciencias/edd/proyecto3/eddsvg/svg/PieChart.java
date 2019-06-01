package mx.unam.ciencias.edd.proyecto3.eddsvg.svg;

public class PieChart {

    private static int rebanadas;
    private static double angulo = 0;
    private static int y = 0;

    public PieChart(int rebanadas) {
        this.rebanadas = 360 / rebanadas;
    }

    public String generaSVG(double porcentaje, int numeroDeRebanada, String color, String texto) {
      double porcentajeAngulo = (porcentaje / 100) * 360;
      double[] valores = calcularPorcion(porcentajeAngulo);

      String s = "      <path fill='" + color + "' d='M115, 115 L115,0 A115, 115 1 0, 1 " + valores[0] + ", " + valores[1] + " z' transform='rotate(" + angulo + ", 115, 115)'></path>\n";

      angulo += porcentajeAngulo;

      return s;
    }

    private double[] calcularPorcion(double porcentajeAngulo) {
        double z = Math.pow(115,2) + Math.pow(115,2) - (2 * 115 * 115 * Math.cos(Math.toRadians(porcentajeAngulo)));
        z = Math.sqrt(z);

        double x = 115 * Math.sin(Math.toRadians(porcentajeAngulo));

        double y = Math.pow(z, 2) - Math.pow(x, 2);
        y = Math.sqrt(y);

        x += 115;

        return new double[]{x, y};
    }

    public String generaTexto(String texto, String color, int numeroDeRebanada, double porcentaje) {
        int x = 10;
        y += 30;

        String s = "      <rect x='" + (x - 10) + "' y='" + y + "' width='20' height='20' stroke='#000000' fill='" + color + "'/>\n";
        s += "      <text x='" + (x + 13) + "' y='" + (y - 2) + "' font-size='12' font-weight='bold' dy='1.35em'>" + texto + " - " + String.format("%.2f", porcentaje) + "%</text>\n";

        return s;
    }
}
