package servidores;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import miscelanea.Utilidades;

/**
 * Socket principal TLS. Se encarga de escuchar en un puerto determinado y cada vez que recibe una solicitud,
 * crea un nuevo hilo de ejecución paralelo para atenderla.
 * <br><br>
 * Al establecer la conexión SSL, el servidor se autenticará ante el cliente con el certificado almacenado en su keystore. 
 * Si el cliente no dispone de dicho certificado en su truststore, el handshake fallará.
 * @author Guillermo Barreiro
 *
 */
public class SocketTLS extends Thread{

	private ArrayList<String> lineasFichero;
	private SSLServerSocket socket;
	
	/**
	 * Crea un socket TLS en el puerto especificado.
	 * Su función será establecer una conexión con el cliente y mantenerla en un hilo de ejecución paralelo,
	 * atendiendo a todas sus solicitudes.
	 * @param lineas Líneas del ficheor de texto a procesar
	 * @param puerto Puerto en el que esuchará el servidor
	 * @throws IOException si se produce algún error al crear el socket
	 */
	public SocketTLS(ArrayList<String> lineas, int puerto) throws IOException {
		this.lineasFichero = lineas;
		
		SSLServerSocketFactory serverFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		this.socket = (SSLServerSocket) serverFactory.createServerSocket(puerto);
	}
	
	/**
	 * Comienza un hilo de ejecución que se encargará de escuchar y recibir solicitudes vía TLS
	 * indefinidamente. Cada vez que se reciba una petición, se crea un socket secundario para atenderla.
	 */
	public void run() {
		System.out.println("Socket TLS funcionando en el puerto " + socket.getLocalPort());
		while(true) {
			try {
				SSLSocket cliente = (SSLSocket) socket.accept(); // el hilo se bloqueará aquí hasta que se reciba una petición
				System.out.println("Nuevo cliente TLS: " + cliente.getInetAddress());
				AtenderTLS hiloSecundario = new AtenderTLS(cliente, lineasFichero); // crea un hilo secundario para atender a este cliente
				hiloSecundario.start();
			} catch (IOException e) {
				Utilidades.errorIO();
			} 
			
		}
	}
	
}
