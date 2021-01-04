package server

import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import kotlin.system.exitProcess

class ChatServer(port: Int): Thread() {
    private val serverSocketChannel: ServerSocketChannel = ServerSocketChannel.open()
    private val selector: Selector = Selector.open()
    private val buffer = ByteBuffer.allocate(8192)
    private var users = ArrayList<String>()
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

    /* Run NIO server in a thread. */
    override fun run() {
        var it: Iterator<SelectionKey?>
        var key: SelectionKey?
        while (serverSocketChannel.isOpen) {
            selector.select()
            it = selector.selectedKeys().iterator()
            //broadcastUsers()
            while (it.hasNext()) {
                key = it.next()
                it.remove()
                /* If the key is not valid, jump to next iteration.
                 * If it is valid, check if we are getting a new connection
                 * or a message from a user.
                 */
                if (!key!!.isValid) {
                    continue
                } else if (key.isAcceptable) {
                    handleAccept(key)
                } else if (key.isReadable) {
                    handleRead(key)
                }
            }
        }
    }

    /* Broadcast a message to all users. */
    private fun broadcast(msg: String, sender: SelectionKey) {
        val msgBuf = ByteBuffer.wrap(msg.toByteArray())
        try {
            for (key in selector.keys()) {
                if (key!!.isValid && key.channel() is SocketChannel && key != sender) {
                    val channel = key.channel() as SocketChannel
                    channel.write(msgBuf)
                    msgBuf.rewind()
                }
            }
        }
        catch (e: IOException) {
            println("Failed to send message.")
        }
    }


    /* Accept new connections. */
    private fun handleAccept(key: SelectionKey?) {
        try {
            val channel = (key!!.channel() as ServerSocketChannel).accept()
            val address = StringBuilder(channel.socket().inetAddress.toString()).append(":").append(channel.socket().port).toString()
            // Non-blocking read channel.
            channel.configureBlocking(false)
            channel.register(selector, SelectionKey.OP_READ, address)
            println("Accepted connection from: $address")
        }
        catch (e: IOException) {
            println("Failed to accept new connection.")
        }
    }

    private fun handleRead(key: SelectionKey?) {
        val channel = key!!.channel() as SocketChannel
        val recvString = StringBuilder()
        var msg = ""
        var usersChanged = false
        // We first read the bytes from the channel.
        buffer.clear()
        var read = 0
        while ({ read = channel.read(buffer); read }() > 0) {
            buffer.flip()
            val bytes = ByteArray(buffer.limit())
            buffer.get(bytes)
            recvString.append(String(bytes))
            buffer.clear()
        }

        /* This means that we are receiving a nick. That means, a new user.
         * We need to check that it doesn't exist and then add it to a users array.
         */
        if (key.attachment().toString().startsWith("/")) {
            val nick = recvString.toString().replace("\n", "")
            if (users.contains(nick) || nick.startsWith("/") || nick == "") {
                channel.write(ByteBuffer.wrap("Username already in use. \r".toByteArray()))
            } else {
                users.add(nick)
                channel.write(ByteBuffer.wrap("OK\r".toByteArray()))
                key.attach(nick)
                msg = "${key.attachment()} entered the chat.\r"
                usersChanged = true
            }
        }

        // If the connection is not new, we update the message to display.
        if (read < 0) {
            msg = "${key.attachment()} left the chat.\r"
            users.remove(key.attachment().toString())
            channel.close()
            usersChanged = true
        } else if (msg == "") {
            msg = "${key.attachment()}: " + recvString
        }
        broadcast(msg, key) // Broadcast here users and message
        println(msg)
        /*if(usersChanged) {
            broadcastUsers(this.users, key) // Hugo: send connected users list.
        }*/
    }
    /*private fun broadcastUsers(userlist: ArrayList<String>, sender: SelectionKey) {
        val msg = "Userlist," + userlist.joinToString(separator = ",")
        val msgBuf = ByteBuffer.wrap(msg.toByteArray())
        try {
            for (key in selector.keys()) {
                if (key!!.isValid && key.channel() is SocketChannel) {
                    val channel = key.channel() as SocketChannel
                    channel.write(msgBuf)
                    msgBuf.rewind()
                }
            }
        }
        catch (e: IOException) {
            println("Failed to send message.")
        }
    }*/
}

/* Main function to run the server. It takes a parameter "port". */
fun main(args: Array<String>) {
    val port: Int
    try {
        port = args[0].toInt()
    }
    catch (e: Exception) {
        println("Please provide a valid port number.")
        exitProcess(1)
    }
    val chatServer = ChatServer(port)
    chatServer.start()
}
