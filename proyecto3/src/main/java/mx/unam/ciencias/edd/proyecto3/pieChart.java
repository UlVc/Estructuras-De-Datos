package mx.unam.ciencias.edd.proyecto3;

public class pieChart {

    private double porcentaje;

    public pieChart(double porcentaje) {
      this.porcentaje = (porcentaje / 100) * 360;
    }

    private double[] calcularPorcion() {

        System.out.println(porcentaje);

        double z = Math.pow(115,2) + Math.pow(115,2) - (2 * 115 * 115 * Math.cos(porcentaje));
        System.out.println(z);
        z = Math.sqrt(z);

        double x = 115 * Math.sin(porcentaje);

        double y = z - x;

        x += 115;

        return new double[]{x, y};
    }

    public String generaSVG() {
      double[] valores = calcularPorcion();

      String s = "<svg style='width: <diameter>px; height: <diameter>px;'>\n";
      s += "<path fill='#61C0BF' d='M115, 115 L225,0 A115, 115 1 0, 1" + valores[0] + "," + valores[1] + "z'></path>\n";
      s += "</svg>";

      System.out.println(s);
      return s;
    }

}
