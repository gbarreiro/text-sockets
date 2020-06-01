package clientes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Cliente capaz de establecer una conexión con un servidor UDP.
 * Su finalidad es solicitar al servidor que busque una cadena determinada en su fichero de texto,
 * mostrándole al usuario la respuesta del servidor en pantalla.
 * @author Guillermo Barreiro
 *
 */
public class ClienteUDP {
	
	private InetAddress ipServidor;
	private int puerto;
	private DatagramSocket socket;
	private static final int BUFFER_SIZE = 1500;
	
	/**
	 * Inicializa el cliente UDP.
	 * @param ip Dirección IP del servidor
	 * @param puerto Puerto del servidor
	 * @throws UnknownHostException en caso de que no se pueda resolver la IP del servidor
	 * @throws SocketException en caso de error al inicializar el socket
	 */
	public ClienteUDP(String ip, int puerto) throws UnknownHostException, SocketException {
		this.ipServidor = InetAddress.getByName(ip);
		this.puerto = puerto;
		this.socket = new DatagramSocket(); // Inicializa un socket para conectarse al servidor
	}
	
	/**
	 * Envía una solicitud al servidor UDP mediante un datagrama y recibe su respuesta.
	 * @param cadena Cadena de texto que buscamos en el fichero del servidor
	 * @return Líneas en las que se encuentra dicha cadena
	 * @throws IOException en caso de que se produzca cualquier error durante la conexión
	 */
	public String enviarSolicitud(String cadena) throws IOException {
		// Envío:
		DatagramPacket datagrama = new DatagramPacket(cadena.getBytes(), cadena.getBytes().length,
				ipServidor, puerto); // Construye un datagrama con la cadena como mensaje y el servidor como destino
		socket.send(datagrama); // Envía el datagrama al servidor UDP
		
		// Recepción:
		byte[] bufferRecepcion = new byte[BUFFER_SIZE]; // crea un buffer de recepción
		datagrama = new DatagramPacket(bufferRecepcion, BUFFER_SIZE);
		
		try {
			socket.setSoTimeout(5000); // si pasan 5 segundos y no hay respuesta, se detendrá la ejecución del programa
			socket.receive(datagrama); // recibe el mensaje del servidor: se bloquea la ejecución hasta recibir respuesta
			
			
			// Si se recibe respuesta...
			String respuesta = new String(datagrama.getData(), 0, datagrama.getLength());
			socket.close(); // cierra el socket UDP
			return respuesta;
		}catch(SocketTimeoutException e) {
			// Si el servidor no respondiese pasados 5 segundos
			System.out.println("Server " + ipServidor.getHostAddress() + ":" + puerto + " does not respond. Connection aborted.");
			System.exit(1);
			return null;
		}
		
		
	}

}
