package mx.unam.ciencias.edd.proyecto2;

/**
 * Enumeracion de diferentes tipos de estructuras de datos, nos serviran para simplificar código.
 */
public enum EstructurasDeDatos {

    LISTA("Lista"), PILA("Pila"), COLA("Cola"), ACOMPLETO("ArbolBinarioCompleto"), AORDENADO("ArbolBinarioOrdenado"), 
    AROJINEGRO("ArbolRojinegro"), AVL("ArbolAVL"), GRAFICA("Grafica"), MONTICULOMINIMO("MonticuloMinimo"), VACIO("");

    private String cadena;

    EstructurasDeDatos(String cadena) {
        this.cadena = cadena;
    }

    String getString() {
        return this.cadena;
    }

    /**
     * Metodo que dado un String, busca si este esta en el enumerdor y devuelve un enumerador con una estructura de datos.
     */
    public static EstructurasDeDatos getEnum(String s) {
        for(EstructurasDeDatos e : EstructurasDeDatos.values())
            if(e.getString().equals(s))
                return e;
        return VACIO;
    }
}