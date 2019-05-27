package mx.unam.ciencias.edd.proyecto3;

public class pieChart {

    private static int rebanadas;

    public pieChart(int rebanadas) {
        this.rebanadas = 360 / rebanadas;
    }

    public String generaSVG(double porcentaje, int numeroDeRebanada, String color) {
      System.out.println(porcentaje);
      porcentaje = (porcentaje / 100) * 360;

      double[] valores = calcularPorcion(porcentaje);
      System.out.println(porcentaje);
      for (Double d : valores)
          System.out.println(d);

      String s = "      <path fill='" + color + "' d='M115, 115 L115,0 A115, 115 1 0, 1 " + valores[0] + ", " + valores[1] + " z' transform='rotate(" + numeroDeRebanada * rebanadas + ", 115, 115)'></path>\n";

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
}
