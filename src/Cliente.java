import java.io.IOException;

import clientes.ClienteTLS;
import clientes.ClienteUDP;
import miscelanea.Utilidades;

/**
 * Aplicación cliente que se conecta a un servidor remoto vía TLS o UDP.
 * Mediante esta aplicación se le solicita al servidor que nos devuelva todas las líneas de su fichero de texto
 * que contienen el literal especificado.
 * @author Guillermo Barreiro
 *
 */
public class Cliente {

	/**
	 * Método principal del programa cliente. 
	 * Establece una conexión con el servidor mediante TLS o le envía la petición vía UDP, según como desee el usuario.
	 * <br><br>
	 * <b>Invocación:</b> <br><i>java Cliente -(udp|tls) ip_servidor puerto [truststore_cliente] [subcadena]</i> 
	 * <ul><li><i>truststore_cliente</i> solo se especifica en TLS
	 * <li><i>subcadena</i> solo se especifica en UDP</ul>
	 * @param args Argumentos de entrada del programa
	 */
	public static void main(String[] args) {
		// Obtiene todos los parámetros
		if(args.length!=4) {
			// Número de argumentos incorrecto: abortamos el programa
			Utilidades.errorArgs("cliente");
		}
		
		String servicio = args[0].toLowerCase();
		if(!servicio.equals("-udp") && !servicio.equals("-tls")) Utilidades.errorArgs("cliente");
		
		String ipServidor = args[1];
		if(!Utilidades.comprobarIP(ipServidor)) Utilidades.errorArgs("cliente");
		
		int puerto = 0;
		try {
			puerto = Integer.parseInt(args[2]);
		}catch(NumberFormatException nfe) {
			// El argumento "puerto" no es un número
			Utilidades.errorArgs("cliente");
		}
		
		String subcadena = null; // solo necesaria en UDP
		if(servicio.equals("-udp")) {
			subcadena = args[3];
			
		}
		
		// Se conecta al servidor
		if(servicio.equals("-udp")) {
			// Cliente UDP
			try {
				ClienteUDP cliente = new ClienteUDP(ipServidor,puerto); // crea un socket para conectarse al servidor
				String respuesta = cliente.enviarSolicitud(subcadena); // envía la petición y espera la respuesta
				System.out.println(respuesta);
				
			} catch (IOException e) {
				// Si se produjese cualquier error durante la conexión
				Utilidades.errorIO();
			}
		}else {
			// Cliente TLS
			
			// Configura el truststore
			String truststore = args[3];
		    System.setProperty("javax.net.ssl.trustStore", truststore);
			System.setProperty("javax.net.ssl.trustStoreType", "JCEKS");
			
			// Intenta establecer la conexión con el servidor
			try {
				ClienteTLS cliente = new ClienteTLS(ipServidor, puerto); // crea un socket para conectarse al servidor
				while(true) {
					// Bucle indefinido: solicitud-respuesta
					String respuesta = cliente.enviarPeticion();
					System.out.println(respuesta);
				}
			} catch (IOException e) {
				// Si se produjese cualquier error durante la conexión
				Utilidades.errorIO();
			}
			
		}

	}
	

	
	

}
