package server

import MySocket
import java.io.IOException
import java.lang.Exception
import kotlin.system.exitProcess

class ChatServer(var port: Int) {
    var users = HashMap<String, MySocket>()
    var threads = HashSet<ClientThread>()

    fun runServer() {
        val ss: MyServerSocket
        try {
            ss = MyServerSocket(port)
            println("Listening on port: $port")
        }
        catch (e: IOException) {
            println("Couldn't listen on port: $port")
            exitProcess(1)
        }
        while (true) {
            val socket: MySocket = ss.accept()
            val newClient = ClientThread(this, socket)
            threads.add(newClient)
            newClient.start()
        }
    }

    fun broadcast(message: String, sender: String) {
        for (nick in users.keys) {
            if (nick != sender) {
                println("Broadcasting: $message")
                users[nick]?.writeMsg(message)
            }
        }
    }

    // TODO: MUTEX
    @Synchronized fun addUser(nick: String, socket: MySocket): Boolean {
        if (!users.containsKey(nick)) {
            users[nick] = socket
            return true
        }
        return false
    }

    @Synchronized fun removeUser(nick: String, thread: ClientThread) {
        if (users.remove(nick, users[nick] as MySocket)) threads.remove(thread)
    }
}

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
    chatServer.runServer()
}
