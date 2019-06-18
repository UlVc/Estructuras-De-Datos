package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (siguiente == null) { throw new NoSuchElementException(); }

            anterior = siguiente;
            siguiente = siguiente.siguiente;

            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (anterior == null) { throw new NoSuchElementException(); }

            siguiente = anterior;
            anterior = anterior.anterior;

            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            siguiente = null;
            anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null) { throw new IllegalArgumentException(); }

        Nodo n = new Nodo(elemento);

        longitud += 1;

        if (rabo == null)
            cabeza = rabo = n;
        else {
            rabo.siguiente = n;
            n.anterior = rabo;
            rabo = n;
        }
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null) { throw new IllegalArgumentException(); }

        Nodo n = new Nodo(elemento);

        longitud += 1;

        if (rabo == null)
            cabeza = rabo = n;
        else {
            cabeza.anterior = n;
            n.siguiente = cabeza;
            cabeza = n;
        }
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null) { throw new IllegalArgumentException(); }

        if (i < 1)
            agregaInicio(elemento);
        else if (i > getElementos() - 1)
            agregaFinal(elemento);
        else {
            longitud += 1;

            Nodo n = new Nodo(elemento);
            Nodo s = obtenNodo(i);
            Nodo anterior = s.anterior;

            n.anterior = anterior;
            anterior.siguiente = n;
            n.siguiente = s;
            s.anterior = n;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo n = obtenNodo(elemento);

        if (n == null)
            return;

        longitud -= 1;

        if (cabeza == rabo)
            cabeza = rabo = null;
        else if (cabeza == n) {
            Nodo siguiente = n.siguiente;

            siguiente.anterior = null;
            cabeza = siguiente;
        } else if (rabo == n) {
            Nodo anterior = n.anterior;

            anterior.siguiente = null;
            rabo = anterior;
        } else {
            Nodo siguiente = n.siguiente;
            Nodo anterior = n.anterior;

            anterior.siguiente = siguiente;
            siguiente.anterior = anterior;
        }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (esVacia()) { throw new NoSuchElementException(); }

        T elemento = cabeza.elemento;
        
        elimina(cabeza.elemento);

        return elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (esVacia()) { throw new NoSuchElementException(); }

        T elemento = rabo.elemento;

        rabo = rabo.anterior;

        if (rabo == null)
            rabo = cabeza = null;
        else
            rabo.siguiente = null;

        longitud -= 1;

        return elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return obtenNodo(elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> reversa = new Lista<T>();
        Nodo nodo = cabeza;

        while (nodo != null) {
            reversa.agregaInicio(nodo.elemento);
            nodo = nodo.siguiente;
        }

        return reversa;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> copia = new Lista<T>();
        Nodo nodo = cabeza;

        while (nodo != null) {
            copia.agregaFinal(nodo.elemento);
            nodo = nodo.siguiente;
        }

        return copia;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza = rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (esVacia()) { throw new NoSuchElementException(); }

        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (esVacia()) { throw new NoSuchElementException(); }

        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i >= getElementos()) { throw new ExcepcionIndiceInvalido(); }

        return obtenNodo(i).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        Nodo n = cabeza;

        for (int contador = 0; n != null; contador++) {
            if (n.elemento.equals(elemento))
                return contador;

            n = n.siguiente;
        }

        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (esVacia())
            return "[]";

        String cadena = "[" + cabeza.elemento;
        Nodo nodo = cabeza.siguiente;

        while (nodo != null) {
            if (nodo != null)
                cadena += ", " + nodo.elemento;

            nodo = nodo.siguiente;
        }

        return cadena + "]";
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if (getLongitud() != lista.getLongitud())
            return false;
        
        Nodo nodo = cabeza;
        
        for(int i = 0; nodo != null; i++) {
            if(!nodo.elemento.equals(lista.get(i)))
                return false;

            nodo = nodo.siguiente;
        }

        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        Lista <T> lista = copia();

        if (getLongitud() < 2)
            return lista;

        Lista <T> l1 = new Lista<T>();
        Lista <T> l2 = new Lista<T>();

        int mitad = lista.getLongitud() / 2;

        for (int i = 0; i < mitad; i++)
            l1.agrega(lista.get(i));

        for (int i = mitad; i < lista.getLongitud(); i++)
            l2.agrega(lista.get(i));

        l1 = l1.mergeSort(comparador);
        l2 = l2.mergeSort(comparador);

        return mezcla(l1, l2, comparador);
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo aux = cabeza;

        while (aux != null) {
            if (comparador.compare(aux.elemento, elemento) == 0)
                return true;

            aux = aux.siguiente;
        }

        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <tt>true</tt> si el elemento está contenido en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }

    /**
     * Obtiene el primer nodo de la lista que contenga el elemento recibido como parámetro.
     * @param  i elemento a buscar.
     * @return primer nodo de la lista que contenga el elemento recibido como parámetro.
     */
    private Nodo obtenNodo(T elemento) {
        Nodo nodo = cabeza;

        while (nodo != null) {
            if (nodo.elemento.equals(elemento))
                return nodo;

            nodo = nodo.siguiente;
        }

        return null;
    }

    /**
     * Obtiene el i-éstimo nodo de la lista.
     * @param  i posición del nodo a buscar en la lista.
     * @return i-ésimo nodo de la lista.
     */
    private Nodo obtenNodo(int i) {
        Nodo nodo = cabeza;

        for (int contador = 0; nodo != null; contador++) {
            if (contador == i)
                return nodo;

            nodo = nodo.siguiente;
        }

        return null;
    }

    /**
     * Méotodo para mezclar dos listas para poder ordenarlas.
     * @param  l1         primera lista a mezclar.
     * @param  l2         segunda lista a mezclar.
     * @param  comparador el comparador que la lista usará para hacer el ordenamiento.
     * @return            lista ordenada.
     */
    private Lista<T> mezcla(Lista<T> l1, Lista<T> l2, Comparator<T> comparador) {
        Lista<T> lista = new Lista<T>();
        Nodo i = l1.cabeza;
        Nodo j = l2.cabeza;

        while (i != null && j != null) {
            if (comparador.compare(i.elemento, j.elemento) <= 0) {
                lista.agrega(i.elemento);
                i = i.siguiente;
            } else {
                lista.agrega(j.elemento);
                j = j.siguiente;
            }
        }

        while (i != null) {
            lista.agrega(i.elemento);
            i = i.siguiente;
        }

        while (j != null) {
            lista.agrega(j.elemento);
            j = j.siguiente;
        }

        return lista;
    }
}
