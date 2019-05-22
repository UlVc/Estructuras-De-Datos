package mx.unam.ciencias.edd.proyecto3;

public class pieChart {

    private double porcentaje;

    public pieChart(double porcentaje) {
      this.porcentaje = (porcentaje / 100) * 360;
    }

    public String generaSVG() {
      double[] valores = calcularPorcion();

      String s = "<svg style='width: 230px; height: 230px;'>\n";
      s += "<path fill='#61C0BF' d='M115, 115 L115,0 A115, 115 1 0, 1 " + valores[0] + ", " + valores[1] + " z'></path>\n";
      s += "</svg>";

      System.out.println(s);
      return s;
    }

    private double[] calcularPorcion() {

        double z = Math.pow(115,2) + Math.pow(115,2) - (2 * 115 * 115 * Math.cos(Math.toRadians(porcentaje)));
        z = Math.sqrt(z);

        double x = 115 * Math.sin(Math.toRadians(porcentaje));

        double y = Math.pow(z, 2) - Math.pow(x, 2);
        y = Math.sqrt(y);

        x += 115;

        return new double[]{x, y};
    }
}
