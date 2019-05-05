package mx.unam.ciencias.edd.proyecto3.html;

import java.io.FileWriter;

public class ConstruyeHTML {

	private static final String doctype = "<!DOCTYPE html>\n";
	private static String title = "";
	private static String body = "";
	private static String path = "";

	public ConstruyeHTML(String body, String title, String path) {
		this.body = body;
		this.title = title;
		this.path = path;
	}

	public static void generaHTML() {
		try {    
            FileWriter fw = new FileWriter(path);
            fw.write(generaCodigoHTML());    
            fw.close();    
    	}catch(Exception e) { 
    		System.out.println("No se encontro ruta especificada."); 
    	}    
	}

	private static String generaCodigoHTML() {
		return doctype + "<html>" + generaTitle() + generaBody() + "</html>";
	}

	private static String generaBody() {
		return "    <body>\n        " + body + "\n    </body>\n";
	}

	private static String generaTitle() {
		return "\n    <head>\n    " + "    <title> " + title + " </title>" + "\n    </head>\n";
	}

}