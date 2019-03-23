package mx.unam.ciencias.edd.proyecto1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import mx.unam.ciencias.edd.Lista;

/* Clase para el manejo de banderas introducidas en la entrada estándar. */
public class Banderas {

	static final String B_REVERSA = "-r";
    static final String B_ARCHIVO = "-o";
    private boolean esReversab = false;

    /**
     * Método que crea un archivo usando la ruta introducida en la entrada estándar,
	 * y hace una copia del archivo que se introdujo por la entrada estándar al nuevo archivo creado.
     */
    protected void banderaArchivo(String path, String entrada, BufferedReader bfr, Lista<String> ls) {
        try{
            File file = new File(path);

            if(!file.exists()) 
            	file.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            Comparador cad;
            int x = 0;

            for(int i=0;i<ls.getElementos();i++) {
            	bw.write(ls.get(i));
            	bw.newLine();
            }

            bw.close();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /* Método que checa si se introdujo la bandera '-r'. */
    protected boolean esReversa(String[] args) {
    	for(int i=0;i<args.length;i++)
        	if(args[i].equals(B_REVERSA)) {
        		esReversab = true;
        		return true;
        	}
        return false;
    }

    /* Método que checa si se introdujo la bandera '-o'. */
    protected boolean esArchivo(String[] args) {
    	for(int i=0;i<args.length;i++)
        	if(args[i].equals(B_ARCHIVO)) 
        		return true;
        return false;
    }

    /* Método que nos regresa la ruta que se introdujo en la entrada estándar. */
    protected String path(String[] args) {
    	String path = "";
    	for(int i=0;i<args.length;i++) {
            if(args[i].equals(B_ARCHIVO))
            	path = args[i+1];
        }
        return path;
    }

}