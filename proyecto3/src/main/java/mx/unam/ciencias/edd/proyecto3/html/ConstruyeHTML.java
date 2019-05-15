package mx.unam.ciencias.edd.proyecto3.html;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;

import java.io.FileWriter;
import java.io.File;
import java.util.Iterator;

public class ConstruyeHTML {

	private static Diccionario<String, Integer> diccionario = new Diccionario<String, Integer>();
	private static Lista<String> archivos = new Lista<String>();
	private static final String doctype = "<!DOCTYPE html>\n";
	private static String titulo = "";
	private static String body = "";
	private static String directorio = "";

	public ConstruyeHTML(String body, String titulo, String directorio, Diccionario<String, Integer> diccionario) {
		this.body = body;
		this.titulo = titulo;
		this.directorio = directorio;
		this.diccionario = diccionario;
	}

	public ConstruyeHTML(Lista<String> archivos, String directorio, String titulo) {
		this.archivos = archivos;
		this.directorio = directorio;
		this.titulo = titulo;
	}

	public static void generaHTML() {
		File newFolder = new File(directorio);
        
        newFolder.mkdirs();

		try {    
            FileWriter fw = new FileWriter(directorio + titulo + ".html");
            fw.write(generaCodigoHTML());    
            fw.close();    
    	} catch (Exception e) { 
    		System.out.println("No se encontro ruta especificada."); 
    	}
	}

	public static void generadorIndex(Lista<Integer> contadorElementos) {
		String index = doctype + "<html>" + generaTitulo();

		for (String s : archivos) {
			int i = contadorElementos.getPrimero();
			index += "    <a href='" + directorio + s + ".html'>" + s + "</a> Número de palabras en el archivo: " + i + " <br> \n";
			contadorElementos.elimina(i);
		}

		index += "</html>";

		try {
			FileWriter fw = new FileWriter(directorio + "index.html");
	        fw.write(index);    
	        fw.close();
		} catch (Exception e) { 
	    	System.out.println("No se encontro ruta especificada."); 
	    }
	}

	private static String generaBarraSVG() {
		Iterator<String> iteradorLLave = diccionario.iteradorLlaves();
		int i = 0;

		String svg = "<svg class='chart' width='420' height='" + diccionario.getElementos() * 20 + "' aria-labelledby='title desc' role='img'> <title id='title'>A bar chart showing information</title>\n";
		
		while (iteradorLLave.hasNext()) {
			String llave = iteradorLLave.next();
			svg += barra(llave,i);
			i += 20;
		}
		
		return svg + "</svg>";
	}

	private static String barra(String llave, int y) {
		return String.format("<g class='bar'>\n  <rect width='%1$s' height='19' y='%2$s'></rect>\n <text x='%5$s' y='%3$s' dy=''.35em'> %4$s </text> </g> \n", diccionario.get(llave) * 10, y, y + 13, llave, (diccionario.get(llave) * 10) + 10);
	}

	private static String generaCodigoHTML() {
		return doctype + "<html>" + generaTitulo() + generaBody() + "</html>";
	}

	private static String generaBody() {
		return "    <body>\n        " + body + "<br>\n" + generaBarraSVG() + "\n    </body>\n";
	}

	private static String generaTitulo() {
		return "\n    <head>\n    " + "    <title> " + titulo + " </title>" + "\n    </head>\n";
	}

}