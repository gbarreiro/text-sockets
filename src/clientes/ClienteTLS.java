package clientes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Cliente capaz de establecer una conexión con un servidor TLS.
 * Su finalidad es conectarse al servidor y de forma reiterada solicitarle que busque una cadena determinada en su fichero de texto,
 * mostrándole al usuario la respuesta del servidor en pantalla.
 * <br><br>Al establecer la conexión SSL, el cliente autentica al servidor mediante el certificado almacenado en su truststore. En caso de que no se corresponda, el handshake fallará.
 * @author Guillermo Barreiro
 *
 */
public class ClienteTLS {

	InetAddress ipServidor;
	int puerto;
	SSLSocket socket;
	DataOutputStream out;
	DataInputStream in;

	/**
	 * Establece una conexión vía TLS con un servidor.
	 * @param ip Dirección IP del servidor
	 * @param puerto Puerto del servidor
	 * @throws IOException si se produjese algún error al intentar establecer la conexión
	 */
	public ClienteTLS(String ip, int puerto) throws IOException {
		this.ipServidor = InetAddress.getByName(ip);
		this.puerto = puerto;
		SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		this.socket = (SSLSocket) socketFactory.createSocket(ipServidor, puerto); // crea un socket SSL (cifrado)
		this.socket.startHandshake(); // inicia el handshake con el servidor
		this.out = new DataOutputStream(socket.getOutputStream()); // stream de salida (cliente --> servidor)
		this.in = new DataInputStream(socket.getInputStream()); // stream de entrada (servidor --> cliente)
	}

	/**
	 * Envía una petición al servidor vía TLS, devolviendo la respuesta de éste.
	 * @return La respuesta del servidor
	 */
	public String enviarPeticion() {
			try {
				String cadena = nuevaSolicitud(); // la cadena de texto que le vamos a enviar al servidor

				// Enviamos al servidor la petición
				out.writeUTF(cadena);

				// Tiempo de espera...

				// Obtenemos la respuesta del servidor
				String respuesta = in.readUTF();

				return respuesta;


			} catch (IOException e2) {
				System.exit(0);
				return null;
			}

	}

	/**
	 * Solicita al usuario que introduzca una nueva cadena.
	 * @return la cadena introducida por el usuario
	 */
	private static String nuevaSolicitud() {
		Scanner scanner = new Scanner(System.in);
		System.out.printf("Type a string [\"end\" to exit]: ");
		String cadena = scanner.nextLine();
		return cadena;

	}

}
