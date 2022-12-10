package tcpConnections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberNotification
import androidx.compose.ui.window.rememberTrayState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

internal class Client1 {
    private var flagNotification = "nothing"
    private lateinit var client1: Socket
    private lateinit var output1: PrintWriter
    private lateinit var input1: BufferedReader

    fun startConnection(host: String, port: Int) {
        client1 = Socket(host, port)
        output1 = PrintWriter(client1.getOutputStream(), true)
        input1 = BufferedReader(InputStreamReader(client1.inputStream))
        println("Client connected : ${client1.inetAddress.hostAddress}")
    }

    fun returnFlag(): String {
        return flagNotification
    }

    fun setFlag(message: String) {
        flagNotification = message
    }

    fun sendMessage(message: String): String {
        println("Client sending [$message]")
        output1.println(message)
        return message
    }

    fun receiveMessage(): String {
        val response = input1.readLine()
        println("Client received [$response]")
        flagNotification = response
        return response
    }

    fun stopConnection() {
        client1.close()
        input1.close()
        output1.close()
        println("${client1.inetAddress.hostAddress} closed the connection")
    }
}

fun main() = application {
    val client1 = Client1()
    client1.startConnection("127.0.0.1", 9999)
    val icon = painterResource("icon.png")
    var isOpen by remember { mutableStateOf(true) }

    val thread = Thread {
        while (true) {
            synchronized(client1) {
                client1.receiveMessage()
            }
        }
    }
    thread.start()

    if (isOpen) {
        val trayState = rememberTrayState()
        val notification1 = rememberNotification("Notification", "Notification1")
        val notification2 = rememberNotification("Notification", "Notification2")
        val notification3 = rememberNotification("Notification", "Notification3")
        var flag = client1.returnFlag()
        Tray(
            state = trayState,
            icon = icon,
            menu = {
                Menu("Notification") {
                    /*val threadPazzo = Thread {
                        synchronized(client1) {
                            while (true) {
                                flag = client1.returnFlag()
                                if (flag == "Notification1") {
                                    trayState.sendNotification(notification1)
                                }
                                if (flag == "Notification2") {
                                    trayState.sendNotification(notification2)
                                }
                                if (flag == "Notification3") {
                                    trayState.sendNotification(notification3)
                                }
                                client1.setFlag("reset")
                            }
                        }
                    }
                    threadPazzo.start()*/
                    Item(
                        "Notification1",

                        onClick = {
                            client1.sendMessage("Notification1")
                        }
                    )
                    Item(
                        "Notification2",

                        onClick = {
                            client1.sendMessage("Notification2")
                        }
                    )
                    Item(
                        "Notification3",
                        onClick = {
                            client1.sendMessage("Notification3")
                        }
                    )
                }

                Item(
                    "Chiudi",

                    onClick = {
                        client1.sendMessage("STOP")
                        while (flag != "STOPPING");
                        client1.stopConnection()
                        exitApplication()
                    }
                )
            }
        )
    }
}