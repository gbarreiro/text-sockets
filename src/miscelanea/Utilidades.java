package miscelanea;

/**
 * Clase con funciones diversas utilizadas desde otras partes del programa.
 * @author Guillermo Barreiro
 *
 */
public class Utilidades {
	
	/**
	 * Comprueba si una dirección IP es válida.
	 * @param ip String que queremos comprobar
	 * @return true si es correcta, false si no
	 */
	public static boolean comprobarIP (String ip) {
	    try {
	        if ( ip == null || ip.isEmpty() ) {
	            return false;
	        }

	        String[] parts = ip.split( "\\." );
	        if ( parts.length != 4 ) {
	            return false;
	        }

	        for ( String s : parts ) {
	            int i = Integer.parseInt( s );
	            if ( (i < 0) || (i > 255) ) {
	                return false;
	            }
	        }
	        if ( ip.endsWith(".") ) {
	            return false;
	        }

	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	
	/**
	 * Busca en una lista de líneas de texto aquellas que contengan una cadena determinada,
	 * devolviéndolas todas en un String.
	 * @param cadena Cadena de texto que se busca
	 * @param lineasFichero Objeto {@link Iterable} en el que cada elemento sea una línea de texto
	 * @return Todas las líneas que cumplan este criterio, separadas con un salto de línea
	 */
	public static String buscarLineas(String cadena, Iterable<String> lineasFichero) {
		final String separador = System.getProperty("line.separator");
		StringBuffer bufferRespuesta = new StringBuffer();
		if(cadena.isEmpty()) return separador;
		for(String linea: lineasFichero) {
			if(linea.toLowerCase().contains(cadena.toLowerCase())) { // non case sensitive
				/* Si se encuentra la cadena en la línea correspondiente, 
				 * se añade la línea entera a la respuesta, seguida de un salto de línea
				 */
				bufferRespuesta.append(linea);
				bufferRespuesta.append(separador);
			}
		}
		
		return new String(bufferRespuesta);
		
	}

	
	/**
	 * Muestra un mensaje diciendo que ha habido un error con los argumentos de entrada 
	 * y finaliza la ejecución del programa.
	 * @param programa: "Cliente" o "Servidor"
	 */
	public static void errorArgs(String programa) {
		System.out.println("Wrong args:");
		if(programa.equalsIgnoreCase("client")) {
			System.out.println("\tjava Client -(udp|tls) <server_ip> <port> [client_truststore] [substring]");
			System.out.println("\t\tclient_truststore: only for TLS");
			System.out.println("\t\tsubstring: only for UDP");
		}else if(programa.equalsIgnoreCase("server")) {
			System.out.println("java Server <port> <keystore> <keystore_password> <textfile>");
		}
		System.exit(1);
	}
	
	/**
	 * Muestra un mensaje diciendo que ha habido un error en la conexión con el servidor 
	 * y finaliza la ejecución del programa.
	 */
	public static void errorIO() {
		System.out.println("Error in connection. Program execution aborted.");
		System.exit(1);
	}
	
	/**
	 * Muestra un mensaje diciendo que ha habido un error al intentar leer el fichero
	 * y finaliza la ejecución del programa.
	 */
	public static void errorFicheroInexistente() {
		System.out.println("The file does not exist or cannot be read. Program execution aborted.");
		System.exit(1);
	}

}
