package mx.unam.ciencias.edd.proyecto2;

/**
 * <p>Enumeracion de diferentes tipos de estructuras de datos.</p>
 * <p><strong>Ademas Canek dice que cuando se usan enumeradores hace que tú y tu codigo se vean cool.</strong></p>
 */
public enum EstructurasDeDatos {
    LISTA("Lista"), PILA("Pila"), COLA("Cola"), ACOMPLETO("ArbolBinarioCompleto"), AORDENADO("ArbolBinarioOrdenado"), 
    AROJINEGRO("ArbolRojinegro"), AVL("ArbolAVL"), GRAFICA("Grafica"), MONTICULOMINIMO("MonticuloMinimo"), VACIO("");

    private String ds;

    EstructurasDeDatos(String ds) {
        this.ds = ds;
    }

    String getString() {
        return this.ds;
    }

    /**
     * Metodo que dado un String, busca si este esta en el enumerdor y devuelve un enumerador con una estructura de datos.
     */
    public static EstructurasDeDatos getEnum(String s) {
        for (EstructurasDeDatos e : EstructurasDeDatos.values())
            if (e.getString().equals(s))
                return e;
        return VACIO;
    }
}