package tcpConnections

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

internal class Server {

    private lateinit var server: ServerSocket
    private lateinit var client1: Socket
    private lateinit var client2: Socket
    private lateinit var output1: PrintWriter
    private lateinit var output2: PrintWriter
    private lateinit var input1: BufferedReader
    private lateinit var input2: BufferedReader


    fun start(port: Int) {
        server = ServerSocket(port)
        println("Server running on port ${server.localPort}")
        client1 = server.accept()
        client2 = server.accept()
        output1 = PrintWriter(client1.getOutputStream(), true)
        input1 = BufferedReader(InputStreamReader(client1.inputStream))
        output2 = PrintWriter(client2.getOutputStream(), true)
        input2 = BufferedReader(InputStreamReader(client2.inputStream))
        println("First client connected : ${client1.inetAddress.hostAddress}")
        println("Second client connected : ${client2.inetAddress.hostAddress}")
        handleMessage1()
        handleMessage2()
    }

    fun handleMessage1() {
        val message1 = input1.readLine()
        println("Server receiving [${message1}]")
        if (message1 == "STOP") {
            output1.println(message1)
            this.stop()
        } else {
            println("Server responding [${message1}]")
            output2.println(message1)
        }
    }

    fun handleMessage2() {
        val message2 = input2.readLine()
        println("Server receiving [${message2}]")
        if (message2 == ("STOP")) {
            output2.println("STOPPING")
            this.stop()
        } else {
            println("Server responding [${message2}]")
            output1.println(message2)
        }
    }

    fun stop() {
        try {
            input1.close()
            output1.close()
            input2.close()
            output2.close()
        } catch (e: Exception) {
            println(e)
        }
    }
}

fun main() {
    val server = Server()
    server.start(9999)
    while (true) {
        server.handleMessage1()
        server.handleMessage2()
    }
}