package client

import java.lang.Exception
import java.lang.StringBuilder
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import kotlin.system.exitProcess

class ChatClient(private val host: String, private val port: Int) {
    private lateinit var username: String

    /* Method to run the chat client. */
    fun runClient() {
        val channel: SocketChannel
        try {
            channel = SocketChannel.open(InetSocketAddress(host, port))
            channel.configureBlocking(true)
        }
        catch (e: Exception) {
            println("Couldn't connect to the server.")
            exitProcess(1)
        }
        // Keep asking for a username while the server doesn't allow us to connect.
        val buffer = ByteBuffer.allocate(8192)
        do {
            val recvString = StringBuilder()
            buffer.flip()
            val bytes = ByteArray(buffer.limit())
            buffer.get(bytes)
            recvString.append(String(bytes))
            buffer.clear()
            if (recvString.toString() == "OK") {
                break
            }
            print("Enter user name: ")
            username = readLine().toString()
            channel.write(ByteBuffer.wrap(username.toByteArray()))
            buffer.clear()
        } while (channel.read(buffer) > 0)
        // With this statement, channel.read() will be non-blocking.
        channel.configureBlocking(false)
        // Start a thread for user input and a thread for server input.
        WriteThread(channel).start()
        ReadThread(channel).start()
    }
}

/* Main function to run the client. It takes two parameters: "host" and "port". */
fun main(args: Array<String>) {
    val host: String
    val port: Int
    try {
        host = args[0]
        port = args[1].toInt()
    }
    catch (e: Exception) {
        println("Please provide a valid host and port number.")
        exitProcess(1)
    }
    val chatClient = ChatClient(host, port)
    chatClient.runClient()
}
