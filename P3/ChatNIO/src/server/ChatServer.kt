package server

import java.io.IOException
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import kotlin.system.exitProcess

class ChatServer(private val port: Int): Runnable {
    private val serverSocketChannel: ServerSocketChannel = ServerSocketChannel.open()
    private val selector: Selector = Selector.open()
    init {
        try {
            serverSocketChannel.socket().bind(InetSocketAddress(port))
            serverSocketChannel.configureBlocking(false)
        }
        catch (e: IOException) {
            println("Couldn't listen on port: $port")
            exitProcess(1)
        }
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)
    }

    override fun run() {
        TODO("Not yet implemented")
    }
}