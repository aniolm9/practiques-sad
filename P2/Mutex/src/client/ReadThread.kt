package client

import MySocket

class ReadThread(private val socket: MySocket): Thread() {
    override fun run() {
        while (true) {
            println(socket.readMsg())
        }
    }
}