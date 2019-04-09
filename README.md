# Sockets Texto
Proyecto de la asignatura Redes de Ordenadores de Teleco. Se trata de un sistema cliente-servidor, en el que el servidor tiene un fichero de texto, de forma que el cliente puede solicitarle todas las líneas de dicho fichero que contengan una subcadena determinada.

Dicha conexión podrá realizarse a través de UDP (una solicitud por invocación), o a través de una conexión TCP cifrada mediante SSL, pudiendo realizar todas las solicitudes que se deseen por invocación.

# Ejecución

<b>Cliente:</b> `java Cliente -(udp|tls) ip_servidor puerto [truststore_cliente] [subcadena]`
<ul>
<li><i>truststore_cliente</i> solo se especifica en TLS
<li><i>subcadena</i> solo se especifica en UDP
</ul>

<br><b>Servidor:</b> `java Servidor puerto keystore password_keystore fichero_de_mensajes`

# Funcionamiento de la conexión TLS
Para poder establecer correctamente la conexión TLS, es necesario que el Cliente disponga del certificado usado por el Servidor, de forma que pueda autenticarlo. Para ello, se pueden usar los almacenes de claves disponibles en este repositorio: `keystore_servidor.ks` como keystore del Servidor y `truststore_cliente.ks` como truststore del Cliente. El certificado utilizado por el Servidor es `cert_server.cer`, incluido dentro de ambos almacenes.

La contraseña de `keystore_servidor.ks` es `unservidor`, y la de `truststore_cliente.ks` es `uncliente`
