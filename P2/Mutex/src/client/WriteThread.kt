package client

import MySocket

class WriteThread(private val socket: MySocket, var username: String): Thread() {
    override fun run() {
        // Send username to server.
        socket.writeMsg(username)

        print("[you] ")
        var text = readLine()
        while (text != null) {
            socket.writeMsg(text)
            print("[you] ")
            text = readLine()
        }
        socket.close()
    }
}
