package client

import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class ReadThread(private val channel: SocketChannel): Thread() {
    override fun run() {
        val buffer = ByteBuffer.allocate(8192)
        while (true) {
            val recvString = StringBuilder()
            buffer.clear()
            while (channel.read(buffer) > 0) {
                buffer.flip()
                val bytes = ByteArray(buffer.limit())
                buffer.get(bytes)
                recvString.append(String(bytes))
                buffer.clear()
                println("\r" + recvString.toString())
                print("[you] ")
            }
        }
    }
}
