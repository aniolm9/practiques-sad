package client

import MySocket

class WriteThread(private val socket: MySocket): Thread() {
    override fun run() {
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
