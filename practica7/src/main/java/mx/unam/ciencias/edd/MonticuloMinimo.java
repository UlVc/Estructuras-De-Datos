package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return (indice < arbol.length) && arbol[indice] != null;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (indice >= arbol.length)
                throw new NoSuchElementException();
            return arbol[indice++];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return 0;
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    ///////////////////////////////////////////////////////////////////////////////////
    private int siguiente = elementos + 1;
    ///////////////////////////////////////////////////////////////////////////////////

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100); /* 100 es arbitrario. */
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        T aux[] = nuevoArreglo(n);
        //aux.agrega(iterable);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        T[] aux;
        int i = 0;
        if (siguiente >= arbol.length) {
            aux = this.arbol;
            this.arbol = nuevoArreglo(siguiente * 2);
            for (T e: aux) {
                arbol[i++] = e;
            }
        }
        arbol[siguiente] = elemento;
        elemento.setIndice(siguiente++);
        reordenaArriba(elemento);
    }

    private void reordenaArriba(T elemento) {
        int padre = (elemento.getIndice() - 1);
        if (padre != -1) {
            padre /= 2;
        }
        if (!this.indiceValido(padre) || arbol[padre].compareTo(elemento) < 0) {
            return;
        }
        this.intercambia(arbol[padre], elemento);
        this.reordenaArriba(elemento);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(esVacia())
            throw new IllegalStateException();
        T elemento = arbol[0];
        elimina(elemento);
        return elemento;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if(elemento == null)
            return;
        int indice = elemento.getIndice();
        intercambia(arbol[indice], arbol[--siguiente]);
        
        arbol[siguiente].setIndice(-1);
        arbol[siguiente] = null;

    }

    private void intercambia(T e1, T e2) {
        int i_e1 = e1.getIndice();
        int i_e2 = e2.getIndice();
        arbol[i_e1] = e2;
        arbol[i_e2] = e1;

        arbol[i_e1].setIndice(i_e1);
        arbol[i_e2].setIndice(i_e2);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(T e: arbol) {
            if(e == null)
                return false;
            if(e.equals(elemento))
                return true;
        }
        return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacia() {
        return this.siguiente == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        elementos = 0;
        arbol = null;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if(elemento == null)
            return;
        int i1 = elemento.getIndice();
        recorreParaArriba(i1);
        minHeapify(i1);
    }

    private int getMenor(int a, int b) {
        if(b >= siguiente)
            return a;
        else if(arbol[a].compareTo(arbol[b]) < 0)
            return a;
        else
            return b;
    }

    private void minHeapify(int i) {
        int izq = i * 2 + 1;
        int der = i * 2 + 2;

        if(izq >= siguiente && der >= siguiente)
            return;

        int menor = getMenor(izq, der);
        menor = getMenor(i, menor);

        if(menor != i) {
            T aux = arbol[i];

            arbol[i] = arbol[menor];
            arbol[i].setIndice(i);

            arbol[menor] = aux;
            arbol[menor].setIndice(menor);

            minHeapify(menor);
        }
    }

    private void recorreParaArriba(int i) {
        int padre = (i - 1) / 2;
        int menor = i;

        if(padre >= 0 && arbol[padre].compareTo(arbol[i]) > 0)
            menor = padre;

        if(menor != i) {
            T aux = arbol[i];

            arbol[i] = arbol[padre];
            arbol[i].setIndice(i);

            arbol[padre] = aux;
            arbol[padre].setIndice(padre);

            recorreParaArriba(menor);
        }
    }
    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(!this.indiceValido(i))
            throw new NoSuchElementException();
        return arbol[i];
    }

    private boolean indiceValido(int i) {
        return !(i < 0 || i >= this.arbol.length || arbol[i] == null);
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String representación = "";
        for(int i = 0;i<arbol.length;i++)
            representación += (arbol[i] + ", ");
        return representación;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if(objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if(monticulo.getElementos() != this.getElementos())
            return false;
        for(int i = 0; i < this.getElementos() ; i++)
            if(!arbol[i].equals(monticulo.get(i)))
                return false;
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {

        Lista<Adaptador<T>> nuevaLista = new Lista<Adaptador<T>>();

        for(T aux : coleccion)
            nuevaLista.agrega(new Adaptador<T>(aux));

        Lista<T> lista = new Lista<T>();

        MonticuloMinimo<Adaptador<T>> minimo = new MonticuloMinimo<Adaptador<T>>(nuevaLista);

        while(!minimo.esVacia())
            lista.agrega(minimo.elimina().elemento);

        return lista;
    }
}
