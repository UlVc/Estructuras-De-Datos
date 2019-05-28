package mx.unam.ciencias.edd.proyecto3;

public class pieChart {

    private static int rebanadas;
    private static double angulo = 0;
    private static int y = 0;

    public pieChart(int rebanadas) {
        this.rebanadas = 360 / rebanadas;
    }

    public String generaSVG(double porcentaje, int numeroDeRebanada, String color, String texto) {
      
      double porcentajeAngulo = (porcentaje / 100) * 360;

      double[] valores = calcularPorcion(porcentajeAngulo);

      String s = "      <path fill='" + color + "' d='M115, 115 L115,0 A115, 115 1 0, 1 " + valores[0] + ", " + valores[1] + " z' transform='rotate(" + angulo + ", 115, 115)'></path>\n";
      s += generaTexto(texto, color, numeroDeRebanada, porcentaje);

      angulo += porcentajeAngulo;

      return s;
    }

    private double[] calcularPorcion(double porcentaje) {

        double z = Math.pow(115,2) + Math.pow(115,2) - (2 * 115 * 115 * Math.cos(Math.toRadians(porcentaje)));
        z = Math.sqrt(z);

        double x = 115 * Math.sin(Math.toRadians(porcentaje));

        double y = Math.pow(z, 2) - Math.pow(x, 2);
        y = Math.sqrt(y);

        x += 115;

        return new double[]{x, y};
    }

    private String generaTexto(String texto, String color, int numeroDeRebanada, double porcentaje) {
        int x = 260;

        if (numeroDeRebanada > 5)
            x = 320;

        if (numeroDeRebanada == 1 || numeroDeRebanada == 6)
            y = 0;
        else {
            y += 60;
        }

        String s = "      <rect x='" + (x - 10) + "' y='" + y + "' width='50' height='50' fill='" + color + "'/>\n";

        s += "      <text x='" + (x - 2) + "' y='" + (y + 5) + "' dy='1.35em'> " + texto + "<tspan x='" + (x - 3) + "' dy='1.2em'>" + String.format("%.2f", porcentaje) + "%</tspan> </text>\n";

        return s;
    }
}
