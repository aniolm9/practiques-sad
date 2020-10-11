package client

import MySocket

class ReadThread(private val socket: MySocket): Thread() {
    override fun run() {
        var text = socket.readMsg()
        while (text != null) {
            println("\r" + text)
            print("[you] ")
            text = socket.readMsg()
        }
    }
}
