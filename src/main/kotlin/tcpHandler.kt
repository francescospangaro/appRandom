package tcp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tcp.SimpleServerClientTcpConnexion.Companion.client
import tcp.SimpleServerClientTcpConnexion.Companion.server
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class SimpleServerClientTcpConnexion {

    companion object {
        fun server() {
            val server = ServerSocket(9999)
            println("Server running on port ${server.localPort}")
            val client = server.accept()
            println("Client connected : ${client.inetAddress.hostAddress}")
            val scanner = Scanner(client.inputStream)
            while (scanner.hasNextLine()) {
                println("Server received: ${scanner.nextLine()}")
                break
            }
            server.close()
        }

        fun client() {
            println("Client's ready to stream")
            val client = Socket("127.0.0.1", 9998)
            client.outputStream.write("Hello from the client!".toByteArray())
            client.close()
        }
    }
}

fun main() {
    GlobalScope.launch {
        client()
    }
    server()

}