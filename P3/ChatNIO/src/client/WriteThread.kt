package client

import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class WriteThread(private val channel: SocketChannel): Thread() {
    override fun run() {
        print("[you] ")
        var text = readLine()
        while (text != null) {
            channel.write(ByteBuffer.wrap(text.toByteArray()))
            print("[you] ")
            text = readLine()
        }
        channel.close()
    }
}
