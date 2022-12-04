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
        val notification1 = rememberNotification("Notifichina", "Ti penso!")
        val notification2 = rememberNotification("Notifichina", "Puzzi!")
        val notification3 = rememberNotification("Notifichina", "Muaaaah!")


        Tray(
            state = trayState,
            icon = icon,

            menu = {
                Menu("Notifichine"){
                    Item(
                        "Chi pensi?",
                        onClick = {
                            trayState.sendNotification(notification1)
                        }
                    )
                    Item(
                        "Puzza?",
                        onClick = {
                            trayState.sendNotification(notification2)
                        }
                    )
                    Item(
                        "Bacino",
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