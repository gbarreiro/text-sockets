# Text Sockets
Project for the Computer Networks course in the Telecommunications Engineering degree. It's a client-server system, where the server has a text file, so the client can ask the server for all the lines of that text file which contain the requested substring.

This connection can be done with UDP (one query per call) or establishing a TCP encrypted socket (with SSL), which will allow the client to make several queries.

# Running

- **Client:** `java Cliente -(udp|tls) server_ip port [client_truststore] [substring]`
  - `client_truststore` only in TLS
  - `substring` only in UDP

- **Server:** `java Servidor port keystore password_keystore textfile`

# TLS connection
For establishing the TLS encrypted connection, the client must have the certificate used by the server, in order to be able of authenticating it. To do this, you can use the keystores available in this repository: `keystore_servidor.ks` as the Server keystore and `truststore_cliente.ks` as the Client truststore. The certificate used by the Server is `cert_server.cer`, included within both keystores.

`keystore_servidor.ks` password is `unservidor`, and `truststore_cliente.ks` is `uncliente`.
