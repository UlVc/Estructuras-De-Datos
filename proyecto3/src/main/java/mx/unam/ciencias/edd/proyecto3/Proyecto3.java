package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.proyecto3.html.ConstruyeHTML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;

public class Proyecto3 {

	private static Lista<Integer> contadorElementos = new Lista<Integer>();

	public static void main(String[] args) throws Exception {
		Lista<String> archivos = new Lista<String>();
		Lista<String> titulosLista = new Lista<String>();
		Lista<String> l = new Lista<String>();

		String directorio = "";
		String cadena = "";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-o"))
				directorio = args[i + 1];
			else
				archivos.agrega(args[i]);
		}

		archivos.elimina(directorio);

		for (String s : archivos) {
			String[] titulos = s.split("/");
			String tituloTxt = titulos[titulos.length - 1];
			String titulo = tituloTxt.substring(0, tituloTxt.length() - 4);
			titulosLista.agrega(titulo);

			try {
				BufferedReader br = new BufferedReader(new FileReader(s));
				while ((cadena = br.readLine()) != null)
	   				l.agrega(cadena.trim());
	   		} catch (Exception e) {
	   			System.out.println("Introduzca de manera correcta un archivo de texto.");
	   			System.exit(-1);
	   		}

	   		html(directorio, l, titulo);
	   		l.limpia();
	   	}

	   	ConstruyeHTML html = new ConstruyeHTML(titulosLista, directorio, "index");
	   	html.generadorIndex(contadorElementos);
	}

	private static void html(String directorio, Lista<String> l, String titulo) {
		String cadena = "";
		String conteo = "";
			
		for (String st : l)
			cadena += ";" + st.toLowerCase().replace(".", "").trim().replace(" ", ";").replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace(",", "");

		// Se tiene problemas cuando el archivo tiene varias lineas vacias.

		String[] arrayElementos = cadena.split(";");

		Diccionario<String, Integer> diccionario = cuentaPalabras(arrayElementos);

		contadorElementos.agrega(diccionario.getElementos());

		Iterator<String> iteradorLLave = diccionario.iteradorLlaves();

		while (iteradorLLave.hasNext()) {
			String llave = iteradorLLave.next();

			if (diccionario.get(llave) > 1)
				conteo += diccionario.get(llave) + " veces: " + llave + "<br>";
			else
				conteo += diccionario.get(llave) + " vez: " + llave + "<br>";
		}

		ConstruyeHTML html = new ConstruyeHTML(conteo, titulo, directorio, diccionario);
		html.generaHTML();
	}

	private static Diccionario<String, Integer> cuentaPalabras(String[] array) {
		Diccionario<String, Integer> diccionario = new Diccionario<String, Integer>();

		for (int i = 1; i < array.length; i++)
			if (diccionario.contiene(array[i])) {
				int n = diccionario.get(array[i]);
				diccionario.elimina(array[i]);
				diccionario.agrega(array[i], n += 1);
			} else
				diccionario.agrega(array[i], 1);

		return diccionario;
	}

}