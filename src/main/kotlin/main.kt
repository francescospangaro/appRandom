import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.rememberNotification
import androidx.compose.ui.window.rememberTrayState

fun main() = application {
    val icon = painterResource("icon.png")
    var isOpen by remember { mutableStateOf(true) }

    if (isOpen) {
        val trayState = rememberTrayState()
        val notification1 = rememberNotification("Notification", "Notification1")
        val notification2 = rememberNotification("Notification", "Notification2")
        val notification3 = rememberNotification("Notification", "Notification3")


        Tray(
            state = trayState,
            icon = icon,

            menu = {
                Menu("Notification"){
                    Item(
                        "Notification1",
                        onClick = {
                            trayState.sendNotification(notification1)
                        }
                    )
                    Item(
                        "Notification2",
                        onClick = {
                            trayState.sendNotification(notification2)
                        }
                    )
                    Item(
                        "Notification3",
                        onClick = {
                            trayState.sendNotification(notification3)
                        }
                    )
                }

                Item(
                    "Chiudi",
                    onClick = ::exitApplication
                )
            }
        )
    }
}