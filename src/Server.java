import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;

import miscelanea.Utilidades;
import servidores.SocketTLS;
import servidores.SocketUDP;

/**
 * Aplicación que funciona como servidor TLS y UDP.
 * Responde a las peticiones de los clientes buscando la cadena solicitada en su fichero de texto
 * y devolviendo las líneas en las que se encuentre.
 * @author Guillermo Barreiro
 *
 */
public class Server {

	/**
	 * Método inicial del programa servidor.
	 * Inicializa un servidor capaz de responder a peticiones vía TLS y UDP.
	 * <br><br>
	 * <b>Invocación:</b> <i>java Servidor puerto keystore password_keystore fichero_de_mensajes</i>
	 * @param args Argumentos de entrada del programa
	 */
	public static void main(String[] args) {
		// Obtiene todos los parámetros
		if(args.length!=4) {
			// Número de argumentos incorrecto: abortamos el programa
			Utilidades.errorArgs("server");
		}
		
		int puerto = 0;
		try {
			puerto = Integer.parseInt(args[0]);
		}catch(NumberFormatException nfe) {
			// El argumento "puerto" no es un número
			Utilidades.errorArgs("server");
		}
		
		// Intenta leer el fichero de texto y cargar su contenido en una lista, línea a línea
		LinkedList<String> lineasFichero = new LinkedList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(args[3]));
			String linea = null;
			while((linea=br.readLine())!=null) {
				lineasFichero.add(linea);
			}
			br.close();
		} catch (IOException e) {
			// Si el fichero no existe o se produce algún problema de I/O
			Utilidades.errorFicheroInexistente();
		}
		
		// Configura el keystore
		String keystore = null, password = null;
		keystore = args[1];
		password = args[2];
		
	    System.setProperty("javax.net.ssl.keyStore", keystore);
		System.setProperty("javax.net.ssl.keyStoreType", "JCEKS");
		System.setProperty("javax.net.ssl.keyStorePassword", password);
		
		// Crea el socket UDP
		try {
			SocketUDP udpsocket = new SocketUDP(new ArrayList<String>(lineasFichero), puerto);
			udpsocket.start(); // el socket UDP se ejecuta en un hilo paralelo
		} catch (SocketException e) {
			// Error al crear el socket
			Utilidades.errorIO();
		}
		
		// Crea el socket principal TLS
		try {
			SocketTLS TLSsocket = new SocketTLS(new ArrayList<String>(lineasFichero), puerto);
			TLSsocket.start(); // el socket principal TLS se ejecuta en un hilo paralelo
		} catch (IOException e) {
			// Error al crear el socket
			e.printStackTrace();
			Utilidades.errorIO();
		}

	}
	
}
