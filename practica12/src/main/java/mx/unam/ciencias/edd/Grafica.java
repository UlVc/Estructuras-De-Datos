package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* El diccionario de vecinos del vértice. */
        public Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.vecinos = new Diccionario<T, Grafica<T>.Vecino>();
            this.color = Color.NINGUNO;
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if (distancia > vertice.distancia)
                return 1;
            else if (distancia < vertice.distancia)
                return -1;
            return 0;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Diccionario<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getElementos();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null || vertices.contiene(elemento)) { throw new IllegalArgumentException(); }

        vertices.agrega(elemento, new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a,b,1);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        if (!contiene(a) || !contiene(b)) { throw new NoSuchElementException(); }
        if (a.equals(b) || sonVecinos(a,b) || peso < 0) { throw new IllegalArgumentException(); }

        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);

        va.vecinos.agrega(b, new Vecino(vb, peso));
        vb.vecinos.agrega(a, new Vecino(va, peso));
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice vA = castVertice(vertice(a));
        Vertice vB = castVertice(vertice(b));

        if (!sonVecinos(a,b) || a.equals(b)) { throw new IllegalArgumentException(); }

        vA.vecinos.elimina(b);
        vB.vecinos.elimina(a);
        aristas--;
    }

    private Vertice castVertice(VerticeGrafica<T> v) {
        return (Vertice)v;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return vertices.contiene(elemento);
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        /*Vertice v = castVertice(vertice(elemento));

        for (T llave : vertices) {
            Vertice vecino = vertices.get(llave);
            vecino.vecinos.elimina(elemento);
            aristas--;
        }

        vertices.elimina(elemento);*/
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        if (!contiene(a) || !contiene(b)) { throw new NoSuchElementException(); }

        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);

        for (Vecino ve : va.vecinos)
            if (ve.vecino.equals(vb)) { return true; }

        return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        if (!contiene(a) || !contiene(b)) { throw new NoSuchElementException(); }
        if (!sonVecinos(a,b)) { throw new IllegalArgumentException(); }

        Vertice x = (Vertice) vertice(a);
        Vertice y = (Vertice) vertice(b);

        for (Vecino v : x.vecinos)
            if (v.vecino.equals(y))
                return v.peso;

        return -1;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        /*if (!contiene(a) || !contiene(b)) { throw new NoSuchElementException(); }
        if (a.equals(b) || !sonVecinos(a,b) || peso <= 0) { throw new IllegalArgumentException(); }

        Vertice x = verticeGrafica(a);
        Vertice y = verticeGrafica(b);

        for (Vecino xv : x.vecinos)
            if(xv.vecino.equals(y))
                xv.peso = peso;

        for (Vecino yv : y.vecinos)
            if (yv.vecino.equals(x))
                yv.peso = peso;
    }

    private Vertice verticeGrafica(T elemento) {
        for (Vertice v : vertices)
            if (v.elemento.equals(elemento))
                return v;
        return null;*/
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        for (Vertice v : vertices)
            if (v.elemento.equals(elemento))
                return v;
        throw new NoSuchElementException();
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || (vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class)) { throw new IllegalArgumentException(); }

        if (vertice.getClass() == Vertice.class) {
            Vertice v = (Vertice)vertice;
            v.color = color;
        }
        if (vertice.getClass() == Vecino.class) {
            Vecino v = (Vecino)vertice;
            v.vecino.color = color;
        }

    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        return false;
        /*for (Vertice v : vertices)
            v.color = Color.ROJO;

        Cola<Vertice> vert = new Cola<Vertice>();
        vert.mete(vertices.getPrimero());
        vertices.getPrimero().color = Color.NEGRO;

        while (!vert.esVacia()) {
            Vertice aux = vert.saca();
            for (Vecino ve : aux.vecinos){
                if (ve.vecino.color == Color.ROJO) {
                    ve.vecino.color = Color.NEGRO;
                    vert.mete(ve.vecino);
                }
            }
        }

        for(Vertice w: vertices)
            if(w.color != Color.NEGRO)
                return false;

        return true;*/
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice v : vertices)
            accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        recorrido(elemento, accion, new Cola<Grafica<T>.Vertice>());
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        recorrido(elemento, accion, new Pila<Grafica<T>.Vertice>());
    }

    /**
     * Auxiliar que recorre ya sea de manera bfs o dfs segun se le pase un MeteSaca y ejecuta una accion.
     */
    private void recorrido(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Grafica<T>.Vertice> metesaca) {

        if(!contiene(elemento)) { throw new NoSuchElementException(); }

        Vertice v = (Vertice) vertice(elemento);
        metesaca.mete(v);

        while(!metesaca.esVacia()) {
            Vertice vt = metesaca.saca();
            setColor(vt,Color.ROJO);
            accion.actua(vt);
            for(Vecino ve : vt.vecinos)
                if(ve.vecino.color != Color.ROJO) {
                    setColor(ve, Color.ROJO);
                    metesaca.mete(ve.vecino);
                }
        }

        paraCadaVertice(vertice -> this.setColor(vertice, Color.NINGUNO));
    }

    private void setColor(Vertice v, Color c) {
        v.color = c;
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        Lista<T> lista = new Lista<T>();

        for (Vertice rojo : vertices)
            rojo.color = Color.ROJO;

        String cadena = "{";
        String aristas = "{";

        for (Vertice v : vertices) {
            cadena += v.elemento + ", ";
            for (Vecino ady : v.vecinos) {
                if (ady.getColor() == Color.ROJO)
                    aristas += "(" + v.get() + ", " + ady.get() + "), ";
                v.color = Color.NEGRO;
            }
            lista.agrega(v.elemento);
        }
        for (Vertice nulo : vertices)
            nulo.color = Color.NINGUNO;

        return cadena + "}, " + aristas + "}";
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la gráfica es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        if ((getElementos() != grafica.getElementos()) || (aristas != grafica.aristas))
            return false;
        for (Vertice v : vertices) {
            v.color = Color.ROJO;
            if (!grafica.contiene(v.elemento)) { return false; }
        }
        for (Vertice v : vertices) {
            for (Vecino y : v.vecinos){
                if (y.getColor() == Color.ROJO)
                    if (!grafica.sonVecinos(y.get(), v.elemento)) { return false; }
            }
            v.color = Color.NEGRO;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        return null;
        /*if (!contiene(origen) || !contiene(destino)) { throw new NoSuchElementException(); }

        Lista<VerticeGrafica<T>> lista = new Lista<VerticeGrafica<T>>();
        Cola<Vertice> cola = new Cola<Vertice>();

        Vertice x = verticeGrafica(origen);
        Vertice y = verticeGrafica(destino);

        if (origen.equals(destino)) {
            lista.agrega(x);
            return lista;
        }

        for (Vertice v : vertices)
            v.distancia = -1;
    
        x.distancia = 0;

        cola.mete(x);

        while (!cola.esVacia()) {
            x = cola.saca();
            for (Vecino vecino : x.vecinos)
                if (vecino.vecino.distancia == - 1) {
                    vecino.vecino.distancia = x.distancia + 1;
                    cola.mete(vecino.vecino);
                }
        }

        if (y.distancia == -1)
            return lista;

        lista.agrega(y);

        while (!x.elemento.equals(origen))
            for (Vecino vertice : x.vecinos)
                if (x.distancia == vertice.vecino.distancia + 1) {
                    lista.agrega(vertice.vecino);
                    x = vertice.vecino;
                }

        return lista.reversa();*/
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        return null;
        /*if (!contiene(origen) || !contiene(destino)) { throw new NoSuchElementException(); }

        Lista<VerticeGrafica<T>> lista = new Lista<VerticeGrafica<T>>();
        MonticuloMinimo<Vertice> monticuloMinimo = new MonticuloMinimo<Vertice>(vertices);

        Vertice x = (Vertice) vertice(origen);
        Vertice y = (Vertice) vertice(destino);

        for (Vertice v : vertices)
            v.distancia = Double.MAX_VALUE;

        x.distancia = 0;

        while (!monticuloMinimo.esVacia()) {
            Vertice v = monticuloMinimo.elimina();
            for (Vecino vertice : v.vecinos)
                if (vertice.vecino.distancia > (v.distancia + vertice.peso)) {
                    vertice.vecino.distancia = v.distancia + vertice.peso;
                    monticuloMinimo.reordena(vertice.vecino);
                }    
        }

        if (y.distancia == Double.MAX_VALUE) 
            return lista;

        lista.agrega(y);

        while (!y.elemento.equals(origen))
            for (Vecino vertice : y.vecinos)
                if (y.distancia == vertice.vecino.distancia + vertice.peso) {
                    lista.agrega(vertice.vecino);
                    y = vertice.vecino;
                }

        return lista.reversa();*/
    }
}
