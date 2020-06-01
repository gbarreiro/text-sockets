package servidores;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;

import miscelanea.Utilidades;

/**
 * Socket secundario TLS. Se encarga de atender a un cliente de forma individual, creando
 * para ello un hilo de ejecución secundario.
 * @author Guillermo Barreiro
 *
 */
public class AtenderTLS extends Thread {
	
	SSLSocket cliente;
	String ipCliente;
	ArrayList<String> lineasFichero;

	public AtenderTLS(SSLSocket socket, ArrayList<String> lineas) {
		this.cliente = socket;
		this.lineasFichero = lineas;
		this.ipCliente = socket.getInetAddress().getHostAddress();
	}
	
	/**
	 * Atiende al cliente de forma indefinida y concurrente.
	 */
	public void run() {	
		boolean escuchando = true;
		try {
			while(escuchando) {
				// Establecemos un time-out de 30 segundos
				cliente.setSoTimeout(60*1000);
				
				// Obtenemos la petición del cliente
				DataInputStream in = new DataInputStream(cliente.getInputStream());
				String peticion = in.readUTF(); // esperando al cliente

				if(!peticion.equals("end")) {
					// Buscamos en el fichero de texto la cadena
					String respuesta = Utilidades.buscarLineas(peticion, lineasFichero);
					
					System.out.println("TLS query received: " + ipCliente + " -- Queried string: \"" + peticion + "\"");

					// Devolvemos la respuesta
					DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
					out.writeUTF(respuesta);
				}else {
					// Abortamos la conexión
					in.close();
					cliente.close();
					escuchando = false;
					System.out.println("Connection closed by client: " + ipCliente);
				}
				
			}
			
		} catch (SocketTimeoutException e1) {
			// Han pasado 60 segundos sin que el usuario hiciese ninguna petición
			System.out.println("Thread finished due inactivity: " + ipCliente);
			try {
				cliente.close();
			} catch (IOException e) {
				System.out.println("Error while trying to close connection with client " + ipCliente);
			}
			escuchando = false;
		} catch(IOException e2) {
			// El usuario ha abortado la conexión
			System.out.println("Connection closed by client: " + ipCliente);
			escuchando = false;
		}
	}
}
