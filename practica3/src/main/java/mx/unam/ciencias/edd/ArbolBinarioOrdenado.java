package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {

            pila = new Pila<>();
            pila.mete(raiz);
            Vertice vertice = raiz;
            while (vertice.hayIzquierdo()) {
                pila.mete(vertice.izquierdo);
                vertice = vertice.izquierdo;
            }

        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {

            if(pila==null) pila.saca();
            Vertice v = pila.saca(), vi;

            if (v.hayDerecho()) {
                vi = v.derecho;
                while(vi != null){
                    pila.mete(vi);
                    vi = vi.izquierdo;
                }
            }

            return v.elemento;
        }

    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if(esVacia())
            raiz = ultimoAgregado = new Vertice(elemento);
        else
            agrega(raiz, elemento);
        elementos++;
    }

    private void agrega(Vertice vertice, T elemento) {
        if(elemento.compareTo(vertice.get()) < 0)
            if(!vertice.hayIzquierdo()) {
                Vertice verticeNuevo = nuevoVertice(elemento);
                verticeNuevo.padre = vertice;
                vertice.izquierdo = ultimoAgregado = verticeNuevo;
            }else
                agrega(vertice.izquierdo, elemento);
        else
            if(!vertice.hayDerecho()) {
                Vertice verticeNuevo = nuevoVertice(elemento);
                verticeNuevo.padre = vertice;
                vertice.derecho = ultimoAgregado = verticeNuevo;
            }else
                agrega(vertice.derecho, elemento);
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if(busca(elemento) == null) return;
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice maxizq = MaximoEnSubArbol(vertice.izquierdo);
        Vertice izq = vertice.izquierdo;
        Vertice der = vertice.derecho;
        der = maxizq;
        return maxizq;
    }

    /* Método recursivo para encontrar el máximo vértice en un subárbol. */
    private Vertice MaximoEnSubArbol(Vertice v) {
        if(v.derecho==null) return v;
        return MaximoEnSubArbol(v.derecho);
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        Vertice u = vertice.derecho;
        Vertice p = vertice.padre;
        if(p != null) {
            if(p.izquierdo==vertice)
                p.izquierdo=u;
        }else if(u != null)
            u.padre=p;
        else
            raiz = u;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz,elemento);
    }

    /* Método auxiliar recursivo para buscar un vértice. */
    private Vertice busca(Vertice vertice, T elemento) {
        if (vertice == null)
            return null;
        if (elemento.equals(vertice.elemento))
            return vertice;
        return elemento.compareTo(vertice.elemento) <= 0 ? busca(vertice.izquierdo,elemento) : busca(vertice.derecho,elemento);
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        
        if (vertice == null || !vertice.hayIzquierdo())
            return;
        
        Vertice q = vertice(vertice);
        Vertice p = q.izquierdo;
        p.padre = q.padre;

        if (q != raiz) {
            if (q.hayIzquierdo())
                p.padre.izquierdo = p;
            else
                p.padre.derecho = p;
        }
        q.izquierdo = p.derecho;
        if (p.hayDerecho())
            p.derecho.padre = q;
        q.padre = p;
        p.derecho = q;

    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        
        if (vertice == null || !vertice.hayIzquierdo())
            return;
        
        Vertice p = vertice(vertice);
        Vertice q = p.izquierdo;
        q.padre = p.padre;

        if (p != raiz) {
            if (p.hayDerecho())
                q.padre.derecho = q;
            else
                q.padre.izquierdo = q;
        }

        p.derecho = q.izquierdo;
        if (q.hayIzquierdo())
            q.izquierdo.padre = p;
        p.padre = q;
        q.izquierdo = p;

    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPreOrder(raiz,accion);
    }

    private void dfsPreOrder(Vertice v, AccionVerticeArbolBinario<T> accion) {
        if(v==null) return;
        accion.actua(v);
        dfsPreOrder(v.izquierdo,accion);
        dfsPreOrder(v.derecho,accion);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsInOrder(raiz,accion);
    }

    private void dfsInOrder(Vertice v, AccionVerticeArbolBinario<T> accion) {
        if(v==null) return;
        dfsInOrder(v.izquierdo,accion);
        accion.actua(v);
        dfsInOrder(v.derecho,accion);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(raiz,accion);
    }

    private void dfsPostOrder(Vertice v, AccionVerticeArbolBinario<T> accion) {
        if(v==null) return;
        dfsPostOrder(v.izquierdo,accion);
        dfsPostOrder(v.derecho,accion);
        accion.actua(v);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
