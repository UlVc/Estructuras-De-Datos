package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        int a = 0;
        int b = arreglo.length - 1;

        quickSort(arreglo, a, b, comparador);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for (int i = 0; i < arreglo.length; i++) {
            int m = i;

            for (int j = i + 1; j < arreglo.length; j++)
                if (comparador.compare(arreglo[j], arreglo[m]) < 0)
                    m = j;

            intercambia(arreglo, i, m);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        int a = 0;
        int b = arreglo.length - 1;

        return busquedaBinaria(arreglo, elemento, a, b, comparador);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }

    /**
     * Intercambia dos elementos de un arreglo.
     * @param  arreglo arreglo a modificar.
     * @param  i       índice del primer elemento.
     * @param  m       índice del segundo elemento.
     */
    private static <T> void intercambia(T[] arreglo, int i, int m) {
        T aux = arreglo[i];

        arreglo[i] = arreglo[m];
        arreglo[m] = aux; 
    }

    /**
     * Método recursivo para busquedaBinaria.
     * @param  arreglo    un arreglo en dónde buscar.
     * @param  elemento   elemento a buscar.
     * @param  a          índice del lado izquierdo del arreglo.
     * @param  b          índidce del lado derecho del arreglo.
     * @param  comparador comparador para comparar el arreglo.
     * @return índice donde se encuentra el elemento.
     */
    
    private static <T> int
    busquedaBinaria(T[] arreglo, T elemento, int a, int b, Comparator<T> comparador) {
        if (b < a)
            return -1;

        int m = a + ((int) Math.floor((b-a) / 2));

        if (arreglo[m].equals(elemento))
            return m;

        if (comparador.compare(elemento, arreglo[m]) < 0) {
            b = m - 1;

            return busquedaBinaria(arreglo, elemento, a, b, comparador);
        } else {
            a = m + 1;

            return busquedaBinaria(arreglo, elemento, a, b, comparador);
        }
    }

    /**
     * Méotodo recursivo para quickSort.
     * @param  arreglo    arreglo a ordenar.
     * @param  a          índice del lado izquierdo del arreglo.
     * @param  b          índidce del lado derecho del arreglo.
     * @param  comparador comparador para comparar el arreglo.
     * @return            un arreglo ordenado.
     */
    private static <T> void quickSort(T[] arreglo, int a, int b, Comparator<T> comparador) {
        if (b <= a)
            return;

        int i = a + 1;
        int j = b;

        while (i < j) {
            if (comparador.compare(arreglo[i], arreglo[a]) > 0 && comparador.compare(arreglo[j], arreglo[a]) <= 0) {
                intercambia(arreglo, i, j);
                i += 1;
                j -= 1;
            } else if (comparador.compare(arreglo[i], arreglo[a]) <= 0)
                i += 1;
            else
                j -= 1;
        }

        if (comparador.compare(arreglo[i], arreglo[a]) > 0)
            i -= 1;

        intercambia(arreglo, a, i);
        quickSort(arreglo, a, i - 1, comparador);
        quickSort(arreglo, i + 1, b, comparador);
    }
}
