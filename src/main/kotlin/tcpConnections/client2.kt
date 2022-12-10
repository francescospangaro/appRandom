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

internal class Client2 {
    private var flagNotification = "nothing"
    private lateinit var client2: Socket
    private lateinit var output2: PrintWriter
    private lateinit var input2: BufferedReader
    private var isConnected = false

    fun startConnection(host: String, port: Int) {
        client2 = Socket(host, port)
        isConnected = true
        output2 = PrintWriter(client2.getOutputStream(), true)
        input2 = BufferedReader(InputStreamReader(client2.inputStream))
        println("Client connected : ${client2.inetAddress.hostAddress}")
    }

    fun returnFlag(): String {
        return flagNotification
    }

    fun setFlag(message: String) {
        flagNotification = message
    }

    fun sendMessage(message: String): String {
        println("Client sending [$message]")
        output2.println(message)
        return message
    }

    fun receiveMessage(): String {
        val response = input2.readLine()
        println("Client received [$response]")
        synchronized(flagNotification) {
            flagNotification = response
        }
        return response
    }

    fun stopConnection() {
        client2.close()
        this.isConnected = false
        input2.close()
        output2.close()
        println("${client2.inetAddress.hostAddress} closed the connection")
    }
}

fun main() = application {
    val client2 = Client2()
    client2.startConnection("127.0.0.1", 9999)
    val icon = painterResource("icon.png")
    var isOpen by remember { mutableStateOf(true) }

    val thread = Thread {
        while (true) {
            synchronized(client2) {
                client2.receiveMessage()
            }
        }
    }
    thread.start()

    if (isOpen) {
        val trayState = rememberTrayState()
        var flag = client2.returnFlag()
        val notification1 = rememberNotification("Notification", "Notification1")
        val notification2 = rememberNotification("Notification", "Notification2")
        val notification3 = rememberNotification("Notification", "Notification3")



        Tray(
            state = trayState,
            icon = icon,

            menu = {
                Menu("Notification") {
                    /*val threadPazzo = Thread {
                        synchronized(flag) {
                            while (true) {
                                flag = client2.returnFlag()
                                if (flag == "Notification1") {
                                    trayState.sendNotification(notification1)
                                }
                                if (flag == "Notification2") {
                                    trayState.sendNotification(notification2)
                                }
                                if (flag == "Notification3") {
                                    trayState.sendNotification(notification3)
                                }
                                client2.setFlag("reset")
                            }
                        }
                    }
                    threadPazzo.start()*/
                    Item(
                        "Notification1",
                        onClick = {
                            client2.sendMessage("Notification1")
                        }
                    )
                    Item(
                        "Notification2",
                        onClick = {
                            client2.sendMessage("Notification2")
                        }
                    )
                    Item(
                        "Notification3",
                        onClick = {
                            client2.sendMessage("Notification3")
                            /*if(flag=="Notification3"){
                                trayState.sendNotification(notification3)
                                client1.setFlag("reset")
                            }*/
                        }
                    )
                }

                Item(
                    "Chiudi",

                    onClick = {
                        client2.sendMessage("STOP")
                        while (flag != "STOPPING");
                        client2.stopConnection()
                        exitApplication()
                    }
                )
            }
        )
    }
}