package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.Lista;

import java.io.BufferedReader;
import java.io.FileReader;

public class Proyecto3 {

	public static void main(String[] args) throws Exception {

		Lista<String> l1 = new Lista<String>();

		String cadena = "";				

		try {
			BufferedReader br = new BufferedReader(new FileReader("/home/ul/Desktop/proyecto3/prueba.txt"));
			while((cadena = br.readLine()) != null)
   				l1.agrega(cadena.trim());
   		}catch(Exception e) {
   			System.out.println("Introduzca de manera correcta un archivo de texto.");
   			System.exit(-1);
   		}

		for(int i = 0; i < l1.getLongitud(); i++)
			cadena += ";" + l1.get(i).toLowerCase().replace(".","").trim().replace(" ",";").replace("á","a").replace("é","e").replace("í","i").replace("ó","o").replace("ú","u");

		String[] arrayElementos = cadena.split(";");
		l1.limpia();

		for(int i = 1; i<arrayElementos.length; i++)
			l1.agrega(arrayElementos[i]);

		l1 = l1.mergeSort(eliminarRepetidos(contarPalabras(l1)));

		for(int i = 0; i < l1.getLongitud(); i++)
			if(Integer.parseInt(l1.get(i).substring(0,1)) > 1)
				System.out.println(l1.get(i).substring(0,1) + " veces: " + l1.get(i).replace(l1.get(i).substring(0,1),""));
			else
				System.out.println(l1.get(i).substring(0,1) + " vez: " + l1.get(i).replace(l1.get(i).substring(0,1),""));

	}

	private static Lista<String> contarPalabras(Lista<String> l1) {

		Lista<String> l2 = new Lista<String>();
		l2 = l1.copia();

		for(int i = 0; i < l1.getLongitud(); i++) {
			int n = 0;
			String nuevo = "";
			for(int x = 0; x < l2.getLongitud(); x++) {
				if(l1.get(i).equals(l2.get(x))) { n++; }
			}
			nuevo = n + l1.get(i);
			l1.elimina(l1.get(i));
			l1.inserta(i,nuevo);
		}

		return l1;

		/*Lista<String> l1 = new Lista<String>();

		for(int i = 0; i < l.getLongitud(); i++) {
			if(!l1.contiene(l.get(i)))
				l1.agrega(l.get(i));
			else {
				String nuevaCad = l1.get(l1.indiceDe(l.get(i)));
				int x = Integer.parseInt(nuevaCad.substring(0,1)) + 1;
				l1.elimina(l.get(i));
				l1.agrega(x + nuevaCad.replace(nuevaCad.substring(0,1),""));
			}
		}

		return l1;*/

	}

	private static Lista<String> eliminarRepetidos(Lista<String> l) {

		Lista<String> l1 = new Lista<String>();

		for(int i = 0; i < l.getLongitud(); i++)
			if(!l1.contiene(l.get(i)))
				l1.agrega(l.get(i));

		return l1;

	}

}