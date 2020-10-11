package client

import MySocket
import java.lang.NullPointerException

class WriteThread(private val socket: MySocket, var username: String): Thread() {
    override fun run() {
        // Send username to server.
        socket.writeMsg(username)

        val console = System.console()
        var text: String?
        // Check if we are running inside and IDE or not.
        text = try {
            console.readLine("[$username] ")
        }
        catch (e: NullPointerException) {
            readLine()
        }
        while (text != null) {
            socket.writeMsg(text)
            // Check if we are running inside and IDE or not.
            text = try {
                console.readLine("[$username] ")
            }
            catch (e: NullPointerException) {
                readLine()
            }
        }
        socket.close()
    }
}
