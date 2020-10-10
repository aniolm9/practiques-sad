package client

import MySocket

class WriteThread(private val socket: MySocket, var username: String): Thread() {
    override fun run() {
        socket.writeMsg(username)

        var text: String
        do {
            //print("> ")
            text = readLine().toString()
            socket.writeMsg(text)
        } while (text.toLowerCase() != "bye")
        socket.close()
    }
}