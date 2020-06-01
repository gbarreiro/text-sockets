package servidores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import miscelanea.Utilidades;

/**
 * Socket UDP. Escucha de forma concurrente e indefinida en el puerto especificado.
 * @author Guillermo Barreiro
 *
 */
public class SocketUDP extends Thread{

	private DatagramSocket socket;
	private final static int BUFFER_SIZE = 1500;
	private byte[] dataBuffer = new byte[BUFFER_SIZE];
	private ArrayList<String> lineasFichero;
	
	/**
	 * Crea un socket UDP en el puerto especificado.
	 * Su función será estar escuchando de forma concurrente e indefinida en el puerto especificado,
	 * y cuando le llegue una petición vía UDP la procesará y devolverá las líneas del fichero de texto
	 * que contengan la cadena solicitada.
	 * @param lineas Líneas del fichero de texto a procesar
	 * @param puerto Puerto en el que escuchará el servidor
	 * @throws SocketException si se produce algún error al crear el socket
	 */
	public SocketUDP(ArrayList<String> lineas, int puerto) throws SocketException {
		this.lineasFichero = lineas;
		this.socket = new DatagramSocket(puerto);
	}
	
	/**
	 * Comienza un hilo de ejecución que se encargará de escuchar y recibir solicitudes vía UDP
	 * indefinidamente.
	 */
	public void run() {
		System.out.println("UDP socket listening in port " + socket.getLocalPort());
		while(true) {
			DatagramPacket datagramaRecibido = new DatagramPacket(dataBuffer,BUFFER_SIZE);
			try {
				// Recibimos el datagrama del cliente
				socket.receive(datagramaRecibido); // el hilo se bloqueará aquí hasta que se reciba una petición
				InetAddress ipCliente = datagramaRecibido.getAddress();
				int puertoCliente = datagramaRecibido.getPort();
				String cadena = new String(datagramaRecibido.getData(), 0, datagramaRecibido.getLength());
				System.out.println("UDP query received: " + ipCliente.getHostAddress() + " -- Queried string: \"" + cadena + "\"");
				
				// Buscamos en el fichero de texto la cadena
				String respuesta = Utilidades.buscarLineas(cadena, lineasFichero);
				
				// Devolvemos la respuesta al cliente mediante otro datagrama
				DatagramPacket datagramaRespuesta = new DatagramPacket(respuesta.getBytes(), 
						respuesta.getBytes().length, ipCliente, puertoCliente); // crea un datagrama con la respuesta
				socket.send(datagramaRespuesta); // lo envía
				
			} catch (IOException e) {
				Utilidades.errorIO();
				
			}
		}
	}
	
}
